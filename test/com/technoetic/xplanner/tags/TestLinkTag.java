package com.technoetic.xplanner.tags;

import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;

import junit.framework.TestCase;

import org.apache.struts.Globals;

import com.technoetic.xplanner.XPlannerTestSupport;

/**
 * The Class TestLinkTag.
 */
public class TestLinkTag extends TestCase {
    
    /** The support. */
    private XPlannerTestSupport support;
    
    /** The tag. */
    private DummyLinkTag tag;

    /** Instantiates a new test link tag.
     *
     * @param name
     *            the name
     */
    public TestLinkTag(String name) {
        super(name);
    }

    /** The Class TestObject.
     */
    public class TestObject {
        
        /** The value. */
        private int value;

        /** Instantiates a new test object.
         *
         * @param value
         *            the value
         */
        public TestObject(int value) {
            this.value = value;
        }

        /** Gets the value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }

        /** Sets the value.
         *
         * @param value
         *            the new value
         */
        public void setValue(int value) {
            this.value = value;
        }
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        support = new XPlannerTestSupport();
        tag = new DummyLinkTag();
        tag.setPageContext(support.pageContext);
        support.mapping.setPath("/page.jsp");
        support.request.setAttribute(Globals.MAPPING_KEY, support.mapping);
    }

    /** Test do start tag.
     *
     * @throws Exception
     *             the exception
     */
    public void testDoStartTag() throws Exception {
        tag.setHref("somepage.jsp");

        executeTag();

        assertTrue("missing or incorrect returnto",
                support.jspWriter.printValue.indexOf("returnto=%2Fdo%2Fpage.jsp") != -1);
    }

    /** Test do start tag with oid.
     *
     * @throws Exception
     *             the exception
     */
    public void testDoStartTagWithOid() throws Exception {
        support.request.setParameterValue("oid", new String[]{"222"});
        tag.setHref("somepage.jsp");

        executeTag();

        assertTrue("missing fkey",
                support.jspWriter.printValue.indexOf("fkey=222") != -1);
        assertTrue("missing or incorrect returnto",
                support.jspWriter.printValue.indexOf("returnto=%2Fdo%2Fpage.jsp%3Foid%3D222") != -1);
    }

    /** Execute tag.
     *
     * @throws JspException
     *             the jsp exception
     */
    private void executeTag() throws JspException {
        int result = tag.doStartTag();
        assertEquals("wrong result", BodyTag.EVAL_BODY_BUFFERED, result);

        result = tag.doEndTag();
        assertEquals("wrong result", BodyTag.EVAL_PAGE, result);
    }

    /** Test do start tag with oid and fkey.
     *
     * @throws Exception
     *             the exception
     */
    public void testDoStartTagWithOidAndFkey() throws Exception {
        support.request.setParameterValue("oid", new String[]{"222"});
        tag.setHref("somepage.jsp");
        tag.setFkey(111);

        executeTag();

        assertTrue("missing fkey", support.jspWriter.printValue.indexOf("fkey=111") != -1);
        assertTrue("missing or incorrect returnto",
                support.jspWriter.printValue.indexOf("returnto=%2Fdo%2Fpage.jsp%3Foid%3D222") != -1);
    }

    /** Test do start tag with params.
     *
     * @throws Exception
     *             the exception
     */
    public void testDoStartTagWithParams() throws Exception {
        support.request.setParameterValue("oid", new String[]{"222"});
        Object object = new Object();
        support.pageContext.setAttribute("bar", object);
        tag.setHref("somepage.jsp");
        tag.setParamId("foo");
        tag.setParamName("bar");
        tag.setParamProperty("class");

        executeTag();

        assertTrue("missing param",
                support.jspWriter.printValue.indexOf("foo=class+java.lang.Object") != -1);
        assertTrue("missing or incorrect returnto",
                support.jspWriter.printValue.indexOf("returnto=%2Fdo%2Fpage.jsp%3Foid%3D222") != -1);
    }

    /** Test do start tag with map.
     *
     * @throws Exception
     *             the exception
     */
    public void testDoStartTagWithMap() throws Exception {
        support.request.setParameterValue("oid", new String[]{"222"});
        Object object = new Object();
        HashMap params = new HashMap();
        params.put("foo", object.getClass());
        support.pageContext.setAttribute("bar", params);
        tag.setHref("somepage.jsp");
        tag.setName("bar");

        executeTag();

        assertTrue("missing param",
                support.jspWriter.printValue.indexOf("foo=class+java.lang.Object") != -1);
        assertTrue("missing or incorrect returnto",
                support.jspWriter.printValue.indexOf("returnto=%2Fdo%2Fpage.jsp%3Foid%3D222") != -1);
    }

    /** Test do tag twice.
     *
     * @throws Exception
     *             the exception
     */
    public void testDoTagTwice() throws Exception {
        support.pageContext.setAttribute("object", new TestObject(100));
        tag.setPage("foo.jsp");
        tag.setParamId("foo");
        tag.setParamName("object");
        tag.setParamProperty("value");

        executeTag();

        assertTrue("missing param",
                support.jspWriter.printValue.indexOf("foo=100") != -1);

        int result = tag.doStartTag();

        assertEquals("wrong result", BodyTag.EVAL_BODY_BUFFERED, result);
        assertTrue("missing param",
                support.jspWriter.printValue.indexOf("foo=100") != -1);
    }

