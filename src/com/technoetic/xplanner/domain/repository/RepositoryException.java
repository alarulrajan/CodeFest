package com.technoetic.xplanner.domain.repository;

/**
 * The Class RepositoryException.
 */
public class RepositoryException extends Exception {
	
	/**
     * Instantiates a new repository exception.
     */
	public RepositoryException() {
	}

	/**
     * Instantiates a new repository exception.
     *
     * @param cause
     *            the cause
     */
	public RepositoryException(final Throwable cause) {
		super(cause);
	}

	/**
     * Instantiates a new repository exception.
     *
     * @param message
     *            the message
     */
	public RepositoryException(final String message) {
		super(message);
	}

	/**
     * Instantiates a new repository exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
	public RepositoryException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
