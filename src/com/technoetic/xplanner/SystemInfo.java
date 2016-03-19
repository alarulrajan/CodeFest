/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Jan 1, 2006
 * Time: 2:44:27 AM
 */
package com.technoetic.xplanner;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jfree.util.Log;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.web.context.ServletContextAware;

import com.technoetic.xplanner.db.hsqldb.HsqlServer;
import com.technoetic.xplanner.util.LogUtil;

/**
 * The Class SystemInfo.
 *
 * @noinspection StringContatenationInLoop,MagicNumber
 */
public class SystemInfo implements ServletContextAware {
	
	/** The Constant MEGABYTE. */
	static final long MEGABYTE = 1048576L;
	
	/** The properties. */
	private Properties properties;
	
	/** The servlet context. */
	private ServletContext servletContext;
	
	/** The session factory. */
	private SessionFactory sessionFactory;

	/**
     * Gets the properties.
     *
     * @return the properties
     */
	public Properties getProperties() {
		return this.properties;
	}

	/**
     * Sets the properties.
     *
     * @param properties
     *            the new properties
     */
	public void setProperties(final Properties properties) {
		this.properties = properties;
	}

	/**
     * Sets the session factory.
     *
     * @param sessionFactory
     *            the new session factory
     */
	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
     * Gets the system properties.
     *
     * @return the system properties
     */
	public static Map<String, String> getSystemProperties() {
		final Properties sysProps = System.getProperties();
		final Map<String, String> props = new ListOrderedMap();
		props.put("System Date", DateFormat.getDateInstance()
				.format(new Date()));
		props.put("System Time", DateFormat.getTimeInstance()
				.format(new Date()));
		props.put("Current directory", SystemInfo.getCurrentDirectory());

		props.put("Java Version", sysProps.getProperty("java.version"));
		props.put("Java Vendor", sysProps.getProperty("java.vendor"));
		props.put("JVM Version",
				sysProps.getProperty("java.vm.specification.version"));
		props.put("JVM Vendor",
				sysProps.getProperty("java.vm.specification.vendor"));
		props.put("JVM Implementation Version",
				sysProps.getProperty("java.vm.version"));
		props.put("Java Runtime", sysProps.getProperty("java.runtime.name"));
		props.put("Java VM", sysProps.getProperty("java.vm.name"));

		props.put("User Name", sysProps.getProperty("user.name"));
		props.put("User Timezone", sysProps.getProperty("user.timezone"));

		props.put("Operating System", sysProps.getProperty("os.name") + " "
				+ sysProps.getProperty("os.version"));
		props.put("OS Architecture", sysProps.getProperty("os.arch"));

		return props;
	}

	/**
     * Gets the current directory.
     *
     * @return the current directory
     */
	private static String getCurrentDirectory() {
		try {
			return new File(".").getCanonicalPath();
		} catch (final IOException e) {
			Log.info(e);
			return "<undefined>";
		}
	}

	/**
     * Gets the JVM statistics.
     *
     * @return the JVM statistics
     */
	public Map<String, String> getJVMStatistics() {
		final Map<String, String> stats = new ListOrderedMap();
		stats.put("Total Memory", "" + this.getTotalMemory() + "MB");
		stats.put("Free Memory", "" + this.getFreeMemory() + "MB");
		stats.put("Used Memory", "" + this.getUsedMemory() + "MB");
		return stats;
	}

	/**
     * Gets the builds the info.
     *
     * @return the builds the info
     */
	public Map<String, String> getBuildInfo() {
		final Map<String, String> stats = new ListOrderedMap();
		stats.put("Version", this.properties
				.getProperty(XPlannerProperties.XPLANNER_VERSION_KEY));
		stats.put("Build Date", this.properties
				.getProperty(XPlannerProperties.XPLANNER_BUILD_DATE_KEY));
		stats.put("Build Revision", this.properties
				.getProperty(XPlannerProperties.XPLANNER_BUILD_REVISION_KEY));
		stats.put("Build Package", this.properties.getProperty(
				XPlannerProperties.XPLANNER_BUILD_PACKAGE_KEY, "War"));
		return stats;
	}

	/**
     * Gets the database info.
     *
     * @return the database info
     */
	public Map<String, String> getDatabaseInfo() {
		final Map<String, String> props = new ListOrderedMap();
		props.put("Dialect", this.properties.getProperty("hibernate.dialect"));
		props.put("Driver", this.properties
				.getProperty("hibernate.connection.driver_class"));
		props.put("Driver Version", this.getDriverVersion());
		props.put("Database Vendor", this.getDatabaseVendor());
		props.put("Database Version", this.getDatabaseVersion());
		props.put("Database Name",
				this.properties.getProperty("hibernate.connection.dbname"));
		props.put("Database Url", this.properties
				.getProperty(XPlannerProperties.CONNECTION_URL_KEY));
		if (HsqlServer.isLocalPublicDatabaseStarted()) {
			props.put("Database File", HsqlServer.getInstance().getDbPath());
		}
		props.put("Database User Name",
				this.properties.getProperty("hibernate.connection.username"));
		props.put("Database User Password", SystemInfo.isEmpty(this.properties
				.getProperty("hibernate.connection.password")) ? "[not set]"
				: "******");
		props.put("Database Patch Level", SystemInfo.getDatabasePatchLevel());
		return props;
	}

