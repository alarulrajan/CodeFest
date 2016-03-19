package com.technoetic.xplanner.acceptance.web;

/**
 * The Class IterationChartsPageTestScript.
 */
public class IterationChartsPageTestScript extends AbstractIterationTestScript {

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.web.AbstractIterationTestScript#setUp()
    */
   public void setUp() throws Exception {
      super.setUp();
      setUpTestStoryAndTask();
   }

   /** Test no exception on charts page.
     *
     * @throws Exception
     *             the exception
     */
   public void testNoExceptionOnChartsPage() throws Exception {
      tester.clickLinkWithText(storyName);
      tester.clickLinkWithText(testTaskName);
      tester.completeCurrentTask();
      tester.clickLinkWithText(testIterationName);
      tester.clickLinkWithKey("iteration.link.statistics");
      tester.assertKeyPresent("iteration.statistics.progress.label");
   }
}
