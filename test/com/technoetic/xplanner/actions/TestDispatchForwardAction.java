package com.technoetic.xplanner.actions;

import junit.framework.TestCase;

import org.apache.struts.action.ActionForward;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.security.auth.MockAuthorizer;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

/**
 * The Class TestDispatchForwardAction.
 */
public class TestDispatchForwardAction extends TestCase {
    
    /** The support. */
    private XPlannerTestSupport support;
    
    /** The action. */
    private DispatchForward action;
    
    /** The authorizer. */
    private MockAuthorizer authorizer;

    /** Instantiates a new test dispatch forward action.
     *
     * @param testName
     *            the test name
     */
    public TestDispatchForwardAction(String testName) {
        super(testName);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        support = new XPlannerTestSupport();
        support.setUpMockAppender();
        support.setForward("@secure", "false");
        authorizer = new MockAuthorizer();
        authorizer.hasPermissionReturns = new Boolean[]{Boolean.TRUE};
        action = new DispatchForward();
        action.setAuthorizer(authorizer);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        SystemAuthorizer.set(null);
        support.tearDownMockAppender();
        super.tearDown();
    }

    /** Test forward using action parameter.
     *
     * @throws Exception
     *             the exception
     */
    public void testForwardUsingActionParameter() throws Exception {
        support.mapping.setParameter("view/projects");
        support.setForward("view/projects", "projects.jsp");

        ActionForward forward = support.executeAction(action);

        assertEquals("wrong forward", "projects.jsp", forward.getPath());
    }

    /** Test secure access without project id.
     *
     * @throws Exception
     *             the exception
     */
    public void testSecureAccessWithoutProjectId() throws Exception {
        support.setForward("@secure", "true");
        support.setForward("security/notAuthorized", "AUTH_FAILED");

        ActionForward forward = support.executeAction(action);

        assertEquals("wrong forward", "AUTH_FAILED", forward.getPath());

    }

    /** Test secure access.
     *
     * @throws Exception
     *             the exception
     */
    public void testSecureAccess() throws Exception {
        support.request.setParameterValue("projectId", new String[]{"33"});
        support.setForward("@secure", "true");
        support.setForward("security/notAuthorized", "AUTH_FAILED");
        support.setUpSubject("user", new String[0]);
        support.setForward("forward", "AUTH_SUCCESS");
        support.mapping.setParameter("forward");

        ActionForward forward = support.executeAction(action);

        assertEquals("wrong forward", "AUTH_SUCCESS", forward.getPath());

    }

    /** Test secure access without permission.
     *
     * @throws Exception
     *             the exception
     */
    public void testSecureAccessWithoutPermission() throws Exception {
        support.request.setParameterValue("projectId", new String[]{"33"});
        support.setForward("@secure", "true");
        support.setForward("security/notAuthorized", "AUTH_FAILED");
        support.setUpSubject("user", new String[0]);
        authorizer.hasPermissionReturns = new Boolean[]{Boolean.FALSE};

        ActionForward forward = support.executeAction(action);

        assertEquals("wrong forward", "AUTH_FAILED", forward.getPath());
    }
}

