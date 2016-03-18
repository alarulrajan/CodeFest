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

public class SpreadsheetStoryWriter implements CookbookFields {
	OutputStream output;
	public static final String END_DATE_HEADER = "Iteration End Date";
	public static final String TITLE_HEADER = "Feature/Story Title";
	public static final String STATUS_HEADER = "Status";
	public static final String PRIORITY_HEADER = "Priority  (1 thru n)";
	public static final String ESTIMATE_HEADER = "Work Unit Estimate";

	public SpreadsheetStoryWriter(final OutputStream stream) {
		this.output = stream;
	}

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

	private void setCellValue(final HSSFRow row, final int col, final Date date) {
		final HSSFCell cell = row.createCell((short) col);
		cell.setCellValue(date);
	}

	private void setCellValue(final HSSFRow row, final int col, final int value) {
		final HSSFCell cell = row.createCell((short) col);
		cell.setCellValue(value);
	}

	private void setCellValue(final HSSFRow row, final int col,
			final double value) {
		final HSSFCell cell = row.createCell((short) col);
		cell.setCellValue(value);
	}

	private void setCellValue(final HSSFRow row, final int col,
			final String value) {
		final HSSFCell cell = row.createCell((short) col);
		cell.setCellValue(value);
	}
}
