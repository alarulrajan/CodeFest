package com.technoetic.xplanner.domain;

import com.technoetic.xplanner.XPlannerProperties;

/**
 * The Class TimeEntry2.
 */
public class TimeEntry2 extends net.sf.xplanner.domain.DomainObject {
    
    /** The task id. */
    private int taskId;
    
    /** The start time. */
    private java.util.Date startTime;
    
    /** The end time. */
    private java.util.Date endTime;
    
    /** The duration. */
    private double duration;
    
    /** The person1 id. */
    private int person1Id;
    
    /** The person2 id. */
    private int person2Id;
    
    /** The report date. */
    private java.util.Date reportDate;
    
    /** The description. */
    private String description;

    /**
     * Instantiates a new time entry2.
     */
    public TimeEntry2() {
    }

    /**
     * Instantiates a new time entry2.
     *
     * @param id
     *            the id
     */
    public TimeEntry2(final int id) {
        this.setId(id);
    }

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
    public void setStartTime(final java.util.Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the start time.
     *
     * @return the start time
     */
    public java.util.Date getStartTime() {
        return this.startTime;
    }

    /**
     * Sets the end time.
     *
     * @param endTime
     *            the new end time
     */
    public void setEndTime(final java.util.Date endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the end time.
     *
     * @return the end time
     */
    public java.util.Date getEndTime() {
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
    public java.util.Date getReportDate() {
        return this.reportDate;
    }

    /**
     * Sets the report date.
     *
     * @param reportDate
     *            the new report date
     */
    public void setReportDate(final java.util.Date reportDate) {
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
     * Checks if is currently active.
     *
     * @param personId
     *            the person id
     * @return true, if is currently active
     */
    public boolean isCurrentlyActive(final int personId) {
        return this.startTime != null && this.endTime == null
                && this.duration == 0
                && (personId == this.person1Id || personId == this.person2Id);
    }

    // DEBT: LSP violation. Should have a class of domain object that are not
    /**
     * Gets the name.
     *
     * @return the name
     */
    // Nameable
    public String getName() {
        return "";
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.domain.DomainObject#toString()
     */
    @Override
    public String toString() {
        return "TimeEntry(id=" + this.getId() + ", person1Id="
                + this.getPerson1Id() + ", person2Id=" + this.getPerson2Id()
                + ", taskId=" + this.getTaskId() + ", startTime="
                + this.getStartTime() + ", endTime=" + this.getEndTime()
                + ", description=" + this.getDescription() + ")";
    }

}