package com.technoetic.xplanner.importer.spreadsheet;

import java.util.Date;

import com.technoetic.xplanner.importer.SpreadsheetStory;

/**
 * Created by IntelliJ IDEA. User: tkmower Date: May 19, 2005 Time: 10:22:45 PM
 */
public class SpreadsheetStoryFilter {
	
	/** The start date. */
	private final Date startDate;
	
	/** The end date. */
	private final Date endDate;

	/**
     * Instantiates a new spreadsheet story filter.
     *
     * @param startDate
     *            the start date
     * @param endDate
     *            the end date
     */
	public SpreadsheetStoryFilter(final Date startDate, final Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
		if (startDate.after(endDate)) {
			throw new IllegalArgumentException(
					"Filter start date after end date " + this);
		}
	}

	/**
     * Matches.
     *
     * @param story
     *            the story
     * @return true, if successful
     */
	public boolean matches(final SpreadsheetStory story) {
		final Date storyEndDate = story.getEndDate();
		if (storyEndDate == null) {
			return false;
		}
		if (storyEndDate.before(this.startDate)) {
			return false;
		}
		if (storyEndDate.after(this.endDate)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SpreadsheetStoryFilter{" + "startDate=" + this.startDate
				+ " - endDate=" + this.endDate + "}";
	}
}
