package com.technoetic.xplanner.importer.spreadsheet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.technoetic.xplanner.importer.SpreadsheetStory;
import com.technoetic.xplanner.importer.SpreadsheetStoryFactory;
import com.technoetic.xplanner.importer.WrongImportFileSpreadsheetImporterException;

/**
 * Created by IntelliJ IDEA. User: Jacques Date: Dec 28, 2003 Time: 5:23:42 PM
 * To change this template use Options | File Templates.
 */
public class SpreadsheetStoryReader implements CookbookFields {
	private final ArrayList stories = new ArrayList();
	private final Logger log = Logger.getLogger(SpreadsheetStoryReader.class);
	private final SpreadsheetStoryFactory spreadsheetStoryFactory;

	public SpreadsheetStoryReader(
			final SpreadsheetStoryFactory spreadsheetStoryFactory) {
		this.spreadsheetStoryFactory = spreadsheetStoryFactory;
	}

	public List readStories(
			final SpreadsheetHeaderConfiguration headerConfiguration,
			final InputStream input) throws IOException {
		final HSSFSheet sheet = this.getWorksheet(input,
				headerConfiguration.getWorksheetName());
		headerConfiguration.setWorksheet(sheet);
		return this.readStories(headerConfiguration, sheet);
	}

	public HSSFSheet getWorksheet(final InputStream input,
			final String worksheetName) throws IOException {
		POIFSFileSystem fs;
		try {
			fs = new POIFSFileSystem(input);
		} catch (final IOException e) {
			throw new WrongImportFileSpreadsheetImporterException(
					"Bad spreadsheet file", e);
		}
		final HSSFWorkbook wb = new HSSFWorkbook(fs);
		final HSSFSheet sheet = wb.getSheet(worksheetName);
		if (sheet == null) {
			throw new MissingWorksheetException(worksheetName);
		}

		return sheet;
	}

	private List readStories(
			final SpreadsheetHeaderConfiguration headerConfiguration,
			final HSSFSheet sheet) {
		final Iterator it = new StoryRowIterator(headerConfiguration, sheet);
		while (it.hasNext()) {
			final SpreadsheetStory spreadsheetStory = (SpreadsheetStory) it
					.next();
			this.stories.add(spreadsheetStory);
		}
		return this.stories;
	}

	private class StoryRowIterator implements Iterator {
		Iterator it;
		SpreadsheetStory nextSpreadsheetStory;
		SpreadsheetHeaderConfiguration headerConfiguration;

		public StoryRowIterator(
				final SpreadsheetHeaderConfiguration headerConfiguration,
				final HSSFSheet sheet) {
			this.headerConfiguration = headerConfiguration;
			this.it = sheet.rowIterator();
			this.it.next();
		}

		/**
		 * TODO: *WARNING* Do not call multiple time. It implements a look ahead
		 * that does not take this case into account
		 */
		@Override
		public boolean hasNext() {
			final boolean hasNext = this.it.hasNext();
			if (!hasNext) {
				return false;
			}
			this.nextSpreadsheetStory = this.readRow((HSSFRow) this.it.next());
			return this.nextSpreadsheetStory != null;
		}

		private SpreadsheetStory readRow(final HSSFRow row) {
			try {
				final String title = this.getCellStringValue(row,
						this.headerConfiguration.getStoryTitleColumnIndex());
				final int priority = this.getCellIntValue(row,
						this.headerConfiguration.getStoryPriorityColumnIndex());
				if (StringUtils.isEmpty(title) && priority == 0) {
					return null;
				}
				final String status = this.getCellStringValue(row,
						this.headerConfiguration.getStoryStatusColumnIndex());
				final double estimate = this.getCellDoubleValue(row,
						this.headerConfiguration.getStoryEstimateColumnIndex());
				final Date storyEndDate = this.getCellDateValue(row,
						this.headerConfiguration.getStoryEndDateColumnIndex());
				return SpreadsheetStoryReader.this.spreadsheetStoryFactory
						.newInstance(storyEndDate, title, status, estimate,
								priority);
			} catch (final RuntimeException e) {
				SpreadsheetStoryReader.this.log.error(
						"Error while reading row " + row.getRowNum(), e);
				throw e;
			}
		}

		private String getCellStringValue(final HSSFRow row, final int column) {
			final HSSFCell cell = row.getCell((short) column);
			if (cell == null) {
				return "";
			}
			return cell.getStringCellValue();
		}

		private int getCellIntValue(final HSSFRow row, final int column) {
			final HSSFCell cell = row.getCell((short) column);
			if (cell == null) {
				return 0;
			}
			return (int) cell.getNumericCellValue();
		}

		private double getCellDoubleValue(final HSSFRow row, final int column) {
			final HSSFCell cell = row.getCell((short) column);
			if (cell == null) {
				return 0;
			}
			return cell.getNumericCellValue();
		}

		private Date getCellDateValue(final HSSFRow row, final int column) {
			final HSSFCell cell = row.getCell((short) column);
			if (cell == null) {
				return null;
			}
			return cell.getDateCellValue();
		}

		@Override
		public Object next() {
			return this.nextSpreadsheetStory;
		}

		@Override
		public void remove() {
		}
	}
}
