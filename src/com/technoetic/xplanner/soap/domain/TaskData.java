package com.technoetic.xplanner.soap.domain;

import java.util.Calendar;

import net.sf.xplanner.domain.Task;

/**
 * The Class TaskData.
 */
public class TaskData extends DomainData {
	
	/** The story id. */
	private int storyId;
	
	/** The acceptor id. */
	private int acceptorId;
	
	/** The created date. */
	private Calendar createdDate;
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/** The completion flag. */
	private boolean completionFlag;
	
	/** The type. */
	private String type;
	
	/** The disposition name. */
	private String dispositionName;
	
	/** The estimated original hours. */
	private double estimatedOriginalHours;
	
	/** The adjusted estimated hours. */
	private double adjustedEstimatedHours;
	
	/** The estimated hours. */
	private double estimatedHours;
	
	/** The actual hours. */
	private double actualHours;
	
	/** The remaining hours. */
	private double remainingHours;

	/**
     * Sets the story id.
     *
     * @param storyId
     *            the new story id
     */
	public void setStoryId(final int storyId) {
		this.storyId = storyId;
	}

	/**
     * Gets the story id.
     *
     * @return the story id
     */
	public int getStoryId() {
		return this.storyId;
	}

	/**
     * Sets the acceptor id.
     *
     * @param acceptorId
     *            the new acceptor id
     */
	public void setAcceptorId(final int acceptorId) {
		this.acceptorId = acceptorId;
	}

	/**
     * Gets the acceptor id.
     *
     * @return the acceptor id
     */
	public int getAcceptorId() {
		return this.acceptorId;
	}

	/**
     * Sets the created date.
     *
     * @param createdDate
     *            the new created date
     */
	public void setCreatedDate(final Calendar createdDate) {
		this.createdDate = createdDate;
	}

	/**
     * Gets the created date.
     *
     * @return the created date
     */
	public Calendar getCreatedDate() {
		return this.createdDate;
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
     * Gets the estimated hours.
     *
     * @return the estimated hours
     */
	public double getEstimatedHours() {
		return this.estimatedHours;
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
     * Checks if is completed.
     *
     * @return true, if is completed
     */
	public boolean isCompleted() {
		return this.completionFlag;
	}

	/**
     * Sets the completed.
     *
     * @param completionFlag
     *            the new completed
     */
	public void setCompleted(final boolean completionFlag) {
		this.completionFlag = completionFlag;
	}

	/**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
	public void setType(final String type) {
		this.type = type;
	}

	/**
     * Gets the type.
     *
     * @return the type
     */
	public String getType() {
		return this.type;
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
     * Gets the disposition name.
     *
     * @return the disposition name
     */
	public String getDispositionName() {
		return this.dispositionName;
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
     * Gets the internal class.
     *
     * @return the internal class
     */
	public static Class getInternalClass() {
		return Task.class;
	}
}