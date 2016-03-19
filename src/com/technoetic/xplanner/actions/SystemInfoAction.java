/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Mar 6, 2006
 * Time: 10:35:16 PM
 */
package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * The Class SystemInfoAction.
 */
public class SystemInfoAction extends Action {
	
	/** The Constant IS_SYSTEM_INFO_KEY. */
	public static final String IS_SYSTEM_INFO_KEY = "isSystemInfo";

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		request.setAttribute(SystemInfoAction.IS_SYSTEM_INFO_KEY, Boolean.TRUE);
		return mapping.findForward("success");
	}
}