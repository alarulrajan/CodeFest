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

public class InvalidateCacheAction extends Action {
	private static final Logger log = LogUtil.getLogger();
	private Authorizer authorizer;
	private Map cacheMap;

	public void setCacheMap(final Map cacheMap) {
		this.cacheMap = cacheMap;
	}

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

	public void setAuthorizer(final Authorizer authorizer) {
		this.authorizer = authorizer;
	}
}
