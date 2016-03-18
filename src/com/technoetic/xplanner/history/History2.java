package com.technoetic.xplanner.history;

import java.util.Date;

import com.technoetic.xplanner.domain.Identifiable;

public class History2 implements Identifiable {
	public static final String CREATED = "created";
	public static final String UPDATED = "updated";
	public static final String DELETED = "deleted";
	public static final String REESTIMATED = "reestimated";
	public static final String ITERATION_STARTED = "started";
	public static final String ITERATION_CLOSED = "closed";
	public static final String MOVED = "moved";
	public static final String MOVED_IN = "moved in";
	public static final String MOVED_OUT = "moved out";
	public static final String CONTINUED = "continued";

	private Date when;
	private int targetObjectId;
	private int containerId;
	private String objectType;
	private String action;
	private String description;
	private int personId;
	private boolean notified;
	private int id;

	// For hibernate
	History2() {
	}

	public History2(final int containerId, final int targetObjectId,
			final String objectType, final String action,
			final String description, final int personId, final Date when) {
		this.containerId = containerId;
		this.objectType = objectType;
		this.when = when;
		this.targetObjectId = targetObjectId;
		this.action = action;
		this.description = description;
		this.personId = personId;
	}

	public Date getWhen() {
		return this.when;
	}

	public void setWhen(final Date when) {
		this.when = when;
	}

	public int getTargetObjectId() {
		return this.targetObjectId;
	}

	public void setTargetObjectId(final int targetObjectId) {
		this.targetObjectId = targetObjectId;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(final String action) {
		this.action = action;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public int getPersonId() {
		return this.personId;
	}

	public void setPersonId(final int personId) {
		this.personId = personId;
	}

	public boolean isNotified() {
		return this.notified;
	}

	public void setNotified(final boolean notified) {
		this.notified = notified;
	}

	@Override
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getObjectType() {
		return this.objectType;
	}

	public void setObjectType(final String objectType) {
		this.objectType = objectType;
	}

	public int getContainerId() {
		return this.containerId;
	}

	public void setContainerId(final int containerId) {
		this.containerId = containerId;
	}

	@Override
	public String toString() {
		return "History(id=" + this.getId() + ", personId="
				+ this.getPersonId() + ", targetObjectId="
				+ this.getTargetObjectId() + ", objectType=" + this.objectType
				+ ", action=" + this.getAction() + ", containerId="
				+ this.getContainerId() + ", desc.='" + this.getDescription()
				+ '\'' + ", date=" + this.getWhen() + ", notified="
				+ this.notified + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}

		final History2 event = (History2) o;

		if (this.containerId != event.containerId) {
			return false;
		}
		if (this.id != event.id) {
			return false;
		}
		if (this.notified != event.notified) {
			return false;
		}
		if (this.personId != event.personId) {
			return false;
		}
		if (this.targetObjectId != event.targetObjectId) {
			return false;
		}
		if (this.action != null ? !this.action.equals(event.action)
				: event.action != null) {
			return false;
		}
		if (this.description != null ? !this.description
				.equals(event.description) : event.description != null) {
			return false;
		}
		if (this.objectType != null ? !this.objectType.equals(event.objectType)
				: event.objectType != null) {
			return false;
		}
		return !(this.when != null ? !this.when.equals(event.when)
				: event.when != null);

	}

	@Override
	public int hashCode() {
		int result;
		result = this.when != null ? this.when.hashCode() : 0;
		result = 29 * result + this.targetObjectId;
		result = 29 * result + this.containerId;
		result = 29 * result
				+ (this.objectType != null ? this.objectType.hashCode() : 0);
		result = 29 * result
				+ (this.action != null ? this.action.hashCode() : 0);
		result = 29 * result
				+ (this.description != null ? this.description.hashCode() : 0);
		result = 29 * result + this.personId;
		result = 29 * result + (this.notified ? 1 : 0);
		result = 29 * result + this.id;
		return result;
	}
}
