package com.technoetic.xplanner.importer.spreadsheet;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.technoetic.xplanner.importer.MissingColumnHeaderSpreadsheetImporterException;

/**
 * Created by IntelliJ IDEA. User: tkmower Date: Apr 1, 2005 Time: 12:17:53 AM
 */
public class SpreadsheetHeaderConfiguration {
	private SpreadsheetHeaderReader headerReader;

	public static final String DEFAULT_WORKSHEET_NAME = "Features";

	private String titleHeader;
	private String endDateHeader;
	private String priorityHeader;
	private String statusHeader;
	private String estimateHeader;
	private String worksheetName = SpreadsheetHeaderConfiguration.DEFAULT_WORKSHEET_NAME;

	public SpreadsheetHeaderConfiguration() {
		super();
	}

	public SpreadsheetHeaderConfiguration(final String titleHeader,
			final String endDateHeader, final String priorityHeader,
			final String statusHeader, final String estimateHeader) {
		this.titleHeader = titleHeader;
		this.endDateHeader = endDateHeader;
		this.priorityHeader = priorityHeader;
		this.statusHeader = statusHeader;
		this.estimateHeader = estimateHeader;
	}

	public void setTitleHeader(final String titleHeader) {
		this.titleHeader = titleHeader;
	}

	public void setEndDateHeader(final String endDateHeader) {
		this.endDateHeader = endDateHeader;
	}

	public void setPriorityHeader(final String priorityHeader) {
		this.priorityHeader = priorityHeader;
	}

	public void setStatusHeader(final String statusHeader) {
		this.statusHeader = statusHeader;
	}

	public void setEstimateHeader(final String estimateHeader) {
		this.estimateHeader = estimateHeader;
	}

	public int getStoryTitleColumnIndex() {
		return this.getRequiredColumnIndex(this.titleHeader);
	}

	public int getStoryEstimateColumnIndex() {
		return this.headerReader.getColumnIndex(this.estimateHeader);
	}

	public int getStoryEndDateColumnIndex() {
		return this.getRequiredColumnIndex(this.endDateHeader);
	}

	public int getStoryPriorityColumnIndex() {
		return this.getRequiredColumnIndex(this.priorityHeader);
	}

	public int getStoryStatusColumnIndex() {
		return this.headerReader.getColumnIndex(this.statusHeader);
	}

	public void setWorksheet(final HSSFSheet worksheet) {
		this.headerReader = new SpreadsheetHeaderReader();
		this.headerReader.setWorksheet(worksheet);
	}

	private int getRequiredColumnIndex(final String header) {
		final int columnIndex = this.headerReader.getColumnIndex(header);
		if (columnIndex == -1) {
			throw new MissingColumnHeaderSpreadsheetImporterException(header);
		}
		return columnIndex;
	}

	public void setWorksheetName(final String worksheetName) {
		this.worksheetName = worksheetName;
	}

	public String getWorksheetName() {
		return this.worksheetName;
	}
}
