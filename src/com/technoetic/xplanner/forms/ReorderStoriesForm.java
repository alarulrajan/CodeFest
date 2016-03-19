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

	/** The Constant INVALID_ORDER_NUMBER. */
	public static final String INVALID_ORDER_NUMBER = "story.editor.invalid.order.number";
	
	/** The story ids. */
	private List<String> storyIds = new ArrayList<String>();
	
	/** The order nos. */
	private List<String> orderNos = new ArrayList<String>();
	
	/** The iteration id. */
	private String iterationId;

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
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

	/**
     * Gets the story ids.
     *
     * @return the story ids
     */
	public List<String> getStoryIds() {
		return this.storyIds;
	}

	/**
     * Sets the story ids.
     *
     * @param storyIds
     *            the new story ids
     */
	public void setStoryIds(final List<String> storyIds) {
		this.storyIds = storyIds;
	}

	/**
     * Sets the story id.
     *
     * @param index
     *            the index
     * @param storyId
     *            the story id
     */
	public void setStoryId(final int index, final String storyId) {
		AbstractEditorForm.ensureSize(this.storyIds, index + 1);
		this.storyIds.set(index, storyId);
	}

	/**
     * Gets the story id.
     *
     * @param index
     *            the index
     * @return the story id
     */
	public String getStoryId(final int index) {
		return this.storyIds.get(index);
	}

	/**
     * Gets the story id as int.
     *
     * @param index
     *            the index
     * @return the story id as int
     */
	public int getStoryIdAsInt(final int index) {
		return Integer.parseInt(this.getStoryId(index));
	}

	/**
     * Gets the story count.
     *
     * @return the story count
     */
	public int getStoryCount() {
		return this.storyIds.size();
	}

	/**
     * Gets the order nos.
     *
     * @return the order nos
     */
	public List<String> getOrderNos() {
		return this.orderNos;
	}

	/**
     * Sets the order nos.
     *
     * @param orderNos
     *            the new order nos
     */
	public void setOrderNos(final List<String> orderNos) {
		this.orderNos = orderNos;
	}

	/**
     * Sets the order no.
     *
     * @param index
     *            the index
     * @param orderNo
     *            the order no
     */
	public void setOrderNo(final int index, final String orderNo) {
		AbstractEditorForm.ensureSize(this.orderNos, index + 1);
		this.orderNos.set(index, orderNo);
	}

	/**
     * Gets the order no.
     *
     * @param index
     *            the index
     * @return the order no
     */
	public String getOrderNo(final int index) {
		return this.orderNos.get(index);
	}

	/**
     * Gets the order no as int.
     *
     * @param index
     *            the index
     * @return the order no as int
     */
	public int getOrderNoAsInt(final int index) {
		return (int) Double.parseDouble(this.getOrderNo(index));
	}

	/**
     * Gets the iteration id.
     *
     * @return the iteration id
     */
	public String getIterationId() {
		return this.iterationId;
	}

	/**
     * Sets the iteration id.
     *
     * @param iterationId
     *            the new iteration id
     */
	public void setIterationId(final String iterationId) {
		this.iterationId = iterationId;
	}

}
