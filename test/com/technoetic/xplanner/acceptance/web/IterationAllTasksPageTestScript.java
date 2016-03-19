package com.technoetic.xplanner.acceptance.web;

import junitx.framework.Assert;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import com.meterware.httpunit.TableCell;
import com.meterware.httpunit.WebImage;
import com.technoetic.xplanner.views.IconConstants;


/**
 * The Class IterationAllTasksPageTestScript.
 */
public class IterationAllTasksPageTestScript extends AbstractPageTestScript
{
    
    /** The first story name. */
    public final String firstStoryName = "First Story";
    
    /** The second story. */
    public final String secondStory = "Second Story";
    
    /** The tt task in first story. */
    public final String ttTaskInFirstStory = "TT Task";
    
    /** The ss task in first story. */
    public final String ssTaskInFirstStory = "SS Task";
    
    /** The started task in second story. */
    public final String startedTaskInSecondStory = "Started Task";
    
    /** The non started task in second story. */
    public final String nonStartedTaskInSecondStory = "Non Started Task";
    
    /** The completed task in second story. */
    public final String completedTaskInSecondStory = "Completed Task";
    
    /** The all tasks table id. */
    public final String allTasksTableId = "objecttable";
    
    /** The status column key. */
    public final String statusColumnKey = "tasks.tableheading.status";
    
    /** The task name column key. */
    public final String taskNameColumnKey = "person.tableheading.task";
    
    /** The started task id. */
    public String startedTaskId;

