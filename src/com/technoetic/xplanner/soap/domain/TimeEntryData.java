package com.technoetic.xplanner.soap.domain;

import java.util.Calendar;

import net.sf.xplanner.domain.TimeEntry;

/**
 * The Class TimeEntryData.
 */
public class TimeEntryData extends DomainData {
    
    /** The task id. */
    private int taskId;
    
    /** The start time. */
    private Calendar startTime;
    
    /** The end time. */
    private Calendar endTime;
    
    /** The duration. */
    private double duration;
    
    /** The person1 id. */
    private int person1Id;
    
    /** The person2 id. */
    private int person2Id;
    
    /** The report date. */
    private Calendar reportDate;
    
    /** The description. */
    private String description = "";

    /**
     * Gets the task id.
     *
     * @return the task id
     */
    public int getTaskId() {
        return this.taskId;
    }

    /**
     * Sets the task id.
     *
     * @param taskId
     *            the new task id
     */
    public void setTaskId(final int taskId) {
        this.taskId = taskId;
    }

    /**
     * Sets the start time.
     *
     * @param startTime
     *            the new start time
     */
    public void setStartTime(final Calendar startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the start time.
     *
     * @return the start time
     */
    public Calendar getStartTime() {
        return this.startTime;
    }

    /**
     * Sets the end time.
     *
     * @param endTime
     *            the new end time
     */
    public void setEndTime(final Calendar endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the end time.
     *
     * @return the end time
     */
    public Calendar getEndTime() {
        return this.endTime;
    }

    /**
     * Sets the person1 id.
     *
     * @param person1Id
     *            the new person1 id
     */
    public void setPerson1Id(final int person1Id) {
        this.person1Id = person1Id;
    }

    /**
     * Gets the person1 id.
     *
     * @return the person1 id
     */
    public int getPerson1Id() {
        return this.person1Id;
    }

    /**
     * Sets the person2 id.
     *
     * @param person2Id
     *            the new person2 id
     */
    public void setPerson2Id(final int person2Id) {
        this.person2Id = person2Id;
    }

    /**
     * Gets the person2 id.
     *
     * @return the person2 id
     */
    public int getPerson2Id() {
        return this.person2Id;
    }

    /**
     * Gets the duration.
     *
     * @return the duration
     */
    public double getDuration() {
        return this.duration;
    }

    /**
     * Sets the duration.
     *
     * @param duration
     *            the new duration
     */
    public void setDuration(final double duration) {
        this.duration = duration;
    }

    /**
     * Gets the report date.
     *
     * @return the report date
     */
    public Calendar getReportDate() {
        return this.reportDate;
    }

    /**
     * Sets the report date.
     *
     * @param reportDate
     *            the new report date
     */
    public void setReportDate(final Calendar reportDate) {
        this.reportDate = reportDate;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description.
     *
     * @param description
     *            the new description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Gets the internal class.
     *
     * @return the internal class
     */
    public static Class getInternalClass() {
        return TimeEntry.class;
    }
}
