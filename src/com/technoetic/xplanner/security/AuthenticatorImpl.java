package com.technoetic.xplanner.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.util.LogUtil;

public class AuthenticatorImpl implements Authenticator {
	private static Logger LOG = LogUtil.getLogger();
	public static final String LOGIN_CONTEXT_SESSION_KEY = "LOGIN_CONTEXT";
	static final String GUESTS_KEY = "xplanner.security.guests";

	private LoginContext loginContext;
	public int NO_PARENT = 0;

	public AuthenticatorImpl(final LoginContext loginContext) {
		this.loginContext = loginContext;
	}

	public AuthenticatorImpl() {
	}

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

	public LoginContext getLoginContext() {
		return this.loginContext;
	}

	public static LoginContext getLoginContext(final HttpServletRequest request) {
		return (LoginContext) request.getSession().getAttribute(
				AuthenticatorImpl.LOGIN_CONTEXT_SESSION_KEY);
	}

	public static void setLoginContext(final HttpServletRequest request,
			final LoginContext context) {
		request.getSession().setAttribute(
				AuthenticatorImpl.LOGIN_CONTEXT_SESSION_KEY, context);
	}

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
