package com.technoetic.xplanner.importer;

/**
 * Created by IntelliJ IDEA. User: tkmower Date: May 19, 2005 Time: 8:23:12 PM
 */
public class SpreadsheetImporterException extends RuntimeException {
	public SpreadsheetImporterException(final String message) {
		super(message);
	}

	public SpreadsheetImporterException(final String message,
			final Throwable cause) {
		super(message, cause);
	}
}
