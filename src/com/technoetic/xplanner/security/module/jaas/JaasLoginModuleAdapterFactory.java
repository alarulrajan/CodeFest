/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.security.module.jaas;

import java.util.Map;
import java.util.MissingResourceException;

import com.technoetic.xplanner.security.LoginModule;
import com.technoetic.xplanner.security.module.ConfigurationException;
import com.technoetic.xplanner.security.module.LoginModuleFactory;
import com.technoetic.xplanner.security.module.LoginSupport;

/**
 * A factory for creating JaasLoginModuleAdapter objects.
 */
public class JaasLoginModuleAdapterFactory implements LoginModuleFactory {
	
	/** The Constant JAAS_USER_PRINCIPAL_CLASS_NAME_KEY. */
	static final String JAAS_USER_PRINCIPAL_CLASS_NAME_KEY = "jaas.principalClass";
	
	/** The Constant JAAS_LOGIN_MODULE_CLASS_NAME_KEY. */
	static final String JAAS_LOGIN_MODULE_CLASS_NAME_KEY = "jaas.loginModuleClass";
	
	/** The support. */
	private final LoginSupport support;

	/**
     * Instantiates a new jaas login module adapter factory.
     *
     * @param loginSupport
     *            the login support
     */
	public JaasLoginModuleAdapterFactory(final LoginSupport loginSupport) {
		this.support = loginSupport;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.security.module.LoginModuleFactory#newInstance(java.util.Map)
	 */
	@Override
	public LoginModule newInstance(final Map options)
			throws ConfigurationException {
		final Class principalClass = this
				.getClassFromName(
						options,
						JaasLoginModuleAdapterFactory.JAAS_USER_PRINCIPAL_CLASS_NAME_KEY);
		return new JaasLoginModuleAdapter(this.support,
				this.getJAASLoginModule(options), principalClass, options);
	}

	/**
     * Gets the class from name.
     *
     * @param options
     *            the options
     * @param property
     *            the property
     * @return the class from name
     */
	private Class getClassFromName(final Map options, final String property) {
		final String className = (String) options.get(property);
		if (className == null) {
			final Exception cause = new MissingResourceException(
					"Missing property", this.getClass().getName(), property);
			throw new ConfigurationException(cause);
		}
		Class aClass;
		try {
			aClass = Class.forName(className);
		} catch (final ClassNotFoundException e) {
			throw new ConfigurationException(e);
		}
		return aClass;
	}

	/**
     * Gets the JAAS login module.
     *
     * @param options
     *            the options
     * @return the JAAS login module
     */
	private javax.security.auth.spi.LoginModule getJAASLoginModule(
			final Map options) {
		final Class loginModuleClass = this.getClassFromName(options,
				JaasLoginModuleAdapterFactory.JAAS_LOGIN_MODULE_CLASS_NAME_KEY);
		try {
			return (javax.security.auth.spi.LoginModule) loginModuleClass
					.newInstance();
		} catch (final Exception e) {
			throw new ConfigurationException(e);
		}
	}

}