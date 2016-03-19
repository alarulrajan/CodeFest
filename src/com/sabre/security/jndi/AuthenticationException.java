package com.sabre.security.jndi;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: sg897500 Date: Oct 14, 2005 Time: 3:14:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticationException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 201603310102030405L;

	/** The error by module. */
	private final Map<String, String> errorByModule = new HashMap<String, String>();

	/**
	 * Instantiates a new authentication exception.
	 *
	 * @param message the message
	 */
	public AuthenticationException(final String message) {
		super(message);
		this.errorByModule.put("Default", message);
	}

	/**
	 * Instantiates a new authentication exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public AuthenticationException(final String message, final Throwable cause) {
		super(message, cause);
		this.errorByModule.put("", message);
	}

	/**
	 * Instantiates a new authentication exception.
	 *
	 * @param cause the cause
	 */
	public AuthenticationException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new authentication exception.
	 *
	 * @param errorByModule the error by module
	 */
	public AuthenticationException(final Map<String, String> errorByModule) {
		if (errorByModule == null) {
			return;
		}
		this.errorByModule.putAll(errorByModule);
	}

	/**
	 * Gets the errors by module.
	 *
	 * @return the errors by module
	 */
	public Map<String, String> getErrorsByModule() {
		return this.errorByModule;
	}

}
