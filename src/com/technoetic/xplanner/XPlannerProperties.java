/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * The Class XPlannerProperties.
 */
public class XPlannerProperties {
    
    /** The Constant LOG. */
    private static final Logger LOG = Logger
            .getLogger(XPlannerProperties.class);

    /** The Constant SUPPORT_PRODUCTION_EMAIL_KEY. */
    public static final String SUPPORT_PRODUCTION_EMAIL_KEY = "support.production.email";
    
    /** The Constant SUPPORT_ISSUE_URL_KEY. */
    public static final String SUPPORT_ISSUE_URL_KEY = "support.issue.url";
    
    /** The Constant ERROR_FILING_INFO_KEY. */
    public static final String ERROR_FILING_INFO_KEY = "error.filingInfo";
    
    /** The Constant XPLANNER_VERSION_KEY. */
    public static final String XPLANNER_VERSION_KEY = "xplanner.version";
    
    /** The Constant XPLANNER_BUILD_REVISION_KEY. */
    public static final String XPLANNER_BUILD_REVISION_KEY = "xplanner.build.revision";
    
    /** The Constant XPLANNER_BUILD_DATE_KEY. */
    public static final String XPLANNER_BUILD_DATE_KEY = "xplanner.build.date";
    
    /** The Constant XPLANNER_BUILD_PACKAGE_KEY. */
    public static final String XPLANNER_BUILD_PACKAGE_KEY = "xplanner.build.package";

    /** The Constant CONNECTION_URL_KEY. */
    public static final String CONNECTION_URL_KEY = "hibernate.connection.url";
    
    /** The Constant DRIVER_CLASS_KEY. */
    public static final String DRIVER_CLASS_KEY = "hibernate.connection.driver_class";
    
    /** The Constant CONNECTION_USERNAME_KEY. */
    public static final String CONNECTION_USERNAME_KEY = "hibernate.connection.username";
    
    /** The Constant CONNECTION_PWD_KEY. */
    public static final String CONNECTION_PWD_KEY = "hibernate.connection.password";
    
    /** The Constant PATCH_DATABASE_TYPE_KEY. */
    public static final String PATCH_DATABASE_TYPE_KEY = "xplanner.migration.databasetype";
    
    /** The Constant PATCH_PATH_KEY. */
    public static final String PATCH_PATH_KEY = "xplanner.migration.patchpath";
    
    /** The Constant EMAIL_FROM. */
    public static final String EMAIL_FROM = "xplanner.mail.from";
    
    /** The Constant APPLICATION_URL_KEY. */
    public static final String APPLICATION_URL_KEY = "xplanner.application.url";
    
    /** The Constant WIKI_URL_KEY. */
    public static final String WIKI_URL_KEY = "twiki.scheme.wiki";
    
    /** The Constant SEND_NOTIFICATION_KEY. */
    public static final String SEND_NOTIFICATION_KEY = "xplanner.project.send.notification";

    /**
     * The Class PropertyInitializer.
     */
    private static final class PropertyInitializer {
        
        /** The Constant properties. */
        public static final Properties properties = PropertyInitializer
                .loadProperties();
        
        /** The Constant FILENAME. */
        private static final String FILENAME = "xplanner.properties";
        
        /** The Constant OVERRIDES_KEY. */
        private static final String OVERRIDES_KEY = "xplanner.overrides";
        
        /** The Constant OVERRIDES_DEFAULT. */
        private static final String OVERRIDES_DEFAULT = "xplanner-custom.properties";

        /**
         * Load properties.
         *
         * @return the properties
         */
        private static Properties loadProperties() {
            final Properties properties = new Properties();
            try {
                final InputStream in = XPlannerProperties.class
                        .getClassLoader().getResourceAsStream(
                                PropertyInitializer.FILENAME);
                if (in != null) {
                    properties.load(in);
                    PropertyInitializer.setCustomPropertyOverrides(properties);
                } else {
                    XPlannerProperties.LOG
                            .error("Can't load xplanner.properties!!!");
                }
            } catch (final IOException ex) {
                XPlannerProperties.LOG.error(
                        "error during xplanner property loading", ex);
            }
            return properties;
        }

        /**
         * This brute-force approach is being used instead of normal
         * java.util.Property defaults because Hibernate copies the properties
         * (without the defaults).
         *
         * @param properties
         *            the new custom property overrides
         */
        private static void setCustomPropertyOverrides(
                final Properties properties) {
            final Properties customProperties = new Properties();
            try {
                final String customPropertyFileName = System.getProperty(
                        PropertyInitializer.OVERRIDES_KEY,
                        PropertyInitializer.OVERRIDES_DEFAULT);
                final InputStream in = XPlannerProperties.class
                        .getClassLoader().getResourceAsStream(
                                customPropertyFileName);
                if (in != null) {
                    customProperties.load(in);
                    for (final Iterator iterator = customProperties.keySet()
                            .iterator(); iterator.hasNext();) {
                        final String key = (String) iterator.next();
                        properties.put(key, customProperties.get(key));
                    }
                }
            } catch (final IOException ex) {
                XPlannerProperties.LOG.error(
                        "error during custom property loading", ex);
            }
        }
    }

    /**
     * Instantiates a new x planner properties.
     */
    public XPlannerProperties() {
    }

    /**
     * Gets the.
     *
     * @return the properties
     */
    public Properties get() {
        return PropertyInitializer.properties;
    }

    // DEBT(Spring) Remove this method once XPlannerProperties is loaded from
    /**
     * Gets the properties.
     *
     * @return the properties
     */
    // spring
    public static Properties getProperties() {
        return PropertyInitializer.properties;
    }

    /**
     * Gets the property.
     *
     * @param key
     *            the key
     * @return the property
     */
    public String getProperty(final String key) {
        return PropertyInitializer.properties.getProperty(key);
    }

    /**
     * Gets the property.
     *
     * @param key
     *            the key
     * @param defaultValue
     *            the default value
     * @return the property
     */
    public String getProperty(final String key, final String defaultValue) {
        final String val = this.getProperty(key);
        if (val != null) {
            return val;
        }
        return defaultValue;
    }

    /**
     * Gets the property.
     *
     * @param key
     *            the key
     * @param defaultValue
     *            the default value
     * @return the property
     */
    public boolean getProperty(final String key, final boolean defaultValue) {
        final String val = this.getProperty(key);
        if (val != null) {
            return Boolean.valueOf(val).booleanValue();
        }
        return defaultValue;
    }

    /**
     * Gets the property names.
     *
     * @return the property names
     */
    public Iterator getPropertyNames() {
        return PropertyInitializer.properties.keySet().iterator();
    }

    /**
     * Sets the property.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    public void setProperty(final String key, final String value) {
        PropertyInitializer.properties.setProperty(key, value);
    }

    /**
     * Removes the property.
     *
     * @param key
     *            the key
     */
    public void removeProperty(final String key) {
        PropertyInitializer.properties.remove(key);
    }

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(final String[] args) {
        final XPlannerProperties properties = new XPlannerProperties();
        XPlannerProperties.LOG.info(properties
                .getProperty("hibernate.connection.driver_class"));
    }
}
