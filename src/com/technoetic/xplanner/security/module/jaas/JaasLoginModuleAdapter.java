package com.technoetic.xplanner.security.module.jaas;

import java.security.Principal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.LoginModule;
import com.technoetic.xplanner.security.module.LoginSupport;

/**
 * This is an unsupported JAAS login module adapter. It's provide as an example
 * of a LoginModule implementation.
 */
public class JaasLoginModuleAdapter implements LoginModule {
	protected transient Logger log = Logger.getLogger(this.getClass());
	private static final String USERID = "javax.security.auth.login.name";
	private static final String PASSWORD = "javax.security.auth.login.password";

	private Class principalClass;
	private Map options;
	private String name;
	private transient javax.security.auth.spi.LoginModule jaasLoginModule;
	private transient LoginSupport loginSupport;

	public JaasLoginModuleAdapter(final LoginSupport support) {
		this.loginSupport = support;
	}

	public JaasLoginModuleAdapter(final LoginSupport support,
			final javax.security.auth.spi.LoginModule jaasLoginModule,
			final Class principalClass, final Map options) {
		this.options = options;
		this.principalClass = principalClass;
		this.jaasLoginModule = jaasLoginModule;
		this.loginSupport = support;
	}

	@Override
	public void setOptions(final Map options) {
		this.options = options;
	}

	public javax.security.auth.spi.LoginModule getJAASLoginModule() {
		return this.jaasLoginModule;
	}

	public Class getPrincipalClass() {
		return this.principalClass;
	}

	@Override
	public Subject authenticate(final String userId, final String password)
			throws AuthenticationException {
		this.log.debug(LoginModule.ATTEMPTING_TO_AUTHENTICATE + this.getName()
				+ " (" + userId + ")");
		final Subject subject = this.loginSupport.createSubject();
		final Map sharedState = new HashMap();
		sharedState.put(JaasLoginModuleAdapter.USERID, userId);
		sharedState
				.put(JaasLoginModuleAdapter.PASSWORD, password.toCharArray());
		this.jaasLoginModule.initialize(subject,
				new UserIdAndPasswordCallbackHandler(userId, password),
				sharedState, this.options);
		try {
			if (this.jaasLoginModule.login()) {
				this.jaasLoginModule.commit();
			} else {
				throw new AuthenticationException(
						LoginModule.MESSAGE_AUTHENTICATION_FAILED_KEY);
			}
		} catch (final FailedLoginException e) {
			throw new AuthenticationException(
					LoginModule.MESSAGE_AUTHENTICATION_FAILED_KEY);
		} catch (final LoginException e) {
			this.log.error("login error", e);
			throw new AuthenticationException(
					LoginModule.MESSAGE_SERVER_ERROR_KEY);
		}
		final Set principals = subject.getPrincipals(this.getPrincipalClass());
		final Iterator principalIterator = principals.iterator();
		if (principalIterator.hasNext()) {
			final Principal jaasUserPrincipal = (Principal) principalIterator
					.next();
			this.loginSupport.populateSubjectPrincipalFromDatabase(subject,
					jaasUserPrincipal.getName());
		}
		this.log.debug(LoginModule.AUTHENTICATION_SUCCESFULL + this.getName());
		return subject;
	}

	@Override
	public boolean isCapableOfChangingPasswords() {
		return false;
	}

	@Override
	public void changePassword(final String userId, final String password)
			throws AuthenticationException {
		throw new UnsupportedOperationException("changePassword not supported");
	}

	@Override
	public void logout(final HttpServletRequest request)
			throws AuthenticationException {
		request.getSession().invalidate();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	public void setLoginSupport(final LoginSupport loginSupport) {
		this.loginSupport = loginSupport;
	}

}
