/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Mar 31, 2005
 * Time: 8:55:31 PM
 */
package com.technoetic.xplanner.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * The Class ImportStoriesForm.
 */
public class ImportStoriesForm extends ImportForm {

	/** The worksheet name. */
	private String worksheetName = null;
	
	/** The estimate column. */
	private String estimateColumn = null;
	
	/** The status column. */
	private String statusColumn = null;
	
	/** The title column. */
	private String titleColumn = null;
	
	/** The end date column. */
	private String endDateColumn = null;
	
	/** The priority column. */
	private String priorityColumn = null;
	
	/** The only incomplete. */
	private boolean onlyIncomplete = false;
	
	/** The completed status. */
	private String completedStatus = null;

	/** The Constant NO_WORKSHEET_NAME_KEY. */
	private static final String NO_WORKSHEET_NAME_KEY = "import.status.worksheet_name";
	
	/** The Constant NO_TITLE_COLUMN_KEY. */
	public static final String NO_TITLE_COLUMN_KEY = "import.status.no_title_column";
	
	/** The Constant NO_END_DATE_COLUMN_KEY. */
	public static final String NO_END_DATE_COLUMN_KEY = "import.status.no_end_date_column";
	
	/** The Constant NO_PRIORITY_COLUMN_KEY. */
	public static final String NO_PRIORITY_COLUMN_KEY = "import.status.no_priority_column";
	
	/** The Constant NO_COMPLETED_STORY_STATUS. */
	private static final String NO_COMPLETED_STORY_STATUS = "import.status.no_completed_story_status";

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.forms.ImportForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void reset(final ActionMapping mapping,
			final HttpServletRequest request) {
		super.reset(mapping, request);
		this.onlyIncomplete = false;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.forms.ImportForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public ActionErrors validate(final ActionMapping mapping,
			final HttpServletRequest request) {
		final ActionErrors errors = super.validate(mapping, request);
		if (this.isSubmitted()) {
			this.validate(this.isNotEmpty(this.worksheetName), errors,
					ImportStoriesForm.NO_WORKSHEET_NAME_KEY);
			this.validate(this.isNotEmpty(this.titleColumn), errors,
					ImportStoriesForm.NO_TITLE_COLUMN_KEY);
			this.validate(this.isNotEmpty(this.endDateColumn), errors,
					ImportStoriesForm.NO_END_DATE_COLUMN_KEY);
			this.validate(this.isNotEmpty(this.priorityColumn), errors,
					ImportStoriesForm.NO_PRIORITY_COLUMN_KEY);
			this.validate(
					this.onlyIncomplete
							&& this.isNotEmpty(this.completedStatus), errors,
					ImportStoriesForm.NO_COMPLETED_STORY_STATUS);
		}
		return errors;
	}

	/**
     * Checks if is not empty.
     *
     * @param worksheetName
     *            the worksheet name
     * @return true, if is not empty
     */
	private boolean isNotEmpty(final String worksheetName) {
		return StringUtils.isEmpty(worksheetName);
	}

	/**
     * Validate.
     *
     * @param condition
     *            the condition
     * @param errors
     *            the errors
     * @param key
     *            the key
     */
	private void validate(final boolean condition, final ActionErrors errors,
			final String key) {
		if (condition) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(key));
		}
	}

	/**
     * Gets the title column.
     *
     * @return the title column
     */
	public String getTitleColumn() {
		return this.titleColumn;
	}

	/**
     * Sets the title column.
     *
     * @param titleColumn
     *            the new title column
     */
	public void setTitleColumn(final String titleColumn) {
		this.titleColumn = titleColumn;
	}

	/**
     * Gets the estimate column.
     *
     * @return the estimate column
     */
	public String getEstimateColumn() {
		return this.estimateColumn;
	}

	/**
     * Sets the estimate column.
     *
     * @param estimateColumn
     *            the new estimate column
     */
	public void setEstimateColumn(final String estimateColumn) {
		this.estimateColumn = estimateColumn;
	}

	/**
     * Gets the end date column.
     *
     * @return the end date column
     */
	public String getEndDateColumn() {
		return this.endDateColumn;
	}

	/**
     * Sets the end date column.
     *
     * @param endDateColumn
     *            the new end date column
     */
	public void setEndDateColumn(final String endDateColumn) {
		this.endDateColumn = endDateColumn;
	}

	/**
     * Gets the priority column.
     *
     * @return the priority column
     */
	public String getPriorityColumn() {
		return this.priorityColumn;
	}

	/**
     * Sets the priority column.
     *
     * @param priorityColumn
     *            the new priority column
     */
	public void setPriorityColumn(final String priorityColumn) {
		this.priorityColumn = priorityColumn;
	}

	/**
     * Gets the status column.
     *
     * @return the status column
     */
	public String getStatusColumn() {
		return this.statusColumn;
	}

	/**
     * Sets the status column.
     *
     * @param statusColumn
     *            the new status column
     */
	public void setStatusColumn(final String statusColumn) {
		this.statusColumn = statusColumn;
	}

	/**
     * Checks if is only incomplete.
     *
     * @return true, if is only incomplete
     */
	public boolean isOnlyIncomplete() {
		return this.onlyIncomplete;
	}

	/**
     * Sets the only incomplete.
     *
     * @param onlyIncomplete
     *            the new only incomplete
     */
	public void setOnlyIncomplete(final boolean onlyIncomplete) {
		this.onlyIncomplete = onlyIncomplete;
	}

	/**
     * Gets the completed status.
     *
     * @return the completed status
     */
	public String getCompletedStatus() {
		return this.completedStatus;
	}

	/**
     * Sets the completed status.
     *
     * @param completedStatus
     *            the new completed status
     */
	public void setCompletedStatus(final String completedStatus) {
		this.completedStatus = completedStatus;
	}

	/**
     * Sets the worksheet name.
     *
     * @param worksheetName
     *            the new worksheet name
     */
	public void setWorksheetName(final String worksheetName) {
		this.worksheetName = worksheetName;
	}

	/**
     * Gets the worksheet name.
     *
     * @return the worksheet name
     */
	public String getWorksheetName() {
		return this.worksheetName;
	}
}