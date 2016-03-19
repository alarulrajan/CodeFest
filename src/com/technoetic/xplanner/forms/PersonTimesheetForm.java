package com.technoetic.xplanner.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.domain.virtual.Timesheet;

/**
 * The Class PersonTimesheetForm.
 */
public class PersonTimesheetForm extends AbstractEditorForm {

    /** The start date. */
    private Date startDate;
    
    /** The start date string. */
    private String startDateString;
    
    /** The end date. */
    private Date endDate;
    
    /** The end date string. */
    private String endDateString;
    
    /** The date format. */
    private SimpleDateFormat dateFormat;
    
    /** The timesheet. */
    private Timesheet timesheet;

    // DEBT(FORM) Refactor to use base class utility. See TimeEditorForm for
    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    // example
    @Override
    public ActionErrors validate(final ActionMapping mapping,
            final HttpServletRequest request) {
        final ActionErrors errors = new ActionErrors();

        if (this.dateFormat == null) {
            final String format = AbstractEditorForm.getResources(request)
                    .getMessage(request.getLocale(), "format.date");
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

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.forms.AbstractEditorForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void reset(final ActionMapping mapping,
            final HttpServletRequest request) {
        super.reset(mapping, request);
        this.endDate = this.getWeekEndDate();
        this.startDate = this.getWeekStartDate();
        this.timesheet = new Timesheet(this.startDate, this.endDate);
    }

    /**
     * Gets the start date.
     *
     * @return the start date
     */
    public java.util.Date getStartDate() {
        return this.startDate;
    }

    /**
     * Sets the start date.
     *
     * @param startDate
     *            the new start date
     */
    public void setStartDate(final java.util.Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the start date string.
     *
     * @return the start date string
     */
    public String getStartDateString() {
        if (this.dateFormat == null) {
            return this.getStartDate().toString();
        }
        return this.dateFormat.format(this.getStartDate());
    }

    /**
     * Sets the start date string.
     *
     * @param start
     *            the new start date string
     */
    public void setStartDateString(final String start) {
        this.startDateString = start;
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
    }

    /**
     * Gets the end date string.
     *
     * @return the end date string
     */
    public String getEndDateString() {
        if (this.dateFormat == null) {
            return this.getEndDate().toString();
        }
        return this.dateFormat.format(this.getEndDate());
    }

    /**
     * Sets the end date string.
     *
     * @param end
     *            the new end date string
     */
    public void setEndDateString(final String end) {
        this.endDateString = end;
    }

    /**
     * Gets the timesheet.
     *
     * @return the timesheet
     */
    public Timesheet getTimesheet() {
        return this.timesheet;
    }

    /**
     * Sets the timesheet.
     *
     * @param timesheet
     *            the new timesheet
     */
    public void setTimesheet(final Timesheet timesheet) {
        this.timesheet = timesheet;
    }

    /**
     * Gets the week end date.
     *
     * @return the week end date
     */
    private Date getWeekEndDate() {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        final int weekday = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, 7 - weekday);
        return cal.getTime();
    }

    /**
     * Gets the week start date.
     *
     * @return the week start date
     */
    private Date getWeekStartDate() {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        final int weekday = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, -weekday + 1);
        return cal.getTime();
    }

    /**
     * Sets the date format.
     *
     * @param format
     *            the new date format
     */
    public void setDateFormat(final SimpleDateFormat format) {
        this.dateFormat = format;
    }

    /**
     * Gets the date format.
     *
     * @return the date format
     */
    public SimpleDateFormat getDateFormat() {
        return this.dateFormat;
    }

    /**
     * Gets the person id.
     *
     * @return the person id
     */
    public int getPersonId() {
        return Integer.parseInt(super.getOid());
    }
}