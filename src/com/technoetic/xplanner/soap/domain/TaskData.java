package com.technoetic.xplanner.soap.domain;

import java.util.Calendar;

import net.sf.xplanner.domain.Task;

public class TaskData extends DomainData {
	private int storyId;
	private int acceptorId;
	private Calendar createdDate;
	private String name;
	private String description;
	private boolean completionFlag;
	private String type;
	private String dispositionName;
	private double estimatedOriginalHours;
	private double adjustedEstimatedHours;
	private double estimatedHours;
	private double actualHours;
	private double remainingHours;

	public void setStoryId(final int storyId) {
		this.storyId = storyId;
	}

	public int getStoryId() {
		return this.storyId;
	}

	public void setAcceptorId(final int acceptorId) {
		this.acceptorId = acceptorId;
	}

	public int getAcceptorId() {
		return this.acceptorId;
	}

	public void setCreatedDate(final Calendar createdDate) {
		this.createdDate = createdDate;
	}

	public Calendar getCreatedDate() {
		return this.createdDate;
	}

	public void setEstimatedHours(final double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	public double getEstimatedHours() {
		return this.estimatedHours;
	}

	public double getRemainingHours() {
		return this.remainingHours;
	}

	public void setRemainingHours(final double remainingHours) {
		this.remainingHours = remainingHours;
	}

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

	public boolean isCompleted() {
		return this.completionFlag;
	}

	public void setCompleted(final boolean completionFlag) {
		this.completionFlag = completionFlag;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setDispositionName(final String dispositionName) {
		this.dispositionName = dispositionName;
	}

	public String getDispositionName() {
		return this.dispositionName;
	}

	public double getActualHours() {
		return this.actualHours;
	}

	public void setActualHours(final double actualHours) {
		this.actualHours = actualHours;
	}

	public double getAdjustedEstimatedHours() {
		return this.adjustedEstimatedHours;
	}

	public void setAdjustedEstimatedHours(final double adjustedEstimatedHours) {
		this.adjustedEstimatedHours = adjustedEstimatedHours;
	}

	public double getEstimatedOriginalHours() {
		return this.estimatedOriginalHours;
	}

	public void setEstimatedOriginalHours(final double estimatedOriginalHours) {
		this.estimatedOriginalHours = estimatedOriginalHours;
	}

	public static Class getInternalClass() {
		return Task.class;
	}
}