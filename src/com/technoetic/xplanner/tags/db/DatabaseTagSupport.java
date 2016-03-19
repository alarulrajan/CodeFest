package com.technoetic.xplanner.tags.db;

import org.hibernate.classic.Session;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.technoetic.xplanner.db.hibernate.HibernateHelper;

/**
 * The Class DatabaseTagSupport.
 */
public abstract class DatabaseTagSupport extends RequestContextAwareTag {
	
	/**
     * Gets the session.
     *
     * @return the session
     * @throws Exception
     *             the exception
     */
	protected Session getSession() throws Exception {
		return HibernateHelper.getSession(this.pageContext.getRequest());
	}
}
