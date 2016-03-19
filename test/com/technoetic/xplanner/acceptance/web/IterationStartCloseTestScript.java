package com.technoetic.xplanner.acceptance.web;

import java.text.MessageFormat;
import java.util.Date;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.acceptance.DataSampleTester;
import com.technoetic.xplanner.testing.DateHelper;
import com.technoetic.xplanner.views.IterationPage;

//DEBT Review how these could be duplicated so badly with all Iteration test scripts

/**
 * The Class IterationStartCloseTestScript.
 */
public class IterationStartCloseTestScript extends AbstractIterationTestScript {
   
   /** The Constant LOG. */
   public static final Logger LOG = Logger.getLogger(IterationStartCloseTestScript.class);
   
   /** The start date. */
   private Date startDate;
   
   /** The end date. */
   private Date endDate;
   
   /** The tester name in viewer role. */
   private String testerNameInViewerRole;
   
   /** The tester name in editor role. */
   private String testerNameInEditorRole;
   
   /** The data sample tester. */
   private DataSampleTester dataSampleTester = new DataSampleTester(iterationTester);

   /** Instantiates a new iteration start close test script.
     *
     * @param test
     *            the test
     */
   public IterationStartCloseTestScript(String test) {
      super(test);
      LOG.debug("initialized using IterationStartCloseTestScript(String)");
   }

