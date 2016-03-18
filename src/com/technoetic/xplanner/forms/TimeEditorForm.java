package com.technoetic.xplanner.forms;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.format.DateTimeFormat;
import com.technoetic.xplanner.util.Interval;
import com.technoetic.xplanner.util.RequestUtils;

public class TimeEditorForm extends AbstractEditorForm {
	public static final int MAX_DESCRIPTION_LENGTH = 500;
	public static final int HOURS = 60 * 60 * 1000;
	public static final int HOUR_IN_MS = TimeEditorForm.HOURS;
	public static final String WIZARD_MODE_ATTR = "wizard_mode";

	private final ArrayList ids = new ArrayList();
	private final ArrayList deletes = new ArrayList();
	private final ArrayList startTimes = new ArrayList();
	private final ArrayList endTimes = new ArrayList();
	private final ArrayList durations = new ArrayList();
	private final ArrayList people1 = new ArrayList();
	private final ArrayList people2 = new ArrayList();
	private final ArrayList reportDates = new ArrayList();
	private final ArrayList descriptions = new ArrayList();

	private final HashSet previousMementos = new HashSet();
	private String remainingHours;

	private int rowcount;
	public static final String UNPARSABLE_TIME_ERROR_KEY = "edittime.error.unparsable_time";
	public static final String UNPARSABLE_NUMBER_ERROR_KEY = "edittime.error.unparsable_number";
	public static final String MISSING_TIME_ERROR_KEY = "edittime.error.missing_time";
	public static final String MISSING_PERSON_ERROR_KEY = "edittime.error.missing_person";
	public static final String SAME_PEOPLE_ERROR_KEY = "edittime.error.same_people";
	public static final String NEGATIVE_INTERVAL_ERROR_KEY = "edittime.error.negative_interval";
	public static final String OVERLAPPING_INTERVAL_ERROR_KEY = "edittime.error.overlapping_interval";
	public static final String BOTH_INTERVAL_AND_DURATION_ERROR_KEY = "edittime.error.both_interval_and_duration";
	public static final String MISSING_REPORT_DATE_ERROR_KEY = "edittime.error.missing_report_date";
	public static final String LONG_DESCRIPTION_ERROR_KEY = "edittime.error.long_description";

	@Override
	public ActionErrors validate(final ActionMapping mapping,
			final HttpServletRequest request) {
		AbstractEditorForm.initConverters(request);

		final ActionErrors errors = new ActionErrors();
		this.previousMementos.clear();

		for (int i = 0; i < this.rowcount; i++) {
			errors.add(this.valideRow(i, request));
		}
		return errors;
	}

