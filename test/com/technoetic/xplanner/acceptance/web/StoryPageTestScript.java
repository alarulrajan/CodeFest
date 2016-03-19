package com.technoetic.xplanner.acceptance.web;

import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.actions.MoveContinueStoryAction;
import com.technoetic.xplanner.actions.MoveContinueTaskAction;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.views.HistoryPage;
import com.technoetic.xplanner.views.IterationAccuracyPage;

/**
 * The Class StoryPageTestScript.
 */
public class StoryPageTestScript extends AbstractStoryPageTestScript {
   
   /** The Constant TASK_NAME. */
   //todo jm: remove dependency on TASK_XXX
   public static final String TASK_NAME = "Test Task";
   
   /** The Constant TASK_DESCRIPTION. */
   public static final String TASK_DESCRIPTION = "A test task";
   
   /** The Constant ESTIMATED_HOURS. */
   public static final String ESTIMATED_HOURS = "23.5";
   
   /** The Constant ESTIMATED_ORIGINAL_HOURS. */
   public static final String ESTIMATED_ORIGINAL_HOURS = "23.5";
   
   /** The Constant STORY_NAME. */
   public static final String STORY_NAME = "Test Story For Origi. Hour.";
   
   /** The Constant ESTIMATED_HOURS_FOR_ADDED_OBJECT. */
   public static final String ESTIMATED_HOURS_FOR_ADDED_OBJECT = "20";
   
   /** The Constant fromIterationName. */
   public static final String fromIterationName = "From Iteration";
   
   /** The Constant fromStoryName. */
   public static final String fromStoryName = "From Story";
   
   /** The Constant toStoryName. */
   public static final String toStoryName = "To story";
   
   /** The Constant TASK_ESTIMATED_ORIGINAL_HOURS. */
   private static final String TASK_ESTIMATED_ORIGINAL_HOURS = "23.5";
   
   /** The Constant STORY_ESTIMATED_ORIGINAL_HOURS. */
   private static final String STORY_ESTIMATED_ORIGINAL_HOURS = "15.5";
   
   /** The Constant ADDED_TASK_HOURS. */
   private static final String ADDED_TASK_HOURS = "23.5";
   
   /** The Constant ADDED_STORY_HOURS. */
   private static final String ADDED_STORY_HOURS = "10.0";
   
   /** The iteration name for org est test. */
   private final String ITERATION_NAME_FOR_ORG_EST_TEST = "Started Iteration for OrgEstHours test";
   
   /** The story name for org est test. */
   private final String STORY_NAME_FOR_ORG_EST_TEST = "Story for OrgEstHours test";
   
