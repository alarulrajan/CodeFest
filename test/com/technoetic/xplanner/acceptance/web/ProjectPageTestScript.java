package com.technoetic.xplanner.acceptance.web;

import org.junit.Assert;

import net.sf.xplanner.domain.Project;

import com.technoetic.xplanner.XPlannerProperties;

/**
 * The Class ProjectPageTestScript.
 */
public class ProjectPageTestScript extends AbstractPageTestScript
{
    
    /** The start date. */
    private String startDate;
    
    /** The end date. */
    private String endDate;
    
    /** The test project. */
    private Project testProject;
    
    /** The test task id. */
    private String testTaskId;

    /** The wiki property. */
    private String wikiProperty = "twiki.scheme.wiki";
    
    /** The description element name. */
    private String descriptionElementName = "description";
    
    /** The wiki url element name. */
    private String wikiUrlElementName = "wikiUrl";
    
    /** The wiki link text. */
    private String wikiLinkText = "newLink";
    
    /** The new wiki url. */
    private String newWikiUrl = "http://newwikilocation/$1";

   /** Instantiates a new project page test script.
     *
     * @param test
     *            the test
     */
   public ProjectPageTestScript(String test)
   {
       super(test);
   }


    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
     */
    public void setUp() throws Exception
    {
        startDate = tester.dateStringForNDaysAway(0);
        endDate = tester.dateStringForNDaysAway(14);
        super.setUp();
        tester.login();
        setUpTestPerson();
        testProject = newProject();
        testProject.setName(testProjectName);
        testProject.setDescription(testProjectDescription);
        commitSession();
        tester.gotoProjectsPage();
        tester.clickLinkWithText(""+ testProject.getId());
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#tearDown()
     */
    public void tearDown() throws Exception
    {
        tester.logout();
        super.tearDown();
    }

    /** Test wiki rendering in description.
     */
    public void testWikiRenderingInDescription() {
       tester.gotoProjectsPage();
       tester.clickLinkWithText(String.valueOf(testProject.getId()));
       tester.clickLinkWithKey("action.edit.project");
       tester.setFormElement(descriptionElementName, "wiki:" + wikiLinkText);
       tester.submit();
       XPlannerProperties properties = new XPlannerProperties();
       String wikiDefaultLink = properties.getProperty(wikiProperty);
       String projectWikiLink = wikiDefaultLink.substring(0, wikiDefaultLink.length()-2) + wikiLinkText;
//       int index = tester.getDialog().getResponseText().indexOf(projectWikiLink);
//       assertTrue("There is no wiki link: " + projectWikiLink, index != -1);
Assert.fail();
       tester.clickLinkWithKey("action.edit.project");
       tester.setFormElement(wikiUrlElementName, newWikiUrl);
       tester.submit();
       projectWikiLink = newWikiUrl.substring(0, newWikiUrl.length()-2) + wikiLinkText;
//       index = tester.getDialog().getResponseText().indexOf(projectWikiLink);
//       assertTrue("There is no wiki link: " + projectWikiLink, index != -1);
    }

    /** Test action buttons on view page.
     *
     * @throws Exception
     *             the exception
     */
    public void testActionButtonsOnViewPage() throws Exception
    {
       tester.gotoProjectsPage();
       tester.clickLinkWithText(String.valueOf(testProject.getId()));
       tester.assertLinkPresentWithKey("action.edit.project");
       tester.assertLinkPresentWithKey("action.delete.project");
    }

    /** Test project attributes settings.
     *
     * @throws Exception
     *             the exception
     */
    public void testProjectAttributesSettings() throws Exception
    {
        //tester.gotoProjectsPage();
        //tester.clickLinkWithText(String.valueOf(testProject.getId()));
        tester.clickLinkWithKey("action.edit.project");
        tester.uncheckCheckbox("sendemail");
        tester.uncheckCheckbox("hidden");
        tester.uncheckCheckbox("optEscapeBrackets");
        tester.setFormElement("wikiUrl", testWikiUrl);
        tester.submit();
        tester.gotoProjectsPage();
        tester.clickLinkWithText(String.valueOf(testProject.getId()));
        tester.clickLinkWithKey("action.edit.project");
        tester.assertFormElementEquals("wikiUrl", testWikiUrl);
        tester.assertCheckboxNotSelected("sendemail");
        tester.assertCheckboxNotSelected("hidden");
        tester.assertCheckboxNotSelected("optEscapeBrackets");
    }


    /** Test project page content and links.
     *
     * @throws Exception
     *             the exception
     */
    public void testProjectPageContentAndLinks() throws Exception
    {
        tester.assertOnProjectPage();
        tester.assertTextPresent(testProjectName);
        tester.assertTextPresent(testProjectDescription);
        tester.assertKeyPresent("iterations.none");
        tester.assertLinkPresentWithKey("action.edit.project");
        tester.assertLinkPresentWithKey("project.link.create_iteration");
    }

    /** _test adding editing and deleting iterations.
     */
    //FIXME Commented out due to failure on the build machine
    public void _testAddingEditingAndDeletingIterations()
    {
        iterationTester.addIteration(testIterationName,
                            startDate,
                            endDate,
                            testIterationDescription);
        assertIterationPresent(testIterationName, "inactive", startDate, endDate, "0.0",
                               "0.0", "0.0", "0.0");
        String originalStoryEstimation = "0.0";
        double taskEntryDuration = 2.0;
        String expectedEstimatedHours = testTaskEstimatedHours;
        String expectedRemainingHours = Double.toString(Double.parseDouble(testTaskEstimatedHours) - taskEntryDuration);
        initializeIteration(originalStoryEstimation, testTaskEstimatedHours, (int) taskEntryDuration);
        assertIterationPresent(testIterationName, "active", startDate, endDate, "0.0",
                               "" + taskEntryDuration, expectedEstimatedHours, expectedRemainingHours);

        tester.clickEditLinkInRowWithText(testIterationName);
        String newIterationName = "A Different Iteration Name";
        tester.setFormElement("name", newIterationName);
        tester.submit();
        tester.assertTextPresent(newIterationName);

        deleteLocalTimeEntry(testTaskId);
        tearDownTestStory();
        tester.clickDeleteLinkForRowWithText(newIterationName);
        tester.assertTextNotPresent(newIterationName);
    }


    /** Test adding and deleting notes.
     *
     * @throws Exception
     *             the exception
     */
    public void testAddingAndDeletingNotes() throws Exception
    {
        runNotesTests(XPlannerWebTester.PROJECT_PAGE);
    }

    /** Test xml export.
     *
     * @throws Exception
     *             the exception
     */
    public void testXmlExport() throws Exception
    {
        checkExportUri("project", "xml");
    }

    /** Test mpx export.
     *
     * @throws Exception
     *             the exception
     */
    public void testMpxExport() throws Exception
    {
        checkExportUri("project", "mpx");
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#traverseLinkWithKeyAndReturn(java.lang.String)
     */
    protected void traverseLinkWithKeyAndReturn(String key) throws Exception
    {
        tester.clickLinkWithKey(key);
        tester.gotoPage("view", "projects", 0);
    }

    /** Initialize iteration.
     *
     * @param originalStoryEstimation
     *            the original story estimation
     * @param taskEstimation
     *            the task estimation
     * @param taskEntryDuration
     *            the task entry duration
     */
    private void initializeIteration(String originalStoryEstimation, String taskEstimation, int taskEntryDuration)
    {
        tester.clickLinkWithText(testIterationName);
        iterationTester.startCurrentIteration();
        String testStoryId = tester.addUserStory(storyName, testStoryDescription, originalStoryEstimation, "1");
        tester.clickLinkWithText(testStoryId);
        testTaskId = tester.addTask(testTaskName, developerName, testTaskDescription, taskEstimation);
        tester.clickLinkWithText(testTaskId);
        tester.assertOnTaskPage();
        tester.assertTextPresent(testTaskName);
        tester.assertLinkPresentWithKey("task.link.edit_time");
        tester.clickLinkWithKey("task.link.edit_time");
        tester.setTimeEntry(0, 0, taskEntryDuration, developerInitials);
        tester.clickLinkWithText(testProjectName);
    }

    /** Assert iteration present.
     *
     * @param iterationName
     *            the iteration name
     * @param status
     *            the status
     * @param startDate
     *            the start date
     * @param endDate
     *            the end date
     * @param daysWorked
     *            the days worked
     * @param actualHours
     *            the actual hours
     * @param estimatedHours
     *            the estimated hours
     * @param remainingHours
     *            the remaining hours
     */
    private void assertIterationPresent(String iterationName, String status, String startDate,
                                        String endDate, String daysWorked, String actualHours,
                                        String estimatedHours, String remainingHours)
    {
        tester.assertKeyNotPresent("iterations.none");
        tester.assertKeyPresent("iterations.tableheading.iteration");
        tester.assertKeyPresent("iterations.tableheading.startDate");
        tester.assertKeyPresent("iterations.tableheading.endDate");
        tester.assertKeyPresent("iterations.tableheading.days_worked");
        //ChangeSoon The following columns on iteration page are taken out for performance reason
//        if (!"image".equals(new com.technoetic.xplanner.XPlannerProperties().getProperty("xplanner.progressbar.impl"))) {
//            tester.assertKeyPresent("iterations.tableheading.actual_hours");
//        }
//        tester.assertKeyPresent("iterations.tableheading.estimated_hours");
//        tester.assertKeyPresent("iterations.tableheading.remaining_hours");
        /*tester.assertKeyPresent("iterations.tableheading.orig_estimated_hours");
        tester.assertKeyPresent("iterations.tableheading.overestimated_hours");
        tester.assertKeyPresent("iterations.tableheading.orig_overestimated_hours");
        tester.assertKeyPresent("iterations.tableheading.underestimated_hours");
        tester.assertKeyPresent("iterations.tableheading.orig_underestimated_hours");
        tester.assertKeyPresent("iterations.tableheading.added_hours");
        tester.assertKeyPresent("iterations.tableheading.orig_added_hours");
        tester.assertKeyPresent("iterations.tableheading.postponed_hours");
        tester.assertKeyPresent("iterations.tableheading.actions");*/
        tester.assertCellTextForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID, iterationName,
                                                              "iterations.tableheading.startDate", startDate);
        tester.assertCellTextForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID, iterationName,
                                                              "iterations.tableheading.endDate", endDate);
        tester.assertCellTextForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID, iterationName,
                                                              "iterations.tableheading.days_worked", daysWorked);
        //ChangeSoon The following columns on iteration page are taken out for performance reason
//        if (!"image".equals(new com.technoetic.xplanner.XPlannerProperties().getProperty("xplanner.progressbar.impl"))) {
//            tester.assertCellTextForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID, iterationName,
//                    "iterations.tableheading.actual_hours", actualHours);
//        }
//        tester.assertCellTextForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID, iterationName,
//                "iterations.tableheading.estimated_hours", estimatedHours);
//        tester.assertCellTextForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID, iterationName,
//                "iterations.tableheading.remaining_hours", remainingHours);
    }
}
