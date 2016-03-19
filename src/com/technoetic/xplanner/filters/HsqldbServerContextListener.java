/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Mar 16, 2006
 * Time: 11:16:45 PM
 */
package com.technoetic.xplanner.filters;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.db.hsqldb.HsqlServer;
import com.technoetic.xplanner.util.LogUtil;

/**
 * The listener interface for receiving hsqldbServerContext events. The class
 * that is interested in processing a hsqldbServerContext event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addHsqldbServerContextListener<code> method. When
 * the hsqldbServerContext event occurs, that object's appropriate
 * method is invoked.
 *
 * @see HsqldbServerContextEvent
 */
public class HsqldbServerContextListener implements ServletContextListener {
	
	/** The Constant LOG. */
	protected static final Logger LOG = LogUtil.getLogger();
	
	/** The Constant HSQLDB_DATABASE_TYPE. */
	protected static final String HSQLDB_DATABASE_TYPE = "hsqldb";

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(final ServletContextEvent event) {
		final String databaseType = new XPlannerProperties()
				.getProperty(XPlannerProperties.PATCH_DATABASE_TYPE_KEY);
		if (HsqldbServerContextListener.HSQLDB_DATABASE_TYPE
				.equalsIgnoreCase(databaseType)) {
			try {
				HsqlServer.start(this.getWebappRoot(event.getServletContext()));
			} catch (final Exception e) {
				HsqldbServerContextListener.LOG.error(
						"Problem during the start up of the in-process HSQLDB",
						e);
			}
		} else {
			HsqldbServerContextListener.LOG.debug("HSQL: "
					+ XPlannerProperties.PATCH_DATABASE_TYPE_KEY
					+ " is not defined or is not set to "
					+ HsqldbServerContextListener.HSQLDB_DATABASE_TYPE);
		}
	}

	/**
     * Gets the webapp root.
     *
     * @param servletContext
     *            the servlet context
     * @return the webapp root
     */
	private String getWebappRoot(final ServletContext servletContext) {
		return servletContext.getRealPath("/");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(final ServletContextEvent event) {
		HsqlServer.shutdown();
	}

}