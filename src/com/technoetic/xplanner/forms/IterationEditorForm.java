package com.technoetic.xplanner.forms;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class IterationEditorForm extends AbstractEditorForm {
	private String name;
	private String description;
	private Date startDate;
	private Date endDate;
	private double daysWorked;
	private String startDateString;
	private String endDateString;
	private String statusKey;
	private int projectId;

	public String getContainerId() {
		return Integer.toString(this.getProjectId());
	}

	@Override
	public ActionErrors validate(final ActionMapping mapping,
			final HttpServletRequest request) {
		AbstractEditorForm.initConverters(request);
		final ActionErrors errors = new ActionErrors();
		if (this.isSubmitted()) {
			AbstractEditorForm.require(errors, this.name,
					"iteration.editor.missing_name");
			AbstractEditorForm.require(errors, this.startDateString,
					"iteration.editor.bad_start_date");
			AbstractEditorForm.require(errors, this.endDateString,
					"iteration.editor.bad_end_date");
			this.requirePositiveInterval(errors);
		}
		return errors;
	}

	@Override
	public void reset(final ActionMapping mapping,
			final HttpServletRequest request) {
		super.reset(mapping, request);
		this.name = null;
		this.description = null;
		this.startDateString = null;
		this.endDateString = null;
		this.projectId = 0;
		AbstractEditorForm.dateConverter = null;
	}

	private void requirePositiveInterval(final ActionErrors errors) {
		if (errors.size() == 0) {
			this.startDate = AbstractEditorForm.convertToDate(
					this.startDateString, "iteration.editor.bad_start_date",
					errors);
			this.endDate = AbstractEditorForm.convertToDate(this.endDateString,
					"iteration.editor.bad_end_date", errors);
			if (this.startDate != null && this.endDate != null
					&& this.endDate.compareTo(this.startDate) <= 0) {
				AbstractEditorForm.error(errors,
						"iteration.editor.nonpositive_interval");
			}
		}
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getStatusKey() {
		return this.statusKey;
	}

	public void setStatusKey(final String statusKey) {
		this.statusKey = statusKey;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setStartDateString(final String startDateString) {
		this.startDateString = startDateString;
	}

	public String getStartDateString() {
		return this.startDateString;
	}

	public void setEndDateString(final String endDateString) {
		this.endDateString = endDateString;
	}

	public String getEndDateString() {
		return this.endDateString;
	}

	public void setProjectId(final int projectId) {
		this.projectId = projectId;
	}

	public int getProjectId() {
		return this.projectId;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
		this.endDateString = this.toString(endDate);
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
		this.startDateString = this.toString(startDate);
	}

	public double getDaysWorked() {
		return this.daysWorked;
	}

	public void setDaysWorked(final double daysWorked) {
		this.daysWorked = daysWorked;
	}

	private String toString(final Date date) {
		return date == null ? "" : AbstractEditorForm.dateConverter
				.format(date);
	}

}
