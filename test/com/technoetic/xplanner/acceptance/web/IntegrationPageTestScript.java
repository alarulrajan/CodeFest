package com.technoetic.xplanner.acceptance.web;

/**
 * The Class IntegrationPageTestScript.
 */
public class IntegrationPageTestScript extends AbstractPageTestScript {

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        tester.login();
        tester.addProject(testProjectName, testProjectDescription);
        tester.clickLinkWithText(testProjectName);
        tester.clickLinkWithText("Integrations");
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#tearDown()
     */
    public void tearDown() throws Exception {
        tearDownTestProject();
        super.tearDown();
    }

    /** Test integration page.
     */
    public void testIntegrationPage() {
        // Just check that the page doesn't have errors
        tester.assertTextNotPresent("error");
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#traverseLinkWithKeyAndReturn(java.lang.String)
     */
    protected void traverseLinkWithKeyAndReturn(String key) throws Exception {
        tester.clickLinkWithKey(key);
        tester.gotoPage("view", "projects", 0);
    }
}
