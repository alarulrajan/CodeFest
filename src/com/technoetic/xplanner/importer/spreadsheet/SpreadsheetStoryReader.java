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
	
	/** The stories. */
	private final ArrayList stories = new ArrayList();
	
	/** The log. */
	private final Logger log = Logger.getLogger(SpreadsheetStoryReader.class);
	
	/** The spreadsheet story factory. */
	private final SpreadsheetStoryFactory spreadsheetStoryFactory;

	/**
     * Instantiates a new spreadsheet story reader.
     *
     * @param spreadsheetStoryFactory
     *            the spreadsheet story factory
     */
	public SpreadsheetStoryReader(
			final SpreadsheetStoryFactory spreadsheetStoryFactory) {
		this.spreadsheetStoryFactory = spreadsheetStoryFactory;
	}

	/**
     * Read stories.
     *
     * @param headerConfiguration
     *            the header configuration
     * @param input
     *            the input
     * @return the list
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
	public List readStories(
			final SpreadsheetHeaderConfiguration headerConfiguration,
			final InputStream input) throws IOException {
		final HSSFSheet sheet = this.getWorksheet(input,
				headerConfiguration.getWorksheetName());
		headerConfiguration.setWorksheet(sheet);
		return this.readStories(headerConfiguration, sheet);
	}

	/**
     * Gets the worksheet.
     *
     * @param input
     *            the input
     * @param worksheetName
     *            the worksheet name
     * @return the worksheet
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
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

	/**
     * Read stories.
     *
     * @param headerConfiguration
     *            the header configuration
     * @param sheet
     *            the sheet
     * @return the list
     */
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

	/**
     * The Class StoryRowIterator.
     */
	private class StoryRowIterator implements Iterator {
		
		/** The it. */
		Iterator it;
		
		/** The next spreadsheet story. */
		SpreadsheetStory nextSpreadsheetStory;
		
		/** The header configuration. */
		SpreadsheetHeaderConfiguration headerConfiguration;

		/**
         * Instantiates a new story row iterator.
         *
         * @param headerConfiguration
         *            the header configuration
         * @param sheet
         *            the sheet
         */
		public StoryRowIterator(
				final SpreadsheetHeaderConfiguration headerConfiguration,
				final HSSFSheet sheet) {
			this.headerConfiguration = headerConfiguration;
			this.it = sheet.rowIterator();
			this.it.next();
		}

		/**
         * ChangeSoon: *WARNING* Do not call multiple time. It implements a look
         * ahead that does not take this case into account
         *
         * @return true, if successful
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

		/**
         * Read row.
         *
         * @param row
         *            the row
         * @return the spreadsheet story
         */
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

		/**
         * Gets the cell string value.
         *
         * @param row
         *            the row
         * @param column
         *            the column
         * @return the cell string value
         */
		private String getCellStringValue(final HSSFRow row, final int column) {
			final HSSFCell cell = row.getCell((short) column);
			if (cell == null) {
				return "";
			}
			return cell.getStringCellValue();
		}

		/**
         * Gets the cell int value.
         *
         * @param row
         *            the row
         * @param column
         *            the column
         * @return the cell int value
         */
		private int getCellIntValue(final HSSFRow row, final int column) {
			final HSSFCell cell = row.getCell((short) column);
			if (cell == null) {
				return 0;
			}
			return (int) cell.getNumericCellValue();
		}

		/**
         * Gets the cell double value.
         *
         * @param row
         *            the row
         * @param column
         *            the column
         * @return the cell double value
         */
		private double getCellDoubleValue(final HSSFRow row, final int column) {
			final HSSFCell cell = row.getCell((short) column);
			if (cell == null) {
				return 0;
			}
			return cell.getNumericCellValue();
		}

		/**
         * Gets the cell date value.
         *
         * @param row
         *            the row
         * @param column
         *            the column
         * @return the cell date value
         */
		private Date getCellDateValue(final HSSFRow row, final int column) {
			final HSSFCell cell = row.getCell((short) column);
			if (cell == null) {
				return null;
			}
			return cell.getDateCellValue();
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Object next() {
			return this.nextSpreadsheetStory;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
		}
	}
}
