package com.technoetic.xplanner.domain.virtual;

import java.io.Serializable;
import java.math.BigDecimal;

public class TimesheetEntry implements Serializable {

	private String projectName;
	private String iterationName;
	private String storyName;
	private BigDecimal totalDuration = new BigDecimal(0.0);
	private String personName;
	private int projectId;
	private int iterationId;
	private int storyId;

	public TimesheetEntry() {
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(final String projectName) {
		this.projectName = projectName;
	}

	public String getIterationName() {
		return this.iterationName;
	}

	public void setIterationName(final String iterationName) {
		this.iterationName = iterationName;
	}

	public String getStoryName() {
		return this.storyName;
	}

	public void setStoryName(final String storyName) {
		this.storyName = storyName;
	}

	public BigDecimal getTotalDuration() {
		return this.totalDuration.setScale(1, BigDecimal.ROUND_HALF_EVEN);
	}

	public void setTotalDuration(final BigDecimal totalDuration) {
		this.totalDuration = totalDuration;
	}

	public void setTotalDuration(final double totalDuration) {
		this.totalDuration = this.totalDuration.add(new BigDecimal(
				totalDuration));
	}

	public String getPersonName() {
		return this.personName;
	}

	public void setPersonName(final String personName) {
		this.personName = personName;
	}

	public int getProjectId() {
		return this.projectId;
	}

	public void setProjectId(final int projectId) {
		this.projectId = projectId;
	}

	public int getIterationId() {
		return this.iterationId;
	}

	public void setIterationId(final int iterationId) {
		this.iterationId = iterationId;
	}

	public int getStoryId() {
		return this.storyId;
	}

	public void setStoryId(final int storyId) {
		this.storyId = storyId;
	}

}