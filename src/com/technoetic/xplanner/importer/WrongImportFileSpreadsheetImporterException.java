package com.technoetic.xplanner.importer;

/**
 * User: Mateusz Prokopowicz Date: Jun 7, 2005 Time: 2:35:48 PM.
 */
public class WrongImportFileSpreadsheetImporterException extends
		SpreadsheetImporterException {
	
	/**
     * Instantiates a new wrong import file spreadsheet importer exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
	public WrongImportFileSpreadsheetImporterException(final String message,
			final Throwable cause) {
		super(message, cause);
	}
}
