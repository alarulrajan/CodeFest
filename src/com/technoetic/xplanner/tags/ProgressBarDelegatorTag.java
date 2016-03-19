package com.technoetic.xplanner.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.technoetic.xplanner.XPlannerProperties;

/**
 * The Class ProgressBarDelegatorTag.
 */
public class ProgressBarDelegatorTag implements ProgressBarTag {
	
	/** The log. */
	private final Logger log = Logger.getLogger(this.getClass());
	
	/** The delegate. */
	private ProgressBarTag delegate;
	
	/** The Constant HTML_TYPE. */
	private static final String HTML_TYPE = "html";
	
	/** The Constant IMAGE_TYPE. */
	private static final String IMAGE_TYPE = "image";

	/**
     * Instantiates a new progress bar delegator tag.
     */
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

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.tags.ProgressBarTag#setActual(double)
	 */
	@Override
	public void setActual(final double actual) {
		this.delegate.setActual(actual);
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.tags.ProgressBarTag#setComplete(boolean)
	 */
	@Override
	public void setComplete(final boolean complete) {
		this.delegate.setComplete(complete);
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.tags.ProgressBarTag#setEstimate(double)
	 */
	@Override
	public void setEstimate(final double estimate) {
		this.delegate.setEstimate(estimate);
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.tags.ProgressBarTag#setHeight(int)
	 */
	@Override
	public void setHeight(final int height) {
		this.delegate.setHeight(height);
	}

	/**
     * Sets the width.
     *
     * @param width
     *            the new width
     */
	public void setWidth(final Object width) {
		this.delegate.setWidth(String.valueOf(width));
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.tags.ProgressBarTag#setWidth(java.lang.String)
	 */
	@Override
	public void setWidth(final String width) {
		this.delegate.setWidth(width);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		return this.delegate.doEndTag();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		return this.delegate.doStartTag();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#getParent()
	 */
	@Override
	public Tag getParent() {
		return this.delegate.getParent();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	@Override
	public void release() {
		this.delegate.release();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#setPageContext(javax.servlet.jsp.PageContext)
	 */
	@Override
	public void setPageContext(final PageContext pageContext) {
		this.delegate.setPageContext(pageContext);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#setParent(javax.servlet.jsp.tagext.Tag)
	 */
	@Override
	public void setParent(final Tag tag) {
		this.delegate.setParent(tag);
	}
}