   /** Instantiates a new iteration start close test script.
     */
   public IterationStartCloseTestScript() {
      LOG.debug("initialized using IterationStartCloseTestScript()");
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.web.AbstractIterationTestScript#setUp()
    */
   public void setUp() throws Exception {
//      new Timer().run("setUp", new Callable() {
//         public Object run() throws Exception {
//            mySetUp();
//            return null;
//         }
//      });
//   }
//
//   private void mySetUp() throws Exception {
      LOG.debug("setUp()...");
      super.setUp();
      LOG.debug("call setUpTestStoryAndTask()...");
      setUpTestStoryAndTask();
      startDate = DateHelper.getDateDaysFromToday(0);
      endDate = DateHelper.getDateDaysFromToday(14);
      testerNameInEditorRole = generateUniqueName("Admin");
      testerNameInViewerRole = generateUniqueName("Viewer");
      dataSampleTester.setUp();
      iteration = new Iteration();
      iteration.setId(Integer.parseInt(iterationId));
      iteration.setStartDate(startDate);
      iteration.setEndDate(endDate);
      LOG.debug("setUp()...done");
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.web.AbstractIterationTestScript#tearDown()
    */
   public void tearDown() throws Exception {
//      new Timer().run("tearDown", new Callable() {
//         public Object run() throws Exception {
//            myTearDown();
//            return null;
//         }
//      });
//   }
//
//
//   public void myTearDown() throws Exception {
      LOG.debug("tearDown()...");
      super.tearDown();
      LOG.debug("tearDown()...done");
      tester.tearDown();

//      deleteLocalTimeEntry(taskId);
//      tearDownTestProject();
//      super.tearDown();
//      tearDownTestPerson();
   }

   /** Assert no future iteration available.
     */
   private void assertNoFutureIterationAvailable() {
      tester.assertKeyPresent("iteration.continue.unfinished.stories");
      tester.assertKeyPresent("iteration.status.editor.no_future_iteration");
   }

   /** Test on iteration start propose to close already started iterations
     * only if there are.
     *
     * @throws Exception
     *             the exception
     */
   public void testOnIterationStartProposeToCloseAlreadyStartedIterationsOnlyIfThereAre() throws Exception {
      String iterationName_1 = testIterationName;
      testIterationName += "_2";
      setUpTestIteration();
      tester.clickLinkWithKey(IterationPage.START_ACTION);
      iterationTester.assertOnIterationPage();
      iterationTester.assertCurrentIterationStarted();

      goToIteration(iterationName_1);
      iterationTester.assertCurrentIterationClosed();
      tester.clickLinkWithKey(IterationPage.START_ACTION);

      iterationTester.assertOnStartIterationPromptPage();
      tester.assertKeyNotPresent("iteration.status.editor.message_1"); // Only from edit time
      String message = tester.getMessage("iteration.status.editor.message_2");
      message = MessageFormat.format(message, new Object[]{"1"});
      tester.assertTextPresent(message);
      tester.assertKeyPresent("iteration.status.editor.message_3");
      tester.assertCheckboxSelected("closeIterations");
      tester.submit("start");

      iterationTester.assertOnIterationPage();
      iterationTester.assertCurrentIterationStarted();
      goToIteration(testIterationName);
      iterationTester.assertCurrentIterationClosed();
   }

   /** Go to iteration.
     *
     * @param iterationName_1
     *            the iteration name_1
     */
   private void goToIteration(String iterationName_1) {
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testProjectName);
      tester.clickLinkWithText(iterationName_1);
   }

   /** Test starting and stopping iteration.
     *
     * @throws Exception
     *             the exception
     */
   public void testStartingAndStoppingIteration() throws Exception {
      LOG.debug("testStartingAndStoppingIteration()...");
      iterationTester.assertOnIterationPage();
      iterationTester.startCurrentIteration();
      tester.clickLinkWithText(testProjectName);
      tester.assertOnProjectPage();
      tester.clickLinkWithText(testIterationName);
      tester.assertTextPresent("" + testTaskEstimatedHours); //tasksEstimate
      iterationTester.closeCurrentIteration();
      assertNoFutureIterationAvailable();
      tester.clickLinkWithText(testProjectName);
      tester.clickLinkWithText(testIterationName);
      dataSampleTester.collectDataSamples(iteration);
      dataSampleTester.assertDataSample("Estimated Hours",
                                        tester.dateStringForNDaysAway(0),
                                        INITIAL_TASK_ESTIMATED_HOURS);
      dataSampleTester.assertDataSample("Actual Hours", tester.dateStringForNDaysAway(0), 0);
      dataSampleTester.assertDataSample("Estimated Hours",
                                        tester.dateStringForNDaysAway(1),
                                        INITIAL_TASK_ESTIMATED_HOURS);
      dataSampleTester.assertDataSample("Actual Hours", tester.dateStringForNDaysAway(1), 0);
      tester.clickLinkWithText(testProjectName);
      LOG.debug("testStartingAndStoppingIteration()...done");
   }

   /** Test auto start iteration.
     *
     * @throws Exception
     *             the exception
     */
   public void testAutoStartIteration() throws Exception {
      LOG.debug("testAutoStartIteration()...");
      iterationTester.assertOnIterationPage();
      startIterationByEnteringTimeEntries();
      tester.clickLinkWithText(testProjectName);
      tester.clickLinkWithText(testIterationName);
      tester.assertTextPresent("" + testTaskEstimatedHours); //tasksEstimate
      dataSampleTester.collectDataSamples(iteration);
      dataSampleTester.assertDataSample("Estimated Hours",
                                        tester.dateStringForNDaysAway(0),
                                        INITIAL_TASK_ESTIMATED_HOURS);
      dataSampleTester.assertDataSample("Actual Hours", tester.dateStringForNDaysAway(0), 0.0);
      tester.clickLinkWithText(testProjectName);
      LOG.debug("testAutoStartIteration()...done");
   }

   /** Test cancel auto start iteration_ time entries should not be added.
     *
     * @throws Exception
     *             the exception
     */
   public void testCancelAutoStartIteration_TimeEntriesShouldNotBeAdded() throws Exception {
      LOG.debug("testCancelAutoStartIteration_TimeEntriesShouldNotBeAdded()...");
      iterationTester.assertOnIterationPage();
      gotoTestTask();
      tester.clickLinkWithKey("action.edittime.task");
      tester.setTimeEntry(0, 0, 19, developer.getName());
      tester.clickLinkWithKey("navigation.iteration");
      gotoTestTask();
      tester.assertTextNotPresent("19.0");
      tester.clickLinkWithText(testProjectName);
      LOG.debug("testCancelAutoStartIteration_TimeEntriesShouldNotBeAdded()...done");
   }

   /** Start iteration by entering time entries.
     */
   private void startIterationByEnteringTimeEntries() {
      LOG.debug("startIterationByEnteringTimeEntries()...");
      gotoTestTask();
      tester.clickLinkWithKey("action.edittime.task");
      int hoursToLog = 20;
      int slot = 0;
      tester.assertKeyPresent("timelog.editor.edit_prefix");
      tester.setTimeEntry(slot, 0, hoursToLog, developer.getName());
      iterationTester.assertOnStartIterationPromptPageAndStart();
      LOG.debug("startIterationByEnteringTimeEntries()...done");
      gotoTestTask();
   }

   /** Test restart iteration.
     *
     * @throws Exception
     *             the exception
     */
   public void testRestartIteration() throws Exception {
      LOG.debug("testRestartIteration()...");
      iterationTester.assertOnIterationPage();
      enterTimeForTask(0, 0, 10);
      LOG.debug("Start iteration");
      iterationTester.assertOnStartIterationPromptPageAndStart();

      LOG.debug("Check iteration is started");
      tester.clickLinkWithText(testProjectName);

      LOG.debug("Close iteration");
      tester.clickLinkWithText(testIterationName);
      tester.assertTextPresent("" + testTaskEstimatedHours); //tasksEstimate
      iterationTester.closeCurrentIteration();
      assertNoFutureIterationAvailable();
      tester.clickLinkWithText(testProjectName);
      tester.assertOnProjectPage();

      LOG.debug("Check iteration is stopped");
      tester.clickLinkWithText(testIterationName);

      LOG.debug("Enter time");
      enterTimeForTask(1, 10, 10);

      LOG.debug("Check restart option presented and Restart iteration");
      iterationTester.assertOnStartIterationPromptPageAndStart();

      LOG.debug("Check iteration is started");
      tester.clickLinkWithText(testProjectName);

      LOG.debug("Verify estiamte");
      tester.clickLinkWithText(testIterationName);
      tester.assertTextPresent("" + testTaskEstimatedHours); //tasksEstimate
      iterationTester.closeCurrentIteration();
      tester.clickLinkWithText(testProjectName);
      tester.assertOnProjectPage();
      tester.clickLinkWithText(testIterationName);
      dataSampleTester.collectDataSamples(iteration);
      dataSampleTester.assertDataSample("Estimated Hours",
                                        tester.dateStringForNDaysAway(0),
                                        INITIAL_TASK_ESTIMATED_HOURS);
      dataSampleTester.assertDataSample("Estimated Hours",
                                        tester.dateStringForNDaysAway(1),
                                        INITIAL_TASK_ESTIMATED_HOURS);

      dataSampleTester.assertDataSample("Actual Hours", tester.dateStringForNDaysAway(0), 10);
      dataSampleTester.assertDataSample("Actual Hours", tester.dateStringForNDaysAway(1), 20);

      tester.clickLinkWithText(testProjectName);
      LOG.debug("testRestartIteration()...done");
   }

   /** Test restart iteration_ in the next day.
     *
     * @throws Exception
     *             the exception
     */
   public void testRestartIteration_InTheNextDay() throws Exception {
      LOG.debug("testRestartIteration_InTheNextDay()...");
      LOG.debug("Start Smtp server to support notification func. while putTheClockForward(...) called");

      //-------- DAY 0 -----------
      iterationTester.assertOnIterationPage();
      enterTimeForTask(0, 0, 5);
      LOG.debug("Start iteration");
      iterationTester.assertOnStartIterationPromptPageAndStart();
      LOG.debug("Check iteration is started");
      tester.clickLinkWithText(testProjectName);
      tester.clickLinkWithText(testIterationName);
      enterTimeForTask(1, 0 + 5, 5);
      dataSampleTester.moveCurrentDayAndGenerateDataSample(1);

      //-------- DAY 1 -----------
      tester.clickLinkWithText(testIterationName);
      enterTimeForTask(2, 0 + 5 + 5, 5);
      tester.clickLinkWithText(testIterationName);
      tester.assertTextPresent("" + testTaskEstimatedHours); //tasksEstimate
      iterationTester.assertOnIterationPage();
      LOG.debug("Close iteration");
      iterationTester.closeCurrentIteration();
      assertNoFutureIterationAvailable();
      tester.clickLinkWithText(testProjectName);
      LOG.debug("Check iteration is stopped");
      LOG.debug("Put the clock 2 days forward");
      dataSampleTester.moveCurrentDayAndGenerateDataSample(1);

      //-------- DAY 2 -----------
      LOG.debug("Go to iteration");
      tester.clickLinkWithText(testIterationName);
      LOG.debug("Enter time for task");
      enterTimeForTask(3, 0 + 5 + 5 + 5, 5);
      LOG.debug("Check if the user has been redirected to start iteration prompt page");
      iterationTester.assertOnStartIterationPromptPageAndStart();

      LOG.debug("Check iteration is started");
      tester.clickLinkWithText(testProjectName);

      LOG.debug("Verify estiamte");
      tester.clickLinkWithText(testIterationName);
      tester.assertTextPresent("" + testTaskEstimatedHours); //tasksEstimate
      iterationTester.assertOnIterationPage();

      dataSampleTester.collectDataSamples(iteration);
      dataSampleTester.assertDataSample("Estimated Hours",
                                        tester.dateStringForNDaysAway(0),
                                        INITIAL_TASK_ESTIMATED_HOURS);
      dataSampleTester.assertNoDataSample("Estimated Hours",
                                        tester.dateStringForNDaysAway(1));
      dataSampleTester.assertDataSample("Estimated Hours",
                                        tester.dateStringForNDaysAway(2),
                                        INITIAL_TASK_ESTIMATED_HOURS);

      dataSampleTester.assertDataSample("Actual Hours", tester.dateStringForNDaysAway(0), 0.0);
      dataSampleTester.assertNoDataSample("Actual Hours", tester.dateStringForNDaysAway(1));
      dataSampleTester.assertDataSample("Actual Hours", tester.dateStringForNDaysAway(2), 15);


      tester.clickLinkWithText(testProjectName);
      LOG.debug("Stop Smtp server to support notification func. while putTheClockForward(...) called");
      LOG.debug("testRestartIteration_InTheNextDay()...done");
   }

   /** Test restart iteration_ in the same day.
     *
     * @throws Exception
     *             the exception
     */
   public void testRestartIteration_InTheSameDay() throws Exception {
      LOG.debug("testRestartIteration_InTheSameDay()...");
      iterationTester.assertOnIterationPage();
      enterTimeForTask(0, 0, 5);
      LOG.debug("Start iteration");
      iterationTester.assertOnStartIterationPromptPageAndStart();
      tester.clickLinkWithText(testProjectName);
      LOG.debug("Check iteration is started");
      tester.clickLinkWithText(testIterationName);
      enterTimeForTask(1, 5, 5);
      dataSampleTester.moveCurrentDayAndGenerateDataSample(1);
      tester.clickLinkWithText(testIterationName);
      enterTimeForTask(2, 10, 5);
      tester.clickLinkWithText(testIterationName);
      tester.assertTextPresent("" + testTaskEstimatedHours); //tasksEstimate
      iterationTester.assertOnIterationPage();
      LOG.debug("Close iteration");
      iterationTester.closeCurrentIteration();
      assertNoFutureIterationAvailable();
      tester.clickLinkWithText(testProjectName);
      LOG.debug("Check iteration is stopped");
      tester.clickLinkWithText(testIterationName);
      enterTimeForTask(3, 15, 5);
      iterationTester.assertOnStartIterationPromptPageAndStart();

      LOG.debug("Check iteration is started");
      tester.clickLinkWithText(testProjectName);

      LOG.debug("Verify estiamte");
      tester.clickLinkWithText(testIterationName);
      tester.assertTextPresent("" + testTaskEstimatedHours); //tasksEstimate
      iterationTester.assertOnIterationPage();

      dataSampleTester.collectDataSamples(iteration);
      dataSampleTester.assertDataSample("Estimated Hours",
                                        tester.dateStringForNDaysAway(0),
                                        INITIAL_TASK_ESTIMATED_HOURS);
      dataSampleTester.assertDataSample("Estimated Hours",
                                        tester.dateStringForNDaysAway(1),
                                        INITIAL_TASK_ESTIMATED_HOURS);
      dataSampleTester.assertDataSample("Estimated Hours",
                                        tester.dateStringForNDaysAway(2),
                                        INITIAL_TASK_ESTIMATED_HOURS);


      dataSampleTester.assertDataSample("Actual Hours", tester.dateStringForNDaysAway(0), 0.0);
      dataSampleTester.assertDataSample("Actual Hours", tester.dateStringForNDaysAway(1), 15.0);
      dataSampleTester.assertDataSample("Actual Hours", tester.dateStringForNDaysAway(2), 15.0);

      tester.clickLinkWithText(testProjectName);

      LOG.debug("testRestartIteration_InTheSameDay()...done");
   }

   /** Test change iteration status not in editor role.
     *
     * @throws Exception
     *             the exception
     */
   public void testChangeIterationStatusNotInEditorRole() throws Exception {
      LOG.debug("testChangeIterationStatusNotInEditorRole()...");
      try {
         setUpTesterPerson(testerNameInViewerRole, "Viewer");
         setUpTesterPerson(testerNameInEditorRole, "Admin");

         logInDifferentRole(testerNameInViewerRole);
         tester.assertKeyNotPresent(IterationPage.START_ACTION);

         logInDifferentRole(testerNameInEditorRole);
         tester.assertKeyPresent(IterationPage.START_ACTION);
         tester.logout();
      } finally {
         tearDownTesterPerson(testerNameInEditorRole);
         tearDownTesterPerson(testerNameInViewerRole);
      }
      LOG.debug("testChangeIterationStatusNotInEditorRole()...done");
   }

   /** Log in different role.
     *
     * @param userName
     *            the user name
     * @throws Exception
     *             the exception
     */
   private void logInDifferentRole(String userName) throws Exception {
      tester.gotoRelativeUrl("/do/login");
      tester.login(userName, XPlannerWebTester.DEFAULT_PASSWORD);
      goToIteration(testIterationName);
   }


   /** Goto test task.
     */
   private void gotoTestTask() {
      tester.clickLinkWithText(storyName);
      tester.clickLinkWithText(testTaskName);
      tester.assertOnTaskPage();
   }

   /** Enter time for task.
     *
     * @param slot
     *            the slot
     * @param offset
     *            the offset
     * @param hoursToLog
     *            the hours to log
     */
   private void enterTimeForTask(int slot, int offset, int hoursToLog) {
      tester.clickLinkWithText(storyName);
      tester.assertOnStoryPage();
      tester.clickLinkWithImage(EDIT_TIME_IMAGE);
      tester.assertKeyPresent("timelog.editor.edit_prefix");
      tester.setTimeEntry(slot, offset, offset + hoursToLog, developer.getName());
   }

   /** Sets the up tester person.
     *
     * @param testerName
     *            the tester name
     * @param role
     *            the role
     * @throws Exception
     *             the exception
     */
   private void setUpTesterPerson(String testerName, String role) throws Exception {
      String email = "ap@nowhere.com";
      String phone = "212-555-5555";
      personTester.gotoPeoplePage();
      tester.clickLinkWithKey("people.link.add_person");
      tester.assertTextPresent("Create Profile:");
      personTester.populatePersonForm(testerName, testerName, "TEST", email, phone, false);
      int[] rows = tester.getRowNumbersWithText(ROLES_TABLE, testProjectName);
      if (rows != null) {
         int rowNbr = rows[0] - 1;
         tester.selectOption("projectRole[" + rowNbr + "]", role);
      }
      tester.submit();
      tester.assertKeyNotPresent("errors.header");
   }

   /** Tear down tester person.
     *
     * @param testerName
     *            the tester name
     */
   private void tearDownTesterPerson(String testerName) {
      tester.deleteObjects(Person.class, "name", testerName);
   }
}