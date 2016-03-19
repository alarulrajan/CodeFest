package com.technoetic.xplanner.acceptance.web;



/**
 * Created by IntelliJ IDEA.
 * User: sg897500
 * Date: Nov 18, 2004
 * Time: 1:49:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectPageFormatingTestScript extends AbstractPageTestScript
{
    
    /** The start date. */
    private String startDate;
    
    /** The end date. */
    private String endDate;
    
    /** The test project description wiki. */
    private String testProjectDescriptionWiki = "|SD1.2|jbond 10/11/04 12:00p|SD1.2 jbond 10/11/04 12:00p |";
    
    /** The first column. */
    private String firstColumn = "SD1.2";
    
    /** The second column. */
    private String secondColumn = "jbond 10-11-04 12:00p";
    
    /** The third column. */
    private String thirdColumn = "SD1.2 jbond 10-11-04 12:00p";
    
    /** The project id. */
    private String projectId = null;


    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
     */
    public void setUp() throws Exception
    {
        startDate = tester.dateStringForNDaysAway(0);
        endDate = tester.dateStringForNDaysAway(14);
        super.setUp();
        setUpTestPerson();
        setUpTestProject(testProjectName, testProjectDescriptionWiki);
        tester.login();
        tester.gotoProjectsPage();
        tester.clickLinkWithText(project.getName());
        projectId = tester.getCurrentPageObjectId();
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#tearDown()
     */
    public void tearDown() throws Exception
    {
        tester.logout();
        super.tearDown();
    }

    /** Test wiki table formating.
     *
     * @throws Exception
     *             the exception
     */
    public void testWikiTableFormating() throws Exception
    {
        tester.assertOnProjectPage();
        tester.assertTextNotPresent(testProjectDescriptionWiki);
        tester.assertTextPresent(firstColumn);
        tester.assertTextPresent(secondColumn);
        tester.assertTextPresent(thirdColumn);
    }

    /** Test project link formating.
     *
     * @throws Exception
     *             the exception
     */
    public void testProjectLinkFormating() throws Exception
    {
        tester.assertOnProjectPage();
        changeProjectDescription("project:" + projectId + "?");
        tester.assertLinkPresentWithText("project: " + testProjectName + "?");
    }

    /** Test rroject brackets rendering.
     *
     * @throws Exception
     *             the exception
     */
    public void testRrojectBracketsRendering() throws Exception
    {
        tester.assertOnProjectPage();
        changeProjectDescription("<>");
        changeRenderBracketsOption(true);
        tester.assertTextPresent("&lt;&gt;");
        changeRenderBracketsOption(false);
        tester.assertTextPresent("<>");
    }

    /** Change project description.
     *
     * @param description
     *            the description
     */
    private void changeProjectDescription(String description)
    {
        tester.clickLinkWithKey("action.edit.project");
        tester.setFormElement("description", description);
        tester.submit();
        tester.assertOnProjectPage();
    }

    /** Change render brackets option.
     *
     * @param checkIt
     *            the check it
     */
    private void changeRenderBracketsOption(boolean checkIt)
    {
        tester.clickLinkWithKey("action.edit.project");
        if (checkIt)
        {
            tester.checkCheckbox("optEscapeBrackets", "true");
        }
        else
        {
            tester.uncheckCheckbox("optEscapeBrackets");
        }
        tester.submit();
        tester.assertOnProjectPage();

    }

    /** Test clear quest link formating.
     *
     * @throws Exception
     *             the exception
     */
    public void testClearQuestLinkFormating() throws Exception
    {
        tester.assertOnProjectPage();
        changeProjectDescription("cq:12345");
        tester.assertLinkPresentWithText("cq:12345");
    }

}
