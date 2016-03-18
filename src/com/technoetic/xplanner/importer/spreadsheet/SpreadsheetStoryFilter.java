package com.technoetic.xplanner.importer.spreadsheet;

import java.util.Date;

import com.technoetic.xplanner.importer.SpreadsheetStory;

/**
 * Created by IntelliJ IDEA. User: tkmower Date: May 19, 2005 Time: 10:22:45 PM
 */
public class SpreadsheetStoryFilter {
	private final Date startDate;
	private final Date endDate;

	public SpreadsheetStoryFilter(final Date startDate, final Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
		if (startDate.after(endDate)) {
			throw new IllegalArgumentException(
					"Filter start date after end date " + this);
		}
	}

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

	@Override
	public String toString() {
		return "SpreadsheetStoryFilter{" + "startDate=" + this.startDate
				+ " - endDate=" + this.endDate + "}";
	}
}
