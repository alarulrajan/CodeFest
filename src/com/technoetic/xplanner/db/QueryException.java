package com.technoetic.xplanner.db;

/**
 * The Class QueryException.
 */
public class QueryException extends Exception {
	
	/**
     * Instantiates a new query exception.
     */
	public QueryException() {
	}

	/**
     * Instantiates a new query exception.
     *
     * @param cause
     *            the cause
     */
	public QueryException(final Throwable cause) {
		super(cause);
	}

	/**
     * Instantiates a new query exception.
     *
     * @param message
     *            the message
     */
	public QueryException(final String message) {
		super(message);
	}

	/**
     * Instantiates a new query exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
	public QueryException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
