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

/**
 * The Class ActionButtonsTag.
 */
public class ActionButtonsTag extends BodyTagSupport {
	
	/** The object. */
	private Nameable object;
	
	/** The iterator. */
	Iterator iterator;
	
	/** The name. */
	private String name;
	
	/** The scope. */
	private String scope;
	
	/** The action mapping. */
	private ActionMapping actionMapping;
	
	/** The show only action with icon. */
	private boolean showOnlyActionWithIcon;
	
	/** The domain meta data repository. */
	private DomainMetaDataRepository domainMetaDataRepository;

	/**
     * Gets the name.
     *
     * @return the name
     */
	public String getName() {
		return this.name;
	}

	/**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
	public void setName(final String name) {
		this.name = name;
	}

	/**
     * Gets the scope.
     *
     * @return the scope
     */
	public String getScope() {
		return this.scope;
	}

	/**
     * Sets the scope.
     *
     * @param scope
     *            the new scope
     */
	public void setScope(final String scope) {
		this.scope = scope;
	}

	/**
     * Gets the object.
     *
     * @return the object
     * @throws JspException
     *             the jsp exception
     */
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

	/**
     * Sets the object.
     *
     * @param object
     *            the new object
     */
	public void setObject(final Nameable object) {
		this.object = object;
	}

	/**
     * Gets the domain meta data repository.
     *
     * @return the domain meta data repository
     */
	public DomainMetaDataRepository getDomainMetaDataRepository() {
		if (this.domainMetaDataRepository == null) {
			this.domainMetaDataRepository = DomainMetaDataRepository
					.getInstance();
		}
		return this.domainMetaDataRepository;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		this.iterator = this.getDomainMetaDataRepository()
				.getMetaData(this.getObject().getClass()).getActions()
				.iterator();
		return this.doAfterBody();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doAfterBody()
	 */
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

	/**
     * Checks if is action visible.
     *
     * @return true, if is action visible
     * @throws JspException
     *             the jsp exception
     */
	private boolean isActionVisible() throws JspException {
		return this.actionMapping.isVisible(this.getObject());
	}

	/**
     * Does action have icon.
     *
     * @return true, if successful
     */
	private boolean doesActionHaveIcon() {
		return this.actionMapping.getIconPath() != null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
	 */
	@Override
	public void release() {
		super.release();
		this.iterator = null;
		this.name = null;
		this.scope = null;
		this.object = null;
		this.id = null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
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

	/**
     * Show only action with icon.
     */
	public void showOnlyActionWithIcon() {
		this.showOnlyActionWithIcon = true;
	}

	/**
     * Sets the domain meta data repository.
     *
     * @param domainMetaDataRepository
     *            the new domain meta data repository
     */
	public void setDomainMetaDataRepository(
			final DomainMetaDataRepository domainMetaDataRepository) {

		this.domainMetaDataRepository = domainMetaDataRepository;
	}
}
