package com.technoetic.xplanner.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.util.LogUtil;

/**
 * The Class AuthenticatorImpl.
 */
public class AuthenticatorImpl implements Authenticator {
    
    /** The log. */
    private static Logger LOG = LogUtil.getLogger();
    
    /** The Constant LOGIN_CONTEXT_SESSION_KEY. */
    public static final String LOGIN_CONTEXT_SESSION_KEY = "LOGIN_CONTEXT";
    
    /** The Constant GUESTS_KEY. */
    static final String GUESTS_KEY = "xplanner.security.guests";

    /** The login context. */
    private LoginContext loginContext;
    
    /** The no parent. */
    public int NO_PARENT = 0;

    /**
     * Instantiates a new authenticator impl.
     *
     * @param loginContext
     *            the login context
     */
    public AuthenticatorImpl(final LoginContext loginContext) {
        this.loginContext = loginContext;
    }

    /**
     * Instantiates a new authenticator impl.
     */
    public AuthenticatorImpl() {
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.Authenticator#authenticate(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
     */
    @Override
    public void authenticate(final HttpServletRequest request,
            final String userId, final String password)
            throws AuthenticationException {
        final LoginContext loginContext = this.getLoginContext();
        if (SecurityHelper.isUserAuthenticated(request)) {
            loginContext.logout(request);
        }
        loginContext.authenticate(userId, password);
        SecurityHelper.setSubject(request, loginContext.getSubject());

        AuthenticatorImpl.setLoginContext(request, loginContext);
    }

    /**
     * Gets the login context.
     *
     * @return the login context
     */
    public LoginContext getLoginContext() {
        return this.loginContext;
    }

    /**
     * Gets the login context.
     *
     * @param request
     *            the request
     * @return the login context
     */
    public static LoginContext getLoginContext(final HttpServletRequest request) {
        return (LoginContext) request.getSession().getAttribute(
                AuthenticatorImpl.LOGIN_CONTEXT_SESSION_KEY);
    }

    /**
     * Sets the login context.
     *
     * @param request
     *            the request
     * @param context
     *            the context
     */
    public static void setLoginContext(final HttpServletRequest request,
            final LoginContext context) {
        request.getSession().setAttribute(
                AuthenticatorImpl.LOGIN_CONTEXT_SESSION_KEY, context);
    }

    /**
     * Gets the login module.
     *
     * @param request
     *            the request
     * @return the login module
     */
    public static LoginModule getLoginModule(final HttpServletRequest request) {
        final LoginContext context = AuthenticatorImpl.getLoginContext(request);
        if (context == null) {
            return null;
        }
        LoginModule loginModule = null;
        try {
            loginModule = context.getLoginModule();
        } catch (final RuntimeException e) {
            AuthenticatorImpl.LOG.error(e);
        }
        return loginModule;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.Authenticator#logout(javax.servlet.http.HttpServletRequest, int)
     */
    @Override
    public void logout(final HttpServletRequest request, final int principalId)
            throws AuthenticationException {
        final LoginModule loginModule = AuthenticatorImpl
                .getLoginModule(request);
        if (loginModule != null) {
            loginModule.logout(request);
        }

    }

}
