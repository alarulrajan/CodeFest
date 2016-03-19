package com.technoetic.xplanner.importer.spreadsheet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.technoetic.xplanner.importer.SpreadsheetStory;
import com.technoetic.xplanner.importer.SpreadsheetStoryFactory;
import com.technoetic.xplanner.importer.util.IOStreamFactory;

/**
 * The Class Spreadsheet.
 */
public class Spreadsheet {
	
	/** The path. */
	protected String path;
	
	/** The stories. */
	protected List/* <Story> */stories = new ArrayList();
	
	/** The stream factory. */
	private final IOStreamFactory streamFactory;
	
	/** The spreadsheet story factory. */
	private SpreadsheetStoryFactory spreadsheetStoryFactory;

	/**
     * Instantiates a new spreadsheet.
     *
     * @param streamFactory
     *            the stream factory
     * @param spreadsheetStoryFactory
     *            the spreadsheet story factory
     */
	public Spreadsheet(final IOStreamFactory streamFactory,
			final SpreadsheetStoryFactory spreadsheetStoryFactory) {
		this.streamFactory = streamFactory;
		this.spreadsheetStoryFactory = spreadsheetStoryFactory;
	}

	/**
     * Gets the stories.
     *
     * @return the stories
     */
	public List getStories() {
		return Collections.unmodifiableList(this.stories);
	}

	/**
     * Gets the path.
     *
     * @return the path
     */
	public String getPath() {
		return this.path;
	}

	/**
     * Open.
     *
     * @param path
     *            the path
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
	public void open(final String path) throws IOException {
		this.path = path;

		try {
			final InputStream stream = this.streamFactory.newInputStream(path);
			// ChangeSoon
			this.stories = new SpreadsheetStoryReader(
					this.spreadsheetStoryFactory).readStories(null, stream);
		} catch (final IOException e) {
		}
	}

	/**
     * Save.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
	public void save() throws IOException {
		new SpreadsheetStoryWriter(new FileOutputStream(this.path))
				.writeStories(this.stories);
	}

	/**
     * Save as.
     *
     * @param path
     *            the path
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
	public void saveAs(final String path) throws IOException {
		this.path = path;
		this.save();
	}

	/**
     * Sets the stories.
     *
     * @param stories
     *            the new stories
     */
	public void setStories(final List stories) {
		this.stories = new ArrayList(stories);
	}

	/**
     * Gets the story factory.
     *
     * @return the story factory
     */
	public SpreadsheetStoryFactory getStoryFactory() {
		return this.spreadsheetStoryFactory;
	}

	/**
     * Sets the story factory.
     *
     * @param spreadsheetStoryFactory
     *            the new story factory
     */
	public void setStoryFactory(
			final SpreadsheetStoryFactory spreadsheetStoryFactory) {
		this.spreadsheetStoryFactory = spreadsheetStoryFactory;
	}

	/**
     * Adds the story.
     *
     * @param spreadsheetStory
     *            the spreadsheet story
     */
	public void addStory(final SpreadsheetStory spreadsheetStory) {
		this.stories.add(spreadsheetStory);
	}

}
