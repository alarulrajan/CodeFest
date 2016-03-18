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

//DEBT(DATADRIVEN) Move the responsability of configuring the link to the actionrender (i.e. it is a strategy object)
public class ActionTag extends LinkTag {
	String action;
	ActionRenderer actionRenderer;
	DomainObject targetBean;

	@Override
	public String getAction() {
		return this.action;
	}

	@Override
	public void setAction(final String action) {
		this.action = action;
	}

	public ActionRenderer getActionRenderer() {
		return this.actionRenderer;
	}

	public void setActionRenderer(final ActionRenderer actionRenderer) {
		this.actionRenderer = actionRenderer;
	}

	public DomainObject getTargetBean() {
		return this.targetBean;
	}

	public void setTargetBean(final DomainObject targetBean) {
		this.targetBean = targetBean;
	}

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

	@Override
	public int doEndTag() throws JspException {
		return super.doEndTag();
	}

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

	private void renderAsText() throws JspException {
		this.text = this.getActionName();
	}

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

	@Override
	public void release() {
		super.release();
		this.actionRenderer = null;
		this.text = null;
	}
}
