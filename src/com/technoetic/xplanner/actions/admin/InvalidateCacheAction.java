/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.actions.admin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;
import com.technoetic.xplanner.util.LogUtil;

/**
 * The Class InvalidateCacheAction.
 */
public class InvalidateCacheAction extends Action {
	
	/** The Constant log. */
	private static final Logger log = LogUtil.getLogger();
	
	/** The authorizer. */
	private Authorizer authorizer;
	
	/** The cache map. */
	private Map cacheMap;

	/**
     * Sets the cache map.
     *
     * @param cacheMap
     *            the new cache map
     */
	public void setCacheMap(final Map cacheMap) {
		this.cacheMap = cacheMap;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.cacheMap.clear();
		if (SystemAuthorizer.get() != this.authorizer) {
			InvalidateCacheAction.log
					.error("Configuration problem: there are 2 Authorizers in the system!");
		}
		InvalidateCacheAction.log.info("cache cleared");
		return null;
	}

	/**
     * Sets the authorizer.
     *
     * @param authorizer
     *            the new authorizer
     */
	public void setAuthorizer(final Authorizer authorizer) {
		this.authorizer = authorizer;
	}
}
