package com.technoetic.xplanner.performance;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.clarkware.junitperf.TimedTest;
import com.technoetic.xplanner.acceptance.soap.RemoteSoapTest;
import com.technoetic.xplanner.acceptance.web.AbstractPageTestScript;
import com.technoetic.xplanner.acceptance.web.IterationPageTestScript;
import com.technoetic.xplanner.acceptance.web.PeoplePageTestScript;

/**
 * User: Mateusz Prokopowicz Date: Feb 3, 2005 Time: 3:01:23 PM.
 */
public class CRUDPerformanceTest extends AbstractPageTestScript {

   /** Instantiates a new CRUD performance test.
     *
     * @param test
     *            the test
     */
   public CRUDPerformanceTest(String test) {
      super(test);
   }

   /** Instantiates a new CRUD performance test.
     */
   public CRUDPerformanceTest(){
      super();
   }


   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
    */
   public void setUp() throws Exception {
      super.setUp();
      tester.login();
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#tearDown()
    */
   public void tearDown() throws Exception {
      tester.logout();
      super.tearDown();
   }

   /** Test set up and tear down overhead.
     *
     * @throws Exception
     *             the exception
     */
   public void testSetUpAndTearDownOverhead() throws Exception
   {
   }

   /** Test view and edit project.
     *
     * @throws Exception
     *             the exception
     */
   public void testViewAndEditProject() throws Exception
   {
      testViewAndEditObject("project", 1, "action.edit.project");
   }

   /** Test view and edit iteration.
     *
     * @throws Exception
     *             the exception
     */
   public void testViewAndEditIteration() throws Exception
   {
      testViewAndEditObject("iteration", 175, "action.edit.iteration");
   }

   /** Test view and edit user story.
     *
     * @throws Exception
     *             the exception
     */
   public void testViewAndEditUserStory() throws Exception
   {
      testViewAndEditObject("userstory", 1453, "action.edit.story");
   }

   /** Test view and edit task.
     *
     * @throws Exception
     *             the exception
     */
   public void testViewAndEditTask() throws Exception
   {
      testViewAndEditObject("task", 1454, "action.edit.task");
   }

   /** Test view and edit time entry.
     *
     * @throws Exception
     *             the exception
     */
   public void testViewAndEditTimeEntry() throws Exception
   {
      testViewAndEditObject("task", 1454, "action.edittime.task");
   }

   /** Test view and edit object.
     *
     * @param objectType
     *            the object type
     * @param oid
     *            the oid
     * @param editLinkKey
     *            the edit link key
     * @throws Exception
     *             the exception
     * @noinspection TestMethodWithIncorrectSignature
     */
   private void testViewAndEditObject(String objectType, int oid, String editLinkKey) throws Exception {
      tester.gotoPage("view", objectType, oid);
      tester.clickLinkWithKey(editLinkKey);
      tester.submit();
   }


   /** Suite.
     *
     * @return the test
     * @throws Exception
     *             the exception
     */
   public static Test suite() throws Exception {
      TestSuite suite = new TestSuite();
      suite.addTest(new TimedTest(new RemoteSoapTest("testIterationCRUD"), 50000));
      suite.addTest(new TimedTest(new RemoteSoapTest("testProjectCRUD"), 10000));
      suite.addTest(new TimedTest(new RemoteSoapTest("testStoryCRUD"), 50000));
      suite.addTest(new TimedTest(new RemoteSoapTest("testTaskCRUD"), 50000));
      suite.addTest(new TimedTest(new RemoteSoapTest("testTimeEntryCRUD"), 50000));
//FIXME A bunch of CRUD tests are missing
      suite.addTest(new TimedTest(new CRUDPerformanceTest("testSetUpAndTearDownOverhead"), 15000));
      suite.addTest(new TimedTest(new CRUDPerformanceTest("testViewAndEditProject"), 50000));
      suite.addTest(new TimedTest(new CRUDPerformanceTest("testViewAndEditIteration"), 50000));
      suite.addTest(new TimedTest(new CRUDPerformanceTest("testViewAndEditUserStory"), 30000));
      suite.addTest(new TimedTest(new CRUDPerformanceTest("testViewAndEditTask"), 30000));
      suite.addTest(new TimedTest(new CRUDPerformanceTest("testViewAndEditTimeEntry"), 30000));
      suite.addTest(new TimedTest(new PeoplePageTestScript("testManipulatingPeople"), 44000));
      suite.addTest(new TimedTest(new IterationPageTestScript("testAddingAndDeletingNotes"), 320000));
      return suite;
   }
}
