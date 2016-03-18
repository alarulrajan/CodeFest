package com.technoetic.xplanner.security;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.security.module.ConfigurationException;
import com.technoetic.xplanner.security.module.LoginModuleLoader;
import com.technoetic.xplanner.util.LogUtil;

public class LoginContext implements Serializable {
	protected static final Logger LOG = LogUtil.getLogger();

	private transient LoginModule[] loginModules;
	private transient LoginModuleLoader moduleLoader;
	private Subject subject;
	private int loginModuleIndex;

	public LoginContext(final LoginModuleLoader moduleLoader) {
		this.moduleLoader = moduleLoader;
	}

	public void authenticate(final String userId, final String password)
			throws AuthenticationException {
		final HashMap errorMap = new HashMap();
		final LoginModule[] modules = this.getLoginModules();
		LoginModule loginModule = null;
		for (int i = 0; i < modules.length; i++) {
			try {
				loginModule = modules[i];
				this.subject = loginModule.authenticate(userId, password);
				this.loginModuleIndex = i;
				LoginContext.LOG.debug("Authenticating successfully " + this);
				return;
			} catch (final AuthenticationException aex) {
				errorMap.put(loginModule.getName(), aex.getMessage());
			}
		}
		this.loginModuleIndex = -1;
		LoginContext.LOG.debug("Failure to authenticate " + this);
		throw new AuthenticationException(errorMap);
	}

	public LoginModule getLoginModule() throws ConfigurationException {
		final LoginModule[] modules = this.getLoginModules();
		if (this.loginModuleIndex >= modules.length) {
			throw new RuntimeException("index of used login module="
					+ this.loginModuleIndex + ", number of modules="
					+ modules.length);
		}
		return modules[this.loginModuleIndex];
	}

	public Subject getSubject() {
		return this.subject;
	}

	public void logout(final HttpServletRequest request)
			throws AuthenticationException {
		final LoginModule[] modules = this.getLoginModules();
		for (int i = 0; i < modules.length; i++) {
			modules[i].logout(request);
		}
	}

	protected LoginModule[] getLoginModules() throws ConfigurationException {
		if (this.loginModules == null) {
			this.loginModules = this.moduleLoader.loadLoginModules();
		}
		return this.loginModules;
	}

	// DEBT: The deserialization creating new LoginModule should be cleaned up
	// when we have a bean context per session/request...
	private final void readObject(final ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.loginModules = this.getLoginModules();
		LoginContext.LOG.debug("Deserializing... " + this);
	}

	@Override
	public String toString() {
		return "LoginContext{"
				+ "subject="
				+ this.subject
				+ ", loginModuleIndex="
				+ this.loginModuleIndex
				+ ", loginModules ="
				+ (this.loginModules == null ? null : Arrays
						.asList(this.loginModules)) + ", moduleLoader="
				+ this.moduleLoader + '}';
	}
}
