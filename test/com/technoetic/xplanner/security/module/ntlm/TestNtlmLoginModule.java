package com.technoetic.xplanner.security.module.ntlm;

import java.net.UnknownHostException;
import java.util.HashMap;

import javax.security.auth.Subject;

import net.sf.xplanner.domain.Person;

import org.easymock.MockControl;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.LoginModule;
import com.technoetic.xplanner.security.module.AbstractLoginModuleTestCase;
import com.technoetic.xplanner.security.module.jaas.JaasLoginModuleAdapter;

/**
 * The Class TestNtlmLoginModule.
 */
public class TestNtlmLoginModule extends AbstractLoginModuleTestCase
{
    
    /** The Constant DOMAIN. */
    static final String DOMAIN = "YANDEX";
    
    /** The mock ntlm login helper control. */
    MockControl mockNtlmLoginHelperControl;
    
    /** The mock ntlm login helper. */
    NtlmLoginHelper mockNtlmLoginHelper;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.module.AbstractLoginModuleTestCase#createLoginModule()
     */
    protected LoginModule createLoginModule()
    {
        mockNtlmLoginHelperControl = MockControl.createControl(NtlmLoginHelper.class);
        mockNtlmLoginHelper = (NtlmLoginHelper) mockNtlmLoginHelperControl.getMock();
        return new NtlmLoginModule(mockLoginSupport, mockNtlmLoginHelper);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.module.AbstractLoginModuleTestCase#setUp()
     */
    public void setUp() throws Exception
    {
        super.setUp();
        HashMap options = new HashMap();
        options.put(NtlmLoginModule.DOMAIN_KEY, DOMAIN);
        options.put(NtlmLoginModule.CONTROLLER_KEY, DOMAIN);
        loginModule.setOptions(options);
    }

    /** Test authenticate_ successful.
     *
     * @throws Exception
     *             the exception
     */
    public void testAuthenticate_Successful() throws Exception
    {
        Person mockPerson = new Person(USER_ID);
        mockNtlmLoginHelper.authenticate(USER_ID, PASSWORD, DOMAIN, DOMAIN);
        mockLoginSupportControl.expectAndReturn(mockLoginSupport.createSubject(), mockSubject);
        mockLoginSupportControl.expectAndReturn(
              mockLoginSupport.populateSubjectPrincipalFromDatabase(mockSubject, USER_ID), mockPerson);
        replay();
        Subject subject = loginModule.authenticate(USER_ID, PASSWORD);
        verify();
        assertEquals(mockSubject,  subject);

    }

    /** Test authenticate_ user not found.
     *
     * @throws Exception
     *             the exception
     */
    public void testAuthenticate_UserNotFound() throws Exception
    {
        mockNtlmLoginHelper.authenticate(USER_ID, PASSWORD, DOMAIN, DOMAIN);
        mockLoginSupportControl.expectAndReturn(mockLoginSupport.createSubject(), mockSubject);
        mockLoginSupport.populateSubjectPrincipalFromDatabase(mockSubject, USER_ID);
        mockLoginSupportControl.setThrowable(new AuthenticationException(LoginModule.MESSAGE_USER_NOT_FOUND_KEY));
        replay();
        authenticateAndCheckException(JaasLoginModuleAdapter.MESSAGE_USER_NOT_FOUND_KEY);
        verify();

    }

    /** Test authenticate_ server not found.
     *
     * @throws Exception
     *             the exception
     */
    public void testAuthenticate_ServerNotFound() throws Exception
    {

        mockNtlmLoginHelper.authenticate(USER_ID, PASSWORD, DOMAIN, DOMAIN);
        mockNtlmLoginHelperControl.setThrowable(new UnknownHostException());
        replay();
        authenticateAndCheckException(NtlmLoginModule.MESSAGE_SERVER_NOT_FOUND_KEY);
        verify();

    }


    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.module.AbstractLoginModuleTestCase#replay()
     */
    protected void replay()
    {
        super.replay();
        mockNtlmLoginHelperControl.replay();
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.module.AbstractLoginModuleTestCase#verify()
     */
    protected void verify()
    {
        super.verify();
        mockNtlmLoginHelperControl.verify();
    }
}