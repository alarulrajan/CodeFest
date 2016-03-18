package com.technoetic.xplanner.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

public class ContentTitleArgTag extends BodyTagSupport {
	private Object value;

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

	public void setValue(final Object value) {
		this.value = value;
	}

	@Override
	public void release() {
		super.release();
		this.value = null;
	}
}
