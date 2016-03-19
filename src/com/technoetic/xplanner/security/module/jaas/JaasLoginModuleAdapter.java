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
	
	/** The log. */
	protected transient Logger log = Logger.getLogger(this.getClass());
	
	/** The Constant USERID. */
	private static final String USERID = "javax.security.auth.login.name";
	
	/** The Constant PASSWORD. */
	private static final String PASSWORD = "javax.security.auth.login.password";

	/** The principal class. */
	private Class principalClass;
	
	/** The options. */
	private Map options;
	
	/** The name. */
	private String name;
	
	/** The jaas login module. */
	private transient javax.security.auth.spi.LoginModule jaasLoginModule;
	
	/** The login support. */
	private transient LoginSupport loginSupport;

	/**
     * Instantiates a new jaas login module adapter.
     *
     * @param support
     *            the support
     */
	public JaasLoginModuleAdapter(final LoginSupport support) {
		this.loginSupport = support;
	}

	/**
     * Instantiates a new jaas login module adapter.
     *
     * @param support
     *            the support
     * @param jaasLoginModule
     *            the jaas login module
     * @param principalClass
     *            the principal class
     * @param options
     *            the options
     */
	public JaasLoginModuleAdapter(final LoginSupport support,
			final javax.security.auth.spi.LoginModule jaasLoginModule,
			final Class principalClass, final Map options) {
		this.options = options;
		this.principalClass = principalClass;
		this.jaasLoginModule = jaasLoginModule;
		this.loginSupport = support;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.security.LoginModule#setOptions(java.util.Map)
	 */
	@Override
	public void setOptions(final Map options) {
		this.options = options;
	}

	/**
     * Gets the JAAS login module.
     *
     * @return the JAAS login module
     */
	public javax.security.auth.spi.LoginModule getJAASLoginModule() {
		return this.jaasLoginModule;
	}

	/**
     * Gets the principal class.
     *
     * @return the principal class
     */
	public Class getPrincipalClass() {
		return this.principalClass;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.security.LoginModule#authenticate(java.lang.String, java.lang.String)
	 */
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

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.security.LoginModule#isCapableOfChangingPasswords()
	 */
	@Override
	public boolean isCapableOfChangingPasswords() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.security.LoginModule#changePassword(java.lang.String, java.lang.String)
	 */
	@Override
	public void changePassword(final String userId, final String password)
			throws AuthenticationException {
		throw new UnsupportedOperationException("changePassword not supported");
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.security.LoginModule#logout(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void logout(final HttpServletRequest request)
			throws AuthenticationException {
		request.getSession().invalidate();
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.security.LoginModule#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.security.LoginModule#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name) {
		this.name = name;
	}

	/**
     * Sets the login support.
     *
     * @param loginSupport
     *            the new login support
     */
	public void setLoginSupport(final LoginSupport loginSupport) {
		this.loginSupport = loginSupport;
	}

}
