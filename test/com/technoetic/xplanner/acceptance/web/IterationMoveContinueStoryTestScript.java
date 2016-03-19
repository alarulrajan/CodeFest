package com.technoetic.xplanner.acceptance.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.HibernateException;

import com.meterware.httpunit.WebForm;
import com.technoetic.xplanner.actions.MoveContinueStoryAction;
import com.technoetic.xplanner.actions.MoveContinueTaskAction;
import com.technoetic.xplanner.domain.Disposition;
import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.testing.DateHelper;
import com.technoetic.xplanner.views.HistoryPage;


/**
 * The Class IterationMoveContinueStoryTestScript.
 */
public class IterationMoveContinueStoryTestScript extends AbstractPageTestScript {
   
   /** The Constant STORIES_TABLE_ID. */
   public static final String STORIES_TABLE_ID = "objecttable";
   
   /** The Constant STORY_DISPOSITION_COLUMN_KEY. */
   public static final String STORY_DISPOSITION_COLUMN_KEY = "stories.tableheading.disposition";
   
   /** The Constant TASK_DISPOSITION_COLUMN_KEY. */
   public static final String TASK_DISPOSITION_COLUMN_KEY = "tasks.tableheading.disposition";
   
   /** The Constant TASKS_TABLE_ID. */
   public static final String TASKS_TABLE_ID = "objecttable";

   /** The test story name for move test. */
   private final String testStoryNameForMoveTest = "Test story name";
   
   /** The from story name. */
   private String fromStoryName = "From Story";
   
   /** The from iteration. */
   private Iteration fromIteration;
   
   /** The from iteration name. */
   private String fromIterationName = "From Iteration";
   
   /** The to iteration. */
   private Iteration toIteration;
   
   /** The test from task name. */
   private String testFromTaskName = "";
   
   /** The test task name. */
   private String testTaskName = "Test task name";

   /** The estimated hours. */
   private final String estimatedHours = "23.5";
   
   /** The project. */
   private Project project;
   
   /** The from story. */
   private UserStory fromStory;
   
   /** The to story. */
   private UserStory toStory;

