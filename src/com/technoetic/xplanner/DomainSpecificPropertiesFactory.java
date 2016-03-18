/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner;

import java.util.Properties;

import org.hibernate.SessionFactory;

/**
 * User: mprokopowicz Date: Feb 6, 2006 Time: 11:24:39 AM
 */
public class DomainSpecificPropertiesFactory {
	SessionFactory sessionFactory;
	Properties defaultProperties;

	public DomainSpecificPropertiesFactory(final SessionFactory sessionFactory,
			final Properties defaultProperties) {
		this.sessionFactory = sessionFactory;
		this.defaultProperties = defaultProperties;
	}

	public Properties createPropertiesFor(final Object domainObject) {
		return new DomainSpecificProperties(this.defaultProperties,
				this.sessionFactory, domainObject);
	}

	public Properties getDefaultProperties() {
		return this.defaultProperties;
	}
}
