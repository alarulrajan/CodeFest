package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.DomainObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.domain.Identifiable;

/**
 * The Class DeleteObjectAction.
 *
 * @param <T>
 *            the generic type
 */
public class DeleteObjectAction<T extends Identifiable> extends
        AbstractAction<T> {
    
    /* (non-Javadoc)
     * @see com.technoetic.xplanner.actions.AbstractAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward doExecute(final ActionMapping actionMapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse reply) throws Exception {
        final Class<Identifiable> objectClass = this.getObjectType(
                actionMapping, request);
        final int objectIdentifier = Integer.parseInt(request
                .getParameter("oid"));
        final Identifiable object = this.getCommonDao().getById(objectClass,
                objectIdentifier);
        this.getEventBus().publishDeleteEvent((DomainObject) object,
                this.getLoggedInUser(request));
        this.getCommonDao().delete(object);
        final String returnto = request.getParameter("returnto");
        return returnto != null ? new ActionForward(returnto, true)
                : actionMapping.findForward("view/projects");
    }
}
