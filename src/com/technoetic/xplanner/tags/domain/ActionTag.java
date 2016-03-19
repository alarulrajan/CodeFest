/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.tags.domain;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import net.sf.xplanner.domain.DomainObject;

import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.ImgTag;
import org.springframework.context.MessageSource;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.technoetic.xplanner.tags.LinkTag;
import com.technoetic.xplanner.views.ActionRenderer;

/**
 * The Class ActionTag.
 */
//DEBT(DATADRIVEN) Move the responsability of configuring the link to the actionrender (i.e. it is a strategy object)
public class ActionTag extends LinkTag {
	
	/** The action. */
	String action;
	
	/** The action renderer. */
	ActionRenderer actionRenderer;
	
	/** The target bean. */
	DomainObject targetBean;

	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.html.LinkTag#getAction()
	 */
	@Override
	public String getAction() {
		return this.action;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.html.LinkTag#setAction(java.lang.String)
	 */
	@Override
	public void setAction(final String action) {
		this.action = action;
	}

	/**
     * Gets the action renderer.
     *
     * @return the action renderer
     */
	public ActionRenderer getActionRenderer() {
		return this.actionRenderer;
	}

	/**
     * Sets the action renderer.
     *
     * @param actionRenderer
     *            the new action renderer
     */
	public void setActionRenderer(final ActionRenderer actionRenderer) {
		this.actionRenderer = actionRenderer;
	}

	/**
     * Gets the target bean.
     *
     * @return the target bean
     */
	public DomainObject getTargetBean() {
		return this.targetBean;
	}

	/**
     * Sets the target bean.
     *
     * @param targetBean
     *            the new target bean
     */
	public void setTargetBean(final DomainObject targetBean) {
		this.targetBean = targetBean;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.tags.LinkTag#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		this.setPage("/do/" + this.actionRenderer.getTargetPage());
		this.setUseReturnto(this.actionRenderer.useReturnTo());
		this.setOnclick(this.actionRenderer.getOnclick());
		if (!this.actionRenderer.shouldPassOidParam()) {
			this.setFkey(0);
		}
		final int result = super.doStartTag();
		if (this.actionRenderer.shouldPassOidParam()) {
			this.addRequestParameter("oid",
					String.valueOf(this.targetBean.getId()));
		}
		if (this.actionRenderer.isDisplayedAsIcon()) {
			this.renderAsIcon();
		} else {
			this.renderAsText();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.tags.LinkTag#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		return super.doEndTag();
	}

	/**
     * Render as icon.
     *
     * @throws JspException
     *             the jsp exception
     */
	private void renderAsIcon() throws JspException {
		final BodyContent body = this.pageContext.pushBody();
		final ImgTag imgTag = new ImgTag();
		imgTag.setPageContext(this.pageContext);
		imgTag.setPage(this.actionRenderer.getIconPath());
		imgTag.setBorder("0");
		// imgTag.setAlt(getActionName());
		imgTag.setStyleClass(this.actionRenderer.getName());
		imgTag.doStartTag();
		imgTag.doEndTag();
		this.pageContext.popBody();
		this.text = body.getString();
	}

	/**
     * Render as text.
     *
     * @throws JspException
     *             the jsp exception
     */
	private void renderAsText() throws JspException {
		this.text = this.getActionName();
	}

	/**
     * Gets the action name.
     *
     * @return the action name
     * @throws JspException
     *             the jsp exception
     */
	private String getActionName() throws JspException {
		return ((MessageSource) WebApplicationContextUtils
				.getRequiredWebApplicationContext(
						this.pageContext.getServletContext()).getBean(
						"messageSource")).getMessage(
				this.actionRenderer.getTitleKey(),
				null,
				TagUtils.getInstance().getUserLocale(this.pageContext,
						Globals.LOCALE_KEY));
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.tags.LinkTag#release()
	 */
	@Override
	public void release() {
		super.release();
		this.actionRenderer = null;
		this.text = null;
	}
}
