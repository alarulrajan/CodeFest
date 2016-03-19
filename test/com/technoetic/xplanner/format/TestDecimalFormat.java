package com.technoetic.xplanner.format;

import java.util.Locale;

import junit.framework.TestCase;

import com.technoetic.xplanner.XPlannerTestSupport;

/**
 * The Class TestDecimalFormat.
 */
public class TestDecimalFormat extends TestCase {
    
    /** The support. */
    private XPlannerTestSupport support;
    
    /** The language. */
    final String LANGUAGE = "da";
    
    /** The country. */
    final String COUNTRY = "nl";
    
    /** The input value. */
    final String INPUT_VALUE = "2,5";

    /** Instantiates a new test decimal format.
     *
     * @param name
     *            the name
     */
    public TestDecimalFormat(String name) {
        super(name);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        support = new XPlannerTestSupport();
    }

    /** Test locale danish.
     *
     * @throws Exception
     *             the exception
     */
    public void testLocaleDanish() throws Exception {
        support.request.setLocale(new Locale(LANGUAGE, COUNTRY));

        double value = new DecimalFormat(support.request).parse(INPUT_VALUE);

        assertEquals(2.5, value, 0);
    }


    /** Test locale danish error.
     *
     * @throws Exception
     *             the exception
     */
    public void testLocaleDanishError() throws Exception {
        support.request.setLocale(new Locale(LANGUAGE, COUNTRY));

        double value = new DecimalFormat(support.request).parse("2.5");

        assertEquals(25, value, 0);
    }

    /** Test locale us.
     *
     * @throws Exception
     *             the exception
     */
    public void testLocaleUS() throws Exception {
        support.request.setLocale(new Locale("en", "us"));

        double value = new DecimalFormat(support.request).parse("2.5");

        assertEquals(2.5, value, 0);
    }

    /** Test locale us error.
     *
     * @throws Exception
     *             the exception
     */
    public void testLocaleUsError() throws Exception {
        support.request.setLocale(new Locale("en", "us"));

        double value = new DecimalFormat(support.request).parse(INPUT_VALUE);

        assertEquals(25, value, 0);
    }

}