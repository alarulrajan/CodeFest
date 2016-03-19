/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner;

import java.util.Properties;

import org.hibernate.SessionFactory;

/**
 * User: mprokopowicz Date: Feb 6, 2006 Time: 11:24:39 AM.
 */
public class DomainSpecificPropertiesFactory {
    
    /** The session factory. */
    SessionFactory sessionFactory;
    
    /** The default properties. */
    Properties defaultProperties;

    /**
     * Instantiates a new domain specific properties factory.
     *
     * @param sessionFactory
     *            the session factory
     * @param defaultProperties
     *            the default properties
     */
    public DomainSpecificPropertiesFactory(final SessionFactory sessionFactory,
            final Properties defaultProperties) {
        this.sessionFactory = sessionFactory;
        this.defaultProperties = defaultProperties;
    }

    /**
     * Creates a new DomainSpecificProperties object.
     *
     * @param domainObject
     *            the domain object
     * @return the properties
     */
    public Properties createPropertiesFor(final Object domainObject) {
        return new DomainSpecificProperties(this.defaultProperties,
                this.sessionFactory, domainObject);
    }

    /**
     * Gets the default properties.
     *
     * @return the default properties
     */
    public Properties getDefaultProperties() {
        return this.defaultProperties;
    }
}
