package net.sf.xplanner.domain.view;

import java.util.Date;

import net.sf.xplanner.domain.NamedObject;

public class TaskView extends NamedObject {
	private String type;
	private boolean completed;
	private double originalEstimate;
	private double estimatedHours;
	private Date createdDate;
	private int acceptorId;

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public boolean isCompleted() {
		return this.completed;
	}

	public void setCompleted(final boolean completed) {
		this.completed = completed;
	}

	public double getOriginalEstimate() {
		return this.originalEstimate;
	}

	public void setOriginalEstimate(final double originalEstimate) {
		this.originalEstimate = originalEstimate;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(final Date createdDate) {
		this.createdDate = createdDate;
	}

	public double getEstimatedHours() {
		return this.estimatedHours;
	}

	public void setEstimatedHours(final double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	public int getAcceptorId() {
		return this.acceptorId;
	}

	public void setAcceptorId(final int acceptorId) {
		this.acceptorId = acceptorId;
	}
}
