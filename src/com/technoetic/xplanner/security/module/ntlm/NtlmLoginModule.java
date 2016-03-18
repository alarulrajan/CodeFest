package com.technoetic.xplanner.security.module.ntlm;

import java.util.Map;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.LoginModule;
import com.technoetic.xplanner.security.module.LoginSupport;
import com.technoetic.xplanner.security.module.LoginSupportImpl;

public class NtlmLoginModule implements LoginModule {
	private String domainController;
	private String domain;
	private String name;
	private transient Logger log = Logger.getLogger(this.getClass());
	transient LoginSupport support = new LoginSupportImpl();
	transient NtlmLoginHelper helper = new NtlmLoginHelperImpl();
	public static final String DOMAIN_KEY = "domain";
	public static final String CONTROLLER_KEY = "controller";

	public NtlmLoginModule(final LoginSupport loginSupport,
			final NtlmLoginHelper helper) {
		this.support = loginSupport;
		this.helper = helper;
	}

	@Override
	public void setOptions(final Map options) {
		this.domain = options.get(NtlmLoginModule.DOMAIN_KEY) != null ? (String) options
				.get(NtlmLoginModule.DOMAIN_KEY) : "YANDEX";
		this.domainController = options.get(NtlmLoginModule.CONTROLLER_KEY) != null ? (String) options
				.get(NtlmLoginModule.CONTROLLER_KEY) : this.domain;
		this.log.debug("initialized");
	}

	@Override
	public Subject authenticate(final String userId, final String password)
			throws AuthenticationException {
		this.log.debug(LoginModule.ATTEMPTING_TO_AUTHENTICATE + this.getName()
				+ " (" + userId + ")");
		try {
			this.helper.authenticate(userId, password, this.domainController,
					this.domain);

		} catch (final SmbAuthException sae) {
			this.log.error("NT domain did not authenticate user " + userId);
			throw new AuthenticationException(
					LoginModule.MESSAGE_AUTHENTICATION_FAILED_KEY);
		} catch (final SmbException se) {
			this.log.error("SmbException while authenticating " + userId, se);
			throw new AuthenticationException(
					LoginModule.MESSAGE_SERVER_ERROR_KEY);
		} catch (final java.net.UnknownHostException e) {
			this.log.error("UnknownHostException while authenticating "
					+ userId, e);
			throw new AuthenticationException(
					LoginModule.MESSAGE_SERVER_NOT_FOUND_KEY);
		}

		this.log.info("NT domain authenticated user " + userId);

		final Subject subject = this.support.createSubject();
		this.log.debug("looking for user: " + userId);
		this.support.populateSubjectPrincipalFromDatabase(subject, userId);
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
		throw new UnsupportedOperationException(
				"change Password not implemented");
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

}
