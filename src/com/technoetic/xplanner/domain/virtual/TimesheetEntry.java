package com.technoetic.xplanner.domain.virtual;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The Class TimesheetEntry.
 */
public class TimesheetEntry implements Serializable {

	/** The project name. */
	private String projectName;
	
	/** The iteration name. */
	private String iterationName;
	
	/** The story name. */
	private String storyName;
	
	/** The total duration. */
	private BigDecimal totalDuration = new BigDecimal(0.0);
	
	/** The person name. */
	private String personName;
	
	/** The project id. */
	private int projectId;
	
	/** The iteration id. */
	private int iterationId;
	
	/** The story id. */
	private int storyId;

	/**
     * Instantiates a new timesheet entry.
     */
	public TimesheetEntry() {
	}

	/**
     * Gets the project name.
     *
     * @return the project name
     */
	public String getProjectName() {
		return this.projectName;
	}

	/**
     * Sets the project name.
     *
     * @param projectName
     *            the new project name
     */
	public void setProjectName(final String projectName) {
		this.projectName = projectName;
	}

	/**
     * Gets the iteration name.
     *
     * @return the iteration name
     */
	public String getIterationName() {
		return this.iterationName;
	}

	/**
     * Sets the iteration name.
     *
     * @param iterationName
     *            the new iteration name
     */
	public void setIterationName(final String iterationName) {
		this.iterationName = iterationName;
	}

	/**
     * Gets the story name.
     *
     * @return the story name
     */
	public String getStoryName() {
		return this.storyName;
	}

	/**
     * Sets the story name.
     *
     * @param storyName
     *            the new story name
     */
	public void setStoryName(final String storyName) {
		this.storyName = storyName;
	}

	/**
     * Gets the total duration.
     *
     * @return the total duration
     */
	public BigDecimal getTotalDuration() {
		return this.totalDuration.setScale(1, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
     * Sets the total duration.
     *
     * @param totalDuration
     *            the new total duration
     */
	public void setTotalDuration(final BigDecimal totalDuration) {
		this.totalDuration = totalDuration;
	}

	/**
     * Sets the total duration.
     *
     * @param totalDuration
     *            the new total duration
     */
	public void setTotalDuration(final double totalDuration) {
		this.totalDuration = this.totalDuration.add(new BigDecimal(
				totalDuration));
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
     * Gets the project id.
     *
     * @return the project id
     */
	public int getProjectId() {
		return this.projectId;
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
     * Gets the iteration id.
     *
     * @return the iteration id
     */
	public int getIterationId() {
		return this.iterationId;
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
     * Gets the story id.
     *
     * @return the story id
     */
	public int getStoryId() {
		return this.storyId;
	}

	/**
     * Sets the story id.
     *
     * @param storyId
     *            the new story id
     */
	public void setStoryId(final int storyId) {
		this.storyId = storyId;
	}

}