/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.security.module;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.security.LoginModule;

public class LoginModuleLoader {
	public static final String LOGIN_MODULE_PROPERTY_PREFIX = "xplanner.security.login";
	public static final String LOGIN_MODULE_CLASS_KEY = LoginModuleLoader.LOGIN_MODULE_PROPERTY_PREFIX
			+ "[{0}].module";
	public static final String LOGIN_MODULE_NAME_KEY = LoginModuleLoader.LOGIN_MODULE_PROPERTY_PREFIX
			+ "[{0}].name";
	static final String LOGIN_OPTION_PREFIX = LoginModuleLoader.LOGIN_MODULE_PROPERTY_PREFIX
			+ "[{0}].option.";
	private ApplicationContext applicationContext;

	public LoginModule[] loadLoginModules() throws ConfigurationException {
		final XPlannerProperties properties = new XPlannerProperties();
		int idx = 0;
		String loginModuleClassName;
		final List loginModuleList = new ArrayList();
		while (true) {
			loginModuleClassName = properties.getProperty(LoginModuleLoader
					.getKey(LoginModuleLoader.LOGIN_MODULE_CLASS_KEY, idx));
			if (loginModuleClassName == null) {
				break;
			}

			final String loginModuleName = properties
					.getProperty(LoginModuleLoader.getKey(
							LoginModuleLoader.LOGIN_MODULE_NAME_KEY, idx));
			if (loginModuleName == null) {
				throw new ConfigurationException(
						LoginModule.MESSAGE_NO_MODULE_NAME_SPECIFIED_ERROR_KEY);
			}
			final Map options = this.getOptions(properties, idx);
			loginModuleList.add(this.createModule(loginModuleClassName,
					loginModuleName, options));
			idx++;
		}
		return (LoginModule[]) loginModuleList.toArray(new LoginModule[] {});
	}

	private LoginModule createModule(final String loginModuleClassName,
			final String loginModuleName, final Map options) {
		LoginModule loginModule;
		try {
			final Object bean = this.applicationContext
					.getBean(loginModuleClassName);
			if (bean instanceof LoginModuleFactory) {
				final LoginModuleFactory factory = (LoginModuleFactory) bean;
				loginModule = factory.newInstance(options);
			} else {
				loginModule = (LoginModule) bean;
			}
			loginModule.setName(loginModuleName);
			loginModule.setOptions(options);
		} catch (final BeansException e) {
			throw new ConfigurationException(e);
		}
		return loginModule;
	}

	private HashMap getOptions(final XPlannerProperties properties,
			final int idx) {
		final HashMap options = new HashMap();
		final Iterator propertyNames = properties.getPropertyNames();
		while (propertyNames.hasNext()) {
			final String name = (String) propertyNames.next();
			final String optionName = MessageFormat.format(
					LoginModuleLoader.LOGIN_OPTION_PREFIX,
					new Integer[] { new Integer(idx) });
			if (name.startsWith(optionName)) {
				options.put(name.substring(optionName.length()),
						properties.getProperty(name));
			}
		}
		return options;
	}

	public static String[] getLoginModuleNames() {
		final XPlannerProperties properties = new XPlannerProperties();
		final List loginModuleNameList = new ArrayList();
		for (int i = 0; properties.getProperty(LoginModuleLoader.getKey(
				LoginModuleLoader.LOGIN_MODULE_NAME_KEY, i)) != null; i++) {
			loginModuleNameList.add(properties.getProperty(LoginModuleLoader
					.getKey(LoginModuleLoader.LOGIN_MODULE_NAME_KEY, i)));
		}
		return (String[]) loginModuleNameList.toArray(new String[] {});
	}

	private static String getKey(final String propertyKey, final int i) {
		return MessageFormat.format(propertyKey,
				new Integer[] { new Integer(i) });
	}

	@Required
	@Autowired
	public void setApplicationContext(
			final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}