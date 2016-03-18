package com.technoetic.xplanner.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.technoetic.xplanner.XPlannerProperties;

public class ProgressBarDelegatorTag implements ProgressBarTag {
	private final Logger log = Logger.getLogger(this.getClass());
	private ProgressBarTag delegate;
	private static final String HTML_TYPE = "html";
	private static final String IMAGE_TYPE = "image";

	public ProgressBarDelegatorTag() {
		final String type = new XPlannerProperties().getProperty(
				"xplanner.progressbar.impl", ProgressBarDelegatorTag.HTML_TYPE);
		if (StringUtils.equalsIgnoreCase(type,
				ProgressBarDelegatorTag.HTML_TYPE)) {
			this.delegate = new ProgressBarHtmlTag();
		} else if (StringUtils.equalsIgnoreCase(type,
				ProgressBarDelegatorTag.IMAGE_TYPE)) {
			this.delegate = new ProgressBarImageTag();
		} else {
			this.log.error("unrecognized progress bar type, using HTML by default: type="
					+ type);
		}
	}

	@Override
	public void setActual(final double actual) {
		this.delegate.setActual(actual);
	}

	@Override
	public void setComplete(final boolean complete) {
		this.delegate.setComplete(complete);
	}

	@Override
	public void setEstimate(final double estimate) {
		this.delegate.setEstimate(estimate);
	}

	@Override
	public void setHeight(final int height) {
		this.delegate.setHeight(height);
	}

	public void setWidth(final Object width) {
		this.delegate.setWidth(String.valueOf(width));
	}

	@Override
	public void setWidth(final String width) {
		this.delegate.setWidth(width);
	}

	@Override
	public int doEndTag() throws JspException {
		return this.delegate.doEndTag();
	}

	@Override
	public int doStartTag() throws JspException {
		return this.delegate.doStartTag();
	}

	@Override
	public Tag getParent() {
		return this.delegate.getParent();
	}

	@Override
	public void release() {
		this.delegate.release();
	}

	@Override
	public void setPageContext(final PageContext pageContext) {
		this.delegate.setPageContext(pageContext);
	}

	@Override
	public void setParent(final Tag tag) {
		this.delegate.setParent(tag);
	}
}
