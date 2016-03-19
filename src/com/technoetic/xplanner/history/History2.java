package com.technoetic.xplanner.history;

import java.util.Date;

import com.technoetic.xplanner.domain.Identifiable;

/**
 * The Class History2.
 */
public class History2 implements Identifiable {
	
	/** The Constant CREATED. */
	public static final String CREATED = "created";
	
	/** The Constant UPDATED. */
	public static final String UPDATED = "updated";
	
	/** The Constant DELETED. */
	public static final String DELETED = "deleted";
	
	/** The Constant REESTIMATED. */
	public static final String REESTIMATED = "reestimated";
	
	/** The Constant ITERATION_STARTED. */
	public static final String ITERATION_STARTED = "started";
	
	/** The Constant ITERATION_CLOSED. */
	public static final String ITERATION_CLOSED = "closed";
	
	/** The Constant MOVED. */
	public static final String MOVED = "moved";
	
	/** The Constant MOVED_IN. */
	public static final String MOVED_IN = "moved in";
	
	/** The Constant MOVED_OUT. */
	public static final String MOVED_OUT = "moved out";
	
	/** The Constant CONTINUED. */
	public static final String CONTINUED = "continued";

	/** The when. */
	private Date when;
	
	/** The target object id. */
	private int targetObjectId;
	
	/** The container id. */
	private int containerId;
	
	/** The object type. */
	private String objectType;
	
	/** The action. */
	private String action;
	
	/** The description. */
	private String description;
	
	/** The person id. */
	private int personId;
	
	/** The notified. */
	private boolean notified;
	
	/** The id. */
	private int id;

	/**
     * Instantiates a new history2.
     */
	// For hibernate
	History2() {
	}

	/**
     * Instantiates a new history2.
     *
     * @param containerId
     *            the container id
     * @param targetObjectId
     *            the target object id
     * @param objectType
     *            the object type
     * @param action
     *            the action
     * @param description
     *            the description
     * @param personId
     *            the person id
     * @param when
     *            the when
     */
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

	/**
     * Gets the when.
     *
     * @return the when
     */
	public Date getWhen() {
		return this.when;
	}

	/**
     * Sets the when.
     *
     * @param when
     *            the new when
     */
	public void setWhen(final Date when) {
		this.when = when;
	}

	/**
     * Gets the target object id.
     *
     * @return the target object id
     */
	public int getTargetObjectId() {
		return this.targetObjectId;
	}

	/**
     * Sets the target object id.
     *
     * @param targetObjectId
     *            the new target object id
     */
	public void setTargetObjectId(final int targetObjectId) {
		this.targetObjectId = targetObjectId;
	}

	/**
     * Gets the action.
     *
     * @return the action
     */
	public String getAction() {
		return this.action;
	}

	/**
     * Sets the action.
     *
     * @param action
     *            the new action
     */
	public void setAction(final String action) {
		this.action = action;
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
     * Sets the description.
     *
     * @param description
     *            the new description
     */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
     * Gets the person id.
     *
     * @return the person id
     */
	public int getPersonId() {
		return this.personId;
	}

	/**
     * Sets the person id.
     *
     * @param personId
     *            the new person id
     */
	public void setPersonId(final int personId) {
		this.personId = personId;
	}

	/**
     * Checks if is notified.
     *
     * @return true, if is notified
     */
	public boolean isNotified() {
		return this.notified;
	}

	/**
     * Sets the notified.
     *
     * @param notified
     *            the new notified
     */
	public void setNotified(final boolean notified) {
		this.notified = notified;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.Identifiable#getId()
	 */
	@Override
	public int getId() {
		return this.id;
	}

	/**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
	public void setId(final int id) {
		this.id = id;
	}

	/**
     * Gets the object type.
     *
     * @return the object type
     */
	public String getObjectType() {
		return this.objectType;
	}

	/**
     * Sets the object type.
     *
     * @param objectType
     *            the new object type
     */
	public void setObjectType(final String objectType) {
		this.objectType = objectType;
	}

	/**
     * Gets the container id.
     *
     * @return the container id
     */
	public int getContainerId() {
		return this.containerId;
	}

	/**
     * Sets the container id.
     *
     * @param containerId
     *            the new container id
     */
	public void setContainerId(final int containerId) {
		this.containerId = containerId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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
