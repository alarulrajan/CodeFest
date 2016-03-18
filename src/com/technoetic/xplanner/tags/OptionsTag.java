package com.technoetic.xplanner.tags;

import java.util.List;

import javax.servlet.jsp.JspException;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.Authorizer;

public abstract class OptionsTag extends
		org.apache.struts.taglib.html.OptionsTag {
	public final String DEFAULT_LABEL_PROPERTY = "name";
	public final String DEFAULT_PROPERTY = "id";
	protected ContextInitiator contextInitiator;

	@Override
	public int doStartTag() throws JspException {
		this.createContextInitiator();
		return super.doStartTag();
	}

	private void createContextInitiator() {
		this.contextInitiator = new ContextInitiator(this.pageContext);
		this.contextInitiator.initStaticContext();
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			final List selectedBeans = this.getOptions();

			if (this.getProperty() == null) {
				this.setProperty(this.DEFAULT_PROPERTY);
			}
			if (this.getLabelProperty() == null) {
				this.setLabelProperty(this.DEFAULT_LABEL_PROPERTY);
			}
			this.pageContext.setAttribute(ContextInitiator.COLLECTION_KEY,
					selectedBeans);
			this.setCollection(ContextInitiator.COLLECTION_KEY);
			return super.doEndTag();
		} catch (final JspException e) {
			throw e;
		} catch (final Exception e) {
			throw new JspException(e);
		}
	}

	protected abstract List getOptions() throws HibernateException,
			AuthenticationException;

	public Session getSession() {
		return this.contextInitiator.getSession();
	}

	public Authorizer getAuthorizer() {
		return this.contextInitiator.getAuthorizer();
	}

	public int getLoggedInUserId() {
		return this.contextInitiator.getLoggedInUserId();
	}

	public void setSession(final Session session) {
		if (this.contextInitiator == null) {
			this.createContextInitiator();
		}
		this.contextInitiator.setSession(session);
	}

	public void setAuthorizer(final Authorizer authorizer) {
		if (this.contextInitiator == null) {
			this.createContextInitiator();
		}
		this.contextInitiator.setAuthorizer(authorizer);
	}

	public void setLoggedInUserId(final int loggedInUserId) {
		if (this.contextInitiator == null) {
			this.createContextInitiator();
		}
		this.contextInitiator.setLoggedInUserId(loggedInUserId);
	}
}