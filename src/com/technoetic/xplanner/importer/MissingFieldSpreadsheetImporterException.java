package com.technoetic.xplanner.importer;

/**
 * User: Mateusz Prokopowicz Date: Jun 7, 2005 Time: 2:35:48 PM.
 */
public class MissingFieldSpreadsheetImporterException extends
		SpreadsheetImporterException {
	
	/** The field. */
	private final String field;

	/**
     * Gets the field.
     *
     * @return the field
     */
	public String getField() {
		return this.field;
	}

	/**
     * Instantiates a new missing field spreadsheet importer exception.
     *
     * @param field
     *            the field
     * @param message
     *            the message
     */
	public MissingFieldSpreadsheetImporterException(final String field,
			final String message) {
		super(message);
		this.field = field;
	}
}
