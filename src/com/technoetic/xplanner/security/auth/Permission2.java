package com.technoetic.xplanner.security.auth;

import com.technoetic.xplanner.domain.Identifiable;

/**
 * The Class Permission2.
 */
public class Permission2 implements Identifiable {
    
    /** The id. */
    private int id;
    
    /** The resource type. */
    private String resourceType;
    
    /** The resource id. */
    private int resourceId;
    
    /** The principal id. */
    private int principalId;
    
    /** The name. */
    private String name;
    
    /** The positive. */
    private boolean positive = true;

    /**
     * Instantiates a new permission2.
     */
    public Permission2() {
    }

    /**
     * Instantiates a new permission2.
     *
     * @param resourceType
     *            the resource type
     * @param resourceId
     *            the resource id
     * @param personId
     *            the person id
     * @param permissionName
     *            the permission name
     */
    public Permission2(final String resourceType, final int resourceId,
            final int personId, final String permissionName) {
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.principalId = personId;
        this.name = permissionName;
    }

    /**
     * Checks if is positive.
     *
     * @return true, if is positive
     */
    public boolean isPositive() {
        return this.positive;
    }

    /**
     * Sets the positive.
     *
     * @param positive
     *            the new positive
     */
    public void setPositive(final boolean positive) {
        this.positive = positive;
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
     * Gets the principal id.
     *
     * @return the principal id
     */
    public int getPrincipalId() {
        return this.principalId;
    }

    /**
     * Sets the principal id.
     *
     * @param principalId
     *            the new principal id
     */
    public void setPrincipalId(final int principalId) {
        this.principalId = principalId;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        this.name = name;
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
     * Sets the resource type.
     *
     * @param resourceType
     *            the new resource type
     */
    public void setResourceType(final String resourceType) {
        this.resourceType = resourceType;
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
     * Sets the resource id.
     *
     * @param resourceId
     *            the new resource id
     */
    public void setResourceId(final int resourceId) {
        this.resourceId = resourceId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
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
