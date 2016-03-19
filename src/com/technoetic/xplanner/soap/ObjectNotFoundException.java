package com.technoetic.xplanner.soap;

/**
 * The Class ObjectNotFoundException.
 */
public class ObjectNotFoundException extends Exception {
	
	/**
     * Instantiates a new object not found exception.
     */
	public ObjectNotFoundException() {
	}

	/**
     * Instantiates a new object not found exception.
     *
     * @param message
     *            the message
     */
	public ObjectNotFoundException(final String message) {
		super(message);
	}

	/**
     * Instantiates a new object not found exception.
     *
     * @param cause
     *            the cause
     */
	public ObjectNotFoundException(final Throwable cause) {
		super(cause);
	}

	/**
     * Instantiates a new object not found exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
	public ObjectNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
     * Instantiates a new object not found exception.
     *
     * @param aClass
     *            the a class
     * @param id
     *            the id
     * @param ex
     *            the ex
     */
	public ObjectNotFoundException(final Class aClass, final int id,
			final ObjectNotFoundException ex) {
		this(aClass.getName() + " with id=" + id + " not found", ex);
	}
}
