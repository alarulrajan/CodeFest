package com.technoetic.xplanner.tags;

import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.Authorizer;

/**
 * The Class TestOptionsTag.
 */
public class TestOptionsTag extends AbstractOptionsTagTestCase {

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.tags.AbstractOptionsTagTestCase#setUp()
     */
    protected void setUp() throws Exception {
        tag = new DummyOptionsTag();
        super.setUp();
    }

    /** Gets the tag.
     *
     * @return the tag
     */
    public DummyOptionsTag getTag() {
        return (DummyOptionsTag) tag;
    }
    
    /** Filter output.
     *
     * @param s
     *            the s
     * @return the string
     */
    private String filterOutput(String s) {
        return s.trim().replaceAll("[\r\n]", "");
    }

    /** Test implicit mode.
     *
     * @throws Throwable
     *             the throwable
     */
    public void testImplicitMode() throws Throwable {

        assertDoEndTagReturn(Tag.EVAL_PAGE);

        assertEquals("wrong output",
                "<option value=\"1_id\">1_name</option>" +
                "<option value=\"2_id\">2_name</option>",
                filterOutput(support.jspWriter.output.toString().trim()));
    }

    /** Test explicit property.
     *
     * @throws Throwable
     *             the throwable
     */
    public void testExplicitProperty() throws Throwable {
        tag.setProperty("userId");

        assertDoEndTagReturn(Tag.EVAL_PAGE);

        assertEquals("wrong output",
                "<option value=\"1_userId\">1_name</option>" +
                "<option value=\"2_userId\">2_name</option>",
                filterOutput(support.jspWriter.output.toString().trim()));
    }

    /** Test explicit label property.
     *
     * @throws Throwable
     *             the throwable
     */
    public void testExplicitLabelProperty() throws Throwable {
        tag.setLabelProperty("userId");

        assertDoEndTagReturn(Tag.EVAL_PAGE);

        assertEquals("wrong output",
                "<option value=\"1_id\">1_userId</option>" +
                "<option value=\"2_id\">2_userId</option>",
                filterOutput(support.jspWriter.output.toString().trim()));
    }

    /** Assert do end tag return.
     *
     * @param expectedReturnValue
     *            the expected return value
     * @throws Throwable
     *             the throwable
     */
    private void assertDoEndTagReturn(int expectedReturnValue) throws Throwable {
        try {
            int result = tag.doEndTag();
            assertEquals("wrong result", expectedReturnValue, result);
            assertSame(support.hibernateSession, getTag().session);
            assertSame(authorizer, getTag().authorizer);
            assertEquals(XPlannerTestSupport.DEFAULT_PERSON_ID, getTag().userId);
        } catch (JspException e) {
            if (e.getRootCause() != null) {
                throw e.getRootCause();
            } else {
                throw e;
            }
        }
    }

    /** The Class DummyOptionsTag.
     */
    public static class DummyOptionsTag extends OptionsTag {
        
        /** The session. */
        public Session session;
        
        /** The authorizer. */
        public Authorizer authorizer;
        
        /** The user id. */
        public int userId;

        /* (non-Javadoc)
         * @see com.technoetic.xplanner.tags.OptionsTag#getOptions()
         */
        protected List getOptions() throws HibernateException, AuthenticationException {
            session = getSession();
            authorizer = getAuthorizer();
            userId = getLoggedInUserId();
            return Arrays.asList(new Bean[]{new Bean("1_id", "1_name", "1_userId"), new Bean("2_id", "2_name", "2_userId")});
        }
    }

    /** The Class Bean.
     */
    public static class Bean {
        
        /** The id. */
        private String id;
        
        /** The name. */
        private String name;
        
        /** The user id. */
        private String userId;

        /** Instantiates a new bean.
         *
         * @param s1
         *            the s1
         * @param s2
         *            the s2
         * @param s3
         *            the s3
         */
        public Bean(String s1, String s2, String s3) {
            this.id = s1;
            this.name = s2;
            this.userId = s3;
        }

        /** Gets the id.
         *
         * @return the id
         */
        public String getId() { return id; }
        
        /** Sets the id.
         *
         * @param id
         *            the new id
         */
        public void setId(String id) { this.id = id; }
        
        /** Gets the name.
         *
         * @return the name
         */
        public String getName() { return name; }
        
        /** Sets the name.
         *
         * @param name
         *            the new name
         */
        public void setName(String name) { this.name = name; }
        
        /** Gets the user id.
         *
         * @return the user id
         */
        public String getUserId() { return userId; }
        
        /** Sets the user id.
         *
         * @param userId
         *            the new user id
         */
        public void setUserId(String userId) { this.userId = userId; }
    }
}