	private ActionErrors valideRow(final int row,
			final HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();
		if (row == this.rowcount - 1 && this.isEmpty(row)) {
			return errors;
		}

		int id = 0;
		if (AbstractEditorForm.isPresent(this.getEntryId(row))) {
			id = Integer.parseInt(this.getEntryId(row));
		}

		if (this.getDeleted(row) != null && this.getDeleted(row).equals("true")) {
			return errors;
		}

		final String startTimeString = this.getStartTime(row);
		final String endTimeString = this.getEndTime(row);
		final Date startTime = AbstractEditorForm.convertToDateTime(
				startTimeString, TimeEditorForm.UNPARSABLE_TIME_ERROR_KEY,
				errors);
		final Date endTime = AbstractEditorForm
				.convertToDateTime(endTimeString,
						TimeEditorForm.UNPARSABLE_TIME_ERROR_KEY, errors);
		final Date reportDate = AbstractEditorForm.convertToDate(
				this.getReportDate(row),
				TimeEditorForm.UNPARSABLE_TIME_ERROR_KEY, errors);

		int person1Id = 0;
		if (AbstractEditorForm.isPresent(this.getPerson1Id(row))) {
			person1Id = Integer.parseInt(this.getPerson1Id(row));
		}
		int person2Id = 0;
		if (AbstractEditorForm.isPresent(this.getPerson2Id(row))) {
			person2Id = Integer.parseInt(this.getPerson2Id(row));
		}

		double duration = 0;
		if (AbstractEditorForm.isPresent(this.getDuration(row))) {
			try {
				duration = AbstractEditorForm.decimalConverter.parse(this
						.getDuration(row));
			} catch (final ParseException ex) {
				AbstractEditorForm.error(errors,
						TimeEditorForm.UNPARSABLE_NUMBER_ERROR_KEY);
			}
		}

		// Validation #1
		// - Start and end must be present in all except the last row
		if (row < this.getRowcount() - 1
				&& (startTime == null || endTime == null) && duration == 0) {
			AbstractEditorForm.error(errors,
					TimeEditorForm.MISSING_TIME_ERROR_KEY);
		}

		// Validation #2
		// - At least one person must be present in all except the last row
		// unless the last row has no time entry
		if ((id > 0 || startTime != null || endTime != null || duration > 0)
				&& person1Id == 0 && person2Id == 0) {
			AbstractEditorForm.error(errors,
					TimeEditorForm.MISSING_PERSON_ERROR_KEY);
		} else
		// Validation #5
		// - Different people
		if (startTime != null && person1Id == person2Id) {
			AbstractEditorForm.error(errors,
					TimeEditorForm.SAME_PEOPLE_ERROR_KEY);
		}

		// Validation #3
		// - End time must be greater than start time
		if (startTime != null && endTime != null
				&& endTime.getTime() <= startTime.getTime()) {
			AbstractEditorForm.error(errors,
					TimeEditorForm.NEGATIVE_INTERVAL_ERROR_KEY);
		} else
		// Validation #4
		// - no overlapping intervals
		if (this.isOverlapping(startTime, endTime, duration, person1Id,
				person2Id)) {
			AbstractEditorForm.error(errors,
					TimeEditorForm.OVERLAPPING_INTERVAL_ERROR_KEY);
		}

		// Validation #6
		// - End time and Duration
		if (startTime == null && endTime != null && duration != 0.0) {
			AbstractEditorForm.error(errors,
					TimeEditorForm.MISSING_TIME_ERROR_KEY);
		}

		// Validation #7
		// - Start time and Duration -- calculate end time
		if (startTime != null && endTime != null && duration != 0.0
				&& row == this.rowcount - 1) {
			// This recovers automatically, no error message
			AbstractEditorForm.error(errors,
					TimeEditorForm.BOTH_INTERVAL_AND_DURATION_ERROR_KEY);
		}

		// Validation #8
		// - Start time and Duration -- calculate end time
		if (startTime != null && endTime == null && duration != 0.0) {
			// This recovers automatically, no error message
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(startTime);
			calendar.add(Calendar.MILLISECOND,
					(int) (duration * TimeEditorForm.HOURS));
			this.setEndTime(row,
					DateTimeFormat.format(request, calendar.getTime()));
		}

		// Validation #9
		// - Report Date must be present
		if (reportDate == null) {
			AbstractEditorForm.error(errors,
					TimeEditorForm.MISSING_REPORT_DATE_ERROR_KEY);
		}

		if (AbstractEditorForm.isPresent(this.getDescription(row))) {
			if (this.getDescription(row).length() > TimeEditorForm.MAX_DESCRIPTION_LENGTH) {
				AbstractEditorForm.error(errors,
						TimeEditorForm.LONG_DESCRIPTION_ERROR_KEY);
			}
		}
		return errors;
	}

	@Override
	public void reset(final ActionMapping mapping,
			final HttpServletRequest request) {
		if (!RequestUtils.isAttributeTrue(request,
				TimeEditorForm.WIZARD_MODE_ATTR)) {
			super.reset(mapping, request);
			this.ids.clear();
			this.deletes.clear();
			this.startTimes.clear();
			this.endTimes.clear();
			this.people1.clear();
			this.people2.clear();
			this.durations.clear();
			this.reportDates.clear();
			this.descriptions.clear();
			this.rowcount = 0;
		}
	}

	public void setEntryId(final int index, final String id) {
		AbstractEditorForm.ensureSize(this.ids, index + 1);
		this.ids.set(index, id);
	}

	public String getEntryId(final int index) {
		AbstractEditorForm.ensureSize(this.ids, index + 1);
		return (String) this.ids.get(index);
	}

	public void setDeleted(final int index, final String flag) {
		AbstractEditorForm.ensureSize(this.deletes, index + 1);
		this.deletes.set(index, flag);
	}

	public String getDeleted(final int index) {
		AbstractEditorForm.ensureSize(this.deletes, index + 1);
		return (String) this.deletes.get(index);
	}

	public void setStartTime(final int index, final String date) {
		AbstractEditorForm.ensureSize(this.startTimes, index + 1);
		this.startTimes.set(index, date);
	}

	public String getStartTime(final int index) {
		AbstractEditorForm.ensureSize(this.startTimes, index + 1);
		return (String) this.startTimes.get(index);
	}

	public void setEndTime(final int index, final String date) {
		AbstractEditorForm.ensureSize(this.endTimes, index + 1);
		this.endTimes.set(index, date);
	}

	public String getEndTime(final int index) {
		AbstractEditorForm.ensureSize(this.endTimes, index + 1);
		return (String) this.endTimes.get(index);
	}

