package com.technoetic.xplanner.actions;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.Authenticator;
import com.technoetic.xplanner.security.CredentialCookie;
import com.technoetic.xplanner.security.SecurityHelper;

public class AuthenticationAction extends Action {
	private final Logger log = Logger.getLogger(this.getClass());
	private Authenticator authenticator;
	public static final String AUTHENTICATION_MODULE_NAME_KEY = "authentication.module.name";
	public static final String MODULE_MESSAGES_KEY = "moduleMessages";

	public void setAuthenticator(final Authenticator authenticator) {
		this.authenticator = authenticator;
	}

	@Override
	public ActionForward execute(final ActionMapping actionMapping,
			final ActionForm actionForm,
			final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse) throws Exception {
		ActionForward forward = actionMapping.findForward("notAuthenticated");
		final DynaActionForm form = (DynaActionForm) actionForm;
		if (StringUtils.isEmpty((String) form.get("action"))) {
			return forward;
		}
		try {
			final String userId = (String) form.get("userId");
			final String password = (String) form.get("password");
			this.authenticator.authenticate(httpServletRequest, userId,
					password);
			if (StringUtils.equals(httpServletRequest.getParameter("remember"),
					"Y")) {
				final CredentialCookie credentials = new CredentialCookie(
						httpServletRequest, httpServletResponse);
				credentials.set(userId, password);
			}
			final String savedUrl = SecurityHelper
					.getSavedUrl(httpServletRequest);
			if (savedUrl != null) {
				return new ActionForward(savedUrl, true);
			} else {
				forward = actionMapping.findForward("authenticated");
			}
		} catch (final AuthenticationException e) {
			// Using message since text will be formatted slightly differently
			// than the normal "error".
			this.log.warn(e.getMessage() + ": " + e.getCause());
			final ActionMessages errors = new ActionMessages();
			final Map errorMap = e.getErrorsByModule();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"login.failed"));
			for (final Iterator iterator = errorMap.keySet().iterator(); iterator
					.hasNext();) {
				final String moduleName = (String) iterator.next();
				final String message = (String) errorMap.get(moduleName);
				errors.add(AuthenticationAction.MODULE_MESSAGES_KEY,
						new ActionMessage(message, moduleName));

			}
			httpServletRequest.setAttribute(Globals.MESSAGE_KEY, errors);
		}
		return forward;
	}
}
