package com.technoetic.xplanner.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

import org.apache.struts.taglib.tiles.InsertTag;
import org.apache.struts.tiles.DirectStringAttribute;

public class ContentTag extends InsertTag implements BodyTag {
	private BodyContent bodyContent;

	@Override
	public int doStartTag() throws JspException {
		if (PrintLinkTag.isInPrintMode(this.pageContext)) {
			this.definitionName = "tiles:print";
		} else if (this.definitionName == null
				|| this.definitionName == "tiles:print") {
			this.definitionName = "tiles:default";
		}
		super.doStartTag();
		return BodyTag.EVAL_BODY_BUFFERED;
	}

	@Override
	public void doInitBody() throws JspException {
		// empty
	}

	@Override
	public void setBodyContent(final BodyContent bodyContent) {
		this.bodyContent = bodyContent;
	}

	@Override
	public int doEndTag() throws JspException {
		this.putAttribute("body",
				new DirectStringAttribute(this.bodyContent.getString()));
		return super.doEndTag();
	}

	@Override
	public void release() {
		this.bodyContent = null;
		super.release();
	}
}
