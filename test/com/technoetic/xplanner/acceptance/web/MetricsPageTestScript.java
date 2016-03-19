package com.technoetic.xplanner.acceptance.web;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;


/**
 * The Class MetricsPageTestScript.
 */
public class MetricsPageTestScript extends AbstractPageTestScript
{
    
    /** The sys admin. */
    private final String sysAdmin = "sysadmin";
    
    /** The tester tt. */
    private final String testerTT = "TT";
    
    /** The tester yy. */
    private final String testerYY = "YY";
    
    /** The tt task estimated hours. */
    private final String ttTaskEstimatedHours = "7.7";
    
    /** The tester task. */
    private final String testerTask = "TesterTask";
    
    /** The other developer task name. */
    private final String otherDeveloperTaskName = "other developer's task";
    
    /** The another test task name. */
    private final String anotherTestTaskName = "Another Task";
    
    /** The second story name. */
    private final String secondStoryName = "Second Story";
    
    /** The third story name. */
    private final String thirdStoryName = "Third Story";
    
    /** The first task in third story. */
    private final String firstTaskInThirdStory = "Third Story First Task";
    
    /** The second task in third story. */
    private final String secondTaskInThirdStory = "Third Story Second Task";
    
    /** The test task id. */
    private String testTaskId = null;
    
    /** The other developer task id. */
    private String otherDeveloperTaskId = null;
    
    /** The another test task id. */
    public String anotherTestTaskId = null;

    /** Instantiates a new metrics page test script.
     *
     * @param test
     *            the test
     */
    public MetricsPageTestScript(String test)
    {
        super(test);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
     */
    public void setUp() throws Exception
    {
        super.setUp();
        simpleSetUp();
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#tearDown()
     */
    public void tearDown() throws Exception
    {
        deleteLocalObjects();
        super.simpleTearDown();
        super.tearDown();
    }

    /** Go to test iteration metrics page.
     *
     * @throws Exception
     *             the exception
     */
    public void goToTestIterationMetricsPage() throws Exception
    {
        tester.gotoProjectsPage();
        tester.clickLinkWithText(testProjectName);
        tester.clickLinkWithText(testIterationName);
        tester.clickLinkWithKey("iteration.link.metrics");
    }

   /** Test content and links.
     *
     * @throws Exception
     *             the exception
     */
   public void testContentAndLinks() throws Exception
   {
       goToTestIterationMetricsPage();
       tester.assertKeyPresent("iteration.metrics.prefix");
       tester.assertKeyPresent("iteration.metrics.total_hours");
   }

    /** Test actual time.
     *
     * @throws Exception
     *             the exception
     */
    public void testActualTime() throws Exception
    {
        //goToTestStoryPage();
        testTaskId = tester.addTask(testTaskName, developerName, testTaskDescription, testTaskEstimatedHours);
        tester.clickLinkWithText(testTaskName);

        tester.clickLinkWithKey("action.edittime.task");
        tester.setTimeEntry(0, 0, 4, developer.getName());
        iterationTester.assertOnStartIterationPromptPageAndStart();

//        tester.clickEditTimeImage();
        tester.clickLinkWithKey("action.edittime.task");
        tester.setTimeEntry(1, 6, 11, developer.getName());
        goToTestIterationMetricsPage();

        Double workedHoursForOwnTasks = new Double(9.0);
        assertMetricsCellValueForRowWithKey("totalTable",
                                            developerName,
                                            "iteration.metrics.tableheading.total",
                                            workedHoursForOwnTasks);
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            this.developerName,
                                            "iteration.metrics.tableheading.worked_hours",
                                            workedHoursForOwnTasks);
        goToTestStoryPage();
        otherDeveloperTaskId = tester.addTask(otherDeveloperTaskName,
                                              sysAdmin,
                                              testTaskDescription,
                                              testTaskEstimatedHours);
        tester.clickLinkWithText(otherDeveloperTaskName);
        tester.clickLinkWithKey("action.edittime.task");
        tester.setTimeEntry(0, 0, 4, developer.getName());
        goToTestIterationMetricsPage();
        Double totalWorkedHours = new Double(13.0);
        assertMetricsCellValueForRowWithKey("totalTable",
                                            developerName,
                                            "iteration.metrics.tableheading.total",
                                            totalWorkedHours);
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            this.developerName,
                                            "iteration.metrics.tableheading.worked_hours",
                                            workedHoursForOwnTasks);
    }


