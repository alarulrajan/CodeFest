package com.technoetic.xplanner.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.technoetic.xplanner.XPlannerProperties;

public class PropertyEqualTag extends TagSupport {
	private final XPlannerProperties properties = new XPlannerProperties();

	private String key;
	private String value;

	@Override
	public int doStartTag() throws JspException {
		return StringUtils.equals(this.properties.getProperty(this.key),
				this.value) ? Tag.EVAL_BODY_INCLUDE : Tag.SKIP_BODY;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public void setValue(final String value) {
		this.value = value;
	}
}