	public void setDuration(final int index, final String duration) {
		AbstractEditorForm.ensureSize(this.durations, index + 1);
		this.durations.set(index, duration);
	}

	public String getDuration(final int index) {
		AbstractEditorForm.ensureSize(this.durations, index + 1);
		return (String) this.durations.get(index);
	}

	public void setPerson1Id(final int index, final String id) {
		AbstractEditorForm.ensureSize(this.people1, index + 1);
		this.people1.set(index, id);
	}

	public String getPerson1Id(final int index) {
		AbstractEditorForm.ensureSize(this.people1, index + 1);
		return (String) this.people1.get(index);
	}

	public void setPerson2Id(final int index, final String id) {
		AbstractEditorForm.ensureSize(this.people2, index + 1);
		this.people2.set(index, id);
	}

	public String getPerson2Id(final int index) {
		AbstractEditorForm.ensureSize(this.people2, index + 1);
		return (String) this.people2.get(index);
	}

	public void setReportDate(final int index, final String date) {
		AbstractEditorForm.ensureSize(this.reportDates, index + 1);
		this.reportDates.set(index, date);
	}

	public String getReportDate(final int index) {
		AbstractEditorForm.ensureSize(this.reportDates, index + 1);
		return (String) this.reportDates.get(index);
	}

	public void setDescription(final int index, final String description) {
		AbstractEditorForm.ensureSize(this.descriptions, index + 1);
		this.descriptions.set(index, description);
	}

	public String getDescription(final int index) {
		AbstractEditorForm.ensureSize(this.descriptions, index + 1);
		return (String) this.descriptions.get(index);
	}

	public String getRemainingHours() {
		return this.remainingHours;
	}

	public void setRemainingHours(final String remainingHours) {
		this.remainingHours = remainingHours;
	}

	public void setRowcount(final int rowcount) {
		this.rowcount = rowcount;
	}

	public int getRowcount() {
		return this.rowcount;
	}

	public boolean isIntervalReadOnly(final int i) {
		return !this.isDurationReadOnly(i) && !this.isEmpty(i);
	}

	public boolean isDurationReadOnly(final int i) {
		return !this.isEmpty(i)
				&& (StringUtils.isNotEmpty(this.getStartTime(i)) || StringUtils
						.isNotEmpty(this.getEndTime(i)));
	}

	public boolean isEmpty(final int index) {
		return StringUtils.isEmpty(this.getStartTime(index))
				&& StringUtils.isEmpty(this.getEndTime(index))
				&& StringUtils.isEmpty(this.getDuration(index));
	}

	private class TimeEntryMemento implements Serializable {
		private final int personId1;
		private final int personId2;
		private Interval interval;

		public TimeEntryMemento(final int personId1, final int personId2,
				final Date startTime, final Date endTime) {
			this.personId1 = personId1;
			this.personId2 = personId2;
			if (startTime != null && endTime != null) {
				this.interval = new Interval(startTime.getTime(),
						endTime.getTime());
			} else if (startTime != null) {
				this.interval = new Interval(startTime.getTime());
			} else if (endTime != null) {
				this.interval = new Interval(endTime.getTime());
			}
		}

		public int getPersonId1() {
			return this.personId1;
		}

		public int getPersonId2() {
			return this.personId2;
		}

		public Interval getInterval() {
			return this.interval;
		}

		public boolean overlaps(final TimeEntryMemento previousMemento) {
			return this.interval.overlaps(previousMemento.getInterval())
					&& (this.personId1 != 0
							&& (this.personId1 == previousMemento
									.getPersonId1() || this.personId1 == previousMemento
									.getPersonId2()) || this.personId2 != 0
							&& (this.personId2 == previousMemento
									.getPersonId1() || this.personId2 == previousMemento
									.getPersonId2()));
		}
	}

	private boolean isOverlapping(final Date startTime, Date endTime,
			final double duration, final int personId1, final int personId2) {
		if (startTime != null && (endTime != null || duration > 0)) {
			if (endTime == null) {
				endTime = new Date((long) (startTime.getTime() + duration
						* TimeEditorForm.HOUR_IN_MS));
			}
			final TimeEntryMemento memento = new TimeEntryMemento(personId1,
					personId2, startTime, endTime);
			if (memento.getInterval() != null) {
				final Iterator mementoItr = this.previousMementos.iterator();
				while (mementoItr.hasNext()) {
					final TimeEntryMemento previousMemento = (TimeEntryMemento) mementoItr
							.next();
					if (memento.overlaps(previousMemento)) {
						return true;
					}
				}
				this.previousMementos.add(memento);
			}
		}
		return false;
	}
}
