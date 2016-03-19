package com.technoetic.xplanner.tags.displaytag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.properties.MediaTypeEnum;
import org.displaytag.util.TagConstants;

import com.technoetic.xplanner.tags.WritableTag;

/**
 * The Class ActionButtonsColumnTag.
 */
public class ActionButtonsColumnTag extends org.displaytag.tags.ColumnTag {
	// ChangeSoon: why not use our ColumnTag instead for consistency?
	// public class ActionButtonsColumnTag extends
	/** The log. */
	// com.technoetic.xplanner.tags.displaytag.ColumnTag {
	private static Log log = LogFactory.getLog(ActionButtonsColumnTag.class);
	
	/** The action buttons tag. */
	ActionButtonsTag actionButtonsTag;

	/**
     * Sets the action buttons tag.
     *
     * @param actionButtonsTag
     *            the new action buttons tag
     */
	public void setActionButtonsTag(final ActionButtonsTag actionButtonsTag) {
		this.actionButtonsTag = actionButtonsTag;
	}

	/**
     * Instantiates a new action buttons column tag.
     */
	public ActionButtonsColumnTag() {
		this.setMedia(MediaTypeEnum.HTML.getName());
		this.actionButtonsTag = new ActionButtonsTag();
		this.actionButtonsTag.showOnlyActionWithIcon();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#setPageContext(javax.servlet.jsp.PageContext)
	 */
	@Override
	public void setPageContext(final PageContext context) {
		super.setPageContext(context);
		this.actionButtonsTag.setPageContext(context);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#setId(java.lang.String)
	 */
	@Override
	public void setId(final String s) {
		this.id = s;
		this.actionButtonsTag.setId(s);
	}

	/**
     * Gets the name.
     *
     * @return the name
     */
	public String getName() {
		return this.actionButtonsTag.getName();
	}

	/**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
	public void setName(final String name) {
		this.actionButtonsTag.setName(name);
	}

	/**
     * Gets the scope.
     *
     * @return the scope
     */
	public String getScope() {
		return this.actionButtonsTag.getScope();
	}

	/**
     * Sets the scope.
     *
     * @param scope
     *            the new scope
     */
	public void setScope(final String scope) {
		this.actionButtonsTag.setScope(scope);
	}

	/* (non-Javadoc)
	 * @see org.displaytag.tags.ColumnTag#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		try {
			final WritableTag parentTable = (WritableTag) this.getParent();
			if (!parentTable.isWritable()) {
				return Tag.SKIP_BODY;
			}
			if (!this.getAttributeMap().containsKey(
					TagConstants.ATTRIBUTE_NOWRAP)) {
				this.getAttributeMap().put(TagConstants.ATTRIBUTE_NOWRAP,
						"true");
			}

			final int status = super.doStartTag();
			if (status != Tag.SKIP_BODY) {
				return this.actionButtonsTag.doStartTag();
			}
			return status;
		} catch (final Exception e) {
			throw new JspException(e);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doAfterBody()
	 */
	@Override
	public int doAfterBody() throws JspException {
		return this.actionButtonsTag.doAfterBody();
	}

	/* (non-Javadoc)
	 * @see org.displaytag.tags.ColumnTag#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		this.actionButtonsTag.doEndTag();
		try {
			final WritableTag parentTable = (WritableTag) this.getParent();
			if (!parentTable.isWritable()) {
				return Tag.SKIP_BODY;
			} else {
				return super.doEndTag();
			}
		} catch (final Exception e) {
			throw new JspException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.displaytag.tags.ColumnTag#release()
	 */
	@Override
	public void release() {
		this.actionButtonsTag.release();
	}
}
