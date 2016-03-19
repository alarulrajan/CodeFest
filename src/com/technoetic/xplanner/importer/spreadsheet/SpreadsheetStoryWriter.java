package com.technoetic.xplanner.importer.spreadsheet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.technoetic.xplanner.importer.SpreadsheetStory;

/*
 * $Header$
 * $Revision: 540 $
 * $Date: 2005-06-07 07:03:50 -0500 (Tue, 07 Jun 2005) $
 *
 * Copyright (c) 1999-2002 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */

/**
 * The Class SpreadsheetStoryWriter.
 */
public class SpreadsheetStoryWriter implements CookbookFields {
	
	/** The output. */
	OutputStream output;
	
	/** The Constant END_DATE_HEADER. */
	public static final String END_DATE_HEADER = "Iteration End Date";
	
	/** The Constant TITLE_HEADER. */
	public static final String TITLE_HEADER = "Feature/Story Title";
	
	/** The Constant STATUS_HEADER. */
	public static final String STATUS_HEADER = "Status";
	
	/** The Constant PRIORITY_HEADER. */
	public static final String PRIORITY_HEADER = "Priority  (1 thru n)";
	
	/** The Constant ESTIMATE_HEADER. */
	public static final String ESTIMATE_HEADER = "Work Unit Estimate";

	/**
     * Instantiates a new spreadsheet story writer.
     *
     * @param stream
     *            the stream
     */
	public SpreadsheetStoryWriter(final OutputStream stream) {
		this.output = stream;
	}

	/**
     * Write stories.
     *
     * @param stories
     *            the stories
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
	public void writeStories(final List stories) throws IOException {
		// assert stories != null;

		final HSSFWorkbook wb = new HSSFWorkbook();
		final HSSFSheet sheet = wb.createSheet("Features");
		this.writeHeader(sheet);
		for (int i = 0; i < stories.size(); i++) {
			final SpreadsheetStory spreadsheetStory = (SpreadsheetStory) stories
					.get(i);
			this.writeStory(sheet, spreadsheetStory, i + 1);
		}
		wb.write(this.output);
		this.output.close();
	}

	/**
     * Write story.
     *
     * @param sheet
     *            the sheet
     * @param spreadsheetStory
     *            the spreadsheet story
     * @param i
     *            the i
     */
	private void writeStory(final HSSFSheet sheet,
			final SpreadsheetStory spreadsheetStory, final int i) {
		final HSSFRow row = sheet.createRow(i);
		this.setCellValue(row, CookbookFields.STORY_END_DATE_COL,
				spreadsheetStory.getEndDate());
		this.setCellValue(row, CookbookFields.TITLE_COL,
				spreadsheetStory.getTitle());
		this.setCellValue(row, CookbookFields.STATUS_COL,
				spreadsheetStory.getStatus());
		this.setCellValue(row, CookbookFields.STORY_PRIORITY_COL,
				spreadsheetStory.getPriority());
		this.setCellValue(row, CookbookFields.ESTIMATE_NUMBER_COL,
				spreadsheetStory.getEstimate());
	}

	/**
     * Write header.
     *
     * @param sheet
     *            the sheet
     */
	private void writeHeader(final HSSFSheet sheet) {
		final HSSFRow row = sheet.createRow(0);
		this.setCellValue(row, CookbookFields.STORY_END_DATE_COL,
				SpreadsheetStoryWriter.END_DATE_HEADER);
		this.setCellValue(row, CookbookFields.TITLE_COL,
				SpreadsheetStoryWriter.TITLE_HEADER);
		this.setCellValue(row, CookbookFields.STATUS_COL,
				SpreadsheetStoryWriter.STATUS_HEADER);
		this.setCellValue(row, CookbookFields.STORY_PRIORITY_COL,
				SpreadsheetStoryWriter.PRIORITY_HEADER);
		this.setCellValue(row, CookbookFields.ESTIMATE_NUMBER_COL,
				SpreadsheetStoryWriter.ESTIMATE_HEADER);
	}

	/**
     * Sets the cell value.
     *
     * @param row
     *            the row
     * @param col
     *            the col
     * @param date
     *            the date
     */
	private void setCellValue(final HSSFRow row, final int col, final Date date) {
		final HSSFCell cell = row.createCell((short) col);
		cell.setCellValue(date);
	}

	/**
     * Sets the cell value.
     *
     * @param row
     *            the row
     * @param col
     *            the col
     * @param value
     *            the value
     */
	private void setCellValue(final HSSFRow row, final int col, final int value) {
		final HSSFCell cell = row.createCell((short) col);
		cell.setCellValue(value);
	}

	/**
     * Sets the cell value.
     *
     * @param row
     *            the row
     * @param col
     *            the col
     * @param value
     *            the value
     */
	private void setCellValue(final HSSFRow row, final int col,
			final double value) {
		final HSSFCell cell = row.createCell((short) col);
		cell.setCellValue(value);
	}

	/**
     * Sets the cell value.
     *
     * @param row
     *            the row
     * @param col
     *            the col
     * @param value
     *            the value
     */
	private void setCellValue(final HSSFRow row, final int col,
			final String value) {
		final HSSFCell cell = row.createCell((short) col);
		cell.setCellValue(value);
	}
}
