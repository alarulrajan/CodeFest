package com.technoetic.xplanner.soap.domain;

import java.util.Calendar;

import net.sf.xplanner.domain.Iteration;

/**
 * The Class IterationData.
 */
public class IterationData extends DomainData {
    
    /** The project id. */
    private int projectId;
    
    /** The name. */
    private String name;
    
    /** The description. */
    private String description;
    
    /** The start date. */
    private Calendar startDate;
    
    /** The end date. */
    private Calendar endDate;
    
    /** The days worked. */
    private double daysWorked;
    
    /** The adjusted estimated hours. */
    private double adjustedEstimatedHours = 0;
    
    /** The remaining hours. */
    private double remainingHours;
    
    /** The estimated hours. */
    private double estimatedHours;
    
    /** The actual hours. */
    private double actualHours;
    
    /** The underestimated hours. */
    private double underestimatedHours;
    
    /** The overestimated hours. */
    private double overestimatedHours;
    
    /** The added hours. */
    private double addedHours;
    
    /** The postponed hours. */
    private double postponedHours;
    
    /** The status key. */
    private String statusKey;

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
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
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the start date.
     *
     * @param startDate
     *            the new start date
     */
    public void setStartDate(final Calendar startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the start date.
     *
     * @return the start date
     */
    public Calendar getStartDate() {
        return this.startDate;
    }

    /**
     * Sets the end date.
     *
     * @param endDate
     *            the new end date
     */
    public void setEndDate(final Calendar endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the end date.
     *
     * @return the end date
     */
    public Calendar getEndDate() {
        return this.endDate;
    }

    /**
     * Sets the project id.
     *
     * @param projectId
     *            the new project id
     */
    public void setProjectId(final int projectId) {
        this.projectId = projectId;
    }

    /**
     * Gets the project id.
     *
     * @return the project id
     */
    public int getProjectId() {
        return this.projectId;
    }

    /**
     * Gets the estimated hours.
     *
     * @return the estimated hours
     */
    public double getEstimatedHours() {
        return this.estimatedHours;
    }

    /**
     * Sets the estimated hours.
     *
     * @param estimatedHours
     *            the new estimated hours
     */
    public void setEstimatedHours(final double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    /**
     * Gets the actual hours.
     *
     * @return the actual hours
     */
    public double getActualHours() {
        return this.actualHours;
    }

    /**
     * Sets the actual hours.
     *
     * @param actualHours
     *            the new actual hours
     */
    public void setActualHours(final double actualHours) {
        this.actualHours = actualHours;
    }

    /**
     * Gets the adjusted estimated hours.
     *
     * @return the adjusted estimated hours
     */
    public double getAdjustedEstimatedHours() {
        return this.adjustedEstimatedHours;
    }

    /**
     * Sets the adjusted estimated hours.
     *
     * @param adjustedEstimatedHours
     *            the new adjusted estimated hours
     */
    public void setAdjustedEstimatedHours(final double adjustedEstimatedHours) {
        this.adjustedEstimatedHours = adjustedEstimatedHours;
    }

    /**
     * Gets the remaining hours.
     *
     * @return the remaining hours
     */
    public double getRemainingHours() {
        return this.remainingHours;
    }

    /**
     * Sets the remaining hours.
     *
     * @param remainingHours
     *            the new remaining hours
     */
    public void setRemainingHours(final double remainingHours) {
        this.remainingHours = remainingHours;
    }

    /**
     * Gets the underestimated hours.
     *
     * @return the underestimated hours
     */
    public double getUnderestimatedHours() {
        return this.underestimatedHours;
    }

    /**
     * Sets the underestimated hours.
     *
     * @param underestimatedHours
     *            the new underestimated hours
     */
    public void setUnderestimatedHours(final double underestimatedHours) {
        this.underestimatedHours = underestimatedHours;
    }

    /**
     * Gets the overestimated hours.
     *
     * @return the overestimated hours
     */
    public double getOverestimatedHours() {
        return this.overestimatedHours;
    }

    /**
     * Sets the overestimated hours.
     *
     * @param overestimatedHours
     *            the new overestimated hours
     */
    public void setOverestimatedHours(final double overestimatedHours) {
        this.overestimatedHours = overestimatedHours;
    }

    /**
     * Gets the added hours.
     *
     * @return the added hours
     */
    public double getAddedHours() {
        return this.addedHours;
    }

    /**
     * Sets the added hours.
     *
     * @param addedHours
     *            the new added hours
     */
    public void setAddedHours(final double addedHours) {
        this.addedHours = addedHours;
    }

    /**
     * Gets the postponed hours.
     *
     * @return the postponed hours
     */
    public double getPostponedHours() {
        return this.postponedHours;
    }

    /**
     * Sets the postponed hours.
     *
     * @param postponedHours
     *            the new postponed hours
     */
    public void setPostponedHours(final double postponedHours) {
        this.postponedHours = postponedHours;
    }

    /**
     * Gets the internal class.
     *
     * @return the internal class
     */
    public static Class getInternalClass() {
        return Iteration.class;
    }

    /**
     * Gets the status key.
     *
     * @return the status key
     */
    public String getStatusKey() {
        return this.statusKey;
    }

    /**
     * Sets the status key.
     *
     * @param statusKey
     *            the new status key
     */
    public void setStatusKey(final String statusKey) {
        this.statusKey = statusKey;
    }

    /**
     * Gets the days worked.
     *
     * @return the days worked
     */
    public double getDaysWorked() {
        return this.daysWorked;
    }

    /**
     * Sets the days worked.
     *
     * @param daysWorked
     *            the new days worked
     */
    public void setDaysWorked(final double daysWorked) {
        this.daysWorked = daysWorked;
    }
}