  /**
     * Instantiates a new iteration move continue story test script.
     *
     * @param test
     *            the test
     */
  public IterationMoveContinueStoryTestScript(String test) {
     super(test);
  }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
    */
   @Override
public void setUp() throws Exception {
      super.setUp();
      project = newProject();
      testProjectName = project.getName();
      setUpTestPerson();
      setUpFromIterationWithOneStory();
      UserStory firstStory = newUserStory(fromIteration);
      firstStory.setOrderNo(1);
      fromStory.setOrderNo(2);
      setUpToIteration();
      commitCloseAndOpenSession();
      tester.login();
      gotoIteration(fromIterationName);

   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#tearDown()
    */
   @Override
public void tearDown() throws Exception {
      super.tearDown();
   }

   /** Test move story and task to started iteration.
     */
   public void testMoveStoryAndTaskToStartedIteration() {
      gotoIteration(toIteration.getName());
      startIteration();
      gotoIteration(fromIteration.getName());
      moveStory(this.fromStoryName, fromIteration.getName(), toIteration.getName());
      gotoIteration(toIteration.getName());
      String expectedDisposition = Disposition.ADDED_TEXT;
      assertStoryDisposition(this.fromStoryName, expectedDisposition);
      tester.clickLinkWithText(this.fromStoryName);
      tester.assertTextPresent(expectedDisposition);
   }

   /** Assert story disposition.
     *
     * @param storyName
     *            the story name
     * @param expectedDisposition
     *            the expected disposition
     */
   private void assertStoryDisposition(String storyName, String expectedDisposition) {
      tester.assertCellTextForRowWithTextAndColumnKeyEquals("objecttable",
                                                            storyName,
                                                            "stories.tableheading.disposition",
                                                            expectedDisposition);
   }

   /** Test move story and task to future iteration.
     *
     * @throws Exception
     *             the exception
     */
   public void testMoveStoryAndTaskToFutureIteration() throws Exception {
      startIteration();
      String newStoryId = tester.addUserStory(testStoryNameForMoveTest, "Test story description", "14", "1");
      tester.assertTextPresent(Disposition.ADDED_TEXT);
      tester.clickLinkWithText(testStoryNameForMoveTest);

      tester.addTask("Test task name", "sysadmin", "Test task desc.", "14");
      tester.assertTextPresent(Disposition.DISCOVERED_TEXT);
      gotoIteration(fromIterationName);

      moveStory(testStoryNameForMoveTest, fromIterationName, toIteration.getName());

      gotoIteration(toIteration.getName());

      assertStoryOrder(newStoryId, 1);
      assertStoryOrder(String.valueOf(toStory.getId()), 2);
      tester.clickLinkWithText(testStoryNameForMoveTest);

      tester.assertKeyInTable("objecttable", TaskDisposition.PLANNED.getNameKey());
   }

  /**
     * Assert story order.
     *
     * @param storyId
     *            the story id
     * @param expectedOrder
     *            the expected order
     */
  private void assertStoryOrder(String storyId, int expectedOrder) {
//    tester.getDialog().setWorkingForm("reordering");
//    WebForm form = tester.getDialog().getForm();
//    int index = getIndexOfFormArrayParamWithValue(form, "storyId", storyId);
//    String actualOrder = form.getParameterValue("orderNo[" + index + "]");
    assertEquals("story " + storyId + " order ", String.valueOf(expectedOrder), "111111111111"); //actualOrder);
  }

  /**
     * Gets the index of form array param with value.
     *
     * @param form
     *            the form
     * @param arrayParamName
     *            the array param name
     * @param value
     *            the value
     * @return the index of form array param with value
     */
  private int getIndexOfFormArrayParamWithValue(WebForm form, String arrayParamName, String value) {
    List params = getParamsWithNameStartingWith(form, arrayParamName);
    for (int i = 0; i < params.size(); i++) {
      String param = (String) params.get(i);
      if (value.equals(form.getParameterValue(param))) {
        return getIndexFromArrayParam(param);
      }
    }
    fail("Could not find storyId[] param with story id " + value + " in " + dumpFormParamsAndTheirValues(form));
    return -1;
  }

  /**
     * Dump form params and their values.
     *
     * @param form
     *            the form
     * @return the string
     */
  private String dumpFormParamsAndTheirValues(WebForm form) {
    StringBuffer buf = new StringBuffer(100);
    for (int i = 0; i < form.getParameterNames().length; i++) {
      String name = form.getParameterNames()[i];
      String value = form.getParameterValue(name);
      if (i > 0) buf.append(", ");
      buf.append(name + "=" + value);
    }
    return buf.toString();
  }

  /**
     * Gets the index from array param.
     *
     * @param param
     *            the param
     * @return the index from array param
     */
  private int getIndexFromArrayParam(String param) {
    Pattern pattern = Pattern.compile("\\w+\\[(\\d+)\\]" );
    Matcher matcher = pattern.matcher(param);
    if (!matcher.matches()) throw new IllegalArgumentException("Cannot find index in param " + param);
    String index = matcher.group(1);
    return Integer.parseInt(index);
  }
  
  /**
     * Gets the params with name starting with.
     *
     * @param form
     *            the form
     * @param prefix
     *            the prefix
     * @return the params with name starting with
     */
  private List getParamsWithNameStartingWith(WebForm form, final String prefix) {
    List params = new ArrayList(Arrays.asList(form.getParameterNames()));
    CollectionUtils.filter(params, new Predicate() {
      public boolean evaluate(Object object) {
        return ((String) object).startsWith(prefix);
      }
    });
    return params;
  }

  /**
     * Test move task to planned story in future iteration.
     *
     * @throws Exception
     *             the exception
     */
  public void testMoveTaskToPlannedStoryInFutureIteration() throws Exception {
     gotoIteration(toIteration.getName());
     tester.clickLinkWithText(storyName);
     tester.clickDeleteLinkForRowWithText(testTaskName);
     moveTaskAndCheckDisposition(Disposition.ADDED_TEXT, Disposition.PLANNED_TEXT);
  }

   /** Test move task to added story in future iteration.
     *
     * @throws Exception
     *             the exception
     */
   public void testMoveTaskToAddedStoryInFutureIteration() throws Exception {
      gotoIteration(toIteration.getName());
      tester.clickLinkWithText(storyName);
      tester.clickDeleteLinkForRowWithText(testTaskName);
      tester.clickLinkWithText("Edit");
      tester.selectOption("dispositionName", Disposition.ADDED_TEXT);
      tester.submit();
      moveTaskAndCheckDisposition(Disposition.PLANNED_TEXT, Disposition.DISCOVERED_TEXT);
   }

   /** Test move task to story in started iteration.
     *
     * @throws Exception
     *             the exception
     */
   public void testMoveTaskToStoryInStartedIteration() throws Exception {
      gotoIteration(toIteration.getName());
      startIteration();
      tester.clickLinkWithText(storyName);
      tester.clickDeleteLinkForRowWithText(testTaskName);
      moveTaskAndCheckDisposition(Disposition.PLANNED_TEXT, Disposition.ADDED_TEXT);
   }

   /** Test continue unfinishied stories.
     *
     * @throws Exception
     *             the exception
     */
   public void testContinueUnfinishiedStories() throws Exception {
      //ChangeSoon remove iteration param since it does not do anything
      tester.assertTextPresent(estimatedHours); //storyEstimate;
      startIteration();
      closeIteration();

      closeIterationAndContinueUnfinishedStories(testIterationName);

      gotoIteration(fromIterationName);
      iterationTester.assertCurrentIterationClosed();
   }

   /** Test no future iteration to continue unfishied stories.
     *
     * @throws Exception
     *             the exception
     */
   public void testNoFutureIterationToContinueUnfishiedStories() throws Exception {
      // remove the toIteration so that there are no future iteration
      deleteObject(toIteration);
      commitCloseAndOpenSession();

      startIteration();
      closeIteration();

      tester.assertKeyPresent("iteration.continue.unfinished.stories");
      // Note: If following assert fails verify there isn't any other iteration later than the fromIteration
      tester.assertKeyPresent("iteration.status.editor.no_future_iteration");
   }

   /** Test default disposition.
     *
     * @throws Exception
     *             the exception
     */
   public void testDefaultDisposition() throws Exception {
      iterationTester.assertCurrentIterationClosed();
      assertNewStoryDisposition("Planned Story", tester.getMessage(StoryDisposition.PLANNED.getNameKey()));

      startIteration();

      assertNewStoryDisposition("Added Story", tester.getMessage(StoryDisposition.ADDED.getNameKey()));
   }

   /** Test no continue story in same iteration.
     *
     * @throws Exception
     *             the exception
     */
   public void testNoContinueStoryInSameIteration() throws Exception {
      tester.clickContinueLinkInRowWithText(fromStoryName);
      tester.assertOnMoveContinueStoryPage(fromStoryName);
      tester.selectOption("targetIterationId", testProjectName + " :: " + fromIterationName);
      tester.clickButton(MoveContinueStoryAction.CONTINUE_ACTION);
      tester.assertKeyPresent("story.editor.same_iteration");
//      ChangeSoon modify tag to exclude the original iteration from the options
//        tester.assertOptionNotListed("targetIterationId", testProjectName + " :: " + fromIterationName);
   }

   /** Test move story to different iteration.
     *
     * @throws Exception
     *             the exception
     */
   public void testMoveStoryToDifferentIteration() throws Exception {
      moveContinueStory(MoveContinueStoryAction.MOVE_ACTION, testIterationName, fromIterationName);
      tester.assertTextNotPresent(fromStoryName);
      verifyHistorys(History.MOVED_OUT, fromStoryName);
      gotoIteration(testIterationName);
      tester.assertTextPresent(fromStoryName);
      verifyDisposition(fromStoryName, StoryDisposition.PLANNED);
      verifyTargetIterationHistory(History.MOVED_IN, fromStoryName);
      verifyMovedStoryHistory();
   }

   /** Test continue story in different iteration.
     *
     * @throws Exception
     *             the exception
     */
   public void testContinueStoryInDifferentIteration() throws Exception {
      moveContinueStory(MoveContinueStoryAction.CONTINUE_ACTION, testIterationName, fromIterationName);
      tester.assertTextPresent(fromStoryName);

      assertStoryOrder(String.valueOf(fromStory.getId()), 2);
      verifyHistorys(History.CONTINUED, fromStoryName + " in iteration: " + testIterationName);
      gotoIteration(testIterationName);
      tester.assertTextPresent(fromStoryName);
      verifyDisposition(fromStoryName, StoryDisposition.CARRIED_OVER);
      tester.assertCellTextForRowWithTextAndColumnKeyEquals("objecttable",
                                                            fromStoryName,
                                                            "stories.tableheading.estimated_original_hours",
                                                            estimatedHours);
      gotoIteration(toIteration.getName());
      tester.clickLinkWithText(fromStoryName);
      tester.assertCellTextForRowWithTextAndColumnKeyEquals(TASKS_TABLE_ID,
                                                            testFromTaskName,
                                                            TASK_DISPOSITION_COLUMN_KEY,
                                                            tester.getMessage(TaskDisposition.CARRIED_OVER.getNameKey()));
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testProjectName);
      gotoIteration(testIterationName);
      verifyHistorys(History.CONTINUED, fromStoryName + " in iteration: " + fromIterationName);
      verifyTargetIterationHistory(History.CONTINUED, fromStoryName);
   }

