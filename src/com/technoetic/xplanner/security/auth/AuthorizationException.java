package com.technoetic.xplanner.security.auth;

/**
 * This exception is for situation where a user has been authenticated but they
 * are not authorized to perform a specified action.
 */
public class AuthorizationException extends RuntimeException {
	
	/**
     * Instantiates a new authorization exception.
     */
	public AuthorizationException() {
	}

	/**
     * Instantiates a new authorization exception.
     *
     * @param message
     *            the message
     */
	public AuthorizationException(final String message) {
		super(message);
	}

	/**
     * Instantiates a new authorization exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
	public AuthorizationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
     * Instantiates a new authorization exception.
     *
     * @param cause
     *            the cause
     */
	public AuthorizationException(final Throwable cause) {
		super(cause);
	}
}
