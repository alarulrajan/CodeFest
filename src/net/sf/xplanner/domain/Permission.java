package net.sf.xplanner.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * XplannerPlus, agile planning software.
 *
 * @author Maksym_Chyrkov. Copyright (C) 2009 Maksym Chyrkov This program is
 *         free software: you can redistribute it and/or modify it under the
 *         terms of the GNU General Public License as published by the Free
 *         Software Foundation, either version 3 of the License, or (at your
 *         option) any later version.
 * 
 *         This program is distributed in the hope that it will be useful, but
 *         WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with this program. If not, see <http://www.gnu.org/licenses/>
 */

@Entity
@Table(name = "permission")
public class Permission implements java.io.Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 598781035652662657L;
	
	/** The id. */
	private int id;
	
	/** The principal. */
	private Integer principal;
	
	/** The name. */
	private String name;
	
	/** The resource type. */
	private String resourceType;
	
	/** The resource id. */
	private Integer resourceId;
	
	/** The positive. */
	private boolean positive = true;

	/**
     * Instantiates a new permission.
     */
	public Permission() {
	}

	/**
     * Instantiates a new permission.
     *
     * @param id
     *            the id
     * @param positive
     *            the positive
     */
	public Permission(final int id, final boolean positive) {
		this.id = id;
		this.positive = positive;
	}

	/**
     * Instantiates a new permission.
     *
     * @param resourceType
     *            the resource type
     * @param resourceId
     *            the resource id
     * @param principal
     *            the principal
     * @param name
     *            the name
     */
	public Permission(final String resourceType, final Integer resourceId,
			final Integer principal, final String name) {
		this.principal = principal;
		this.name = name;
		this.resourceType = resourceType;
		this.resourceId = resourceId;
	}

	/**
     * Gets the id.
     *
     * @return the id
     */
	@Id
	@GeneratedValue(generator = "commonId")
	@GenericGenerator(name = "commonId", strategy = "com.technoetic.xplanner.db.hibernate.HibernateIdentityGenerator")
	@Column(name = "id", unique = true, nullable = false)
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
     * Gets the principal.
     *
     * @return the principal
     */
	@Column(name = "principal")
	public Integer getPrincipal() {
		return this.principal;
	}

	/**
     * Sets the principal.
     *
     * @param principal
     *            the new principal
     */
	public void setPrincipal(final Integer principal) {
		this.principal = principal;
	}

	/**
     * Gets the name.
     *
     * @return the name
     */
	@Column(name = "name")
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
	@Column(name = "resource_type")
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
	@Column(name = "resource_id")
	public Integer getResourceId() {
		return this.resourceId;
	}

	/**
     * Sets the resource id.
     *
     * @param resourceId
     *            the new resource id
     */
	public void setResourceId(final Integer resourceId) {
		this.resourceId = resourceId;
	}

	/**
     * Checks if is positive.
     *
     * @return true, if is positive
     */
	@Column(name = "positive", nullable = false)
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
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.id;
		result = prime * result
				+ (this.name == null ? 0 : this.name.hashCode());
		result = prime * result + (this.positive ? 1231 : 1237);
		result = prime * result
				+ (this.principal == null ? 0 : this.principal.hashCode());
		result = prime * result
				+ (this.resourceId == null ? 0 : this.resourceId.hashCode());
		result = prime
				* result
				+ (this.resourceType == null ? 0 : this.resourceType.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final Permission other = (Permission) obj;
		if (this.id != other.id) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.positive != other.positive) {
			return false;
		}
		if (this.principal == null) {
			if (other.principal != null) {
				return false;
			}
		} else if (!this.principal.equals(other.principal)) {
			return false;
		}
		if (this.resourceId == null) {
			if (other.resourceId != null) {
				return false;
			}
		} else if (!this.resourceId.equals(other.resourceId)) {
			return false;
		}
		if (this.resourceType == null) {
			if (other.resourceType != null) {
				return false;
			}
		} else if (!this.resourceType.equals(other.resourceType)) {
			return false;
		}
		return true;
	}

}
