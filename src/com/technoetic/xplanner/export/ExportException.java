package com.technoetic.xplanner.export;

/**
 * The Class ExportException.
 */
public class ExportException extends Exception {
    
    /**
     * Instantiates a new export exception.
     */
    public ExportException() {
    }

    /**
     * Instantiates a new export exception.
     *
     * @param message
     *            the message
     */
    public ExportException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new export exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public ExportException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new export exception.
     *
     * @param cause
     *            the cause
     */
    public ExportException(final Throwable cause) {
        super(cause);
    }
}
