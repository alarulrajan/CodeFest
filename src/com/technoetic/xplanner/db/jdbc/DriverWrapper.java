/*
 * Copyright (c) 2005, Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.db.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.collections.EnumerationUtils;

import com.technoetic.xplanner.XPlannerProperties;
import com.thoughtworks.proxy.toys.echo.Echoing;

/**
 * The Class DriverWrapper.
 */
public class DriverWrapper implements Driver {
    
    /** The driver. */
    Driver driver;

    static {
        try {
            DriverWrapper.initWrapperDriver();
        } catch (final SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Inits the wrapper driver.
     *
     * @throws SQLException
     *             the SQL exception
     */
    private static void initWrapperDriver() throws SQLException {
        DriverManager.setLogWriter(new PrintWriter(System.err));
        // List registeredDrivers = new
        // ArrayList(EnumerationUtils.toList(DriverManager.getDrivers()));
        // for (Iterator it = registeredDrivers.iterator(); it.hasNext();) {
        // Driver driver = (Driver) it.next();
        // DriverManager.deregisterDriver(driver);
        // }
        DriverManager.registerDriver(new DriverWrapper());
        // for (Iterator it = registeredDrivers.iterator(); it.hasNext();) {
        // Driver driver = (Driver) it.next();
        // DriverManager.registerDriver(driver);
        // }
    }

    /**
     * Find driver.
     *
     * @param driverClassName
     *            the driver class name
     * @return the driver
     * @throws SQLException
     *             the SQL exception
     */
    private static Driver findDriver(final String driverClassName)
            throws SQLException {
        Class driverClass = null;
        try {
            driverClass = Class.forName(driverClassName);
        } catch (final ClassNotFoundException e) {
            DriverWrapper.throwDriverNotFoundException(driverClassName);
        }
        final List registeredDrivers = new ArrayList(
                EnumerationUtils.toList(DriverManager.getDrivers()));
        for (final Iterator it = registeredDrivers.iterator(); it.hasNext();) {
            final Driver driver = (Driver) it.next();
            if (driver.getClass().equals(driverClass)) {
                return driver;
            }
        }
        DriverWrapper.throwDriverNotFoundException(driverClassName);
        return null; // never reached
    }

    /**
     * Throw driver not found exception.
     *
     * @param driverClassName
     *            the driver class name
     * @throws SQLException
     *             the SQL exception
     */
    private static void throwDriverNotFoundException(
            final String driverClassName) throws SQLException {
        throw new SQLException("Could not find driver '" + driverClassName
                + "'");
    }

    /**
     * Instantiates a new driver wrapper.
     *
     * @throws SQLException
     *             the SQL exception
     */
    public DriverWrapper() throws SQLException {
        final String driverClassName = new XPlannerProperties()
                .getProperty("xplanner.wrapped.driver");
        this.driver = DriverWrapper.findDriver(driverClassName);
        DriverManager.deregisterDriver(this.driver);
    }

    /* (non-Javadoc)
     * @see java.sql.Driver#getMajorVersion()
     */
    @Override
    public int getMajorVersion() {
        return this.driver.getMajorVersion();
    }

    /* (non-Javadoc)
     * @see java.sql.Driver#getMinorVersion()
     */
    @Override
    public int getMinorVersion() {
        return this.driver.getMinorVersion();
    }

    /* (non-Javadoc)
     * @see java.sql.Driver#jdbcCompliant()
     */
    @Override
    public boolean jdbcCompliant() {
        return this.driver.jdbcCompliant();
    }

    /* (non-Javadoc)
     * @see java.sql.Driver#acceptsURL(java.lang.String)
     */
    @Override
    public boolean acceptsURL(final String url) throws SQLException {
        return this.driver.acceptsURL(url);
    }

    /* (non-Javadoc)
     * @see java.sql.Driver#connect(java.lang.String, java.util.Properties)
     */
    @Override
    public Connection connect(final String url, final Properties info)
            throws SQLException {
        return (Connection) Echoing.object(Connection.class,
                this.driver.connect(url, info));
    }

    /* (non-Javadoc)
     * @see java.sql.Driver#getPropertyInfo(java.lang.String, java.util.Properties)
     */
    @Override
    public DriverPropertyInfo[] getPropertyInfo(final String url,
            final Properties info) throws SQLException {
        return this.driver.getPropertyInfo(url, info);
    }

    /* (non-Javadoc)
     * @see java.sql.Driver#getParentLogger()
     */
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}
