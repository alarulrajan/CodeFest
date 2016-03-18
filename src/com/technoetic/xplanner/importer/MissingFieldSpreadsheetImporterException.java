package com.technoetic.xplanner.importer;

/**
 * User: Mateusz Prokopowicz Date: Jun 7, 2005 Time: 2:35:48 PM
 */
public class MissingFieldSpreadsheetImporterException extends
		SpreadsheetImporterException {
	private final String field;

	public String getField() {
		return this.field;
	}

	public MissingFieldSpreadsheetImporterException(final String field,
			final String message) {
		super(message);
		this.field = field;
	}
}
