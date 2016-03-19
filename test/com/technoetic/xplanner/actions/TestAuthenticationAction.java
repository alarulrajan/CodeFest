package com.technoetic.xplanner.actions;

import java.util.Iterator;
import java.util.Properties;

import junit.extensions.FieldAccessor;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.FormPropertyConfig;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.MockAuthenticator;

/**
 * The Class TestAuthenticationAction.
 */
public class TestAuthenticationAction extends TestCase {
    
    /** The action. */
    private AuthenticationAction action;
    
    /** The properties. */
    private Properties properties;
    
    /** The support. */
    private XPlannerTestSupport support;
    
    /** The dyna action form. */
    private DynaActionForm dynaActionForm;
    
    /** The mock authenticator. */
    private MockAuthenticator mockAuthenticator;

    /** Instantiates a new test authentication action.
     *
     * @param s
     *            the s
     */
    public TestAuthenticationAction(String s) {
        super(s);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        Logger.getRootLogger().setLevel(Level.OFF);
        support = new XPlannerTestSupport();
        support.setForward("notAuthenticated", "path");
        support.setForward("authenticated", "path");
        FormBeanConfig formBeanConfig = new FormBeanConfig();
        formBeanConfig.addFormPropertyConfig(new FormPropertyConfig("userId", "java.lang.String", null));
        formBeanConfig.addFormPropertyConfig(new FormPropertyConfig("password", "java.lang.String", null));
        formBeanConfig.addFormPropertyConfig(new FormPropertyConfig("action", "java.lang.String", null));
        formBeanConfig.addFormPropertyConfig(new FormPropertyConfig("loginModuleNames", "java.util.List", null));
        formBeanConfig.setType(DynaActionForm.class.getName());
        formBeanConfig.setName("form");
        dynaActionForm = new DynaActionForm();
        support.form = dynaActionForm;
        FieldAccessor.set(support.form, "dynaClass", DynaActionFormClass.createDynaActionFormClass(formBeanConfig));
//        properties = new XPlannerProperties().get();
//        properties.clear();
//        properties.put(AuthenticatorImpl.LOGIN_MODULE_KEY, MockLoginModule.class.fromNameKey());
        mockAuthenticator = new MockAuthenticator();
        action = new AuthenticationAction();
        action.setAuthenticator(mockAuthenticator);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /** Test no action in form.
     *
     * @throws Exception
     *             the exception
     */
    public void testNoActionInForm() throws Exception {
        ActionForward forward = support.executeAction(action);

        assertEquals("wrong forward", "notAuthenticated", forward.getName());
    }

    /** Test successful login.
     *
     * @throws Exception
     *             the exception
     */
    public void testSuccessfulLogin() throws Exception {
        dynaActionForm.set("action", "login");

        ActionForward forward = support.executeAction(action);

        assertEquals("wrong forward", "authenticated", forward.getName());
    }

    /** Test unsuccessful login.
     *
     * @throws Exception
     *             the exception
     */
    public void testUnsuccessfulLogin() throws Exception {
        dynaActionForm.set("action", "login");
        mockAuthenticator.authenticateException = new AuthenticationException("test");
        mockAuthenticator.authenticateException.getErrorsByModule().put("TestLoginContextSerializability", "test");

        ActionForward forward = support.executeAction(action);

        assertEquals("wrong forward", "notAuthenticated", forward.getName());
        ActionMessages messages = (ActionMessages)support.request.getAttribute(Globals.MESSAGE_KEY);
        assertEquals("wrong # of messages", 3, messages.size());
        Iterator messageIterator = messages.get();
        assertEquals("wrong module message key", "login.failed",
                     ((ActionMessage)messageIterator.next()).getKey());
        assertEquals("wrong module message key", "test",
                     ((ActionMessage)messageIterator.next()).getKey());

    }

}
