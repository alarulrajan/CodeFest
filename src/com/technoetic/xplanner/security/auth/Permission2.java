package com.technoetic.xplanner.security.auth;

import com.technoetic.xplanner.domain.Identifiable;

public class Permission2 implements Identifiable {
	private int id;
	private String resourceType;
	private int resourceId;
	private int principalId;
	private String name;
	private boolean positive = true;

	public Permission2() {
	}

	public Permission2(final String resourceType, final int resourceId,
			final int personId, final String permissionName) {
		this.resourceType = resourceType;
		this.resourceId = resourceId;
		this.principalId = personId;
		this.name = permissionName;
	}

	public boolean isPositive() {
		return this.positive;
	}

	public void setPositive(final boolean positive) {
		this.positive = positive;
	}

	@Override
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getPrincipalId() {
		return this.principalId;
	}

	public void setPrincipalId(final int principalId) {
		this.principalId = principalId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getResourceType() {
		return this.resourceType;
	}

	public void setResourceType(final String resourceType) {
		this.resourceType = resourceType;
	}

	public int getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(final int resourceId) {
		this.resourceId = resourceId;
	}

	@Override
	public String toString() {
		return this.getClass().getName()
				.substring(this.getClass().getName().lastIndexOf(".") + 1)
				+ "("
				+ "id="
				+ this.id
				+ ", principalId="
				+ this.principalId
				+ ", resourceType='"
				+ this.resourceType
				+ "'"
				+ ", resourceId="
				+ this.resourceId
				+ ", name='"
				+ this.name
				+ "'," + (this.positive ? "positive" : "negative") + " )";
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}

		final Permission2 that = (Permission2) o;

		if (this.id != that.id) {
			return false;
		}
		if (this.positive != that.positive) {
			return false;
		}
		if (this.principalId != that.principalId) {
			return false;
		}
		if (this.resourceId != that.resourceId) {
			return false;
		}
		if (this.name != null ? !this.name.equals(that.name)
				: that.name != null) {
			return false;
		}
		if (this.resourceType != null ? !this.resourceType
				.equals(that.resourceType) : that.resourceType != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		result = this.id;
		result = 29
				* result
				+ (this.resourceType != null ? this.resourceType.hashCode() : 0);
		result = 29 * result + this.resourceId;
		result = 29 * result + this.principalId;
		result = 29 * result + (this.name != null ? this.name.hashCode() : 0);
		result = 29 * result + (this.positive ? 1 : 0);
		return result;
	}
}
