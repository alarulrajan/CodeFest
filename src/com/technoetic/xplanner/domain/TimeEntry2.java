package com.technoetic.xplanner.domain;

import com.technoetic.xplanner.XPlannerProperties;

public class TimeEntry2 extends net.sf.xplanner.domain.DomainObject {
	private int taskId;
	private java.util.Date startTime;
	private java.util.Date endTime;
	private double duration;
	private int person1Id;
	private int person2Id;
	private java.util.Date reportDate;
	private String description;

	public TimeEntry2() {
	}

	public TimeEntry2(final int id) {
		this.setId(id);
	}

	public int getTaskId() {
		return this.taskId;
	}

	public void setTaskId(final int taskId) {
		this.taskId = taskId;
	}

	public void setStartTime(final java.util.Date startTime) {
		this.startTime = startTime;
	}

	public java.util.Date getStartTime() {
		return this.startTime;
	}

	public void setEndTime(final java.util.Date endTime) {
		this.endTime = endTime;
	}

	public java.util.Date getEndTime() {
		return this.endTime;
	}

	public void setPerson1Id(final int person1Id) {
		this.person1Id = person1Id;
	}

	public int getPerson1Id() {
		return this.person1Id;
	}

	public void setPerson2Id(final int person2Id) {
		this.person2Id = person2Id;
	}

	public int getPerson2Id() {
		return this.person2Id;
	}

	public double getDuration() {
		if (this.startTime != null && this.endTime != null) {
			this.duration = (this.endTime.getTime() - this.startTime.getTime()) / 3600000.0;
		}
		return this.duration;
	}

	/**
	 * The classic way for XPlanner to calculate "effort" is in idea wall clock
	 * time or pair-programming hours. Some teams would like to do labor
	 * tracking using XPlanner and want to double the effort measured for a
	 * paired time entry.
	 * 
	 * This behavior is configurable in xplanner.properties.
	 * 
	 * @return measured effort
	 */
	public double getEffort() {
		final boolean adjustHoursForPairing = "double"
				.equalsIgnoreCase(new XPlannerProperties().getProperty(
						"xplanner.pairprogramming", "single"));
		final boolean isPairedEntry = this.person1Id != 0
				&& this.person2Id != 0;
		return adjustHoursForPairing && isPairedEntry ? this.getDuration() * 2
				: this.getDuration();
	}

	public void setDuration(final double duration) {
		this.duration = duration;
	}

	public java.util.Date getReportDate() {
		return this.reportDate;
	}

	public void setReportDate(final java.util.Date reportDate) {
		this.reportDate = reportDate;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public boolean isCurrentlyActive(final int personId) {
		return this.startTime != null && this.endTime == null
				&& this.duration == 0
				&& (personId == this.person1Id || personId == this.person2Id);
	}

	// DEBT: LSP violation. Should have a class of domain object that are not
	// Nameable
	public String getName() {
		return "";
	}

	@Override
	public String toString() {
		return "TimeEntry(id=" + this.getId() + ", person1Id="
				+ this.getPerson1Id() + ", person2Id=" + this.getPerson2Id()
				+ ", taskId=" + this.getTaskId() + ", startTime="
				+ this.getStartTime() + ", endTime=" + this.getEndTime()
				+ ", description=" + this.getDescription() + ")";
	}

}