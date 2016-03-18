package com.technoetic.xplanner.domain.virtual;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.TreeMap;

public class Timesheet implements Serializable {

	private final ArrayList entries = new ArrayList();
	private final TreeMap dailyEntries = new TreeMap();
	private String personName;
	private BigDecimal total = new BigDecimal(0.0);
	private final Hashtable projectData = new Hashtable();
	private final Hashtable iterationData = new Hashtable();
	private final Hashtable storyData = new Hashtable();

	public Timesheet() {
	}

	public Timesheet(final Date startDate, final Date endDate) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);

		for (; cal.getTimeInMillis() <= endDate.getTime(); cal.add(
				Calendar.DATE, 1)) {
			this.dailyEntries
					.put(cal.getTime(), new DailyTimesheetEntry(cal.getTime(),
							new BigDecimal(0.0)));
		}
	}

	public String getPersonName() {
		return this.personName;
	}

	public void setPersonName(final String personName) {
		this.personName = personName;
	}

	public Collection getEntries() {
		return this.entries;
	}

	public Collection getDailyEntries() {
		return this.dailyEntries.values();
	}

	public void addEntry(final TimesheetEntry entry) {
		this.total = this.total.add(entry.getTotalDuration());
		this.personName = entry.getPersonName();
		this.entries.add(entry);
		this.updateGroupedData(entry);
	}

	public void addDailyEntry(final DailyTimesheetEntry entry) {
		final DailyTimesheetEntry dailyEntry = (DailyTimesheetEntry) this.dailyEntries
				.get(entry.getEntryDate());
		dailyEntry.setTotalDuration(dailyEntry.getTotalDuration().add(
				entry.getTotalDuration()));

		this.dailyEntries.put(dailyEntry.getEntryDate(), dailyEntry);
	}

	public BigDecimal getTotal() {
		return this.total.setScale(1, BigDecimal.ROUND_HALF_EVEN);
	}

	private void updateGroupedData(final TimesheetEntry entry) {

		// Project Data
		BigDecimal projectTotal = (BigDecimal) this.projectData.get(entry
				.getProjectName());
		if (projectTotal == null) {
			projectTotal = new BigDecimal(0.0);
		}
		projectTotal = projectTotal.add(entry.getTotalDuration()).setScale(1,
				BigDecimal.ROUND_HALF_EVEN);
		this.projectData.put(entry.getProjectName(), projectTotal);

		// Iteration Data
		BigDecimal iterationTotal = (BigDecimal) this.iterationData.get(entry
				.getIterationName());
		if (iterationTotal == null) {
			iterationTotal = new BigDecimal(0.0);
		}
		iterationTotal = iterationTotal.add(entry.getTotalDuration()).setScale(
				1, BigDecimal.ROUND_HALF_EVEN);
		this.iterationData.put(entry.getIterationName(), iterationTotal);

		// Iteration Data
		BigDecimal storyTotal = (BigDecimal) this.storyData.get(entry
				.getStoryName());
		if (storyTotal == null) {
			storyTotal = new BigDecimal(0.0);
		}
		storyTotal = storyTotal.add(entry.getTotalDuration()).setScale(1,
				BigDecimal.ROUND_HALF_EVEN);
		this.storyData.put(entry.getStoryName(), storyTotal);
	}

	public Hashtable getIterationData() {
		return this.iterationData;
	}

	public Hashtable getProjectData() {
		return this.projectData;
	}

	public Hashtable getStoryData() {
		return this.storyData;
	}
}