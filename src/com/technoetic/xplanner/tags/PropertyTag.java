package com.technoetic.xplanner.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.technoetic.xplanner.XPlannerProperties;

public class PropertyTag extends TagSupport {
	private String key;
	private final XPlannerProperties properties = new XPlannerProperties();

	@Override
	public int doEndTag() throws JspException {
		try {
			this.pageContext.getOut().print(
					this.properties.getProperty(this.key));
		} catch (final IOException ex) {
			throw new JspException("IO error");
		}
		return Tag.EVAL_PAGE;
	}

	public void setKey(final String key) {
		this.key = key;
	}
}