    /** Test do start tag with oid and fkey and project id.
     *
     * @throws Exception
     *             the exception
     */
    public void testDoStartTagWithOidAndFkeyAndProjectId() throws Exception {
        DomainContext context = new DomainContext();
        context.setProjectId(44);
        context.save(support.request);
        support.request.setParameterValue("oid", new String[]{"222"});
        tag.setHref("somepage.jsp");
        tag.setFkey(111);

        executeTag();

        assertOutputContains("projectId=44");
        assertTrue("missing fkey",
                support.jspWriter.printValue.indexOf("fkey=111") != -1);
        assertTrue("missing or incorrect returnto",
                support.jspWriter.printValue.indexOf("returnto=%2Fdo%2Fpage.jsp%3Foid%3D222") != -1);
    }

    /** Test do start tag with oid and fkey and inhibited project id.
     *
     * @throws Exception
     *             the exception
     */
    public void testDoStartTagWithOidAndFkeyAndInhibitedProjectId() throws Exception {
        DomainContext context = new DomainContext();
        context.setProjectId(44);
        context.save(support.request);
        support.request.setParameterValue("oid", new String[]{"222"});
        tag.setHref("somepage.jsp");
        tag.setFkey(111);
        tag.setIncludeProjectId("false");

        executeTag();

        assertTrue("missing projectId",
                support.jspWriter.printValue.indexOf("projectId=44") == -1);
        assertTrue("missing fkey",
                support.jspWriter.printValue.indexOf("fkey=111") != -1);
        assertTrue("missing or incorrect returnto",
                support.jspWriter.printValue.indexOf("returnto=%2Fdo%2Fpage.jsp%3Foid%3D222") != -1);
    }

    /** Test do end tag mnemonic in first character.
     *
     * @throws Exception
     *             the exception
     */
    public void testDoEndTagMnemonicInFirstCharacter() throws Exception {
        tag.setText("&All");
        tag.doEndTag();
        assertOutputContains("accesskey=\"A\"");
        assertOutputContains("<span class=\"mnemonic\">A</span>ll");
    }

    /** Test do end tag mnemonic in last character.
     *
     * @throws Exception
     *             the exception
     */
    public void testDoEndTagMnemonicInLastCharacter() throws Exception {
        tag.setText("All&");
        tag.doEndTag();
        assertOutputDoesNotContain("accesskey");
        assertOutputContains("All&");
    }

    /** Test do end tag mnemonic in middle character.
     *
     * @throws Exception
     *             the exception
     */
    public void testDoEndTagMnemonicInMiddleCharacter() throws Exception {
        tag.setText("A&ll");
        tag.doEndTag();
        assertOutputContains("accesskey=\"L\"");
        assertOutputContains("A<span class=\"mnemonic\">l</span>l");
    }

    /** Test do end tag mnemonic before space.
     *
     * @throws Exception
     *             the exception
     */
    public void testDoEndTagMnemonicBeforeSpace() throws Exception {
        tag.setText("A& ll");
        tag.doEndTag();
        assertOutputDoesNotContain("accesskey");
        assertOutputContains("A& ll");
    }

    /** Test do end tag escaped ampersand.
     *
     * @throws Exception
     *             the exception
     */
    public void testDoEndTagEscapedAmpersand() throws Exception {
        tag.setText("A&&ll");
        tag.doEndTag();
        assertOutputDoesNotContain("accesskey");
        assertOutputContains("A&ll");
    }

    /** Test do end tag no mnemonic.
     *
     * @throws Exception
     *             the exception
     */
    public void testDoEndTagNoMnemonic() throws Exception {
        tag.setText("All");
        tag.doEndTag();
        assertOutputDoesNotContain("accesskey");
        assertOutputContains("All");
    }

    /** Assert output contains.
     *
     * @param expectedOutputPart
     *            the expected output part
     */
    private void assertOutputContains(String expectedOutputPart) {
        assertTrue("missing "+expectedOutputPart + " in '" + support.jspWriter.printValue + "'",
                support.jspWriter.printValue.indexOf(expectedOutputPart) != -1);
    }

    /** Assert output does not contain.
     *
     * @param unexpectedOutputPart
     *            the unexpected output part
     */
    private void assertOutputDoesNotContain(String unexpectedOutputPart) {
        assertTrue("contains "+unexpectedOutputPart + " in '" + support.jspWriter.printValue + "'",
                   support.jspWriter.printValue.indexOf(unexpectedOutputPart) == -1);
    }

    /** The Class DummyLinkTag.
     */
    private static class DummyLinkTag extends LinkTag {
        
        /** Sets the text.
         *
         * @param text
         *            the new text
         */
        public void setText(String text) {
            this.text = text;
        }
    }

}