   /** Test continue story in different iteration_ localized.
     *
     * @throws Exception
     *             the exception
     */
   public void testContinueStoryInDifferentIteration_Localized() throws Exception {
      try {
         tester.changeLocale("fr");
         moveContinueStory(MoveContinueStoryAction.CONTINUE_ACTION, testIterationName,
                           fromIterationName);
      }
      catch (Exception ex) {
         fail("Cannot continue story");
      }
      finally {
         tester.changeLocale(null);
      }
   }

   /** Test continue from future iteration_ is not available.
     *
     * @throws Exception
     *             the exception
     */
   public void testContinueFromFutureIteration_IsNotAvailable() throws Exception {
      setIterationDatesFromNow(fromIteration, 4, 8);
      tester.clickContinueLinkInRowWithText(fromStoryName);
      tester.assertOnMoveContinueStoryPage(fromStoryName);
      tester.selectOption("targetIterationId", testProjectName + " :: " + toIteration.getName());
      tester.assertButtonNotPresent(MoveContinueStoryAction.CONTINUE_ACTION);

   }

   /** Test move from future iteration_ to current iteration default
     * disposition is added.
     *
     * @throws Exception
     *             the exception
     */
   public void testMoveFromFutureIteration_ToCurrentIterationDefaultDispositionIsAdded() throws Exception {
      setIterationDatesFromNow(fromIteration, 4, 8);
      setIterationDatesFromNow(toIteration, -1, 3);
      moveContinueStory(MoveContinueStoryAction.MOVE_ACTION, testIterationName, fromIterationName);
      gotoIteration(testIterationName);
      assertStoryDisposition(fromStoryName, Disposition.PLANNED_TEXT);
      moveContinueStory(MoveContinueStoryAction.MOVE_ACTION, fromIterationName, testIterationName);
      gotoIteration(fromIterationName);
      assertStoryDisposition(fromStoryName, Disposition.PLANNED_TEXT);

   }