    /** Test zero hour task.
     *
     * @throws Exception
     *             the exception
     */
    public void testZeroHourTask() throws Exception
    {
        goToTestStoryPage();
        tester.addTask(testTaskName, developerName, testTaskDescription, "0.0");
        goToTestIterationMetricsPage();
        tester.assertTextNotPresent(developerName);
        tester.assertTextPresent("Total Person Hours Worked: 0.0");
        tester.assertTextPresent("Total Paired Hours Percentage: 0.0%");
    }

    /** Test total hours table.
     *
     * @throws Exception
     *             the exception
     */
    public void testTotalHoursTable() throws Exception
    {
        personTester.addPerson(testerTT, "tt", "tt", "tt", "tt");
        tester.gotoProjectsPage();
        tester.clickLinkWithText(testIterationName);
        iterationTester.startCurrentIteration();
        updateStoryTracker(sysAdmin, storyName);

        goToTestStoryPage();
        anotherTestTaskId = tester.addTask(anotherTestTaskName, sysAdmin, testTaskDescription, testTaskEstimatedHours);
        tester.clickEnterTimeLinkInRowWithText(anotherTestTaskName);
        tester.setTimeEntry(0, 0, 4, sysAdmin);
        goToTestIterationMetricsPage();
        assertTotalWorkedHoursTable(sysAdmin, 4, 0, 4);

        goToTestStoryPage();
        testTaskId = tester.addTask(testTaskName, sysAdmin, testTaskDescription, testTaskEstimatedHours);
        tester.clickEnterTimeLinkInRowWithText(testTaskName);
        tester.setTimeEntry(0, 1, 4, sysAdmin, testerTT);
        goToTestIterationMetricsPage();
        assertTotalWorkedHoursTable(sysAdmin, 7, 3, 4);
        assertTotalWorkedHoursTable(testerTT, 3, 3, 0);
    }

    /** Assert total worked hours table.
     *
     * @param person
     *            the person
     * @param totalHours
     *            the total hours
     * @param pairedHours
     *            the paired hours
     * @param soloHours
     *            the solo hours
     * @throws Exception
     *             the exception
     */
    private void assertTotalWorkedHoursTable(String person, int totalHours, int pairedHours, int soloHours)
        throws Exception
    {
        assertMetricsCellValueForRowWithKey("totalTable",
                                            person,
                                            "iteration.metrics.tableheading.total",
                                            new Double(totalHours));
        assertMetricsCellValueForRowWithKey("totalTable",
                                            person,
                                            "iteration.metrics.tableheading.paired_hours",
                                            new Double(pairedHours));
        assertMetricsCellValueForRowWithKey("totalTable",
                                            person,
                                            "iteration.metrics.tableheading.solo_hours",
                                            new Double(soloHours));
    }

    /** Assert metrics cell value for row with key.
     *
     * @param tableId
     *            the table id
     * @param person
     *            the person
     * @param key
     *            the key
     * @param hours
     *            the hours
     */
    private void assertMetricsCellValueForRowWithKey(String tableId,
                                                     String person,
                                                     String key, Double hours)
    {
        if (hours.doubleValue() != 0.0d)
        {
            tester.assertCellNumberForRowWithTextAndColumnKeyEquals(tableId,
                                                                    person,
                                                                    key,
                                                                    hours);
        }
        else
        {
            tester.assertCellTextForRowWithTextAndColumnKeyEquals(tableId, person, key, "");
        }
    }

    /** Test hours accepted graph.
     *
     * @throws Exception
     *             the exception
     */
    public void testHoursAcceptedGraph() throws Exception
    {
        tester.gotoProjectsPage();
        tester.clickLinkWithText(testIterationName);
        updateStoryTracker(sysAdmin, storyName);
        goToTestIterationMetricsPage();
        final Double totalHours = new Double(estimatedHoursString);
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            sysAdmin,
                                            "iteration.metrics.tableheading.total",
                                            totalHours);
        tester.assertCellTextForRowWithTextAndColumnKeyEquals("acceptedTable",
                                                              sysAdmin,
                                                              "iteration.metrics.tableheading.worked_hours",
                                                              "");

