package com.technoetic.xplanner.security.filter;

import javax.servlet.ServletException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.MockAuthenticator;
import com.technoetic.xplanner.security.util.Base64;

/**
 * The Class TestBasicSecurityFilter.
 */
public class TestBasicSecurityFilter extends AbstractSecurityFilterTestCase {
    
    /** The security filter. */
    private BasicSecurityFilter securityFilter;
    
    /** The mock authenticator. */
    private MockAuthenticator mockAuthenticator;

    /** Instantiates a new test basic security filter.
     *
     * @param s
     *            the s
     */
    public TestBasicSecurityFilter(String s) {
        super(s);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.filter.AbstractSecurityFilterTestCase#setUp()
     */
    protected void setUp() throws Exception {
        mockAuthenticator = new MockAuthenticator();
        securityFilter = new BasicSecurityFilter(mockAuthenticator);
        super.setUp();
        Logger.getLogger(BasicSecurityFilter.class).setLevel(Level.ERROR);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.filter.AbstractSecurityFilterTestCase#getSecurityFilter()
     */
    protected AbstractSecurityFilter getSecurityFilter() {
        return securityFilter;
    }

    /** Test on authentiation request when authenticated.
     *
     * @throws Exception
     *             the exception
     */
    public void testOnAuthentiationRequestWhenAuthenticated() throws Exception {
        support.request.setHeader("Authorization", "Basic " + new String(Base64.encode("user:pass".getBytes())));

        securityFilter.doFilter(support.request, support.response, mockFilterChain);

        assertAuthenticatorUsage();
    }

    /** Assert authenticator usage.
     */
    private void assertAuthenticatorUsage() {
        assertTrue("authenticator not called", mockAuthenticator.authenticateCalled);
        assertEquals("wrong auth userId", "user", mockAuthenticator.authenticateUserId);
        assertEquals("wrong auth password", "pass", mockAuthenticator.authenticatePassword);
    }

    /** Test ignored authentication exception.
     *
     * @throws Exception
     *             the exception
     */
    public void testIgnoredAuthenticationException() throws Exception {
        // Auth exception simply means the user will not have any credentials in session
        support.request.setHeader("Authorization", "Basic " + new String(Base64.encode("user:pass".getBytes())));
        mockAuthenticator.authenticateException = new AuthenticationException("test");

        securityFilter.doFilter(support.request, support.response, mockFilterChain);

        assertAuthenticatorUsage();
    }

    /** Test converted other exception.
     *
     * @throws Exception
     *             the exception
     */
    public void testConvertedOtherException() throws Exception {
        support.request.setHeader("Authorization", "Basic " + new String(Base64.encode("user:pass".getBytes())));
        mockAuthenticator.authenticateException = new AuthenticationException("test");

        try {
            securityFilter.doFilter(support.request, support.response, mockFilterChain);
        } catch (ServletException ex) {
            // expected
            assertEquals("wrong nested exception", mockAuthenticator.authenticateException, ex.getRootCause());
            assertAuthenticatorUsage();
        }

    }

    /** Test already authenticated.
     *
     * @throws Exception
     *             the exception
     */
    public void testAlreadyAuthenticated() throws Exception {
        setUpSubject();

        securityFilter.doFilter(support.request, support.response, mockFilterChain);

        assertNoChallenge();
    }

    /** Test not authenticated.
     *
     * @throws Exception
     *             the exception
     */
    public void testNotAuthenticated() throws Exception {
        setUpRequest("/x/*", "foo");

        securityFilter.doFilter(support.request, support.response, mockFilterChain);

        assertChallenge();
    }

    /** Assert no challenge.
     */
    private void assertNoChallenge() {
        assertNull("redirect", support.response.getRedirect());
        assertTrue("filter chain not called", mockFilterChain.doFilterCalled);
    }

    /** Assert challenge.
     */
    private void assertChallenge() {
        assertEquals("no challenge", "Basic realm=\"XPlanner\"", support.response.getHeader("WWW-Authenticate"));
        assertEquals("wrong HTTP status", 401, support.response.getStatus());
        assertFalse("filter chain called", mockFilterChain.doFilterCalled);
    }

}
