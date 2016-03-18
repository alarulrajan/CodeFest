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

public class Spreadsheet {
	protected String path;
	protected List/* <Story> */stories = new ArrayList();
	private final IOStreamFactory streamFactory;
	private SpreadsheetStoryFactory spreadsheetStoryFactory;

	public Spreadsheet(final IOStreamFactory streamFactory,
			final SpreadsheetStoryFactory spreadsheetStoryFactory) {
		this.streamFactory = streamFactory;
		this.spreadsheetStoryFactory = spreadsheetStoryFactory;
	}

	public List getStories() {
		return Collections.unmodifiableList(this.stories);
	}

	public String getPath() {
		return this.path;
	}

	public void open(final String path) throws IOException {
		this.path = path;

		try {
			final InputStream stream = this.streamFactory.newInputStream(path);
			// TODO
			this.stories = new SpreadsheetStoryReader(
					this.spreadsheetStoryFactory).readStories(null, stream);
		} catch (final IOException e) {
		}
	}

	public void save() throws IOException {
		new SpreadsheetStoryWriter(new FileOutputStream(this.path))
				.writeStories(this.stories);
	}

	public void saveAs(final String path) throws IOException {
		this.path = path;
		this.save();
	}

	public void setStories(final List stories) {
		this.stories = new ArrayList(stories);
	}

	public SpreadsheetStoryFactory getStoryFactory() {
		return this.spreadsheetStoryFactory;
	}

	public void setStoryFactory(
			final SpreadsheetStoryFactory spreadsheetStoryFactory) {
		this.spreadsheetStoryFactory = spreadsheetStoryFactory;
	}

	public void addStory(final SpreadsheetStory spreadsheetStory) {
		this.stories.add(spreadsheetStory);
	}

}
