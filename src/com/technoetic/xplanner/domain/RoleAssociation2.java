package com.technoetic.xplanner.domain;

import java.io.Serializable;

public class RoleAssociation2 implements Serializable {
	private int projectId;
	private int personId;
	private int roleId;

	RoleAssociation2() {
		// Hibernate
	}

	public RoleAssociation2(final int projectId, final int personId,
			final int roleId) {
		this.projectId = projectId;
		this.personId = personId;
		this.roleId = roleId;
	}

	public int getProjectId() {
		return this.projectId;
	}

	public void setProjectId(final int projectId) {
		this.projectId = projectId;
	}

	public int getPersonId() {
		return this.personId;
	}

	public void setPersonId(final int personId) {
		this.personId = personId;
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(final int roleId) {
		this.roleId = roleId;
	}

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

	@Override
	public int hashCode() {
		int result;
		result = this.projectId;
		result = 29 * result + this.personId;
		result = 29 * result + this.roleId;
		return result;
	}

	@Override
	public String toString() {
		return "RoleAssociation(" + "personId=" + this.getPersonId()
				+ ", projectId=" + this.getProjectId() + ", roleId="
				+ this.getRoleId() + ")";
	}
}