   /** Instantiates a new story page test script.
     *
     * @param test
     *            the test
     */
   public StoryPageTestScript(String test) {
      super(test);
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
    */
   //todo jm convert class setup to direct db.
   public void setUp() throws Exception {
      super.setUp();
      simpleSetUp();
   }
   
   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#tearDown()
    */
   public void tearDown() throws Exception {
      tearDownLocalObjects();
      simpleTearDown();
      super.tearDown();
   }
   
   /** Tear down local objects.
     */
   private void tearDownLocalObjects() {
      tester.deleteObjects(Task.class, "name", TASK_NAME);
      tester.deleteObjects(UserStory.class, "name", fromStoryName);
      tester.deleteObjects(Iteration.class, "name", fromIterationName);
   }
   
   /** Test adding and deleting notes.
     */
   public void testAddingAndDeletingNotes() {
      runNotesTests(XPlannerWebTester.STORY_PAGE);
   }
   
   /** Test adding and deleting tasks.
     */
   public void testAddingAndDeletingTasks() {
      addTaskToCurrentStory();
      tester.clickEditLinkInRowWithText(TASK_NAME);

      // modify the task name
      String newTaskName = "A new name for task";
      tester.assertFormElementEquals("name", TASK_NAME);
      tester.assertFormElementEquals("estimatedHours", ESTIMATED_HOURS);
      tester.assertFormElementEquals("description", TASK_DESCRIPTION);
      tester.setFormElement("name", newTaskName);
      tester.submit();
      tester.assertOnStoryPage();
      tester.assertTextNotPresent(TASK_NAME);
      tester.assertTextPresent(newTaskName);

      // put back the original task
      addTaskToCurrentStory();
      // clean up both tasks
      tester.clickDeleteLinkForRowWithText(newTaskName);
      tester.assertTextNotPresent(newTaskName);
      tester.clickDeleteLinkForRowWithText(TASK_NAME);
      tester.assertTextNotPresent(TASK_NAME);
   }
   
   /** Adds the task to current story.
     */
   private void addTaskToCurrentStory() {
      tester.addTask(TASK_NAME, developerName, TASK_DESCRIPTION, ESTIMATED_HOURS);
      tester.assertOnStoryPage();
      tester.assertTextPresent(TASK_NAME);
      tester.assertKeyPresent("task.disposition.planned.name");
   }
   
   /** Test change story disposition.
     */
   public void testChangeStoryDisposition() {
// todo jm why is it called action.edit.story? It should be generic and called action.edit (was story.link.edit before merge)
      tester.clickLinkWithKey("action.edit.story");
      tester.assertOptionEquals("dispositionName", tester.getMessage(StoryDisposition.PLANNED.getNameKey()));
      tester.selectOption("dispositionName", tester.getMessage(StoryDisposition.CARRIED_OVER.getNameKey()));
      tester.submit();
      assertDispositionEquals(storyName, StoryDisposition.CARRIED_OVER);
   }

   /** Test content and links.
     */
   // todo jm reenable this test
   public void testContentAndLinks() {
        tester.assertOnStoryPage();
        tester.assertKeyPresent("story.label.status");
//        tester.assertKeyPresent("story.no_tasks");
//        tester.assertTextPresent(testStoryName);
//        tester.assertTextPresent(testStoryDescription);
//        tester.clickLinkWithKey("navigation.iteration");
//        iterationTester.assertOnIterationPage();
//        tester.clickLinkWithText(testStoryName);
//        tester.assertOnStoryPage();
//        tester.clickLinkWithKey("navigation.project");
//        tester.assertOnProjectPage();
//        tester.assertActualHoursColumnPresent();
   }
   
   /** Test continue task in different story.
     *
     * @throws Exception
     *             the exception
     */
   public void testContinueTaskInDifferentStory() throws Exception {
      setUpMoveContinueTask();
      moveContinueTask(MoveContinueTaskAction.CONTINUE_ACTION);
      tester.assertTextPresent(TASK_NAME);
      assertDispositionEquals(TASK_NAME, TaskDisposition.PLANNED);
      verifyHistorys(History.CONTINUED);
      assertContinuingTaskExistsInTargetIteration();
      assertDispositionEquals(TASK_NAME, TaskDisposition.CARRIED_OVER);
      verifyTargetStoryHistory(History.CONTINUED);
      verifyTaskHistory(History.CONTINUED);
   }
   
   /** Test move task to different story.
     *
     * @throws Exception
     *             the exception
     */
   public void testMoveTaskToDifferentStory() throws Exception {
      setUpMoveContinueTask();
      moveContinueTask(MoveContinueTaskAction.MOVE_ACTION);
      tester.assertTextNotPresent(TASK_NAME);
//       assertDispositionEquals(TASK_NAME, TaskDisposition.PLANNED);
      verifyHistorys(History.MOVED_OUT);
      assertContinuingTaskExistsInTargetIteration();
      assertDispositionEquals(TASK_NAME, TaskDisposition.PLANNED);
      verifyTargetStoryHistory(History.MOVED_IN);
      verifyTaskHistory(History.MOVED);
   }
   
   /** Move continue task.
     *
     * @param actionType
     *            the action type
     */
   private void moveContinueTask(String actionType) {
      tester.clickContinueLinkInRowWithText(TASK_NAME);
      tester.assertOnMoveContinueTaskPage(TASK_NAME);
      tester.selectOption("targetStoryId", testProjectName + " :: " + testIterationName + " :: " + storyName);
      tester.clickButton(actionType);
      tester.assertOnStoryPage(fromStoryName);
//        assertDispositionEquals(TASK_NAME, TaskDisposition.PLANNED);
   }
   
   /** Verify historys.
     *
     * @param moveType
     *            the move type
     */
   private void verifyHistorys(String moveType) {
      tester.clickLink("aKH");
      tester.assertTitleEquals("History");
      tester.assertTextInTable(HistoryPage.EVENT_TABLE_ID, moveType);
   }

   /** Assert continuing task exists in target iteration.
     *
     * @throws Exception
     *             the exception
     */
   //todo jm change navigation to new optimized one
   private void assertContinuingTaskExistsInTargetIteration() throws Exception {
      tester.gotoRelativeUrl("/do/view/projects");
      tester.clickLinkWithText(testProjectName);
      tester.clickLinkWithText(testIterationName);
      tester.clickLinkWithText(storyName);
      tester.assertOnStoryPage(storyName);
      tester.assertTextPresent(TASK_NAME);
   }
   
   /** Verify target story history.
     *
     * @param actionType
     *            the action type
     * @throws Exception
     *             the exception
     */
   private void verifyTargetStoryHistory(String actionType) throws Exception {
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testProjectName);
      tester.clickLinkWithText(testIterationName);
      tester.clickLinkWithText(storyName);
      verifyHistorys(actionType);
   }
   
