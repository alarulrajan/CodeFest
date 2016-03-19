package com.technoetic.xplanner.importer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.importer.spreadsheet.SpreadsheetHeaderConfiguration;
import com.technoetic.xplanner.importer.spreadsheet.SpreadsheetStoryFilter;
import com.technoetic.xplanner.importer.spreadsheet.SpreadsheetStoryReader;

/**
 * The Class SpreadsheetStoryImporter.
 */
public class SpreadsheetStoryImporter {
	
	/** The spreadsheet story factory. */
	private final SpreadsheetStoryFactory spreadsheetStoryFactory;

	/**
     * Instantiates a new spreadsheet story importer.
     *
     * @param spreadsheetStoryFactory
     *            the spreadsheet story factory
     */
	public SpreadsheetStoryImporter(
			final SpreadsheetStoryFactory spreadsheetStoryFactory) {
		this.spreadsheetStoryFactory = spreadsheetStoryFactory;
	}

	/**
     * Import stories.
     *
     * @param iteration
     *            the iteration
     * @param headerConfiguration
     *            the header configuration
     * @param inputStream
     *            the input stream
     * @param onlyUncompleted
     *            the only uncompleted
     * @return the list
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
	public List importStories(final Iteration iteration,
			final SpreadsheetHeaderConfiguration headerConfiguration,
			final InputStream inputStream, final boolean onlyUncompleted)
			throws IOException {
		final List newStories = new ArrayList();
		final List stories = this.readStoriesFromSpreadsheet(
				headerConfiguration, inputStream);
		final SpreadsheetStoryFilter storyFilter = new SpreadsheetStoryFilter(
				iteration.getStartDate(), iteration.getEndDate());
		for (final Iterator iterator = stories.iterator(); iterator.hasNext();) {
			final SpreadsheetStory spreadsheetStory = (SpreadsheetStory) iterator
					.next();
			if (!storyFilter.matches(spreadsheetStory) || onlyUncompleted
					&& spreadsheetStory.getStatus().equalsIgnoreCase("C")) {
				continue;
			}
			final UserStory userStory = new UserStory();
			if (spreadsheetStory.getTitle() == null
					|| "".equals(spreadsheetStory.getTitle().trim())) {
				throw new MissingFieldSpreadsheetImporterException("name",
						"missing field");
			}
			userStory.setName(spreadsheetStory.getTitle());
			userStory.setEstimatedHoursField(spreadsheetStory.getEstimate());
			userStory.setIteration(iteration);
			userStory.setPriority(spreadsheetStory.getPriority());
			iteration.getUserStories().add(userStory);
			userStory.setIteration(iteration);
			newStories.add(userStory);
		}
		return newStories;
	}

	/**
     * Read stories from spreadsheet.
     *
     * @param headerConfiguration
     *            the header configuration
     * @param inputStream
     *            the input stream
     * @return the list
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
	protected List readStoriesFromSpreadsheet(
			final SpreadsheetHeaderConfiguration headerConfiguration,
			final InputStream inputStream) throws IOException {
		return new SpreadsheetStoryReader(this.spreadsheetStoryFactory)
				.readStories(headerConfiguration, inputStream);
	}
}
