package com.technoetic.xplanner.importer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SpreadsheetStory {
	public static final String STATUS_COMPLETED = "C";
	public final static SimpleDateFormat formatter = new SimpleDateFormat(
			"ddMMMyy");
	private String title = "Default title";
	private final double estimate;
	// TODO status should be an enum
	private String status = "";
	private boolean complete = false;
	private Date endDate;
	private int priority = 4;

	SpreadsheetStory(final String title, final String status,
			final double estimate) {
		this.title = title;
		this.estimate = estimate;
		this.setStatus(status);
	}

	public SpreadsheetStory(final Date storyEndDate, final String title,
			final String status, final double estimate, final int priority) {
		this(title, status, estimate);
		this.endDate = storyEndDate;
		this.priority = priority;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SpreadsheetStory)) {
			return false;
		}

		final SpreadsheetStory spreadsheetStory = (SpreadsheetStory) o;

		if (this.status != null ? !this.status.equals(spreadsheetStory.status)
				: spreadsheetStory.status != null) {
			return false;
		}
		if (this.title != null ? !this.title.equals(spreadsheetStory.title)
				: spreadsheetStory.title != null) {
			return false;
		}
		if (this.estimate != spreadsheetStory.estimate) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		result = this.title != null ? this.title.hashCode() : 0;
		result = 29 * result
				+ (this.status != null ? this.status.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Story{" + " priority=" + this.priority + ", title='"
				+ this.title + "'" + ", status='" + this.status + "'"
				+ ", estimate=" + this.estimate + ", endDate="
				+ SpreadsheetStory.formatter.format(this.endDate) + "}";
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
		if (status.equals(SpreadsheetStory.STATUS_COMPLETED)) {
			this.complete = true;
		}
	}

	public boolean isCompleted() {
		return this.complete;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public double getEstimate() {
		return this.estimate;
	}

	public int getPriority() {
		return this.priority;
	}
}
