package com.technoetic.xplanner.importer.spreadsheet;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * Created by IntelliJ IDEA. User: tkmower Date: Mar 31, 2005 Time: 11:54:54 PM
 */
public class SpreadsheetHeaderReader {
    
    /** The sheet. */
    private HSSFSheet sheet;

    /**
     * Sets the worksheet.
     *
     * @param worksheet
     *            the new worksheet
     */
    public void setWorksheet(final HSSFSheet worksheet) {
        this.sheet = worksheet;
    }

    /**
     * Gets the column index.
     *
     * @param headerText
     *            the header text
     * @return the column index
     */
    public int getColumnIndex(final String headerText) {
        final HSSFRow row = this.sheet.getRow(0);

        for (short i = row.getFirstCellNum(); i < row.getLastCellNum(); ++i) {
            final String stringCellValue = this.getTextForCell(row, i);
            if (stringCellValue != null && !StringUtils.isEmpty(headerText)
                    && headerText.equalsIgnoreCase(stringCellValue.trim())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the text for cell.
     *
     * @param row
     *            the row
     * @param i
     *            the i
     * @return the text for cell
     */
    private String getTextForCell(final HSSFRow row, final short i) {
        final HSSFCell cell = row.getCell(i);
        if (cell == null) {
            return null;
        }
        return cell.getStringCellValue();
    }
}