   /** Test move from future iteration_ to future iteration default
     * disposition is planned.
     *
     * @throws Exception
     *             the exception
     */
   public void testMoveFromFutureIteration_ToFutureIterationDefaultDispositionIsPlanned() throws Exception {
      setIterationDatesFromNow(fromIteration, 4, 8);
      setIterationDatesFromNow(toIteration, 9, 13);
      moveContinueStory(MoveContinueStoryAction.MOVE_ACTION, testIterationName, fromIterationName);
      gotoIteration(testIterationName);
      assertStoryDisposition(fromStoryName, Disposition.PLANNED_TEXT);
   }

   /** Test continue story in not started iteration.
     *
     * @throws Exception
     *             the exception
     */
   public void testContinueStoryInNotStartedIteration() throws Exception {
      assertContinuingStoryDisposition(IterationStatus.INACTIVE, StoryDisposition.CARRIED_OVER);
   }

   /** Test continue story in started iteration.
     *
     * @throws Exception
     *             the exception
     */
   public void testContinueStoryInStartedIteration() throws Exception {
      assertContinuingStoryDisposition(IterationStatus.ACTIVE, StoryDisposition.ADDED);
   }

   /** Move task and check disposition.
     *
     * @param sourceDisposition
     *            the source disposition
     * @param targetDisposition
     *            the target disposition
     */
   private void moveTaskAndCheckDisposition(String sourceDisposition, String targetDisposition) {
      gotoIteration(fromIterationName);
      tester.addUserStory(testStoryNameForMoveTest, "Test story description", "14", "1");
      tester.clickLinkWithText(testStoryNameForMoveTest);
      tester.addTask(testTaskName, null, "Test task desc.", "14", sourceDisposition);
      moveContinueTask(MoveContinueTaskAction.MOVE_ACTION,
                       toIteration.getName(),
                       fromIterationName,
                       storyName,
                       testTaskName);
      gotoIteration(toIteration.getName());
      tester.clickLinkWithText(storyName);
      tester.assertTextPresent(testTaskName);
      tester.assertTextInTable("objecttable", targetDisposition);
   }

