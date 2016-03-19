/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.security.Authenticator;
import com.technoetic.xplanner.security.CredentialCookie;
import com.technoetic.xplanner.security.SecurityHelper;

/**
 * The Class LogoutAction.
 */
public class LogoutAction extends Action {
	
	/** The authenticator. */
	private Authenticator authenticator;

	/**
     * Sets the authenticator.
     *
     * @param authenticator
     *            the new authenticator
     */
	public void setAuthenticator(final Authenticator authenticator) {
		this.authenticator = authenticator;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(final ActionMapping actionMapping,
			final ActionForm actionForm,
			final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse) throws Exception {
		this.authenticator.logout(httpServletRequest,
				SecurityHelper.getRemoteUserId(httpServletRequest));
		new CredentialCookie(httpServletRequest, httpServletResponse).remove();
		return actionMapping.findForward("security/login");
	}
}
