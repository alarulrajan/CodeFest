package com.technoetic.xplanner.soap.domain;

import java.util.Calendar;

import net.sf.xplanner.domain.Iteration;

public class IterationData extends DomainData {
	private int projectId;
	private String name;
	private String description;
	private Calendar startDate;
	private Calendar endDate;
	private double daysWorked;
	private double adjustedEstimatedHours = 0;
	private double remainingHours;
	private double estimatedHours;
	private double actualHours;
	private double underestimatedHours;
	private double overestimatedHours;
	private double addedHours;
	private double postponedHours;
	private String statusKey;

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

	public void setStartDate(final Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getStartDate() {
		return this.startDate;
	}

	public void setEndDate(final Calendar endDate) {
		this.endDate = endDate;
	}

	public Calendar getEndDate() {
		return this.endDate;
	}

	public void setProjectId(final int projectId) {
		this.projectId = projectId;
	}

	public int getProjectId() {
		return this.projectId;
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

	public double getUnderestimatedHours() {
		return this.underestimatedHours;
	}

	public void setUnderestimatedHours(final double underestimatedHours) {
		this.underestimatedHours = underestimatedHours;
	}

	public double getOverestimatedHours() {
		return this.overestimatedHours;
	}

	public void setOverestimatedHours(final double overestimatedHours) {
		this.overestimatedHours = overestimatedHours;
	}

	public double getAddedHours() {
		return this.addedHours;
	}

	public void setAddedHours(final double addedHours) {
		this.addedHours = addedHours;
	}

	public double getPostponedHours() {
		return this.postponedHours;
	}

	public void setPostponedHours(final double postponedHours) {
		this.postponedHours = postponedHours;
	}

	public static Class getInternalClass() {
		return Iteration.class;
	}

	public String getStatusKey() {
		return this.statusKey;
	}

	public void setStatusKey(final String statusKey) {
		this.statusKey = statusKey;
	}

	public double getDaysWorked() {
		return this.daysWorked;
	}

	public void setDaysWorked(final double daysWorked) {
		this.daysWorked = daysWorked;
	}
}