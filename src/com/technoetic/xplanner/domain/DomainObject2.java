package com.technoetic.xplanner.domain;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import net.sf.xplanner.domain.DomainObject;

public abstract class DomainObject2 implements Identifiable, Nameable {
	private Date lastUpdateTime;
	private int id;
	private Map attributes;

	public Date getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(final Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

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

	@Override
	public int hashCode() {
		return this.id;
	}

	@Override
	public String getAttribute(final String name) {
		this.attributes = this.getAttributes();
		if (this.attributes != null) {
			return (String) this.attributes.get(name);
		} else {
			return null;
		}
	}

	public Map getAttributes() {
		return this.attributes != null ? Collections
				.unmodifiableMap(this.attributes) : null;
	}

	protected void setAttributes(final Map attributes) {
		this.attributes = attributes;
	}

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