   /** Close iteration and continue unfinished stories.
     *
     * @param targetIterationName
     *            the target iteration name
     */
   private void closeIterationAndContinueUnfinishedStories(String targetIterationName) {
      tester.assertKeyPresent("iteration.continue.unfinished.stories");
      tester.setWorkingForm("continue");
      tester.selectOption("targetIterationId", testProjectName + " :: " + targetIterationName);
      tester.clickButton("Ok");
   }

   /** Assert new story disposition.
     *
     * @param storyName
     *            the story name
     * @param expectedDisposition
     *            the expected disposition
     */
   private void assertNewStoryDisposition(String storyName, String expectedDisposition) {
      gotoIteration(fromIterationName);
      tester.addUserStory(storyName, "Some description", "11", "1");
      iterationTester.assertOnIterationPage();
      tester.assertCellTextForRowWithTextAndColumnKeyEquals(STORIES_TABLE_ID,
                                                            storyName,
                                                            STORY_DISPOSITION_COLUMN_KEY,
                                                            expectedDisposition);
   }

   /** Sets the iteration dates from now.
     *
     * @param iteration
     *            the iteration
     * @param daysFromDate
     *            the days from date
     * @param daysToDate
     *            the days to date
     * @throws HibernateException
     *             the hibernate exception
     * @throws SQLException
     *             the SQL exception
     */
   private void setIterationDatesFromNow(Iteration iteration, int daysFromDate, int daysToDate)
         throws HibernateException, SQLException {
      Date currentDate = new Date();
      iteration.setStartDate(DateHelper.getDateDaysFromDate(currentDate, daysFromDate));
      iteration.setEndDate(DateHelper.getDateDaysFromDate(currentDate, daysToDate));
      getSession().update(iteration);
      commitCloseAndOpenSession();
   }

   /** Assert continuing story disposition.
     *
     * @param targetIterationStatus
     *            the target iteration status
     * @param expectedDisposition
     *            the expected disposition
     * @throws Exception
     *             the exception
     */
   private void assertContinuingStoryDisposition(IterationStatus targetIterationStatus,
                                                 StoryDisposition expectedDisposition) throws Exception {
      toIteration.setIterationStatus(targetIterationStatus);
      getSession().update(toIteration);
      commitCloseAndOpenSession();
      moveContinueStory(MoveContinueStoryAction.CONTINUE_ACTION, testIterationName, fromIterationName);
      tester.assertTextPresent(fromStoryName);
      gotoIteration(testIterationName);
      tester.assertTextPresent(fromStoryName);
      verifyDisposition(fromStoryName, expectedDisposition);
   }

