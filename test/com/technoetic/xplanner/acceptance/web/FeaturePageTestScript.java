package com.technoetic.xplanner.acceptance.web;


/**
 * The Class FeaturePageTestScript.
 */
public class FeaturePageTestScript extends AbstractPageTestScript {

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();
        simpleSetUp();
        tester.addFeature(testFeatureName, testFeatureDescription);
        tester.clickLinkWithText(testFeatureName);

    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#tearDown()
     */
    public void tearDown() throws Exception {
        simpleTearDown();
        super.tearDown();
    }

    /** Test content and links.
     */
    public void testContentAndLinks() {
        tester.assertOnFeaturePage();
        tester.assertTextPresent(testFeatureName);
        tester.assertTextPresent(testFeatureDescription);
        tester.clickLinkWithKey("navigation.story");
        tester.assertOnStoryPage();
        tester.clickLinkWithText(testFeatureName);
        tester.assertOnFeaturePage();
        tester.clickLinkWithKey("navigation.iteration");
        iterationTester.assertOnIterationPage();
        tester.clickLinkWithText(storyName);
        tester.clickLinkWithText(testFeatureName);
        tester.assertOnFeaturePage();
        tester.clickLinkWithKey("navigation.project");
        tester.assertOnProjectPage();
        tester.clickLinkWithText(testIterationName);
        tester.clickLinkWithText(storyName);
        tester.clickLinkWithText(testFeatureName);
        tester.assertOnFeaturePage();
    }

    /** Test adding and deleting notes.
     */
    public void testAddingAndDeletingNotes() {
        runNotesTests(XPlannerWebTester.FEATURE_PAGE);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#traverseLinkWithKeyAndReturn(java.lang.String)
     */
    protected void traverseLinkWithKeyAndReturn(String key) throws Exception {
        tester.clickLinkWithKey(key);
        tester.gotoPage("view", "projects", 0);
    }
}
