package com.technoetic.xplanner.domain;

import java.io.Serializable;

/**
 * The Class RoleAssociation2.
 */
public class RoleAssociation2 implements Serializable {
	
	/** The project id. */
	private int projectId;
	
	/** The person id. */
	private int personId;
	
	/** The role id. */
	private int roleId;

	/**
     * Instantiates a new role association2.
     */
	RoleAssociation2() {
		// Hibernate
	}

	/**
     * Instantiates a new role association2.
     *
     * @param projectId
     *            the project id
     * @param personId
     *            the person id
     * @param roleId
     *            the role id
     */
	public RoleAssociation2(final int projectId, final int personId,
			final int roleId) {
		this.projectId = projectId;
		this.personId = personId;
		this.roleId = roleId;
	}

	/**
     * Gets the project id.
     *
     * @return the project id
     */
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
     * Gets the role id.
     *
     * @return the role id
     */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof RoleAssociation2)) {
			return false;
		}

		final RoleAssociation2 roleAssociation = (RoleAssociation2) o;

		if (this.personId != roleAssociation.personId) {
			return false;
		}
		if (this.projectId != roleAssociation.projectId) {
			return false;
		}
		if (this.roleId != roleAssociation.roleId) {
			return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result;
		result = this.projectId;
		result = 29 * result + this.personId;
		result = 29 * result + this.roleId;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RoleAssociation(" + "personId=" + this.getPersonId()
				+ ", projectId=" + this.getProjectId() + ", roleId="
				+ this.getRoleId() + ")";
	}
}