   //DEBT Test attachement copy behavior

   /** Sets the up from iteration with one story.
     *
     * @throws Exception
     *             the exception
     */
   private void setUpFromIterationWithOneStory() throws Exception {
      fromIteration = newIteration(project);
      fromIteration.setStartDate(new Date());
      fromIteration.setEndDate(DateHelper.getDateDaysFromDate(fromIteration.getStartDate(), 10));
      fromIterationName = fromIteration.getName();
      fromStory = newUserStory(fromIteration);
      fromStory.setDisposition(StoryDisposition.PLANNED);
      fromStoryName = fromStory.getName();
      newNote(fromStory, developer);
      createTestTask(fromStory);
      testFromTaskName = testTaskName;
   }

   /** Sets the up to iteration.
     *
     * @throws Exception
     *             the exception
     */
   private void setUpToIteration() throws Exception {
      toIteration = newIteration(project);
      toIteration.setStartDate(DateHelper.getDateDaysFromDate(fromIteration.getEndDate(), 1));
      toIteration.setEndDate(DateHelper.getDateDaysFromDate(toIteration.getStartDate(), 10));
      testIterationName = toIteration.getName();
      toStory = newUserStory(toIteration);
      storyName = toStory.getName();
      toStory.setDescription(testStoryDescription);
      toStory.setEstimatedHoursField(Double.parseDouble(estimatedHours));
      Task task = newTask(toStory);
      testTaskName = task.getName();
      task.setEstimatedHours(Double.parseDouble(testTaskEstimatedHours));
   }

   /** Creates the test task.
     *
     * @param story
     *            the story
     * @throws HibernateException
     *             the hibernate exception
     * @throws RepositoryException
     *             the repository exception
     */
   private void createTestTask(UserStory story) throws HibernateException, RepositoryException {
      Task task = newTask(story);
      testTaskName = task.getName();
      task.setEstimatedHours(Double.parseDouble(estimatedHours));
//       testTaskId = tester.addTask(testTaskName, developerName, testTaskDescription, testTaskEstimatedHours);
   }

   /** Goto test project.
     */
   private void gotoTestProject() {
      if (!tester.isTextPresent(tester.getMessage("project.prefix") + " " + testProjectName)) {
         if (!tester.isLinkPresentWithText(testProjectName)) {
            tester.gotoProjectsPage();
         }
         tester.clickLinkWithText(testProjectName);
      }
      clickLinkWithKeyIfPresent("navigation.project");
   }

   /** Goto iteration.
     *
     * @param iterationName
     *            the iteration name
     */
   private void gotoIteration(String iterationName) {
      if (!tester.isTextPresent(tester.getMessage("iteration.prefix") + " " + iterationName)) {
         if (!tester.isLinkPresentWithText(iterationName)) {
            gotoTestProject();
         }
         tester.clickLinkWithText(iterationName);
         iterationTester.assertOnIterationPage();
      }
      clickLinkWithKeyIfPresent("navigation.iteration"); // for history view
      clickLinkWithKeyIfPresent("iteration.link.stories"); // for all other views than Stories
   }

   /** Start iteration.
     */
   private void startIteration() { iterationTester.startCurrentIteration(); }

   /** Close iteration.
     */
   private void closeIteration() { iterationTester.closeCurrentIteration(); }

   /** Move continue story.
     *
     * @param actionType
     *            the action type
     * @param targetIteration
     *            the target iteration
     * @param origIteration
     *            the orig iteration
     * @param storyName
     *            the story name
     */
   private void moveContinueStory(String actionType, String targetIteration, String origIteration, String storyName) {
      tester.clickContinueLinkInRowWithText(storyName);
      tester.assertOnMoveContinueStoryPage(storyName);
      tester.selectOption("targetIterationId", testProjectName + " :: " + targetIteration);
      tester.clickButton(actionType);
      iterationTester.assertOnIterationPage();
      tester.assertTextPresent(origIteration);
   }

