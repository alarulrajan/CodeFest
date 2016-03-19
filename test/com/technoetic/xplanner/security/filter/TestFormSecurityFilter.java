package com.technoetic.xplanner.security.filter;

/**
 * The Class TestFormSecurityFilter.
 */
public class TestFormSecurityFilter extends AbstractSecurityFilterTestCase {
    
    /** The security filter. */
    private FormSecurityFilter securityFilter;
    
    /** The authenticator url. */
    private final String AUTHENTICATOR_URL = "/login";

    /** Instantiates a new test form security filter.
     *
     * @param s
     *            the s
     */
    public TestFormSecurityFilter(String s) {
        super(s);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.filter.AbstractSecurityFilterTestCase#setUp()
     */
    protected void setUp() throws Exception {
        securityFilter = new FormSecurityFilter();
        securityFilter.setAuthenticatorUrl(AUTHENTICATOR_URL);
        super.setUp(); // super.setUp depends on securityFilter
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.filter.AbstractSecurityFilterTestCase#getSecurityFilter()
     */
    protected AbstractSecurityFilter getSecurityFilter() {
        return securityFilter;
    }

    /** Test bypassed request.
     *
     * @throws Exception
     *             the exception
     */
    public void testBypassedRequest() throws Exception {
        setUpRequest("", "/do/login");

        securityFilter.doFilter(support.request, support.response, mockFilterChain);

        assertNull("redirect", support.response.getRedirect());
        assertTrue("filter chain not called", mockFilterChain.doFilterCalled);
    }

    /** Test authenticated.
     *
     * @throws Exception
     *             the exception
     */
    public void testAuthenticated() throws Exception {
        setUpSubject();
        setUpRequest("", "/do/something");

        securityFilter.doFilter(support.request, support.response, mockFilterChain);

        assertNull("redirect", support.response.getRedirect());
        assertTrue("filter chain not called", mockFilterChain.doFilterCalled);
    }

    /** Test not authenticated.
     *
     * @throws Exception
     *             the exception
     */
    public void testNotAuthenticated() throws Exception {
        setUpRequest("/x/*", "/do/something");

        securityFilter.doFilter(support.request, support.response, mockFilterChain);

        assertEquals("wrong redirect", CONTEXT + AUTHENTICATOR_URL, support.response.getRedirect());
        assertFalse("filter chain called", mockFilterChain.doFilterCalled);
    }
}
