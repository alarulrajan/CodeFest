/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.Command;

/**
 * User: mprokopowicz Date: Feb 3, 2006 Time: 11:32:41 AM.
 */
public class CommandExecutorAction extends Action {
    
    /** The command. */
    private Command command;

    /**
     * Sets the task.
     *
     * @param command
     *            the new task
     */
    public void setTask(final Command command) {
        this.command = command;
    }

    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        this.command.execute();
        final String returnto = request
                .getParameter(EditObjectAction.RETURNTO_PARAM);
        if (returnto != null) {
            return new ActionForward(returnto, true);
        }
        return mapping.findForward("view/projects");
    }
}
