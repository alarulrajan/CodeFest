package com.technoetic.xplanner.acceptance.soap;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.soap.XPlanner;


/**
 * The Class RemoteXPlannerTest.
 */
public class RemoteXPlannerTest extends AbstractSoapTestCase {

    /** Instantiates a new remote x planner test.
     *
     * @param s
     *            the s
     */
    public RemoteXPlannerTest(String s) {
        super(s);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.soap.AbstractSoapTestCase#createXPlanner()
     */
    public XPlanner createXPlanner() throws Exception {
       XPlannerProperties properties = new XPlannerProperties();
        //XPlannerServiceLocator xplannerServiceLocator = new XPlannerServiceLocator();
        String url = XPlannerTestSupport.getAbsoluteTestURL() + "/soap/XPlanner";
        //String url = "http://localhost:8888/xplanner/soap/XPlanner";
        //XPlanner xplanner = xplannerServiceLocator.getXPlanner(new URL(url));
        //((Stub)xplanner).setUsername(properties.getProperty("xplanner.test.user"));
        //((Stub)xplanner).setPassword(properties.getProperty("xplanner.test.password"));
        return xplanner;
        // This really just tests the core methods
        // It will ensure that SOAP is not completely broken, but
        // once we integrate DBUnit to initialize the DB, we should
        // definitely add more tests.
    }


}
