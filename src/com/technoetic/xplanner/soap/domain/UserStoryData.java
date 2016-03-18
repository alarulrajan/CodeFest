package com.technoetic.xplanner.soap.domain;

import net.sf.xplanner.domain.UserStory;

public class UserStoryData extends DomainData {
	private int iterationId;
	private int trackerId;
	private String name;
	private String description;
	private double adjustedEstimatedHours = 0;
	private double estimatedOriginalHours;
	private double remainingHours;
	private double estimatedHours;
	private double actualHours;
	private double postponedHours;
	private int priority;
	private String dispositionName;
	private int customerId;
	private boolean completed;

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setTrackerId(final int trackerId) {
		this.trackerId = trackerId;
	}

	public int getTrackerId() {
		return this.trackerId;
	}

	public void setIterationId(final int iterationId) {
		this.iterationId = iterationId;
	}

	public int getIterationId() {
		return this.iterationId;
	}

	public double getEstimatedHours() {
		return this.estimatedHours;
	}

	public void setEstimatedHours(final double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	public double getActualHours() {
		return this.actualHours;
	}

	// TODO : should not be available in generated DTO. Can it be done? JM
	// 2-22-4
	public void setActualHours(final double actualHours) {
		this.actualHours = actualHours;
	}

	public double getAdjustedEstimatedHours() {
		return this.adjustedEstimatedHours;
	}

	public void setAdjustedEstimatedHours(final double adjustedEstimatedHours) {
		this.adjustedEstimatedHours = adjustedEstimatedHours;
	}

	public double getRemainingHours() {
		return this.remainingHours;
	}

	public void setRemainingHours(final double remainingHours) {
		this.remainingHours = remainingHours;
	}

	public void setCustomerId(final int customerId) {
		this.customerId = customerId;
	}

	public int getCustomerId() {
		return this.customerId;
	}

	public void setPriority(final int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return this.priority;
	}

	public String getDispositionName() {
		return this.dispositionName;
	}

	public void setDispositionName(final String dispositionName) {
		this.dispositionName = dispositionName;
	}

	public boolean isCompleted() {
		return this.completed;
	}

	public void setCompleted(final boolean completed) {
		this.completed = completed;
	}

	public double getEstimatedOriginalHours() {
		return this.estimatedOriginalHours;
	}

	public void setEstimatedOriginalHours(final double estimatedOriginalHours) {
		this.estimatedOriginalHours = estimatedOriginalHours;
	}

	public double getPostponedHours() {
		return this.postponedHours;
	}

	public void setPostponedHours(final double postponedHours) {
		this.postponedHours = postponedHours;
	}

	public static Class getInternalClass() {
		return UserStory.class;
	}
}