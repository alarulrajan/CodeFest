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

public class LogoutAction extends Action {
	private Authenticator authenticator;

	public void setAuthenticator(final Authenticator authenticator) {
		this.authenticator = authenticator;
	}

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
