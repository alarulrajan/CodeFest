package com.technoetic.xplanner.security;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Properties;

import javax.security.auth.Subject;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Person;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.security.module.LoginModuleLoader;
import com.technoetic.xplanner.security.module.MockLoginModule;
import com.technoetic.xplanner.util.MainBeanFactory;

/**
 * The Class TestAuthenticator.
 */
public class TestAuthenticator extends TestCase {
    
    /** The authenticator. */
    private AuthenticatorImpl authenticator;
    
    /** The properties. */
    private Properties properties;
    
    /** The saved properties. */
    private Properties savedProperties;
    
    /** The support. */
    private XPlannerTestSupport support;
    
    /** The Constant MODULE_PREFIX. */
    private static final String MODULE_PREFIX = "Mock";
   
   /** The Constant MODULE_2_NAME. */
   public static final String MODULE_2_NAME = MODULE_PREFIX +"2";
   
   /** The Constant MODULE_2_CLASS. */
   public static final String MODULE_2_CLASS = MockLoginModule.class.getName()+"2";
   
   /** The next module index. */
   public int nextModuleIndex = 0;

   /** Instantiates a new test authenticator.
     *
     * @param s
     *            the s
     */
   public TestAuthenticator(String s) {
       super(s);
   }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
       super.setUp();
       Logger.getRootLogger().setLevel(Level.OFF);
       support = new XPlannerTestSupport();
       properties = new XPlannerProperties().get();
       savedProperties = new Properties();
       savedProperties.putAll(properties);
       properties.remove(LoginModuleLoader.LOGIN_MODULE_CLASS_KEY);
       properties.remove(LoginModuleLoader.LOGIN_MODULE_NAME_KEY);
       addLoginModuleToProperties();
       HibernateHelper.setSession(support.request, support.hibernateSession);
       authenticator = new AuthenticatorImpl(new LoginContext(null));
    }

   /** Adds the login module to properties.
     */
   private void addLoginModuleToProperties() {
      properties.put(MessageFormat.format(LoginModuleLoader.LOGIN_MODULE_CLASS_KEY, new Integer[] {new Integer(nextModuleIndex)}),
                     MockLoginModule.class.getName()+nextModuleIndex);
      properties.put(MessageFormat.format(LoginModuleLoader.LOGIN_MODULE_NAME_KEY, new Integer[] {new Integer(nextModuleIndex)}),
                     MODULE_PREFIX + nextModuleIndex);
      MainBeanFactory.setBean(MockLoginModule.class.getName()+nextModuleIndex, new MockLoginModule());
      nextModuleIndex++;
   }

   /* (non-Javadoc)
    * @see junit.framework.TestCase#tearDown()
    */
   protected void tearDown() throws Exception {
      properties.clear();
      properties.putAll(savedProperties);
      MainBeanFactory.reset();
      super.tearDown();
   }

    /** Test successful login.
     *
     * @throws Exception
     *             the exception
     */
    public void testSuccessfulLogin() throws Exception {
        Person person = new Person("foo");
        support.hibernateSession.find2Return = new ArrayList();
        support.hibernateSession.find2Return.add(person);
        LoginContext loginContext = authenticator.getLoginContext();
        MockLoginModule mockLoginModule = (MockLoginModule)loginContext.getLoginModules()[0];
        assertNotNull(mockLoginModule);
        mockLoginModule.authenticateReturn = new Subject();
        mockLoginModule.authenticateReturn.getPrincipals().add(new PersonPrincipal(person));

        authenticator.authenticate(support.request, "user", "password");

        assertFalse("unnecessary logout", mockLoginModule.logoutCalled);
        assertTrue(SecurityHelper.isUserAuthenticated(support.request));
    }

    /** Test chain successful login.
     *
     * @throws Exception
     *             the exception
     */
    public void testChainSuccessfulLogin() throws Exception {
       addLoginModuleToProperties();
        Person person = new Person("foo");
        support.hibernateSession.find2Return = new ArrayList();
        support.hibernateSession.find2Return.add(person);
        LoginContext loginContext = authenticator.getLoginContext();
        MockLoginModule mockLoginModule1 = (MockLoginModule)loginContext.getLoginModules()[0];
        assertNotNull(mockLoginModule1);
        mockLoginModule1.authenticateException = new AuthenticationException("Can not authenticate the user");
        MockLoginModule mockLoginModule2 = (MockLoginModule)loginContext.getLoginModules()[1];
        mockLoginModule2.authenticateReturn = new Subject();
        mockLoginModule2.authenticateReturn.getPrincipals().add(new PersonPrincipal(person));

        authenticator.authenticate(support.request, "user", "password");

        assertTrue(SecurityHelper.isUserAuthenticated(support.request));
    }

    /** Test successful login when already logged in.
     *
     * @throws Exception
     *             the exception
     */
    public void testSuccessfulLoginWhenAlreadyLoggedIn() throws Exception {
        Person person = new Person("foo");
        support.hibernateSession.find2Return = new ArrayList();
        support.hibernateSession.find2Return.add(person);
        LoginContext loginContext =authenticator.getLoginContext();
        MockLoginModule mockLoginModule = (MockLoginModule)loginContext.getLoginModules()[0];
        assertNotNull(mockLoginModule);
        mockLoginModule.authenticateReturn = new Subject();
        mockLoginModule.authenticateReturn.getPrincipals().add(new PersonPrincipal(person));

        authenticator.authenticate(support.request, "user", "password");

        assertFalse("unnecessary logout", mockLoginModule.logoutCalled);
        assertTrue(SecurityHelper.isUserAuthenticated(support.request));

        authenticator.authenticate(support.request, "user", "password");

        assertTrue("did not ensure logout", mockLoginModule.logoutCalled);
        assertTrue(SecurityHelper.isUserAuthenticated(support.request));
    }

    /** Test unsuccessful login.
     *
     * @throws Exception
     *             the exception
     */
    public void testUnsuccessfulLogin() throws Exception {
        LoginContext loginContext =authenticator.getLoginContext();
        MockLoginModule mockLoginModule = (MockLoginModule)loginContext.getLoginModules()[0];
        mockLoginModule.authenticateException = new AuthenticationException("test");

        try {
            authenticator.authenticate(support.request, "user", "password");
        } catch (AuthenticationException e) {
            // expected
            assertFalse("unnecessary logout", mockLoginModule.logoutCalled);
            assertFalse(SecurityHelper.isUserAuthenticated(support.request));
            assertFalse(support.hibernateSession.find2Called);
        }
    }
}
