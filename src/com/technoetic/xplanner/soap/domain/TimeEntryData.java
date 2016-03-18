package com.technoetic.xplanner.soap.domain;

import java.util.Calendar;

import net.sf.xplanner.domain.TimeEntry;

public class TimeEntryData extends DomainData {
	private int taskId;
	private Calendar startTime;
	private Calendar endTime;
	private double duration;
	private int person1Id;
	private int person2Id;
	private Calendar reportDate;
	private String description = "";

	public int getTaskId() {
		return this.taskId;
	}

	public void setTaskId(final int taskId) {
		this.taskId = taskId;
	}

	public void setStartTime(final Calendar startTime) {
		this.startTime = startTime;
	}

	public Calendar getStartTime() {
		return this.startTime;
	}

	public void setEndTime(final Calendar endTime) {
		this.endTime = endTime;
	}

	public Calendar getEndTime() {
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
		return this.duration;
	}

	public void setDuration(final double duration) {
		this.duration = duration;
	}

	public Calendar getReportDate() {
		return this.reportDate;
	}

	public void setReportDate(final Calendar reportDate) {
		this.reportDate = reportDate;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public static Class getInternalClass() {
		return TimeEntry.class;
	}
}
