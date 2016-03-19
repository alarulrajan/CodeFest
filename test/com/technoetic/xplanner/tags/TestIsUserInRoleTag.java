package com.technoetic.xplanner.tags;

import javax.servlet.jsp.tagext.Tag;

import junit.framework.TestCase;

import com.technoetic.xplanner.XPlannerTestSupport;

/**
 * The Class TestIsUserInRoleTag.
 */
public class TestIsUserInRoleTag extends TestCase {
    
    /** The tag. */
    private IsUserInRoleTag tag;
    
    /** The support. */
    private XPlannerTestSupport support;

    /** Instantiates a new test is user in role tag.
     *
     * @param s
     *            the s
     */
    public TestIsUserInRoleTag(String s) {
        super(s);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        support = new XPlannerTestSupport();
        tag = new IsUserInRoleTag();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /** Test role match.
     *
     * @throws Exception
     *             the exception
     */
    public void testRoleMatch() throws Exception {
        support.setUpSubjectInRole("viewer");
        tag.setPageContext(support.pageContext);
        tag.setRole("viewer");

        int result = tag.doStartTag();

        assertEquals("wrong result", Tag.EVAL_BODY_INCLUDE, result);

    }

    /** Test role nonmatch.
     *
     * @throws Exception
     *             the exception
     */
    public void testRoleNonmatch() throws Exception {
        support.setUpSubjectInRole("bogus");
        tag.setPageContext(support.pageContext);
        tag.setRole("viewer");

        int result = tag.doStartTag();

        assertEquals("wrong result", Tag.SKIP_BODY, result);
    }

    /** Test role and user id match.
     *
     * @throws Exception
     *             the exception
     */
    public void testRoleAndUserIdMatch() throws Exception {
        support.setUpSubject("foo", new String[]{"viewer"});
        tag.setPageContext(support.pageContext);
        tag.setRole("viewer");
        tag.setUserid("foo");

        int result = tag.doStartTag();

        assertEquals("wrong result", Tag.EVAL_BODY_INCLUDE, result);
    }

    /** Test role match and user id nonmatch.
     *
     * @throws Exception
     *             the exception
     */
    public void testRoleMatchAndUserIdNonmatch() throws Exception {
        support.setUpSubject("foo", new String[]{"viewer"});
        tag.setPageContext(support.pageContext);
        tag.setRole("viewer");
        tag.setUserid("bar");

        int result = tag.doStartTag();

        assertEquals("wrong result", Tag.SKIP_BODY, result);
    }

    /** Test role match and user id match and admin.
     *
     * @throws Exception
     *             the exception
     */
    public void testRoleMatchAndUserIdMatchAndAdmin() throws Exception {
        support.setUpSubject("bar", new String[]{"viewer"});
        tag.setPageContext(support.pageContext);
        tag.setRole("viewer");
        tag.setUserid("bar");
        tag.setAdminRole("admin");

        int result = tag.doStartTag();

        assertEquals("wrong result", Tag.EVAL_BODY_INCLUDE, result);
    }

    /** Test role nonmatch and user id nonmatch and admin.
     *
     * @throws Exception
     *             the exception
     */
    public void testRoleNonmatchAndUserIdNonmatchAndAdmin() throws Exception {
        support.setUpSubject("foo", new String[]{"admin"});
        tag.setPageContext(support.pageContext);
        tag.setRole("viewer");
        tag.setUserid("foo");
        tag.setAdminRole("admin");

        int result = tag.doStartTag();

        assertEquals("wrong result", Tag.EVAL_BODY_INCLUDE, result);
    }

    /** Test multiple roles.
     *
     * @throws Exception
     *             the exception
     */
    public void testMultipleRoles() throws Exception {
        support.setUpSubject("foo", new String[]{"viewer"});
        tag.setPageContext(support.pageContext);
        tag.setRole("viewer,editor");
        tag.setUserid("foo");
        tag.setAdminRole("admin");

        int result = tag.doStartTag();

        assertEquals("wrong result", Tag.EVAL_BODY_INCLUDE, result);
    }
}
