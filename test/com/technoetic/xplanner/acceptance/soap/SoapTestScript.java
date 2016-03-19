package com.technoetic.xplanner.acceptance.soap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * The Class SoapTestScript.
 */
public class SoapTestScript extends TestCase {

    /** Instantiates a new soap test script.
     *
     * @param s
     *            the s
     */
    public SoapTestScript(String s) {
        super(s);
    }

    /** Suite.
     *
     * @return the test
     */
    static public Test suite() {
        TestSuite suite = new TestSuite();
//        suite.addTestSuite(LocalSoapTestDeprecated.class);
        suite.addTestSuite(RemoteSoapTest.class);
        return suite;
    }
}
