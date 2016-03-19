package com.technoetic.xplanner.importer.spreadsheet;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.technoetic.xplanner.importer.MissingColumnHeaderSpreadsheetImporterException;

/**
 * Created by IntelliJ IDEA. User: tkmower Date: Apr 1, 2005 Time: 12:17:53 AM
 */
public class SpreadsheetHeaderConfiguration {
	
	/** The header reader. */
	private SpreadsheetHeaderReader headerReader;

	/** The Constant DEFAULT_WORKSHEET_NAME. */
	public static final String DEFAULT_WORKSHEET_NAME = "Features";

	/** The title header. */
	private String titleHeader;
	
	/** The end date header. */
	private String endDateHeader;
	
	/** The priority header. */
	private String priorityHeader;
	
	/** The status header. */
	private String statusHeader;
	
	/** The estimate header. */
	private String estimateHeader;
	
	/** The worksheet name. */
	private String worksheetName = SpreadsheetHeaderConfiguration.DEFAULT_WORKSHEET_NAME;

	/**
     * Instantiates a new spreadsheet header configuration.
     */
	public SpreadsheetHeaderConfiguration() {
		super();
	}

	/**
     * Instantiates a new spreadsheet header configuration.
     *
     * @param titleHeader
     *            the title header
     * @param endDateHeader
     *            the end date header
     * @param priorityHeader
     *            the priority header
     * @param statusHeader
     *            the status header
     * @param estimateHeader
     *            the estimate header
     */
	public SpreadsheetHeaderConfiguration(final String titleHeader,
			final String endDateHeader, final String priorityHeader,
			final String statusHeader, final String estimateHeader) {
		this.titleHeader = titleHeader;
		this.endDateHeader = endDateHeader;
		this.priorityHeader = priorityHeader;
		this.statusHeader = statusHeader;
		this.estimateHeader = estimateHeader;
	}

	/**
     * Sets the title header.
     *
     * @param titleHeader
     *            the new title header
     */
	public void setTitleHeader(final String titleHeader) {
		this.titleHeader = titleHeader;
	}

	/**
     * Sets the end date header.
     *
     * @param endDateHeader
     *            the new end date header
     */
	public void setEndDateHeader(final String endDateHeader) {
		this.endDateHeader = endDateHeader;
	}

	/**
     * Sets the priority header.
     *
     * @param priorityHeader
     *            the new priority header
     */
	public void setPriorityHeader(final String priorityHeader) {
		this.priorityHeader = priorityHeader;
	}

	/**
     * Sets the status header.
     *
     * @param statusHeader
     *            the new status header
     */
	public void setStatusHeader(final String statusHeader) {
		this.statusHeader = statusHeader;
	}

	/**
     * Sets the estimate header.
     *
     * @param estimateHeader
     *            the new estimate header
     */
	public void setEstimateHeader(final String estimateHeader) {
		this.estimateHeader = estimateHeader;
	}

	/**
     * Gets the story title column index.
     *
     * @return the story title column index
     */
	public int getStoryTitleColumnIndex() {
		return this.getRequiredColumnIndex(this.titleHeader);
	}

	/**
     * Gets the story estimate column index.
     *
     * @return the story estimate column index
     */
	public int getStoryEstimateColumnIndex() {
		return this.headerReader.getColumnIndex(this.estimateHeader);
	}

	/**
     * Gets the story end date column index.
     *
     * @return the story end date column index
     */
	public int getStoryEndDateColumnIndex() {
		return this.getRequiredColumnIndex(this.endDateHeader);
	}

	/**
     * Gets the story priority column index.
     *
     * @return the story priority column index
     */
	public int getStoryPriorityColumnIndex() {
		return this.getRequiredColumnIndex(this.priorityHeader);
	}

	/**
     * Gets the story status column index.
     *
     * @return the story status column index
     */
	public int getStoryStatusColumnIndex() {
		return this.headerReader.getColumnIndex(this.statusHeader);
	}

	/**
     * Sets the worksheet.
     *
     * @param worksheet
     *            the new worksheet
     */
	public void setWorksheet(final HSSFSheet worksheet) {
		this.headerReader = new SpreadsheetHeaderReader();
		this.headerReader.setWorksheet(worksheet);
	}

	/**
     * Gets the required column index.
     *
     * @param header
     *            the header
     * @return the required column index
     */
	private int getRequiredColumnIndex(final String header) {
		final int columnIndex = this.headerReader.getColumnIndex(header);
		if (columnIndex == -1) {
			throw new MissingColumnHeaderSpreadsheetImporterException(header);
		}
		return columnIndex;
	}

	/**
     * Sets the worksheet name.
     *
     * @param worksheetName
     *            the new worksheet name
     */
	public void setWorksheetName(final String worksheetName) {
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
