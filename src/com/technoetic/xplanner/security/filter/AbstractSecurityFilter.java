package com.technoetic.xplanner.security.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.config.SecurityConfiguration;

/**
 * The Class AbstractSecurityFilter.
 */
public abstract class AbstractSecurityFilter implements Filter {
    
    /** The log. */
    protected Logger log = Logger.getLogger(this.getClass());
    
    /** The security configuration. */
    private SecurityConfiguration securityConfiguration;

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public final void init(final FilterConfig filterConfig)
            throws ServletException {
        try {
            this.doInit(filterConfig);
            final String filename = filterConfig
                    .getInitParameter("securityConfiguration");
            // do-before-release unit test for null filename
            if (filename != null) {
                final InputStream configurationStream = filterConfig
                        .getServletContext().getResourceAsStream(filename);
                if (configurationStream == null) {
                    throw new ServletException(
                            "could not load security configuration: "
                                    + filename);
                }
                this.securityConfiguration = SecurityConfiguration
                        .load(configurationStream);
            }
        } catch (final Exception e) {
            throw new ServletException(e);
        }
    }

    /**
     * Do init.
     *
     * @param filterConfig
     *            the filter config
     * @throws ServletException
     *             the servlet exception
     */
    protected abstract void doInit(FilterConfig filterConfig)
            throws ServletException;

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public final void doFilter(final ServletRequest servletRequest,
            final ServletResponse servletResponse, final FilterChain filterChain)
            throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        boolean continueFilterChain = true;
        if (this.isSecureRequest(request)
                && !this.isAuthenticated(request, response)) {
            continueFilterChain = this.onAuthenticationFailure(request,
                    response);
        }
        if (continueFilterChain) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * Checks if is secure request.
     *
     * @param request
     *            the request
     * @return true, if is secure request
     */
    // do-before-release unit test
    private boolean isSecureRequest(final HttpServletRequest request) {
        return this.securityConfiguration == null
                || this.securityConfiguration.isSecureRequest(request);
    }

    /**
     * Checks if is authenticated.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @return true, if is authenticated
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected abstract boolean isAuthenticated(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException;

    /**
     * On authentication failure.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @return true, if successful
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected abstract boolean onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        // empty
    }

    /**
     * Sets the security configuration.
     *
     * @param securityConfiguration
     *            the new security configuration
     */
    public void setSecurityConfiguration(
            final SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
    }

    /**
     * Checks if is subject in session.
     *
     * @param request
     *            the request
     * @return true, if is subject in session
     */
    protected boolean isSubjectInSession(final HttpServletRequest request) {
        return SecurityHelper.isUserAuthenticated(request);
    }
}
