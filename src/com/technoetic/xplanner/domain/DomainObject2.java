package com.technoetic.xplanner.domain;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import net.sf.xplanner.domain.DomainObject;

/**
 * The Class DomainObject2.
 */
public abstract class DomainObject2 implements Identifiable, Nameable {
	
	/** The last update time. */
	private Date lastUpdateTime;
	
	/** The id. */
	private int id;
	
	/** The attributes. */
	private Map attributes;

	/**
     * Gets the last update time.
     *
     * @return the last update time
     */
	public Date getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	/**
     * Sets the last update time.
     *
     * @param lastUpdateTime
     *            the new last update time
     */
	public void setLastUpdateTime(final Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DomainObject)) {
			return false;
		}

		final DomainObject object = (DomainObject) o;

		if (this.id == 0) {
			return this == o;
		}
		if (this.id != object.getId()) {
			return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.Nameable#getAttribute(java.lang.String)
	 */
	@Override
	public String getAttribute(final String name) {
		this.attributes = this.getAttributes();
		if (this.attributes != null) {
			return (String) this.attributes.get(name);
		} else {
			return null;
		}
	}

	/**
     * Gets the attributes.
     *
     * @return the attributes
     */
	public Map getAttributes() {
		return this.attributes != null ? Collections
				.unmodifiableMap(this.attributes) : null;
	}

	/**
     * Sets the attributes.
     *
     * @param attributes
     *            the new attributes
     */
	protected void setAttributes(final Map attributes) {
		this.attributes = attributes;
	}

	/**
     * Gets the valid property.
     *
     * @param beanClass
     *            the bean class
     * @param property
     *            the property
     * @return the valid property
     */
	protected static String getValidProperty(final Class beanClass,
			final String property) {
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(beanClass);
		} catch (final IntrospectionException e) {
			throw new RuntimeException("could not introspect " + beanClass, e);
		}
		final PropertyDescriptor[] properties = beanInfo
				.getPropertyDescriptors();
		boolean found = false;
		for (int i = 0; i < properties.length; i++) {
			if (properties[i].getName().equals(property)) {
				found = true;
				break;
			}
		}
		if (!found) {
			throw new RuntimeException("Could not find property " + property
					+ " in " + beanClass);
		}

		return property;

	}
}