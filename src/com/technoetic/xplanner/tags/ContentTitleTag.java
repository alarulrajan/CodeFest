package com.technoetic.xplanner.tags;

import java.text.MessageFormat;
import java.util.ArrayList;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;

/**
 * The Class ContentTitleTag.
 */
public class ContentTitleTag extends BodyTagSupport {
	
	/** The title arguments. */
	private ArrayList titleArguments;
	
	/** The title. */
	private String title;
	
	/** The title key. */
	private String titleKey;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		this.titleArguments = new ArrayList();
		return super.doStartTag();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		String formattedTitle = null;
		if (this.title == null) {
			if (this.titleKey != null) {
				final MessageResources resources = (MessageResources) this.pageContext
						.findAttribute(Globals.MESSAGES_KEY);
				if (resources == null) {
					throw new JspException("no resource bundle in request");
				}
				formattedTitle = resources.getMessage(TagUtils.getInstance()
						.getUserLocale(this.pageContext, ""), this.titleKey,
						this.titleArguments.toArray());
			} else {
				formattedTitle = this.getBodyContent().getString().trim();
			}
		} else {
			formattedTitle = MessageFormat.format(this.title,
					this.titleArguments.toArray());
		}
		final ContentTag tag = (ContentTag) TagSupport.findAncestorWithClass(
				this, ContentTag.class);
		tag.putAttribute("title", formattedTitle);
		return super.doEndTag();
	}

	/**
     * Sets the title.
     *
     * @param title
     *            the new title
     */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
     * Sets the title key.
     *
     * @param titleKey
     *            the new title key
     */
	public void setTitleKey(final String titleKey) {
		this.titleKey = titleKey;
	}

	/**
     * Adds the title argument.
     *
     * @param value
     *            the value
     */
	public void addTitleArgument(final Object value) {
		this.titleArguments.add(value);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
	 */
	@Override
	public void release() {
		this.title = null;
		this.titleKey = null;
		this.titleArguments = null;
		super.release();
	}
}
