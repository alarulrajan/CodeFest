/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Apr 21, 2005
 * Time: 10:33:00 PM
 */
package com.technoetic.xplanner.acceptance.web;

/**
 * The Class AbstractIterationTestScript.
 */
public abstract class AbstractIterationTestScript extends AbstractPageTestScript {
   
   /** The iteration id. */
   protected String iterationId;
   
   /** The task id. */
   protected String taskId;

   /** Instantiates a new abstract iteration test script.
     *
     * @param test
     *            the test
     */
   public AbstractIterationTestScript(String test) { super(test); }

   /** Instantiates a new abstract iteration test script.
     */
   public AbstractIterationTestScript() {
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
    */
   public void setUp() throws Exception
   {
       super.setUp();
       setUpTestPerson();
       setUpTestProject();
       setUpTestRole("editor");
       tester.login();
       tester.clickLinkWithText(testProjectName);
       setUpTestIteration();
   }

   /** Sets the up test iteration.
     *
     * @throws Exception
     *             the exception
     */
   protected void setUpTestIteration()
        throws Exception
    {
        tester.gotoProjectsPage();
        tester.clickLinkWithText(testProjectName);
        iterationTester.addIteration(testIterationName,
                            tester.dateStringForNDaysAway(0),
                            tester.dateStringForNDaysAway(14),
                            testIterationDescription);
        tester.clickLinkWithText(testIterationName);
        iterationId = tester.getCurrentPageObjectId();
   }

   /** Sets the up test story and task.
     */
   protected void setUpTestStoryAndTask() {
      tester.addUserStory(storyName, testStoryDescription, "23.5", "1");
      tester.assertTextPresent(storyName);
      tester.clickLinkWithText(storyName);
      tester.assertOnStoryPage();
      taskId = tester.addTask(testTaskName, developerName, testTaskDescription, testTaskEstimatedHours);
      tester.assertOnStoryPage();
      tester.assertTextPresent(testTaskName);
      tester.clickLinkWithText(testIterationName);
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#tearDown()
    */
   public void tearDown() throws Exception
    {
        super.tearDown();
    }

   /** Adds the iteration.
     *
     * @param iterationName
     *            the iteration name
     * @throws Exception
     *             the exception
     */
   protected void addIteration(String iterationName)
       throws Exception
   {
       tester.gotoProjectsPage();
       tester.clickLinkWithText(testProjectName);
       tester.assertOnProjectPage();
       tester.clickLinkWithKey("project.link.create_iteration");

       tester.setFormElement("name", iterationName);
       tester.setFormElement("startDateString", tester.dateStringForNDaysAway(15));
       tester.setFormElement("endDateString", tester.dateStringForNDaysAway(28));
       tester.setFormElement("description", testIterationDescription);
       tester.submit();
       tester.assertOnProjectPage();
   }
}