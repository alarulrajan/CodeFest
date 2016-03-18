package com.technoetic.xplanner.domain;

import java.util.Date;

import net.sf.xplanner.domain.DomainObject;

public class Integration extends DomainObject {
	public static final char PENDING = 'P';
	public static final char ACTIVE = 'A';
	public static final char FINISHED = 'F';
	public static final char CANCELED = 'X';

	private int personId;
	private char state;
	private String comment;
	private Date whenStarted;
	private Date whenRequested;
	private Date whenComplete;
	private int projectId;

	public int getPersonId() {
		return this.personId;
	}

	public void setPersonId(final int personId) {
		this.personId = personId;
	}

	public void setState(final char state) {
		this.state = state;
	}

	public char getState() {
		return this.state;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return this.comment;
	}

	public String getDescription() {
		return this.getComment();
	}

	public void setWhenStarted(final Date whenStarted) {
		this.whenStarted = whenStarted;
	}

	public Date getWhenStarted() {
		return this.whenStarted;
	}

	public void setWhenRequested(final Date whenRequested) {
		this.whenRequested = whenRequested;
	}

	public Date getWhenRequested() {
		return this.whenRequested;
	}

	public Date getWhenComplete() {
		return this.whenComplete;
	}

	public void setWhenComplete(final Date whenComplete) {
		this.whenComplete = whenComplete;
	}

	public double getDuration() {
		if (this.whenStarted != null && this.whenComplete != null) {
			final long delta = this.whenComplete.getTime()
					- this.whenStarted.getTime();
			return delta / 3600000.0;
		} else {
			return 0;
		}
	}

	public int getProjectId() {
		return this.projectId;
	}

	public void setProjectId(final int projectId) {
		this.projectId = projectId;
	}

	// DEBT: LSP violation. Should have a class of domain object that are not
	// Nameable
	public String getName() {
		return "";
	}

	@Override
	public String toString() {
		return "Integration(id=" + this.getId() + ", personId="
				+ this.getPersonId() + ", projectId=" + this.getProjectId()
				+ ", comment=" + this.getComment() + ", lastUpdateTime="
				+ this.getLastUpdateTime() + ", whenComplete="
				+ this.getWhenComplete() + ", whenStarted="
				+ this.getWhenStarted() + ")";
	}
}