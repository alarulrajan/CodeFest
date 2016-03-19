package com.technoetic.xplanner.actions;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * User: Mateusz Prokopowicz Date: Aug 2, 2005 Time: 12:29:23 PM.
 */
public class ChangeLocaleAction extends Action {
    
    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final String language = request.getParameter("language");
        final String returnto = request
                .getParameter(EditObjectAction.RETURNTO_PARAM);
        Locale locale;
        if (!StringUtils.isEmpty(language)) {
            locale = new Locale(language);
        } else {
            locale = Locale.getDefault();
        }
        final HttpSession session = request.getSession();
        session.setAttribute(Globals.LOCALE_KEY, locale);
        if (StringUtils.isEmpty(returnto)) {
            return mapping.findForward("view/projects");
        } else {
            return new ActionForward(returnto, true);
        }
    }
}
