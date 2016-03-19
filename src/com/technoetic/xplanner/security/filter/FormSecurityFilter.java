package com.technoetic.xplanner.security.filter;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.Authenticator;
import com.technoetic.xplanner.security.CredentialCookie;
import com.technoetic.xplanner.security.SecurityHelper;

/**
 * The Class FormSecurityFilter.
 */
public class FormSecurityFilter extends AbstractSecurityFilter {
    
    /** The authenticator url. */
    private String authenticatorUrl;
    
    /** The authentication url key. */
    public final String AUTHENTICATION_URL_KEY = "authenticatorUrl";
    
    /** The authenticator. */
    private Authenticator authenticator;

    /**
     * Instantiates a new form security filter.
     */
    public FormSecurityFilter() {
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
        this.authenticatorUrl = this.getInitParameter(filterConfig,
                this.AUTHENTICATION_URL_KEY);
    }

    /**
     * Gets the inits the parameter.
     *
     * @param filterConfig
     *            the filter config
     * @param parameterName
     *            the parameter name
     * @return the inits the parameter
     * @throws ServletException
     *             the servlet exception
     */
    private String getInitParameter(final FilterConfig filterConfig,
            final String parameterName) throws ServletException {
        final String value = filterConfig.getInitParameter(parameterName);
        if (StringUtils.isEmpty(value)) {
            throw new ServletException(this.getClass().getName() + ": "
                    + parameterName + " is required");
        }
        return value;
    }

    /**
     * Sets the authenticator url.
     *
     * @param authenticatorUrl
     *            the new authenticator url
     */
    public void setAuthenticatorUrl(final String authenticatorUrl) {
        this.authenticatorUrl = authenticatorUrl;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.filter.AbstractSecurityFilter#isAuthenticated(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected boolean isAuthenticated(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        boolean isAuthenticated = false;
        final CredentialCookie credentials = new CredentialCookie(request,
                response);
        if (!this.isSubjectInSession(request)) {
            if (credentials.isPresent()) {
                try {
                    this.authenticator.authenticate(request,
                            credentials.getUserId(), credentials.getPassword());
                    isAuthenticated = true;
                } catch (final AuthenticationException e) {
                    this.log.info("cookie-based authentication failed: user="
                            + credentials.getUserId() + ", reason="
                            + e.getMessage());
                } catch (final Exception e) {
                    throw new ServletException(e);
                }
            }
        } else {
            isAuthenticated = true;
        }
        return isAuthenticated;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.filter.AbstractSecurityFilter#onAuthenticationFailure(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public boolean onAuthenticationFailure(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        final CredentialCookie credentials = new CredentialCookie(request,
                response);
        if (credentials.isPresent()) {
            credentials.remove();
        }
        if (request.getMethod().equals("GET")) {
            SecurityHelper.saveUrl(request);
        }
        final String redirectUrl = this.authenticatorUrl;
        this.log.debug(request.getRequestURL() + " being redirected to "
                + redirectUrl);
        response.sendRedirect(request.getContextPath() + redirectUrl);
        return false;
    }
}
