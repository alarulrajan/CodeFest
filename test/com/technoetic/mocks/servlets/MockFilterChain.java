package com.technoetic.mocks.servlets;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * The Class MockFilterChain.
 */
public class MockFilterChain implements FilterChain {
    
    /** The do filter called. */
    public boolean doFilterCalled;
    
    /** The do filter servlet request. */
    public ServletRequest doFilterServletRequest;
    
    /** The do filter servlet response. */
    public ServletResponse doFilterServletResponse;

    /* (non-Javadoc)
     * @see javax.servlet.FilterChain#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse)
            throws IOException, ServletException {
        doFilterCalled = true;
        doFilterServletRequest = servletRequest;
        doFilterServletResponse = servletResponse;
    }
}
