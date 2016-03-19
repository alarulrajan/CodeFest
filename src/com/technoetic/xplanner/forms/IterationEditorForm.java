package com.technoetic.xplanner.forms;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * The Class IterationEditorForm.
 */
public class IterationEditorForm extends AbstractEditorForm {
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/** The start date. */
	private Date startDate;
	
	/** The end date. */
	private Date endDate;
	
	/** The days worked. */
	private double daysWorked;
	
	/** The start date string. */
	private String startDateString;
	
	/** The end date string. */
	private String endDateString;
	
	/** The status key. */
	private String statusKey;
	
	/** The project id. */
	private int projectId;

	/**
     * Gets the container id.
     *
     * @return the container id
     */
	public String getContainerId() {
		return Integer.toString(this.getProjectId());
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
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

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.forms.AbstractEditorForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
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

	/**
     * Require positive interval.
     *
     * @param errors
     *            the errors
     */
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

	/**
     * Gets the name.
     *
     * @return the name
     */
	public String getName() {
		return this.name;
	}

	/**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
	public void setName(final String name) {
		this.name = name;
	}

	/**
     * Gets the status key.
     *
     * @return the status key
     */
	public String getStatusKey() {
		return this.statusKey;
	}

	/**
     * Sets the status key.
     *
     * @param statusKey
     *            the new status key
     */
	public void setStatusKey(final String statusKey) {
		this.statusKey = statusKey;
	}

	/**
     * Sets the description.
     *
     * @param description
     *            the new description
     */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
     * Gets the description.
     *
     * @return the description
     */
	public String getDescription() {
		return this.description;
	}

	/**
     * Sets the start date string.
     *
     * @param startDateString
     *            the new start date string
     */
	public void setStartDateString(final String startDateString) {
		this.startDateString = startDateString;
	}

	/**
     * Gets the start date string.
     *
     * @return the start date string
     */
	public String getStartDateString() {
		return this.startDateString;
	}

	/**
     * Sets the end date string.
     *
     * @param endDateString
     *            the new end date string
     */
	public void setEndDateString(final String endDateString) {
		this.endDateString = endDateString;
	}

	/**
     * Gets the end date string.
     *
     * @return the end date string
     */
	public String getEndDateString() {
		return this.endDateString;
	}

	/**
     * Sets the project id.
     *
     * @param projectId
     *            the new project id
     */
	public void setProjectId(final int projectId) {
		this.projectId = projectId;
	}

	/**
     * Gets the project id.
     *
     * @return the project id
     */
	public int getProjectId() {
		return this.projectId;
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
     * Sets the end date.
     *
     * @param endDate
     *            the new end date
     */
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
		this.endDateString = this.toString(endDate);
	}

	/**
     * Gets the start date.
     *
     * @return the start date
     */
	public Date getStartDate() {
		return this.startDate;
	}

	/**
     * Sets the start date.
     *
     * @param startDate
     *            the new start date
     */
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
		this.startDateString = this.toString(startDate);
	}

	/**
     * Gets the days worked.
     *
     * @return the days worked
     */
	public double getDaysWorked() {
		return this.daysWorked;
	}

	/**
     * Sets the days worked.
     *
     * @param daysWorked
     *            the new days worked
     */
	public void setDaysWorked(final double daysWorked) {
		this.daysWorked = daysWorked;
	}

	/**
     * To string.
     *
     * @param date
     *            the date
     * @return the string
     */
	private String toString(final Date date) {
		return date == null ? "" : AbstractEditorForm.dateConverter
				.format(date);
	}

}
