package com.technoetic.xplanner.acceptance.soap;

import javax.servlet.http.HttpServletRequest;

import com.kizna.servletunit.HttpServletRequestSimulator;
import com.technoetic.xplanner.filters.ThreadServletRequest;
import com.technoetic.xplanner.security.SecurityTestHelper;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;
import com.technoetic.xplanner.soap.XPlanner;

/**
 * The Class LocalSoapTestDeprecated.
 *
 * @deprecated Decide if this test is valuable once migrated to XFire
 */
public class LocalSoapTestDeprecated extends AbstractSoapTestCase {

    /** Instantiates a new local soap test deprecated.
     *
     * @param s
     *            the s
     */
    public LocalSoapTestDeprecated(String s) {
        super(s);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.soap.AbstractSoapTestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();
        SystemAuthorizer.set(createAuthorizer());
    }

    /** Sets the up request.
     *
     * @return the http servlet request
     * @throws Exception
     *             the exception
     */
    private HttpServletRequest setUpRequest() throws Exception {
       HttpServletRequestSimulator request = new HttpServletRequestSimulator();
       SecurityTestHelper.setRemoteUserId(getUserId(), request, getSession());
       return request;
    }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.soap.AbstractSoapTestCase#tearDown()
    */
   public void tearDown() throws Exception {
        ThreadServletRequest.set(null);
        super.tearDown();
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.soap.AbstractSoapTestCase#createXPlanner()
     */
    public XPlanner createXPlanner() throws Exception {
        ThreadServletRequest.set(setUpRequest());
        return XPlannerTestAdapter.create();
    }
}
