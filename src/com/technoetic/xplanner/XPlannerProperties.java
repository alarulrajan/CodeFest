/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;

public class XPlannerProperties {
	private static final Logger LOG = Logger
			.getLogger(XPlannerProperties.class);

	public static final String SUPPORT_PRODUCTION_EMAIL_KEY = "support.production.email";
	public static final String SUPPORT_ISSUE_URL_KEY = "support.issue.url";
	public static final String ERROR_FILING_INFO_KEY = "error.filingInfo";
	public static final String XPLANNER_VERSION_KEY = "xplanner.version";
	public static final String XPLANNER_BUILD_REVISION_KEY = "xplanner.build.revision";
	public static final String XPLANNER_BUILD_DATE_KEY = "xplanner.build.date";
	public static final String XPLANNER_BUILD_PACKAGE_KEY = "xplanner.build.package";

	public static final String CONNECTION_URL_KEY = "hibernate.connection.url";
	public static final String DRIVER_CLASS_KEY = "hibernate.connection.driver_class";
	public static final String CONNECTION_USERNAME_KEY = "hibernate.connection.username";
	public static final String CONNECTION_PWD_KEY = "hibernate.connection.password";
	public static final String PATCH_DATABASE_TYPE_KEY = "xplanner.migration.databasetype";
	public static final String PATCH_PATH_KEY = "xplanner.migration.patchpath";
	public static final String EMAIL_FROM = "xplanner.mail.from";
	public static final String APPLICATION_URL_KEY = "xplanner.application.url";
	public static final String WIKI_URL_KEY = "twiki.scheme.wiki";
	public static final String SEND_NOTIFICATION_KEY = "xplanner.project.send.notification";

	private static final class PropertyInitializer {
		public static final Properties properties = PropertyInitializer
				.loadProperties();
		private static final String FILENAME = "xplanner.properties";
		private static final String OVERRIDES_KEY = "xplanner.overrides";
		private static final String OVERRIDES_DEFAULT = "xplanner-custom.properties";

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

	public XPlannerProperties() {
	}

	public Properties get() {
		return PropertyInitializer.properties;
	}

	// DEBT(Spring) Remove this method once XPlannerProperties is loaded from
	// spring
	public static Properties getProperties() {
		return PropertyInitializer.properties;
	}

	public String getProperty(final String key) {
		return PropertyInitializer.properties.getProperty(key);
	}

	public String getProperty(final String key, final String defaultValue) {
		final String val = this.getProperty(key);
		if (val != null) {
			return val;
		}
		return defaultValue;
	}

	public boolean getProperty(final String key, final boolean defaultValue) {
		final String val = this.getProperty(key);
		if (val != null) {
			return Boolean.valueOf(val).booleanValue();
		}
		return defaultValue;
	}

	public Iterator getPropertyNames() {
		return PropertyInitializer.properties.keySet().iterator();
	}

	public void setProperty(final String key, final String value) {
		PropertyInitializer.properties.setProperty(key, value);
	}

	public void removeProperty(final String key) {
		PropertyInitializer.properties.remove(key);
	}

	public static void main(final String[] args) {
		final XPlannerProperties properties = new XPlannerProperties();
		XPlannerProperties.LOG.info(properties
				.getProperty("hibernate.connection.driver_class"));
	}
}
