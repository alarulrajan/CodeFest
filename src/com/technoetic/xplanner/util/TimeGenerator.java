package com.technoetic.xplanner.util;

import java.util.Calendar;
import java.util.Date;

/**
 * User: Mateusz Prokopowicz Date: Aug 24, 2004 Time: 4:47:22 PM.
 */
public class TimeGenerator {

    /** The days offset. */
    int daysOffset = 0;

    /**
     * Move current day.
     *
     * @param days
     *            the days
     * @return the int
     */
    public int moveCurrentDay(final int days) {
        return this.daysOffset += days;
    }

    /**
     * Reset.
     */
    public void reset() {
        this.daysOffset = 0;
    }

    /**
     * Gets the current time.
     *
     * @return the current time
     */
    public Date getCurrentTime() {
        final Date now = new Date();
        if (this.daysOffset != 0) {
            return TimeGenerator.shiftDate(now, Calendar.DATE, this.daysOffset);
        }
        return now;
    }

    /**
     * Gets the todays midnight.
     *
     * @return the todays midnight
     */
    public Date getTodaysMidnight() {
        return TimeGenerator.getMidnight(this.getCurrentTime());
    }

    /**
     * Gets the midnight.
     *
     * @param date
     *            the date
     * @return the midnight
     */
    public static Date getMidnight(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Shift date.
     *
     * @param date
     *            the date
     * @param code
     *            the code
     * @param value
     *            the value
     * @return the date
     */
    public static Date shiftDate(final Date date, final int code,
            final int value) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime((Date) date.clone());
        cal.add(code, value);
        return cal.getTime();
    }
}
