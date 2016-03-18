package com.technoetic.xplanner.metrics;

public class DeveloperMetrics {
	private int id;
	private String name;
	private int iterationId;
	private double hours;
	private double pairedHours;
	private double acceptedTaskHours;
	private double acceptedStoryHours;
	private double ownTasksWorkedHours;

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setIterationId(final int iterationId) {
		this.iterationId = iterationId;
	}

	public int getIterationId() {
		return this.iterationId;
	}

	public void setHours(final double hours) {
		this.hours = hours;
	}

	public double getHours() {
		return this.hours;
	}

	public void setPairedHours(final double pairedHours) {
		this.pairedHours = pairedHours;
	}

	public double getPairedHours() {
		return this.pairedHours;
	}

	public double getPairedHoursPercentage() {
		if (this.hours == 0) {
			return 0;
		}
		return this.pairedHours * 100 / this.hours;
	}

	public double getUnpairedHours() {
		return this.hours - this.pairedHours;
	}

	public double getAcceptedHours() {
		return this.getAcceptedStoryHours() + this.getAcceptedTaskHours();
	}

	public double getAcceptedTaskHours() {
		return this.acceptedTaskHours;
	}

	public void setAcceptedTaskHours(final double acceptedTaskHours) {
		this.acceptedTaskHours = acceptedTaskHours;
	}

	public double getAcceptedStoryHours() {
		return this.acceptedStoryHours;
	}

	public void setAcceptedStoryHours(final double acceptedStoryHours) {
		this.acceptedStoryHours = acceptedStoryHours;
	}

	public double getOwnTaskHours() {
		return this.ownTasksWorkedHours;
	}

	public void setOwnTasksWorkedHours(final double ownTasksWorkedHours) {
		this.ownTasksWorkedHours = ownTasksWorkedHours;
	}

	public double getRemainingTaskHours() {
		return this.getAcceptedTaskHours() + this.getAcceptedStoryHours()
				- this.getOwnTaskHours();
	}
}
