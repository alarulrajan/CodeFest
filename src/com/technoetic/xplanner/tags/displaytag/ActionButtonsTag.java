package com.technoetic.xplanner.tags.displaytag;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts.util.RequestUtils;

import com.technoetic.xplanner.domain.ActionMapping;
import com.technoetic.xplanner.domain.DomainMetaDataRepository;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.views.ActionRenderer;

public class ActionButtonsTag extends BodyTagSupport {
	private Nameable object;
	Iterator iterator;
	private String name;
	private String scope;
	private ActionMapping actionMapping;
	private boolean showOnlyActionWithIcon;
	private DomainMetaDataRepository domainMetaDataRepository;

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getScope() {
		return this.scope;
	}

	public void setScope(final String scope) {
		this.scope = scope;
	}

	public Nameable getObject() throws JspException {
		Nameable object = this.object;
		if (object == null) {
			object = (Nameable) RequestUtils.lookup(this.pageContext,
					this.name, this.scope);
		}

		if (object == null) {
			final JspException e = new JspException("no object");
			RequestUtils.saveException(this.pageContext, e);
			throw e;
		}
		return object;
	}

	public void setObject(final Nameable object) {
		this.object = object;
	}

	public DomainMetaDataRepository getDomainMetaDataRepository() {
		if (this.domainMetaDataRepository == null) {
			this.domainMetaDataRepository = DomainMetaDataRepository
					.getInstance();
		}
		return this.domainMetaDataRepository;
	}

	@Override
	public int doStartTag() throws JspException {
		this.iterator = this.getDomainMetaDataRepository()
				.getMetaData(this.getObject().getClass()).getActions()
				.iterator();
		return this.doAfterBody();
	}

	@Override
	public int doAfterBody() throws JspException {
		try {
			if (this.iterator.hasNext()) {
				this.actionMapping = (ActionMapping) this.iterator.next();
				if ((!this.showOnlyActionWithIcon || this.doesActionHaveIcon())
						&& this.isActionVisible()) {
					this.pageContext.setAttribute(this.id, new ActionRenderer(
							this.actionMapping, this.getObject(),
							this.showOnlyActionWithIcon,
							this.showOnlyActionWithIcon));
					return BodyTag.EVAL_BODY_BUFFERED;
				} else {
					return this.doAfterBody();
				}
			} else {
				return Tag.SKIP_BODY;
			}
		} catch (final Exception e) {
			throw new JspException(e);
		}
	}

	private boolean isActionVisible() throws JspException {
		return this.actionMapping.isVisible(this.getObject());
	}

	private boolean doesActionHaveIcon() {
		return this.actionMapping.getIconPath() != null;
	}

	@Override
	public void release() {
		super.release();
		this.iterator = null;
		this.name = null;
		this.scope = null;
		this.object = null;
		this.id = null;
	}

	@Override
	public int doEndTag() throws JspException {
		this.iterator = null;
		try {
			if (this.bodyContent != null) {
				this.pageContext.getOut().print(this.bodyContent.getString());
			}

		} catch (final IOException e) {
			throw new JspException(e);
		}
		return super.doEndTag();
	}

	public void showOnlyActionWithIcon() {
		this.showOnlyActionWithIcon = true;
	}

	public void setDomainMetaDataRepository(
			final DomainMetaDataRepository domainMetaDataRepository) {

		this.domainMetaDataRepository = domainMetaDataRepository;
	}
}
