/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.forms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * Created by IntelliJ IDEA. User: SG0897500 Date: Mar 6, 2006 Time: 11:58:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReorderStoriesForm extends AbstractEditorForm {

	public static final String INVALID_ORDER_NUMBER = "story.editor.invalid.order.number";
	private List<String> storyIds = new ArrayList<String>();
	private List<String> orderNos = new ArrayList<String>();
	private String iterationId;

	@Override
	public ActionErrors validate(final ActionMapping mapping,
			final HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();
		if (this.orderNos != null) {
			for (final Iterator<String> iterator = this.orderNos.iterator(); iterator
					.hasNext();) {
				final String orderValue = iterator.next();
				try {
					Double.parseDouble(orderValue);
				} catch (final NumberFormatException e) {
					AbstractEditorForm.error(errors,
							ReorderStoriesForm.INVALID_ORDER_NUMBER,
							new Object[] { orderValue });
				}
			}
		}
		return errors;
	}

	public List<String> getStoryIds() {
		return this.storyIds;
	}

	public void setStoryIds(final List<String> storyIds) {
		this.storyIds = storyIds;
	}

	public void setStoryId(final int index, final String storyId) {
		AbstractEditorForm.ensureSize(this.storyIds, index + 1);
		this.storyIds.set(index, storyId);
	}

	public String getStoryId(final int index) {
		return this.storyIds.get(index);
	}

	public int getStoryIdAsInt(final int index) {
		return Integer.parseInt(this.getStoryId(index));
	}

	public int getStoryCount() {
		return this.storyIds.size();
	}

	public List<String> getOrderNos() {
		return this.orderNos;
	}

	public void setOrderNos(final List<String> orderNos) {
		this.orderNos = orderNos;
	}

	public void setOrderNo(final int index, final String orderNo) {
		AbstractEditorForm.ensureSize(this.orderNos, index + 1);
		this.orderNos.set(index, orderNo);
	}

	public String getOrderNo(final int index) {
		return this.orderNos.get(index);
	}

	public int getOrderNoAsInt(final int index) {
		return (int) Double.parseDouble(this.getOrderNo(index));
	}

	public String getIterationId() {
		return this.iterationId;
	}

	public void setIterationId(final String iterationId) {
		this.iterationId = iterationId;
	}

}
