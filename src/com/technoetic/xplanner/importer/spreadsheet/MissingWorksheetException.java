package com.technoetic.xplanner.importer.spreadsheet;

import com.technoetic.xplanner.importer.SpreadsheetImporterException;

/**
 * Created by IntelliJ IDEA. User: tkmower Date: Nov 15, 2005 Time: 11:34:44 AM
 */
public class MissingWorksheetException extends SpreadsheetImporterException {
    
    /** The worksheet name. */
    private final String worksheetName;

    /**
     * Instantiates a new missing worksheet exception.
     *
     * @param worksheetName
     *            the worksheet name
     */
    public MissingWorksheetException(final String worksheetName) {
        super("Could not find worksheet named " + worksheetName);
        this.worksheetName = worksheetName;
    }

    /**
     * Gets the worksheet name.
     *
     * @return the worksheet name
     */
    public String getWorksheetName() {
        return this.worksheetName;
    }
}
