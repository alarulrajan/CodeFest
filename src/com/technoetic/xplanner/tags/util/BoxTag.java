/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Jan 2, 2006
 * Time: 7:07:47 PM
 */
package com.technoetic.xplanner.tags.util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.util.ResponseUtils;

public class BoxTag extends BodyTagSupport {
	private String id;
	private String styleClass = "box";
	private String title;

	@Override
	public int doEndTag() throws JspException {
		String id = this.id;
		if (id == null) {
			id = this.title;
		}
		final StringBuffer results = new StringBuffer();
		results.append("<div class=\"" + this.styleClass + "\">");
		results.append("<h3 style=\"border-bottom-width:0px\">");
		results.append("<span class=\"boxIcon\">");
		results.append("   <span class=\"boxIconText\" id=\"" + id
				+ "-switch\" onclick=\"toggle('" + id + "')\">[hide]</span>\n");
		results.append("</span>");
		results.append(this.title);
		results.append("</h3>");
		results.append("<div id=\"" + id + "\" class=\"boxBody\">");
		this.renderBody(results);
		results.append("</div>");
		results.append("</div>");

		ResponseUtils.write(this.pageContext, results.toString());

		return super.doEndTag();
	}

	protected void renderBody(final StringBuffer results) {
		if (this.bodyContent != null) {
			results.append(this.bodyContent.getString());
		}
	}

	public void setStyleClass(final String styleClass) {
		this.styleClass = styleClass;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Override
	public void setId(final String id) {
		this.id = id;
	}
}