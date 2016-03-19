package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Note;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.db.NoteHelper;
import com.technoetic.xplanner.db.hibernate.ThreadSession;

/**
 * Created by IntelliJ IDEA. User: sg897500 Date: Nov 26, 2004 Time: 12:27:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeleteNoteAction extends DeleteObjectAction<Note> {

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.actions.DeleteObjectAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward doExecute(final ActionMapping actionMapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse reply) throws Exception {
        final Note note = this.getCommonDao().getById(Note.class,
                new Integer(request.getParameter("oid")).intValue());
        this.getEventBus().publishDeleteEvent(note,
                this.getLoggedInUser(request));
        NoteHelper.deleteNote(note, ThreadSession.get());

        final String returnto = request.getParameter("returnto");
        return returnto != null ? new ActionForward(returnto, true)
                : actionMapping.findForward("view/projects");
    }

}
