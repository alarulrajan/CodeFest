package com.technoetic.xplanner.domain.virtual;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.TreeMap;

/**
 * The Class Timesheet.
 */
public class Timesheet implements Serializable {

	/** The entries. */
	private final ArrayList entries = new ArrayList();
	
	/** The daily entries. */
	private final TreeMap dailyEntries = new TreeMap();
	
	/** The person name. */
	private String personName;
	
	/** The total. */
	private BigDecimal total = new BigDecimal(0.0);
	
	/** The project data. */
	private final Hashtable projectData = new Hashtable();
	
	/** The iteration data. */
	private final Hashtable iterationData = new Hashtable();
	
	/** The story data. */
	private final Hashtable storyData = new Hashtable();

	/**
     * Instantiates a new timesheet.
     */
	public Timesheet() {
	}

	/**
     * Instantiates a new timesheet.
     *
     * @param startDate
     *            the start date
     * @param endDate
     *            the end date
     */
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

	/**
     * Gets the person name.
     *
     * @return the person name
     */
	public String getPersonName() {
		return this.personName;
	}

	/**
     * Sets the person name.
     *
     * @param personName
     *            the new person name
     */
	public void setPersonName(final String personName) {
		this.personName = personName;
	}

	/**
     * Gets the entries.
     *
     * @return the entries
     */
	public Collection getEntries() {
		return this.entries;
	}

	/**
     * Gets the daily entries.
     *
     * @return the daily entries
     */
	public Collection getDailyEntries() {
		return this.dailyEntries.values();
	}

	/**
     * Adds the entry.
     *
     * @param entry
     *            the entry
     */
	public void addEntry(final TimesheetEntry entry) {
		this.total = this.total.add(entry.getTotalDuration());
		this.personName = entry.getPersonName();
		this.entries.add(entry);
		this.updateGroupedData(entry);
	}

	/**
     * Adds the daily entry.
     *
     * @param entry
     *            the entry
     */
	public void addDailyEntry(final DailyTimesheetEntry entry) {
		final DailyTimesheetEntry dailyEntry = (DailyTimesheetEntry) this.dailyEntries
				.get(entry.getEntryDate());
		dailyEntry.setTotalDuration(dailyEntry.getTotalDuration().add(
				entry.getTotalDuration()));

		this.dailyEntries.put(dailyEntry.getEntryDate(), dailyEntry);
	}

	/**
     * Gets the total.
     *
     * @return the total
     */
	public BigDecimal getTotal() {
		return this.total.setScale(1, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
     * Update grouped data.
     *
     * @param entry
     *            the entry
     */
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

	/**
     * Gets the iteration data.
     *
     * @return the iteration data
     */
	public Hashtable getIterationData() {
		return this.iterationData;
	}

	/**
     * Gets the project data.
     *
     * @return the project data
     */
	public Hashtable getProjectData() {
		return this.projectData;
	}

	/**
     * Gets the story data.
     *
     * @return the story data
     */
	public Hashtable getStoryData() {
		return this.storyData;
	}
}