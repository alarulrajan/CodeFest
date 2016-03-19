package com.technoetic.xplanner.security.auth;

/**
 * The Class PermissionSetKey.
 */
public class PermissionSetKey {
    
    /** The principal id. */
    int principalId;
    
    /** The resource type. */
    String resourceType;
    
    /** The resource id. */
    int resourceId;
    
    /** The permission. */
    String permission;

    /**
     * Instantiates a new permission set key.
     *
     * @param principalId
     *            the principal id
     * @param resourceType
     *            the resource type
     * @param resourceId
     *            the resource id
     * @param permission
     *            the permission
     */
    public PermissionSetKey(final int principalId, final String resourceType,
            final int resourceId, final String permission) {
        this.principalId = principalId;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.permission = permission;
    }

    /**
     * Gets the principal id.
     *
     * @return the principal id
     */
    public int getPrincipalId() {
        return this.principalId;
    }

    /**
     * Gets the resource type.
     *
     * @return the resource type
     */
    public String getResourceType() {
        return this.resourceType;
    }

    /**
     * Gets the resource id.
     *
     * @return the resource id
     */
    public int getResourceId() {
        return this.resourceId;
    }

    /**
     * Gets the permission.
     *
     * @return the permission
     */
    public String getPermission() {
        return this.permission;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
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
