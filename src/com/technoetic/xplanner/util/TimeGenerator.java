package com.technoetic.xplanner.util;

import java.util.Calendar;
import java.util.Date;

/**
 * User: Mateusz Prokopowicz Date: Aug 24, 2004 Time: 4:47:22 PM
 */
public class TimeGenerator {

	int daysOffset = 0;

	public int moveCurrentDay(final int days) {
		return this.daysOffset += days;
	}

	public void reset() {
		this.daysOffset = 0;
	}

	public Date getCurrentTime() {
		final Date now = new Date();
		if (this.daysOffset != 0) {
			return TimeGenerator.shiftDate(now, Calendar.DATE, this.daysOffset);
		}
		return now;
	}

	public Date getTodaysMidnight() {
		return TimeGenerator.getMidnight(this.getCurrentTime());
	}

	public static Date getMidnight(final Date date) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date shiftDate(final Date date, final int code,
			final int value) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime((Date) date.clone());
		cal.add(code, value);
		return cal.getTime();
	}
}
