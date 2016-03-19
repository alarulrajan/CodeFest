package com.technoetic.xplanner.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.technoetic.xplanner.XPlannerProperties;

/**
 * The Class PropertyTag.
 */
public class PropertyTag extends TagSupport {
	
	/** The key. */
	private String key;
	
	/** The properties. */
	private final XPlannerProperties properties = new XPlannerProperties();

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
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

	/**
     * Sets the key.
     *
     * @param key
     *            the new key
     */
	public void setKey(final String key) {
		this.key = key;
	}
}