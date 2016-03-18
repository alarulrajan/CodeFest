package com.technoetic.xplanner.security.auth;

public class PermissionSetKey {
	int principalId;
	String resourceType;
	int resourceId;
	String permission;

	public PermissionSetKey(final int principalId, final String resourceType,
			final int resourceId, final String permission) {
		this.principalId = principalId;
		this.resourceType = resourceType;
		this.resourceId = resourceId;
		this.permission = permission;
	}

	public int getPrincipalId() {
		return this.principalId;
	}

	public String getResourceType() {
		return this.resourceType;
	}

	public int getResourceId() {
		return this.resourceId;
	}

	public String getPermission() {
		return this.permission;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PermissionSetKey)) {
			return false;
		}

		final PermissionSetKey permissionSetKey = (PermissionSetKey) o;

		if (this.principalId != permissionSetKey.principalId) {
			return false;
		}
		if (this.resourceId != permissionSetKey.resourceId) {
			return false;
		}
		if (this.permission != null ? !this.permission
				.equals(permissionSetKey.permission)
				: permissionSetKey.permission != null) {
			return false;
		}
		if (this.resourceType != null ? !this.resourceType
				.equals(permissionSetKey.resourceType)
				: permissionSetKey.resourceType != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		result = this.principalId;
		result = 29
				* result
				+ (this.resourceType != null ? this.resourceType.hashCode() : 0);
		result = 29 * result + this.resourceId;
		result = 29 * result
				+ (this.permission != null ? this.permission.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return this.getClass().getName()
				.substring(this.getClass().getName().lastIndexOf(".") + 1)
				+ "("
				+ this.principalId
				+ ","
				+ this.resourceType
				+ ","
				+ this.resourceId + "," + this.permission + ")";
	}
}
