package com.technoetic.xplanner.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.domain.virtual.Timesheet;

public class AggregateTimesheetForm extends AbstractEditorForm {

	private java.util.Date startDate;
	private String startDateString;
	private java.util.Date endDate;
	private String endDateString;
	private SimpleDateFormat dateFormat;
	private Timesheet timesheet;
	private Collection allPeople;
	private String[] selectedPeople;

	@Override
	public ActionErrors validate(final ActionMapping mapping,
			final HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();

		if (this.dateFormat == null) {
			final String format = AbstractEditorForm.getResources(request)
					.getMessage("format.date");
			this.dateFormat = new SimpleDateFormat(format);
		}

		Date startDate = null;
		if (AbstractEditorForm.isPresent(this.startDateString)) {
			try {
				startDate = this.dateFormat.parse(this.startDateString);
				this.setStartDate(startDate);
			} catch (final ParseException ex) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"timesheet.error.unparsable_date"));
				request.setAttribute(Globals.ERROR_KEY, errors);
				return errors;
			}
		}

		Date endDate = null;
		if (AbstractEditorForm.isPresent(this.endDateString)) {
			try {
				endDate = this.dateFormat.parse(this.endDateString);
				this.setEndDate(endDate);
			} catch (final ParseException ex) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"timesheet.error.unparsable_date"));
				request.setAttribute(Globals.ERROR_KEY, errors);
				return errors;
			}
		}

		return errors;
	}

	@Override
	public void reset(final ActionMapping mapping,
			final HttpServletRequest request) {
		super.reset(mapping, request);
		this.endDate = this.getWeekEndDate();
		this.startDate = this.getWeekStartDate();
		this.timesheet = new Timesheet(this.startDate, this.endDate);
		this.allPeople = new ArrayList();
		this.selectedPeople = new String[] {};
	}

	public java.util.Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(final java.util.Date startDate) {
		this.startDate = startDate;
	}

	public String getStartDateString() {
		if (this.dateFormat == null) {
			return this.getStartDate().toString();
		}
		return this.dateFormat.format(this.getStartDate());
	}

	public void setStartDateString(final String start) {
		this.startDateString = start;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	public String getEndDateString() {
		if (this.dateFormat == null) {
			return this.getEndDate().toString();
		}
		return this.dateFormat.format(this.getEndDate());
	}

	public void setEndDateString(final String end) {
		this.endDateString = end;
	}

	public Timesheet getTimesheet() {
		return this.timesheet;
	}

	public void setTimesheet(final Timesheet timesheet) {
		this.timesheet = timesheet;
	}

	private Date getWeekEndDate() {
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		final int weekday = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DATE, 7 - weekday);
		return cal.getTime();
	}

	private Date getWeekStartDate() {
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		final int weekday = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DATE, -weekday + 1);
		return cal.getTime();
	}

	public void setDateFormat(final SimpleDateFormat format) {
		this.dateFormat = format;
	}

	public SimpleDateFormat getDateFormat() {
		return this.dateFormat;
	}

	public Collection getAllPeople() {
		return this.allPeople;
	}

	public void setAllPeople(final Collection allPeople) {
		this.allPeople = allPeople;
	}

	public String[] getSelectedPeople() {
		return this.selectedPeople;
	}

	public void setSelectedPeople(final String[] selectedPeople) {
		this.selectedPeople = selectedPeople;
	}
}