    /** Instantiates a new iteration all tasks page test script.
     *
     * @param test
     *            the test
     */
    public IterationAllTasksPageTestScript(String test)
    {
        super(test);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
     */
    public void setUp() throws Exception
    {
        super.setUp();
        //tester.addProject(testProjectName, "Project Description");
        setUpTestProject();
        setUpTestPerson();
        setUpTestRole("editor");
        tester.login();
        tester.clickLinkWithText(project.getName());
        setUpTestIteration();
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#tearDown()
     */
    public void tearDown() throws Exception
    {
        deleteLocalObjects();
        super.tearDown();
    }

    /** Delete local objects.
     */
    private void deleteLocalObjects()
    {
        tester.deleteObjects(Task.class, "name", ssTaskInFirstStory);
        tester.deleteObjects(Task.class, "name", ttTaskInFirstStory);
        tester.deleteObjects(Task.class, "name", completedTaskInSecondStory);
        tester.deleteObjects(Task.class, "name", nonStartedTaskInSecondStory);
        tester.deleteObjects(Task.class, "name", startedTaskInSecondStory);
        tester.deleteObjects(UserStory.class, "name", firstStoryName);
        tester.deleteObjects(UserStory.class, "name", secondStory);
    }

    /** Sets the up test iteration.
     *
     * @throws Exception
     *             the exception
     */
    private void setUpTestIteration()
        throws Exception
    {
        tester.gotoProjectsPage();
        tester.clickLinkWithText(testProjectName);
        iterationTester.addIteration(testIterationName,
                            tester.dateStringForNDaysAway(15),
                            tester.dateStringForNDaysAway(28),
                            testIterationDescription);
        tester.clickLinkWithText(testIterationName);
        tester.addUserStory(firstStoryName, "First story with higher priority", "23.5", "1");
        tester.assertTextPresent(firstStoryName);
        tester.clickLinkWithText(firstStoryName);
        tester.assertOnStoryPage();
        tester.clickLinkWithKey("action.edit.story");
        tester.setWorkingForm("storyEditor");
        tester.setFormElement("priority", "3");
        tester.submit();
        addTaskToCurrentStory(ttTaskInFirstStory, "First task in first story", "15.5");
        addTaskToCurrentStory(ssTaskInFirstStory, "Second task in first story", "2.3");
        tester.clickLinkWithText(testIterationName);
    }

    /** Test all tasks page.
     *
     * @throws Exception
     *             the exception
     */
    public void testAllTasksPage() throws Exception
    {
        tester.addUserStory(secondStory, testStoryDescription, "10.0", "1");
        tester.assertTextPresent(secondStory);
        tester.clickLinkWithText(secondStory);
        tester.assertOnStoryPage();
        // add the first task to this story
        startedTaskId = addTaskToCurrentStory(startedTaskInSecondStory, "A started task", "3.5");
        tester.clickLinkWithText(startedTaskInSecondStory);

        tester.clickLinkWithKey("action.edittime.task");
        tester.setTimeEntry(0, 1, 2, developer.getName());
        iterationTester.assertOnStartIterationPromptPageAndStart();
        tester.assertOnTaskPage();
        tester.clickLinkWithText(secondStory);

        // add a second task to this story
        addTaskToCurrentStory(nonStartedTaskInSecondStory, "A non started task", "2.0");

        // add a third task to this story
        addTaskToCurrentStory(completedTaskInSecondStory, "completed task", "4.0");
        tester.clickLinkWithText(completedTaskInSecondStory);
        tester.completeCurrentTask();

       // go back to the iteration
        tester.clickLinkWithText(testIterationName);
        tester.clickLinkWithKey("iteration.link.all_tasks");
        tester.assertKeyNotPresent("security.notauthorized");

        verifyAllTasksPage();
        deleteLocalTimeEntry(startedTaskId);
    }

   /** Test edit task.
     */
   public void testEditTask(){
      tester.clickLinkWithKey("iteration.link.all_tasks");
      tester.clickEditLinkInRowWithText(ttTaskInFirstStory);
      tester.selectOption("acceptorId", tester.getXPlannerLoginId());
      tester.submit();
      tester.assertTextPresent(tester.getXPlannerLoginId());
   }

    /** Verify all tasks page.
     */
    private void verifyAllTasksPage()
    {
        tester.assertKeyPresent("iteration.alltasks.prefix");
        assertCellEquals(startedTaskInSecondStory, 1, taskNameColumnKey);
        assertCellContains(IconConstants.STATUS_STARTED_ICON, 1, statusColumnKey);
        assertCellEquals(ssTaskInFirstStory, 2, taskNameColumnKey);
        assertCellContains(IconConstants.STATUS_OPEN_ICON, 2, statusColumnKey);
        assertCellEquals(ttTaskInFirstStory, 3, taskNameColumnKey);
        assertCellContains(IconConstants.STATUS_OPEN_ICON, 3, statusColumnKey);
        assertCellEquals(nonStartedTaskInSecondStory, 4, taskNameColumnKey);
        assertCellContains(IconConstants.STATUS_OPEN_ICON, 4, statusColumnKey);

       assertCellEquals(completedTaskInSecondStory, 5, taskNameColumnKey);
        assertCellContains(IconConstants.STATUS_COMPLETED_ICON, 5, statusColumnKey);
    }

   /** Assert cell equals.
     *
     * @param expectedCellText
     *            the expected cell text
     * @param rowIndex
     *            the row index
     * @param columnKey
     *            the column key
     */
   private void assertCellEquals(String expectedCellText, int rowIndex, String columnKey) {
      tester.assertCellTextForRowIndexAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID, rowIndex, columnKey,
                                                         expectedCellText);
   }

   /** Assert cell contains.
     *
     * @param image
     *            the image
     * @param rowIndex
     *            the row index
     * @param columnKey
     *            the column key
     */
   private void assertCellContains(String image, int rowIndex, String columnKey) {
      TableCell cell = tester.getCell(XPlannerWebTester.MAIN_TABLE_ID, columnKey, rowIndex);
      WebImage[] images = cell.getImages();
      for (int i = 0; i < images.length; i++) {
         WebImage webImage = images[i];
         String src = webImage.getSource();
         if (src.endsWith(image)) return;
      }

      Assert.fail("Could not find image " + image + " in " + dumpImageArray(images));
   }

   /** Dump image array.
     *
     * @param images
     *            the images
     * @return the string
     */
   private String dumpImageArray(WebImage[] images) {
      StringBuffer buf = new StringBuffer();
      buf.append("[");
      for (int i = 0; i < images.length; i++) {
         if (i>0) buf.append(", ");
         buf.append(images[i].getSource());
      }
      buf.append("]");
      String imagesArrayString = buf.toString();
      return imagesArrayString;
   }

   /** Adds the task to current story.
     *
     * @param taskToAdd
     *            the task to add
     * @param description
     *            the description
     * @param estimatedHours
     *            the estimated hours
     * @return the string
     */
   private String addTaskToCurrentStory(String taskToAdd, String description, String estimatedHours)
    {
        String taskId = tester.addTask(taskToAdd, developerName, description, estimatedHours);
        tester.assertOnStoryPage();
        tester.assertTextPresent(taskToAdd);

        return taskId;
    }
}
