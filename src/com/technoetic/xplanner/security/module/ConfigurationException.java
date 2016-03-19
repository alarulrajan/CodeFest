package com.technoetic.xplanner.security.module;

import com.technoetic.xplanner.security.LoginModule;

/**
 * The Class ConfigurationException.
 */
public class ConfigurationException extends RuntimeException {
	
	/**
     * Instantiates a new configuration exception.
     */
	public ConfigurationException() {
	}

	/**
     * Instantiates a new configuration exception.
     *
     * @param message
     *            the message
     */
	public ConfigurationException(final String message) {
		super(message);
	}

	/**
     * Instantiates a new configuration exception.
     *
     * @param cause
     *            the cause
     */
	public ConfigurationException(final Throwable cause) {
		super(LoginModule.MESSAGE_CONFIGURATION_ERROR_KEY, cause);
	}

	/**
     * Instantiates a new configuration exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
	public ConfigurationException(final String message, final Throwable cause) {
		super(message, cause);
	}
}