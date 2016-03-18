package com.technoetic.xplanner.security;

import java.util.HashMap;
import java.util.Map;

/**
 * This exception should be used when a user's identity can't be verified.
 */
public class AuthenticationException extends Exception {
	private Map errorByModule = new HashMap();

	public AuthenticationException() {
	}

	public AuthenticationException(final String message) {
		super(message);
		this.errorByModule.put("Default", message);
	}

	public AuthenticationException(final String message, final Throwable cause) {
		super(message, cause);
		this.errorByModule.put("", message);
	}

	public AuthenticationException(final Throwable cause) {
		super(cause);
	}

	public AuthenticationException(final Map errorByModule) {
		this.errorByModule = errorByModule;
	}

	public Map getErrorsByModule() {
		return this.errorByModule;
	}

}
