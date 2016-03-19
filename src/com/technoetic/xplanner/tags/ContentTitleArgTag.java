package com.technoetic.xplanner.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * The Class ContentTitleArgTag.
 */
public class ContentTitleArgTag extends BodyTagSupport {
	
	/** The value. */
	private Object value;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		final ContentTitleTag tag = (ContentTitleTag) TagSupport
				.findAncestorWithClass(this, ContentTitleTag.class);
		if (this.value != null) {
			tag.addTitleArgument(this.value);
		} else {
			tag.addTitleArgument(this.getBodyContent().getString());
		}
		return Tag.EVAL_PAGE;
	}

	/**
     * Sets the value.
     *
     * @param value
     *            the new value
     */
	public void setValue(final Object value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
	 */
	@Override
	public void release() {
		super.release();
		this.value = null;
	}
}
