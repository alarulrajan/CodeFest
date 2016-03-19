package com.technoetic.xplanner.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.technoetic.xplanner.XPlannerProperties;

/**
 * The Class PropertyEqualTag.
 */
public class PropertyEqualTag extends TagSupport {
	
	/** The properties. */
	private final XPlannerProperties properties = new XPlannerProperties();

	/** The key. */
	private String key;
	
	/** The value. */
	private String value;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		return StringUtils.equals(this.properties.getProperty(this.key),
				this.value) ? Tag.EVAL_BODY_INCLUDE : Tag.SKIP_BODY;
	}

	/**
     * Sets the key.
     *
     * @param key
     *            the new key
     */
	public void setKey(final String key) {
		this.key = key;
	}

	/**
     * Sets the value.
     *
     * @param value
     *            the new value
     */
	public void setValue(final String value) {
		this.value = value;
	}
}
