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

public class ImportStoriesForm extends ImportForm {

	private String worksheetName = null;
	private String estimateColumn = null;
	private String statusColumn = null;
	private String titleColumn = null;
	private String endDateColumn = null;
	private String priorityColumn = null;
	private boolean onlyIncomplete = false;
	private String completedStatus = null;

	private static final String NO_WORKSHEET_NAME_KEY = "import.status.worksheet_name";
	public static final String NO_TITLE_COLUMN_KEY = "import.status.no_title_column";
	public static final String NO_END_DATE_COLUMN_KEY = "import.status.no_end_date_column";
	public static final String NO_PRIORITY_COLUMN_KEY = "import.status.no_priority_column";
	private static final String NO_COMPLETED_STORY_STATUS = "import.status.no_completed_story_status";

	@Override
	public void reset(final ActionMapping mapping,
			final HttpServletRequest request) {
		super.reset(mapping, request);
		this.onlyIncomplete = false;
	}

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

	private boolean isNotEmpty(final String worksheetName) {
		return StringUtils.isEmpty(worksheetName);
	}

	private void validate(final boolean condition, final ActionErrors errors,
			final String key) {
		if (condition) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(key));
		}
	}

	public String getTitleColumn() {
		return this.titleColumn;
	}

	public void setTitleColumn(final String titleColumn) {
		this.titleColumn = titleColumn;
	}

	public String getEstimateColumn() {
		return this.estimateColumn;
	}

	public void setEstimateColumn(final String estimateColumn) {
		this.estimateColumn = estimateColumn;
	}

	public String getEndDateColumn() {
		return this.endDateColumn;
	}

	public void setEndDateColumn(final String endDateColumn) {
		this.endDateColumn = endDateColumn;
	}

	public String getPriorityColumn() {
		return this.priorityColumn;
	}

	public void setPriorityColumn(final String priorityColumn) {
		this.priorityColumn = priorityColumn;
	}

	public String getStatusColumn() {
		return this.statusColumn;
	}

	public void setStatusColumn(final String statusColumn) {
		this.statusColumn = statusColumn;
	}

	public boolean isOnlyIncomplete() {
		return this.onlyIncomplete;
	}

	public void setOnlyIncomplete(final boolean onlyIncomplete) {
		this.onlyIncomplete = onlyIncomplete;
	}

	public String getCompletedStatus() {
		return this.completedStatus;
	}

	public void setCompletedStatus(final String completedStatus) {
		this.completedStatus = completedStatus;
	}

	public void setWorksheetName(final String worksheetName) {
		this.worksheetName = worksheetName;
	}

	public String getWorksheetName() {
		return this.worksheetName;
	}
}