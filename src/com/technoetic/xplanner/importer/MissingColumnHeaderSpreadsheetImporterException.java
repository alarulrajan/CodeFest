package com.technoetic.xplanner.importer;

public class MissingColumnHeaderSpreadsheetImporterException extends
		SpreadsheetImporterException {
	String columnName;

	public String getColumnName() {
		return this.columnName;
	}

	public MissingColumnHeaderSpreadsheetImporterException(
			final String columnName) {
		super("Missing column '" + columnName + "'");
		this.columnName = columnName;
	}

}
