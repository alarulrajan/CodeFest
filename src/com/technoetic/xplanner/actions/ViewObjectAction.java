package com.technoetic.xplanner.actions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.domain.Identifiable;

/**
 * The Class ViewObjectAction.
 *
 * @param <T>
 *            the generic type
 */
public class ViewObjectAction<T extends Identifiable> extends AbstractAction<T> {
    
    /** The authorization required. */
    private boolean authorizationRequired = true;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.actions.AbstractAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ActionForward doExecute(final ActionMapping actionMapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse reply) throws Exception {
        final Class objectClass = this.getObjectType(actionMapping, request);
        final String forwardPath = this.getForwardPath(actionMapping, request);
        if (this.isSecure(actionMapping)) {
            final Object object = this.getCommonDao().getById(objectClass,
                    Integer.parseInt(request.getParameter("oid")));
            this.setDomainContext(request, object, actionMapping);
        }
        return new ActionForward(forwardPath);
    }

    /**
     * Gets the forward path.
     *
     * @param actionMapping
     *            the action mapping
     * @param request
     *            the request
     * @return the forward path
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    private String getForwardPath(final ActionMapping actionMapping,
            final HttpServletRequest request)
            throws UnsupportedEncodingException {
        String forwardPath = actionMapping.findForward("display").getPath();
        final String returnto = request.getParameter("returnto");
        if (returnto != null) {
            forwardPath += (forwardPath.indexOf("?") != -1 ? "&" : "?")
                    + "returnto=" + URLEncoder.encode(returnto, "UTF-8");
        }
        return forwardPath;
    }

    /**
     * Checks if is secure.
     *
     * @param actionMapping
     *            the action mapping
     * @return true, if is secure
     */
    private boolean isSecure(final ActionMapping actionMapping) {
        return this.authorizationRequired;
    }

    /**
     * Sets the authorization required.
     *
     * @param authorizationRequired
     *            the new authorization required
     */
    public void setAuthorizationRequired(final boolean authorizationRequired) {
        this.authorizationRequired = authorizationRequired;
    }
}