package com.technoetic.xplanner.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.Authorizer;

/**
 * Generic dispatcher to ActionForwards. Original Author: Ted Husted Source:
 * http://husted.com/about/scaffolding/catalog.htm
 */
public final class DispatchForward extends Action {
    
    /** The log. */
    private final Logger log = Logger.getLogger(this.getClass());
    
    /** The is authorization required. */
    private boolean isAuthorizationRequired = true;
    
    /** The authorizer. */
    private Authorizer authorizer;

    /**
     * Forward request to "cancel", {forward}, or "error" mapping, where
     * {forward} is an action path given in the parameter mapping or in the
     * request as "forward=actionPath".
     *
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request (if any)
     * @param request
     *            The HTTP request we are processing
     * @param response
     *            The HTTP response we are creating
     * @return the action forward
     * @throws Exception
     *             the exception
     */
    @Override
    public ActionForward execute(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        if (this.isSecure(mapping)) {
            int projectId = 0;
            final String projectIdParameter = request.getParameter("projectId");
            if (projectIdParameter != null) {
                projectId = Integer.parseInt(request.getParameter("projectId"));
            }
            if (projectId == 0) {
                this.log.error("no project identifier supplied for secure access");
                return mapping.findForward("security/notAuthorized");
            }
            if (!this.authorizer.hasPermission(projectId,
                    SecurityHelper.getRemoteUserId(request), "system.project",
                    projectId, "read")) {
                return mapping.findForward("security/notAuthorized");
            }
        }

        // -- Locals
        ActionForward thisForward = null;
        String wantForward = null;

        // -- Check internal parameter for forward
        wantForward = mapping.getParameter();

        // -- If found, consult mappings
        if (wantForward != null) {
            thisForward = mapping.findForward(wantForward);
        }

        // -- If anything not found, dispatch error
        if (thisForward == null) {
            thisForward = mapping.findForward("error");
            final ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                    "action.missing.parameter"));
            this.saveErrors(request, errors);
        }

        return thisForward;

    }

    /**
     * Sets the authorization required.
     *
     * @param authorizationRequired
     *            the new authorization required
     */
    public void setAuthorizationRequired(final boolean authorizationRequired) {
        this.isAuthorizationRequired = authorizationRequired;
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

    /**
     * Checks if is secure.
     *
     * @param mapping
     *            the mapping
     * @return true, if is secure
     */
    private boolean isSecure(final ActionMapping mapping) {
        return mapping.findForward("@secure") != null ? Boolean.valueOf(mapping
                .findForward("@secure").getPath()) != Boolean.FALSE
                : this.isAuthorizationRequired;
    }

}
