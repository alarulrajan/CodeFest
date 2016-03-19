package com.technoetic.xplanner.domain.virtual;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class DailyTimesheetEntry.
 */
public class DailyTimesheetEntry implements Serializable {
    
    /** The entry date. */
    private java.util.Date entryDate;
    
    /** The total duration. */
    private java.math.BigDecimal totalDuration = new BigDecimal(0.0);
    
    /** The short date format. */
    public static SimpleDateFormat shortDateFormat = new SimpleDateFormat(
            "EEE d-MMM-yy");

    /**
     * Instantiates a new daily timesheet entry.
     */
    public DailyTimesheetEntry() {
    }

    /**
     * Instantiates a new daily timesheet entry.
     *
     * @param entryDate
     *            the entry date
     * @param duration
     *            the duration
     */
    public DailyTimesheetEntry(final Date entryDate, final BigDecimal duration) {
        this.setEntryDate(entryDate);
        this.setTotalDuration(duration);
    }

    /**
     * Gets the entry date.
     *
     * @return the entry date
     */
    public Date getEntryDate() {
        return this.entryDate;
    }

    /**
     * Gets the entry date short.
     *
     * @return the entry date short
     */
    public String getEntryDateShort() {
        return DailyTimesheetEntry.shortDateFormat.format(this.getEntryDate());
    }

    /**
     * Sets the entry date.
     *
     * @param date
     *            the new entry date
     */
    public void setEntryDate(final Date date) {
        this.entryDate = date;
    }

    /**
     * Gets the total duration.
     *
     * @return the total duration
     */
    public java.math.BigDecimal getTotalDuration() {
        return this.totalDuration.setScale(1, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * Sets the total duration.
     *
     * @param totalDuration
     *            the new total duration
     */
    public void setTotalDuration(final java.math.BigDecimal totalDuration) {
        this.totalDuration = totalDuration;
    }

    /**
     * Sets the total duration.
     *
     * @param totalDuration
     *            the new total duration
     */
    public void setTotalDuration(final double totalDuration) {
        this.totalDuration = this.totalDuration.add(new BigDecimal(
                totalDuration));
    }
}