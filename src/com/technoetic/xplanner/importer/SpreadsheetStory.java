package com.technoetic.xplanner.importer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class SpreadsheetStory.
 */
public class SpreadsheetStory {
	
	/** The Constant STATUS_COMPLETED. */
	public static final String STATUS_COMPLETED = "C";
	
	/** The Constant formatter. */
	public final static SimpleDateFormat formatter = new SimpleDateFormat(
			"ddMMMyy");
	
	/** The title. */
	private String title = "Default title";
	
	/** The estimate. */
	private final double estimate;
	
	/** The status. */
	// ChangeSoon status should be an enum
	private String status = "";
	
	/** The complete. */
	private boolean complete = false;
	
	/** The end date. */
	private Date endDate;
	
	/** The priority. */
	private int priority = 4;

	/**
     * Instantiates a new spreadsheet story.
     *
     * @param title
     *            the title
     * @param status
     *            the status
     * @param estimate
     *            the estimate
     */
	SpreadsheetStory(final String title, final String status,
			final double estimate) {
		this.title = title;
		this.estimate = estimate;
		this.setStatus(status);
	}

	/**
     * Instantiates a new spreadsheet story.
     *
     * @param storyEndDate
     *            the story end date
     * @param title
     *            the title
     * @param status
     *            the status
     * @param estimate
     *            the estimate
     * @param priority
     *            the priority
     */
	public SpreadsheetStory(final Date storyEndDate, final String title,
			final String status, final double estimate, final int priority) {
		this(title, status, estimate);
		this.endDate = storyEndDate;
		this.priority = priority;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result;
		result = this.title != null ? this.title.hashCode() : 0;
		result = 29 * result
				+ (this.status != null ? this.status.hashCode() : 0);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Story{" + " priority=" + this.priority + ", title='"
				+ this.title + "'" + ", status='" + this.status + "'"
				+ ", estimate=" + this.estimate + ", endDate="
				+ SpreadsheetStory.formatter.format(this.endDate) + "}";
	}

	/**
     * Gets the title.
     *
     * @return the title
     */
	public String getTitle() {
		return this.title;
	}

	/**
     * Sets the title.
     *
     * @param title
     *            the new title
     */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
     * Gets the status.
     *
     * @return the status
     */
	public String getStatus() {
		return this.status;
	}

	/**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
	public void setStatus(final String status) {
		this.status = status;
		if (status.equals(SpreadsheetStory.STATUS_COMPLETED)) {
			this.complete = true;
		}
	}

	/**
     * Checks if is completed.
     *
     * @return true, if is completed
     */
	public boolean isCompleted() {
		return this.complete;
	}

	/**
     * Gets the end date.
     *
     * @return the end date
     */
	public Date getEndDate() {
		return this.endDate;
	}

	/**
     * Gets the estimate.
     *
     * @return the estimate
     */
	public double getEstimate() {
		return this.estimate;
	}

	/**
     * Gets the priority.
     *
     * @return the priority
     */
	public int getPriority() {
		return this.priority;
	}
}
