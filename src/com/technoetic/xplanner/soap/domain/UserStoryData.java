package com.technoetic.xplanner.soap.domain;

import net.sf.xplanner.domain.UserStory;

/**
 * The Class UserStoryData.
 */
public class UserStoryData extends DomainData {
	
	/** The iteration id. */
	private int iterationId;
	
	/** The tracker id. */
	private int trackerId;
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/** The adjusted estimated hours. */
	private double adjustedEstimatedHours = 0;
	
	/** The estimated original hours. */
	private double estimatedOriginalHours;
	
	/** The remaining hours. */
	private double remainingHours;
	
	/** The estimated hours. */
	private double estimatedHours;
	
	/** The actual hours. */
	private double actualHours;
	
	/** The postponed hours. */
	private double postponedHours;
	
	/** The priority. */
	private int priority;
	
	/** The disposition name. */
	private String dispositionName;
	
	/** The customer id. */
	private int customerId;
	
	/** The completed. */
	private boolean completed;

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
     * Sets the tracker id.
     *
     * @param trackerId
     *            the new tracker id
     */
	public void setTrackerId(final int trackerId) {
		this.trackerId = trackerId;
	}

	/**
     * Gets the tracker id.
     *
     * @return the tracker id
     */
	public int getTrackerId() {
		return this.trackerId;
	}

	/**
     * Sets the iteration id.
     *
     * @param iterationId
     *            the new iteration id
     */
	public void setIterationId(final int iterationId) {
		this.iterationId = iterationId;
	}

	/**
     * Gets the iteration id.
     *
     * @return the iteration id
     */
	public int getIterationId() {
		return this.iterationId;
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

	// ChangeSoon : should not be available in generated DTO. Can it be done? JM
	/**
     * Sets the actual hours.
     *
     * @param actualHours
     *            the new actual hours
     */
	// 2-22-4
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
     * Sets the customer id.
     *
     * @param customerId
     *            the new customer id
     */
	public void setCustomerId(final int customerId) {
		this.customerId = customerId;
	}

	/**
     * Gets the customer id.
     *
     * @return the customer id
     */
	public int getCustomerId() {
		return this.customerId;
	}

	/**
     * Sets the priority.
     *
     * @param priority
     *            the new priority
     */
	public void setPriority(final int priority) {
		this.priority = priority;
	}

	/**
     * Gets the priority.
     *
     * @return the priority
     */
	public int getPriority() {
		return this.priority;
	}

	/**
     * Gets the disposition name.
     *
     * @return the disposition name
     */
	public String getDispositionName() {
		return this.dispositionName;
	}

	/**
     * Sets the disposition name.
     *
     * @param dispositionName
     *            the new disposition name
     */
	public void setDispositionName(final String dispositionName) {
		this.dispositionName = dispositionName;
	}

	/**
     * Checks if is completed.
     *
     * @return true, if is completed
     */
	public boolean isCompleted() {
		return this.completed;
	}

	/**
     * Sets the completed.
     *
     * @param completed
     *            the new completed
     */
	public void setCompleted(final boolean completed) {
		this.completed = completed;
	}

	/**
     * Gets the estimated original hours.
     *
     * @return the estimated original hours
     */
	public double getEstimatedOriginalHours() {
		return this.estimatedOriginalHours;
	}

	/**
     * Sets the estimated original hours.
     *
     * @param estimatedOriginalHours
     *            the new estimated original hours
     */
	public void setEstimatedOriginalHours(final double estimatedOriginalHours) {
		this.estimatedOriginalHours = estimatedOriginalHours;
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
		return UserStory.class;
	}
}