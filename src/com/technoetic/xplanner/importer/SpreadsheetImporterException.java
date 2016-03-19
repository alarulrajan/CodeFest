package com.technoetic.xplanner.importer;

/**
 * Created by IntelliJ IDEA. User: tkmower Date: May 19, 2005 Time: 8:23:12 PM
 */
public class SpreadsheetImporterException extends RuntimeException {
    
    /**
     * Instantiates a new spreadsheet importer exception.
     *
     * @param message
     *            the message
     */
    public SpreadsheetImporterException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new spreadsheet importer exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public SpreadsheetImporterException(final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