   /** Move story.
     *
     * @param fromStoryName
     *            the from story name
     * @param fromIterationName
     *            the from iteration name
     * @param toIterationName
     *            the to iteration name
     */
   private void moveStory(String fromStoryName, String fromIterationName,
                          String toIterationName) {
      moveContinueStory(MoveContinueStoryAction.MOVE_ACTION, toIterationName, fromIterationName, fromStoryName);
   }

   /** Move continue story.
     *
     * @param actionType
     *            the action type
     * @param targetIteration
     *            the target iteration
     * @param origIteration
     *            the orig iteration
     */
   private void moveContinueStory(String actionType, String targetIteration, String origIteration) {
      moveContinueStory(actionType, targetIteration, origIteration, fromStoryName);
   }

   /** Move continue task.
     *
     * @param actionType
     *            the action type
     * @param targetIteration
     *            the target iteration
     * @param origIteration
     *            the orig iteration
     * @param targetStory
     *            the target story
     * @param taskName
     *            the task name
     */
   private void moveContinueTask(String actionType,
                                 String targetIteration,
                                 String origIteration,
                                 String targetStory,
                                 String taskName) {
      tester.clickContinueLinkInRowWithText(taskName);
      tester.assertOnMoveContinueTaskPage(taskName);
      tester.selectOption("targetStoryId", testProjectName + " :: " + targetIteration + " :: " + targetStory);
      tester.clickButton(actionType);
      tester.assertOnStoryPage();
      tester.assertTextPresent(origIteration);
   }

   /** Verify moved story history.
     *
     * @throws Exception
     *             the exception
     */
   private void verifyMovedStoryHistory() throws Exception {
      gotoIteration(testIterationName);
      tester.clickLinkWithText(fromStoryName);
      verifyHistorys(History.MOVED, "from " + fromIterationName + " to " + testIterationName);
   }

   /** Verify target iteration history.
     *
     * @param eventType
     *            the event type
     * @param description
     *            the description
     * @throws Exception
     *             the exception
     */
   private void verifyTargetIterationHistory(String eventType, String description) throws Exception {
      gotoIteration(testIterationName);
      verifyHistorys(eventType, description);
   }

   /** Verify historys.
     *
     * @param eventType
     *            the event type
     * @param description
     *            the description
     */
   private void verifyHistorys(String eventType, String description) {
      tester.clickLink("aKH");
      tester.assertTitleEquals("History");
      tester.assertTextInTable(HistoryPage.EVENT_TABLE_ID, eventType);
      tester.assertCellTextForRowWithTextAndColumnKeyEquals(HistoryPage.EVENT_TABLE_ID,
                                                            description,
                                                            "history.tableheading.action",
                                                            eventType);
   }

   /** Verify disposition.
     *
     * @param taskOrStoryName
     *            the task or story name
     * @param disposition
     *            the disposition
     */
   private void verifyDisposition(String taskOrStoryName, StoryDisposition disposition) {
      tester.assertTextPresent(taskOrStoryName);
      tester.assertTextPresent(tester.getMessage(disposition.getNameKey()));
   }

   /** Verify continued attachments.
     */
   private void verifyContinuedAttachments() {
      tester.clickLinkWithText(fromStoryName);
      tester.assertTextPresent(testTaskName);
      tester.assertTextPresent(testNoteSubject);
      tester.assertLinkPresentWithText(
            testNoteAttachmentFilename.substring(testNoteAttachmentFilename.lastIndexOf("/") + 1));
      tester.assertTextPresent(testNoteSubject + "without att.");

      tester.clickLinkWithText(testTaskName);
      tester.assertTextPresent(testTaskName);
      tester.assertTextPresent(testNoteSubject);
      tester.assertLinkPresentWithText(
            testNoteAttachmentFilename.substring(testNoteAttachmentFilename.lastIndexOf("/") + 1));
      tester.assertTextPresent(testNoteSubject + "without att.");
   }

   /** Click link with key if present.
     *
     * @param key
     *            the key
     */
   private void clickLinkWithKeyIfPresent(String key) {
      if (tester.isLinkPresentWithKey(key)) {
         tester.clickLinkWithKey(key);
      }
   }

}
