package com.technoetic.xplanner.security;

import java.util.HashMap;
import java.util.Map;

/**
 * This exception should be used when a user's identity can't be verified.
 */
public class AuthenticationException extends Exception {
	
	/** The error by module. */
	private Map errorByModule = new HashMap();

	/**
     * Instantiates a new authentication exception.
     */
	public AuthenticationException() {
	}

	/**
     * Instantiates a new authentication exception.
     *
     * @param message
     *            the message
     */
	public AuthenticationException(final String message) {
		super(message);
		this.errorByModule.put("Default", message);
	}

	/**
     * Instantiates a new authentication exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
	public AuthenticationException(final String message, final Throwable cause) {
		super(message, cause);
		this.errorByModule.put("", message);
	}

	/**
     * Instantiates a new authentication exception.
     *
     * @param cause
     *            the cause
     */
	public AuthenticationException(final Throwable cause) {
		super(cause);
	}

	/**
     * Instantiates a new authentication exception.
     *
     * @param errorByModule
     *            the error by module
     */
	public AuthenticationException(final Map errorByModule) {
		this.errorByModule = errorByModule;
	}

	/**
     * Gets the errors by module.
     *
     * @return the errors by module
     */
	public Map getErrorsByModule() {
		return this.errorByModule;
	}

}
