package com.technoetic.xplanner.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

public class ContextInitiator {
	public static final String COLLECTION_KEY = "optionCollection";
	private final PageContext pageContext;
	private Session session;
	private Authorizer authorizer;
	private int loggedInUserId;

	public ContextInitiator(final PageContext pageContext) {
		this.pageContext = pageContext;
	}

	public void initStaticContext() {
		this.setSession(ThreadSession.get());
		this.setAuthorizer(SystemAuthorizer.get());
		try {
			this.setLoggedInUserId(this.getRemoteUserId());
		} catch (final JspException e) {
			e.printStackTrace();
		}
	}

	private int getRemoteUserId() throws JspException {
		try {
			return SecurityHelper.getRemoteUserId(this.pageContext);
		} catch (final AuthenticationException e) {
			throw new JspException(e);
		}
	}

	public Session getSession() {
		return this.session;
	}

	public Authorizer getAuthorizer() {
		return this.authorizer;
	}

	public int getLoggedInUserId() {
		return this.loggedInUserId;
	}

	public void setSession(final Session session) {
		this.session = session;
	}

	public void setAuthorizer(final Authorizer authorizer) {
		this.authorizer = authorizer;
	}

	public void setLoggedInUserId(final int loggedInUserId) {
		this.loggedInUserId = loggedInUserId;
	}
}
