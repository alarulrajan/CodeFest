/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.forms.ReorderStoriesForm;

/**
 * Created by IntelliJ IDEA. User: SG0897500 Date: Mar 6, 2006 Time: 11:55:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReorderStoriesAction extends AbstractAction {

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.actions.AbstractAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ActionForward doExecute(final ActionMapping actionMapping,
            final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse reply) throws Exception {

        final ReorderStoriesForm form = (ReorderStoriesForm) actionForm;

        this.getIteration(Integer.parseInt(form.getIterationId()))
                .modifyStoryOrder(this.buildStoryIdNewOrderArray(form));

        final ActionForward actionForward = actionMapping.getInputForward();
        actionForward.setPath(actionForward.getPath() + "?oid="
                + form.getIterationId());
        actionForward.setRedirect(true);
        return actionForward;

    }

    /**
     * Builds the story id new order array.
     *
     * @param form
     *            the form
     * @return the int[][]
     */
    private int[][] buildStoryIdNewOrderArray(final ReorderStoriesForm form) {
        final List<String> storyIds = form.getStoryIds();
        final List<String> orderNos = form.getOrderNos();
        final int[][] storyIdAndNewOrder = new int[storyIds.size()][2];
        for (int index = 0; index < storyIdAndNewOrder.length; index++) {
            storyIdAndNewOrder[index][0] = Integer.parseInt(storyIds.get(index)
                    .toString());
            storyIdAndNewOrder[index][1] = (int) Double.parseDouble(orderNos
                    .get(index).toString());
        }
        return storyIdAndNewOrder;
    }

}