   /** Verify task history.
     *
     * @param actionType
     *            the action type
     * @throws Exception
     *             the exception
     */
   private void verifyTaskHistory(String actionType) throws Exception {
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testProjectName);
      tester.clickLinkWithText(testIterationName);
      tester.clickLinkWithText(storyName);
      tester.clickLinkWithText(TASK_NAME);
      verifyHistorys(actionType);
   }
   
   /** Test move task to different story in the same started iteration.
     *
     * @throws Exception
     *             the exception
     */
   public void testMoveTaskToDifferentStoryInTheSameStartedIteration() throws Exception {
      setUpMoveContinueTask();
      tester.gotoProjectsPage();
      tester.clickLinkWithText(project.getName());
      tester.clickLinkWithText(fromIterationName);
      tester.addUserStory(toStoryName, "desc.", "10", "1");
      tester.assertTextPresent(toStoryName);
      iterationTester.startCurrentIteration();
      tester.clickLinkWithText(fromStoryName);
      moveContinueTask(MoveContinueTaskAction.MOVE_ACTION, TASK_NAME, fromStoryName, toStoryName, fromIterationName);
      tester.gotoProjectsPage();
      tester.clickLinkWithText(project.getName());
      tester.clickLinkWithText(fromIterationName);
      tester.clickLinkWithText(toStoryName);
      tester.assertTextPresent(TASK_NAME);
      tester.assertKeyInTable("objecttable", TaskDisposition.PLANNED.getNameKey());
   }
   
   /** Sets the up move continue task.
     *
     * @throws Exception
     *             the exception
     */
   private void setUpMoveContinueTask() throws Exception {
      tester.gotoRelativeUrl("/do/view/projects");
      tester.clickLinkWithText(testProjectName);
      // create the to story and to task
      createIterationAndStory(fromIterationName, fromStoryName, 15, 28);
      // create the from story and from task
      addTaskToCurrentStory();
   }
   
   /** Move continue task.
     *
     * @param actionType
     *            the action type
     * @param taskName
     *            the task name
     * @param fromStoryName
     *            the from story name
     * @param targetStoryName
     *            the target story name
     * @param targetIterationName
     *            the target iteration name
     */
   private void moveContinueTask(String actionType, String taskName, String fromStoryName, String targetStoryName, String targetIterationName) {
      tester.clickContinueLinkInRowWithText(taskName);
      tester.assertOnMoveContinueTaskPage(taskName);
      tester.selectOption("targetStoryId", testProjectName + " :: " + targetIterationName + " :: " + targetStoryName);
      tester.clickButton(actionType);
      tester.assertOnStoryPage(fromStoryName);
   }
   
   /** Test move task to different story_ localized.
     *
     * @throws Exception
     *             the exception
     */
   public void testMoveTaskToDifferentStory_Localized() throws Exception {
      setUpMoveContinueTask();
      try {
         tester.changeLocale("fr");
         moveContinueTask(MoveContinueTaskAction.MOVE_ACTION);
      } catch (Exception ex) {
         fail("Cannot continue task");
      } finally {
         tester.changeLocale(null);
      }
   }
   
   /** Test no move continue task in same story.
     *
     * @throws Exception
     *             the exception
     */
   public void testNoMoveContinueTaskInSameStory() throws Exception {
      addTaskToCurrentStory();
      tester.clickContinueLinkInRowWithText(TASK_NAME);
      tester.assertOnMoveContinueTaskPage(TASK_NAME);
      tester.assertOptionNotListed("targetStoryId", testProjectName + " :: " + testIterationName + " :: " + storyName);
   }
   
   /** Test org est hours in restarted iteration with added story.
     *
     * @throws Exception
     *             the exception
     */
   public void testOrgEstHoursInRestartedIterationWithAddedStory() throws Exception {
      performOrgEstHoursWithDeletedTaskTest();
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testIterationName);
      iterationTester.closeCurrentIteration();
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testIterationName);
      tester.addUserStory(STORY_NAME, "", ESTIMATED_HOURS_FOR_ADDED_OBJECT, "1");
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testIterationName);
      iterationTester.startCurrentIteration();
      tester.clickLinkWithKey("iteration.link.accuracy");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.story",
                                                           "35.5");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.task",
                                                           ESTIMATED_ORIGINAL_HOURS);
   }
   
   /** Test org est hours in restarted iteration with deleted and added
     * task.
     *
     * @throws Exception
     *             the exception
     */
   public void testOrgEstHoursInRestartedIterationWithDeletedAndAddedTask() throws Exception {
      performOrgEstHoursWithDeletedTaskTest();
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testIterationName);
      iterationTester.closeCurrentIteration();
      goToTestStoryPage();
      tester.addTask(TASK_NAME, developerName, TASK_DESCRIPTION, ESTIMATED_HOURS_FOR_ADDED_OBJECT);
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testIterationName);
      iterationTester.startCurrentIteration();
      tester.clickLinkWithKey("iteration.link.accuracy");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.story",
                                                           "15.5");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.task",
                                                           ESTIMATED_ORIGINAL_HOURS);
   }
   
   /** Test org est hours in started iteration with deleted task.
     *
     * @throws Exception
     *             the exception
     */
   public void testOrgEstHoursInStartedIterationWithDeletedTask() throws Exception {
      performOrgEstHoursWithDeletedTaskTest();
   }
   
   /** Perform org est hours with deleted task test.
     *
     * @throws Exception
     *             the exception
     */
   private void performOrgEstHoursWithDeletedTaskTest() throws Exception {
      addTaskToCurrentStory();
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testIterationName);
      iterationTester.startCurrentIteration();
      tester.clickLinkWithKey("iteration.link.accuracy");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.story",
                                                           "15.5");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.task",
                                                           ESTIMATED_ORIGINAL_HOURS);
      goToTestStoryPage();
      tester.clickDeleteLinkForRowWithText(TASK_NAME);
      tester.assertTextNotPresent(TASK_NAME);
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testIterationName);
      tester.clickLinkWithKey("iteration.link.accuracy");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.story",
                                                           "15.5");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.task",
                                                           ESTIMATED_ORIGINAL_HOURS);
   }
   
   /** Test org est hours while moved from not started to started iteration.
     *
     * @throws Exception
     *             the exception
     */
   public void testOrgEstHoursWhileMovedFromNotStartedToStartedIteration() throws Exception {
      goToTestStoryPage();
      tester.addTask(TASK_NAME, developerName, TASK_DESCRIPTION, ESTIMATED_HOURS);
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testIterationName);
      iterationTester.startCurrentIteration();
      tester.gotoProjectsPage();
      tester.clickLinkWithText(project.getName());
      iterationTester.addIteration(ITERATION_NAME_FOR_ORG_EST_TEST,
                                   tester.dateStringForNDaysAway(1),
                                   tester.dateStringForNDaysAway(15),
                                   "desc.");
      tester.clickLinkWithText(ITERATION_NAME_FOR_ORG_EST_TEST);
      tester.addUserStory(STORY_NAME_FOR_ORG_EST_TEST, "desc.", "10", "1");
      tester.clickLinkWithText(STORY_NAME_FOR_ORG_EST_TEST);
      tester.addTask(TASK_NAME, developerName, TASK_DESCRIPTION, ESTIMATED_HOURS);
      tester.gotoProjectsPage();
      tester.clickLinkWithText(project.getName());
      tester.clickLinkWithText(ITERATION_NAME_FOR_ORG_EST_TEST);
      tester.clickContinueLinkInRowWithText(STORY_NAME_FOR_ORG_EST_TEST);
      tester.assertOnMoveContinueStoryPage(STORY_NAME_FOR_ORG_EST_TEST);
      tester.selectOption("targetIterationId", project.getName() + " :: " + iteration.getName());
      tester.clickButton(MoveContinueStoryAction.MOVE_ACTION);
      iterationTester.assertOnIterationPage();
      tester.gotoProjectsPage();
      tester.clickLinkWithText(project.getName());
      tester.clickLinkWithText(iteration.getName());
      tester.clickLinkWithKey("iteration.link.accuracy");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.story",
                                                           STORY_ESTIMATED_ORIGINAL_HOURS);
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.task",
                                                           TASK_ESTIMATED_ORIGINAL_HOURS);
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           2,
                                                           "iteration.label.story",
                                                           ADDED_STORY_HOURS);
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           2,
                                                           "iteration.label.task",
                                                           ADDED_TASK_HOURS);
      tester.gotoProjectsPage();
      tester.clickLinkWithText(project.getName());
      tester.clickLinkWithText(iteration.getName());
      tester.clickLinkWithText(STORY_NAME_FOR_ORG_EST_TEST);
      tester.addTask(TASK_NAME, developerName, TASK_DESCRIPTION, ESTIMATED_HOURS);
      tester.clickLinkWithText(project.getName());
      tester.clickLinkWithText(iteration.getName());
      tester.clickLinkWithKey("iteration.link.accuracy");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.story",
                                                           STORY_ESTIMATED_ORIGINAL_HOURS);
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.task",
                                                           TASK_ESTIMATED_ORIGINAL_HOURS);
   }
   
   /** Test pdf export.
     *
     * @throws Exception
     *             the exception
     */
   public void testPdfExport() throws Exception {
      checkExportUri("userstory", "pdf");
   }
   
   /** Test report export.
     *
     * @throws Exception
     *             the exception
     */
   public void testReportExport() throws Exception {
      checkExportUri("userstory", "jrpdf");
   }
   
   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#traverseLinkWithKeyAndReturn(java.lang.String)
    */
   protected void traverseLinkWithKeyAndReturn(String key) throws Exception {
      tester.clickLinkWithKey(key);
      tester.gotoPage("view", "projects", 0);
   }
}