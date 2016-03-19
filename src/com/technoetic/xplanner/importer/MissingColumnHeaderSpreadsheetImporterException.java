package com.technoetic.xplanner.importer;

/**
 * The Class MissingColumnHeaderSpreadsheetImporterException.
 */
public class MissingColumnHeaderSpreadsheetImporterException extends
        SpreadsheetImporterException {
    
    /** The column name. */
    String columnName;

    /**
     * Gets the column name.
     *
     * @return the column name
     */
    public String getColumnName() {
        return this.columnName;
    }

    /**
     * Instantiates a new missing column header spreadsheet importer exception.
     *
     * @param columnName
     *            the column name
     */
    public MissingColumnHeaderSpreadsheetImporterException(
            final String columnName) {
        super("Missing column '" + columnName + "'");
        this.columnName = columnName;
    }

}
