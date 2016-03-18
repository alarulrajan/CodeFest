package com.technoetic.xplanner.tags.displaytag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.properties.MediaTypeEnum;
import org.displaytag.util.TagConstants;

import com.technoetic.xplanner.tags.WritableTag;

public class ActionButtonsColumnTag extends org.displaytag.tags.ColumnTag {
	// TODO: why not use our ColumnTag instead for consistency?
	// public class ActionButtonsColumnTag extends
	// com.technoetic.xplanner.tags.displaytag.ColumnTag {
	private static Log log = LogFactory.getLog(ActionButtonsColumnTag.class);
	ActionButtonsTag actionButtonsTag;

	public void setActionButtonsTag(final ActionButtonsTag actionButtonsTag) {
		this.actionButtonsTag = actionButtonsTag;
	}

	public ActionButtonsColumnTag() {
		this.setMedia(MediaTypeEnum.HTML.getName());
		this.actionButtonsTag = new ActionButtonsTag();
		this.actionButtonsTag.showOnlyActionWithIcon();
	}

	@Override
	public void setPageContext(final PageContext context) {
		super.setPageContext(context);
		this.actionButtonsTag.setPageContext(context);
	}

	@Override
	public void setId(final String s) {
		this.id = s;
		this.actionButtonsTag.setId(s);
	}

	public String getName() {
		return this.actionButtonsTag.getName();
	}

	public void setName(final String name) {
		this.actionButtonsTag.setName(name);
	}

	public String getScope() {
		return this.actionButtonsTag.getScope();
	}

	public void setScope(final String scope) {
		this.actionButtonsTag.setScope(scope);
	}

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

	@Override
	public int doAfterBody() throws JspException {
		return this.actionButtonsTag.doAfterBody();
	}

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

	@Override
	public void release() {
		this.actionButtonsTag.release();
	}
}
