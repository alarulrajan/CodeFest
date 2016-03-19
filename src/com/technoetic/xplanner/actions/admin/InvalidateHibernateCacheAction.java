package com.technoetic.xplanner.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.SessionFactory;

import com.technoetic.xplanner.util.LogUtil;

/**
 * The Class InvalidateHibernateCacheAction.
 */
public class InvalidateHibernateCacheAction extends Action {
	
	/** The Constant log. */
	private static final Logger log = LogUtil.getLogger();
	
	/** The session factory. */
	SessionFactory sessionFactory;

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.sessionFactory.evictQueries();
		InvalidateHibernateCacheAction.log.info("hibernate cache cleared");
		return null;
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
}
