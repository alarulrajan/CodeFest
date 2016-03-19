package net.sf.xplanner.domain;

// Generated 16-Nov-2009 23:34:52 by Hibernate Tools 3.2.4.GA

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * PersonRoleId generated by hbm2java.
 */
@Embeddable
public class PersonRoleId implements java.io.Serializable {

	/** The role id. */
	private int roleId;
	
	/** The person id. */
	private int personId;
	
	/** The project id. */
	private int projectId;

	/**
     * Instantiates a new person role id.
     */
	public PersonRoleId() {
	}

	/**
     * Instantiates a new person role id.
     *
     * @param roleId
     *            the role id
     * @param personId
     *            the person id
     * @param projectId
     *            the project id
     */
	public PersonRoleId(final int roleId, final int personId,
			final int projectId) {
		this.roleId = roleId;
		this.personId = personId;
		this.projectId = projectId;
	}

	/**
     * Gets the role id.
     *
     * @return the role id
     */
	@Column(name = "role_id", nullable = false)
	public int getRoleId() {
		return this.roleId;
	}

	/**
     * Sets the role id.
     *
     * @param roleId
     *            the new role id
     */
	public void setRoleId(final int roleId) {
		this.roleId = roleId;
	}

	/**
     * Gets the person id.
     *
     * @return the person id
     */
	@Column(name = "person_id", nullable = false)
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
     * Gets the project id.
     *
     * @return the project id
     */
	@Column(name = "project_id", nullable = false)
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (!(other instanceof PersonRoleId)) {
			return false;
		}
		final PersonRoleId castOther = (PersonRoleId) other;

		return this.getRoleId() == castOther.getRoleId()
				&& this.getPersonId() == castOther.getPersonId()
				&& this.getProjectId() == castOther.getProjectId();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getRoleId();
		result = 37 * result + this.getPersonId();
		result = 37 * result + this.getProjectId();
		return result;
	}

}
