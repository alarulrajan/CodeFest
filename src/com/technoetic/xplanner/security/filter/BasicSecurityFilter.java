package com.technoetic.xplanner.security.filter;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.Authenticator;

/**
 * The Class BasicSecurityFilter.
 */
public class BasicSecurityFilter extends AbstractSecurityFilter {
    
    /** The authenticator. */
    private Authenticator authenticator;
    
    /** The basic prefix. */
    private final String BASIC_PREFIX = "Basic ";

    /**
     * Instantiates a new basic security filter.
     */
    public BasicSecurityFilter() {
    }

    /**
     * Instantiates a new basic security filter.
     *
     * @param authenticator
     *            the authenticator
     */
    public BasicSecurityFilter(final Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.filter.AbstractSecurityFilter#doInit(javax.servlet.FilterConfig)
     */
    @Override
    protected void doInit(final FilterConfig filterConfig)
            throws ServletException {
        this.authenticator = (Authenticator) WebApplicationContextUtils
                .getRequiredWebApplicationContext(
                        filterConfig.getServletContext()).getBean(
                        "authenticator");
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.filter.AbstractSecurityFilter#isAuthenticated(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected boolean isAuthenticated(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        return this.isSubjectInSession(request)
                || this.isChallengeAuthenticated(request);
    }

    /**
     * Checks if is challenge authenticated.
     *
     * @param request
     *            the request
     * @return true, if is challenge authenticated
     * @throws ServletException
     *             the servlet exception
     */
    private boolean isChallengeAuthenticated(final HttpServletRequest request)
            throws ServletException {
        boolean isAuthenticated = false;
        String credentials = request.getHeader("Authorization");
        if (credentials != null && credentials.startsWith(this.BASIC_PREFIX)) {
            credentials = credentials.substring(this.BASIC_PREFIX.length());
            if (credentials != null) {
                final String[] userIdAndPassword = new String(
                        com.sabre.security.jndi.util.Base64.decode(credentials
                                .getBytes())).split(":");
                try {
                    this.authenticator.authenticate(request,
                            userIdAndPassword[0], userIdAndPassword[1]);
                    isAuthenticated = true;
                } catch (final AuthenticationException e) {
                    this.log.info("basic authentication failed: user="
                            + userIdAndPassword[0] + ", reason="
                            + e.getMessage());
                } catch (final Exception e) {
                    throw new ServletException(e);
                }
            }
        }
        return isAuthenticated;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.filter.AbstractSecurityFilter#onAuthenticationFailure(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected boolean onAuthenticationFailure(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        this.challenge(response);
        return false;
    }

    /**
     * Challenge.
     *
     * @param response
     *            the response
     */
    private void challenge(final HttpServletResponse response) {
        response.setHeader("WWW-Authenticate", "Basic realm=\"XPlanner\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