        checkTaskHoursAfterAddingTasks();
        checkMetricsAfterAddingSecondPerson();
        checkMetricsAfterAddingSecondStory();
        checkMetricsAfterAddingThirdStory();
    }

    /** Delete local objects.
     */
    private void deleteLocalObjects()
    {
        tester.deleteObjects(Person.class, "name", testerTT);
        tester.deleteObjects(Person.class, "name", testerYY);
        if (testTaskId != null)
            deleteLocalTimeEntry(testTaskId);
        if (anotherTestTaskId != null)
            deleteLocalTimeEntry(anotherTestTaskId);
        tester.deleteObjects(Task.class, "name", anotherTestTaskName);

        tester.deleteObjects(Task.class, "name", testerTask);
        tester.deleteObjects(Task.class, "name", firstTaskInThirdStory);
        tester.deleteObjects(Task.class, "name", secondTaskInThirdStory);

        if (otherDeveloperTaskId != null)
            deleteLocalTimeEntry(otherDeveloperTaskId);
        tester.deleteObjects(Task.class, "name", otherDeveloperTaskName);

        tester.deleteObjects(UserStory.class, "name", secondStoryName);
        tester.deleteObjects(UserStory.class, "name", thirdStoryName);
    }

    /** Check metrics after adding third story.
     *
     * @throws Exception
     *             the exception
     */
    private void checkMetricsAfterAddingThirdStory() throws Exception
    {
        tester.gotoProjectsPage();
        tester.clickLinkWithText(testIterationName);
        tester.addUserStory(thirdStoryName, "Third Story Description", "21.0", "1");
        updateStoryTracker(testerYY, thirdStoryName);
        tester.assertOnStoryPage(thirdStoryName);
        tester.addTask(firstTaskInThirdStory, sysAdmin, "Some Description", "3");
        tester.addTask(secondTaskInThirdStory, testerYY, "Some Description", "7");

        goToTestIterationMetricsPage();
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            sysAdmin,
                                            "iteration.metrics.tableheading.total",
                                            new Double(23.5));
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            testerYY,
                                            "iteration.metrics.tableheading.total",
                                            new Double(18.0));
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            testerTT,
                                            "iteration.metrics.tableheading.total",
                                            new Double(ttTaskEstimatedHours));
    }

    /** Check metrics after adding second story.
     *
     * @throws Exception
     *             the exception
     */
    private void checkMetricsAfterAddingSecondStory() throws Exception
    {
        personTester.addPerson(testerYY, "yy", "yy", "yy", "yy");

        String secondStoryEstimatedHours = "11.0";
        String storyDescription = "Second Story Description";

        tester.gotoProjectsPage();
        tester.clickLinkWithText(testIterationName);
        tester.addUserStory(secondStoryName, storyDescription, secondStoryEstimatedHours, "1");
        updateStoryTracker(testerYY, secondStoryName);


        goToTestIterationMetricsPage();
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            sysAdmin,
                                            "iteration.metrics.tableheading.total",
                                            new Double(testTaskEstimatedHours));
        assertMetricsCellValueForRowWithKey("acceptedTable", testerTT,
                                            "iteration.metrics.tableheading.total",
                                            new Double(ttTaskEstimatedHours));
        assertMetricsCellValueForRowWithKey("acceptedTable", testerYY,
                                            "iteration.metrics.tableheading.total",
                                            new Double(secondStoryEstimatedHours));
    }

    /** Check metrics after adding second person.
     *
     * @throws Exception
     *             the exception
     */
    private void checkMetricsAfterAddingSecondPerson() throws Exception
    {
        String anotherTaskDescription = "Tester Task Description";
        personTester.addPerson(testerTT, "tt", "tt", "tt", "tt");
        goToTestStoryPage();
        tester.addTask(testerTask,
                       testerTT,
                       anotherTaskDescription,
                       ttTaskEstimatedHours);

        goToTestIterationMetricsPage();
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            sysAdmin,
                                            "iteration.metrics.tableheading.task",
                                            new Double(testTaskEstimatedHours));
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            testerTT,
                                            "iteration.metrics.tableheading.task",
                                            new Double(ttTaskEstimatedHours));
    }

    /** Check task hours after adding tasks.
     *
     * @throws Exception
     *             the exception
     */
    private void checkTaskHoursAfterAddingTasks() throws Exception
    {
        goToTestStoryPage();
        tester.addTask(testTaskName, sysAdmin, testTaskDescription, testTaskEstimatedHours);
        goToTestIterationMetricsPage();
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            sysAdmin,
                                            "iteration.metrics.tableheading.task",
                                            new Double(testTaskEstimatedHours));
    }

    /** Update story tracker.
     *
     * @param tracker
     *            the tracker
     * @param testStoryName
     *            the test story name
     */
    private void updateStoryTracker(String tracker, String testStoryName)
    {
        tester.clickLinkWithText(testStoryName);
        tester.assertTextPresent(testStoryName);
        tester.assertLinkPresentWithKey("action.edit.story");
        tester.clickLinkWithKey("action.edit.story");
        tester.selectOption("trackerId", tracker);
        tester.submit();
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#traverseLinkWithKeyAndReturn(java.lang.String)
     */
    protected void traverseLinkWithKeyAndReturn(String key) throws Exception
    {
        tester.clickLinkWithKey(key);
        tester.gotoPage("view", "projects", 0);
    }
}
