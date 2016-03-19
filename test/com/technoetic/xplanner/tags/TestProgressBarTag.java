package com.technoetic.xplanner.tags;

import junit.framework.TestCase;

import com.technoetic.xplanner.XPlannerTestSupport;

/**
 * The Class TestProgressBarTag.
 */
public class TestProgressBarTag extends TestCase {
    
    /** Instantiates a new test progress bar tag.
     *
     * @param name
     *            the name
     */
    public TestProgressBarTag(String name) {
        super(name);
    }

    /** The support. */
    private XPlannerTestSupport support;
    
    /** The html tag. */
    private ProgressBarHtmlTag htmlTag = new ProgressBarHtmlTag();

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        support = new XPlannerTestSupport();
        htmlTag = new ProgressBarHtmlTag();
        htmlTag.setPageContext(support.pageContext);
    }

    /** Test no actual uncomplete.
     *
     * @throws Exception
     *             the exception
     */
    public void testNoActualUncomplete() throws Exception {
        htmlTag.setActual(0.0);
        htmlTag.setEstimate(10.0);
        htmlTag.setComplete(false);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_unworked") != -1);
        assertTrue("wrong cell %", output.indexOf("100%") != -1);
    }

    /** Test some actual uncomplete.
     *
     * @throws Exception
     *             the exception
     */
    public void testSomeActualUncomplete() throws Exception {
        htmlTag.setActual(2.0);
        htmlTag.setEstimate(10.0);
        htmlTag.setComplete(false);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_uncompleted") != -1);
        assertTrue("wrong cell class", output.indexOf("progressbar_unworked") != -1);
        assertTrue("wrong cell %", output.indexOf("20%") != -1);
        assertTrue("wrong cell %", output.indexOf("80%") != -1);
    }

    /** Test equal actual estimate.
     *
     * @throws Exception
     *             the exception
     */
    public void testEqualActualEstimate() throws Exception {
        htmlTag.setActual(10.0);
        htmlTag.setEstimate(10.0);
        htmlTag.setComplete(false);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_uncompleted") != -1);
        assertTrue("wrong cell %", output.indexOf("100%") != -1);
    }

    /** Test no actual complete.
     *
     * @throws Exception
     *             the exception
     */
    public void testNoActualComplete() throws Exception {
        htmlTag.setActual(0.0);
        htmlTag.setEstimate(10.0);
        htmlTag.setComplete(true);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_unworked") != -1);
        assertTrue("wrong cell %", output.indexOf("100%") != -1);
    }

    /** Test some actual complete.
     *
     * @throws Exception
     *             the exception
     */
    public void testSomeActualComplete() throws Exception {
        htmlTag.setActual(2.0);
        htmlTag.setEstimate(10.0);
        htmlTag.setComplete(true);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_completed") != -1);
        assertTrue("wrong cell class", output.indexOf("progressbar_unworked") != -1);
        assertTrue("wrong cell %", output.indexOf("20%") != -1);
        assertTrue("wrong cell %", output.indexOf("80%") != -1);
    }

    /** Test exceeded actual uncomplete.
     *
     * @throws Exception
     *             the exception
     */
    public void testExceededActualUncomplete() throws Exception {
        htmlTag.setActual(15.0);
        htmlTag.setEstimate(10.0);
        htmlTag.setComplete(false);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_uncompleted") != -1);
        assertTrue("wrong cell class", output.indexOf("progressbar_exceeded") != -1);
        assertTrue("wrong cell %", output.indexOf("67%") != -1);
        assertTrue("wrong cell %", output.indexOf("33%") != -1);
    }

    /** Test exceeded actual complete.
     *
     * @throws Exception
     *             the exception
     */
    public void testExceededActualComplete() throws Exception {
        htmlTag.setActual(15.0);
        htmlTag.setEstimate(10.0);
        htmlTag.setComplete(true);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_completed") != -1);
        assertTrue("wrong cell class", output.indexOf("progressbar_exceeded") != -1);
        assertTrue("wrong cell %", output.indexOf("67%") != -1);
        assertTrue("wrong cell %", output.indexOf("33%") != -1);
    }

    /** Test exceeded actual no estimate.
     *
     * @throws Exception
     *             the exception
     */
    public void testExceededActualNoEstimate() throws Exception {
        htmlTag.setActual(15.0);
        htmlTag.setEstimate(0.0);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_exceeded") != -1);
        assertTrue("wrong cell %", output.indexOf("100%") != -1);
    }

    /** Test no actual no estimate uncomplete.
     *
     * @throws Exception
     *             the exception
     */
    public void testNoActualNoEstimateUncomplete() throws Exception {
        htmlTag.setActual(0.0);
        htmlTag.setEstimate(0.0);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_unworked") != -1);
        assertTrue("wrong cell %", output.indexOf("100%") != -1);
    }

    /** Test no actual no estimate complete.
     *
     * @throws Exception
     *             the exception
     */
    public void testNoActualNoEstimateComplete() throws Exception {
        htmlTag.setActual(0.0);
        htmlTag.setEstimate(0.0);
        htmlTag.setComplete(true);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_completed") != -1);
        assertTrue("wrong cell %", output.indexOf("100%") != -1);
    }
}