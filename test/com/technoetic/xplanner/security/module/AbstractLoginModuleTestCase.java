package com.technoetic.xplanner.security.module;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;

import junit.framework.TestCase;

import org.easymock.MockControl;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.LoginModule;
import com.technoetic.xplanner.util.MainBeanFactory;

/**
 * The Class AbstractLoginModuleTestCase.
 */
public abstract class AbstractLoginModuleTestCase extends TestCase
{
    
    /** The Constant PASSWORD. */
    protected static final String PASSWORD = "password";
    
    /** The Constant USER_ID. */
    protected static final String USER_ID = "user";
    
    /** The login module. */
    protected LoginModule loginModule;
    
    /** The mock login support control. */
    protected MockControl mockLoginSupportControl;
    
    /** The mock login support. */
    protected LoginSupport mockLoginSupport;
    
    /** The mock subject. */
    protected Subject mockSubject;

    /** Instantiates a new abstract login module test case.
     */
    public AbstractLoginModuleTestCase(){
    }

    /** Instantiates a new abstract login module test case.
     *
     * @param s
     *            the s
     */
    public AbstractLoginModuleTestCase(String s){
        super (s);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
       super.setUp();
       mockLoginSupportControl = MockControl.createControl(LoginSupport.class);
       mockLoginSupport = (LoginSupport) mockLoginSupportControl.getMock();
       mockSubject = new Subject();
       loginModule = createLoginModule();
    }

   /** Creates the login module.
     *
     * @return the login module
     */
   protected LoginModule createLoginModule() {
      return null;
   }

   /* (non-Javadoc)
    * @see junit.framework.TestCase#tearDown()
    */
   protected void tearDown() throws Exception {
      MainBeanFactory.reset();
      super.tearDown();
   }

   /** Replay.
     */
   protected void replay()
    {
        mockLoginSupportControl.replay();
    }

    /** Verify.
     */
    protected void verify()
    {
        mockLoginSupportControl.verify();
    }

    /** Authenticate and check exception.
     *
     * @param errorMessageKey
     *            the error message key
     */
    protected void authenticateAndCheckException(String errorMessageKey)
    {
        try
        {
            loginModule.authenticate(USER_ID, PASSWORD);
            fail("Exception should be thrown");
        }
        catch (AuthenticationException e)
        {
            assertEquals("Wrong error message", errorMessageKey, e.getMessage());
        }
    }

   /** Creates the subject with principal.
     *
     * @param principal
     *            the principal
     * @return the subject
     */
   protected Subject createSubjectWithPrincipal(Principal principal) {
      Set principalSet = new HashSet();
      principalSet.add(principal);
      return new Subject(true, principalSet, Collections.EMPTY_SET, Collections.EMPTY_SET);
   }
}
