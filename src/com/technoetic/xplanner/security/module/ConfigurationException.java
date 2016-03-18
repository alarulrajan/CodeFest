package com.technoetic.xplanner.security.module;

import com.technoetic.xplanner.security.LoginModule;

public class ConfigurationException extends RuntimeException {
	public ConfigurationException() {
	}

	public ConfigurationException(final String message) {
		super(message);
	}

	public ConfigurationException(final Throwable cause) {
		super(LoginModule.MESSAGE_CONFIGURATION_ERROR_KEY, cause);
	}

	public ConfigurationException(final String message, final Throwable cause) {
		super(message, cause);
	}
}