	/**
     * Gets the database vendor.
     *
     * @return the database vendor
     */
	private String getDatabaseVendor() {
		return (String) new HibernateTemplate(this.sessionFactory)
				.execute(new HibernateCallback() {
					@Override
					public Object doInHibernate(final Session session)
							throws HibernateException, SQLException {
						return session.connection().getMetaData()
								.getDatabaseProductName();
					}
				});
	}

	/**
     * Gets the database version.
     *
     * @return the database version
     */
	private String getDatabaseVersion() {
		return (String) new HibernateTemplate(this.sessionFactory)
				.execute(new HibernateCallback() {
					@Override
					public Object doInHibernate(final Session session)
							throws HibernateException, SQLException {
						return session.connection().getMetaData()
								.getDatabaseProductVersion();
					}
				});
	}

	/**
     * Gets the driver version.
     *
     * @return the driver version
     */
	private String getDriverVersion() {
		return (String) new HibernateTemplate(this.sessionFactory)
				.execute(new HibernateCallback() {
					@Override
					public Object doInHibernate(
							final org.hibernate.Session session)
							throws HibernateException, SQLException {
						return session.connection().getMetaData()
								.getDriverVersion();
					}
				});
	}

	/**
     * Gets the database patch level.
     *
     * @return the database patch level
     */
	private static String getDatabasePatchLevel() {
		try {
			return "to be fixed to use liquid";
		} catch (final Exception e) {
			Log.info(e);
			return "Unknown (Exception during retrieval: " + e.getMessage()
					+ ")";
		}
	}

	/**
     * Gets the app server info.
     *
     * @return the app server info
     */
	public Map<String, String> getAppServerInfo() {
		final Map<String, String> props = new ListOrderedMap();
		props.put("Application Server", this.servletContext.getServerInfo());
		props.put("Servlet Version", this.servletContext.getMajorVersion()
				+ "." + this.servletContext.getMinorVersion());
		return props;
	}

	/**
     * Checks if is empty.
     *
     * @param value
     *            the value
     * @return true, if is empty
     */
	private static boolean isEmpty(final String value) {
		return value == null || "".equals(value);
	}

	/**
     * Gets the total memory.
     *
     * @return the total memory
     */
	public long getTotalMemory() {
		return Runtime.getRuntime().totalMemory() / SystemInfo.MEGABYTE;
	}

	/**
     * Gets the free memory.
     *
     * @return the free memory
     */
	public long getFreeMemory() {
		return Runtime.getRuntime().freeMemory() / SystemInfo.MEGABYTE;
	}

	/**
     * Gets the used memory.
     *
     * @return the used memory
     */
	public long getUsedMemory() {
		return this.getTotalMemory() - this.getFreeMemory();
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.context.ServletContextAware#setServletContext(javax.servlet.ServletContext)
	 */
	@Override
	public void setServletContext(final ServletContext servletContext) {
		this.servletContext = servletContext;
		LogUtil.getLogger().info(
				"*********************** XPLANNER INFO ************************\n"
						+ this.toString());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append(SystemInfo.propertiesMapToString("Build",
				this.getBuildInfo()));
		buf.append(SystemInfo.propertiesMapToString("Database",
				this.getDatabaseInfo()));
		buf.append(SystemInfo.propertiesMapToString("App Server",
				this.getAppServerInfo()));
		buf.append(SystemInfo.propertiesMapToString("System",
				SystemInfo.getSystemProperties()));
		return buf.toString();
	}

	/**
     * Properties map to string.
     *
     * @param mapName
     *            the map name
     * @param properties
     *            the properties
     * @return the string
     */
	private static String propertiesMapToString(final String mapName,
			final Map<String, String> properties) {
		final StringBuffer buf = new StringBuffer();
		buf.append(mapName);
		buf.append(":\n");

		final Iterator<String> iterator = properties.keySet().iterator();
		while (iterator.hasNext()) {
			final String name = iterator.next();
			final String value = properties.get(name);
			buf.append("   ");
			buf.append(StringUtils.rightPad(name + ":", 30));
			buf.append(value).append("\n");
		}
		buf.append("\n");
		return buf.toString();
	}

}
