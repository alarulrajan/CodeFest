package com.technoetic.xplanner.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

/**
 * The Class ContextInitiator.
 */
public class ContextInitiator {
	
	/** The Constant COLLECTION_KEY. */
	public static final String COLLECTION_KEY = "optionCollection";
	
	/** The page context. */
	private final PageContext pageContext;
	
	/** The session. */
	private Session session;
	
	/** The authorizer. */
	private Authorizer authorizer;
	
	/** The logged in user id. */
	private int loggedInUserId;

	/**
     * Instantiates a new context initiator.
     *
     * @param pageContext
     *            the page context
     */
	public ContextInitiator(final PageContext pageContext) {
		this.pageContext = pageContext;
	}

	/**
     * Inits the static context.
     */
	public void initStaticContext() {
		this.setSession(ThreadSession.get());
		this.setAuthorizer(SystemAuthorizer.get());
		try {
			this.setLoggedInUserId(this.getRemoteUserId());
		} catch (final JspException e) {
			e.printStackTrace();
		}
	}

	/**
     * Gets the remote user id.
     *
     * @return the remote user id
     * @throws JspException
     *             the jsp exception
     */
	private int getRemoteUserId() throws JspException {
		try {
			return SecurityHelper.getRemoteUserId(this.pageContext);
		} catch (final AuthenticationException e) {
			throw new JspException(e);
		}
	}

	/**
     * Gets the session.
     *
     * @return the session
     */
	public Session getSession() {
		return this.session;
	}

	/**
     * Gets the authorizer.
     *
     * @return the authorizer
     */
	public Authorizer getAuthorizer() {
		return this.authorizer;
	}

	/**
     * Gets the logged in user id.
     *
     * @return the logged in user id
     */
	public int getLoggedInUserId() {
		return this.loggedInUserId;
	}

	/**
     * Sets the session.
     *
     * @param session
     *            the new session
     */
	public void setSession(final Session session) {
		this.session = session;
	}

	/**
     * Sets the authorizer.
     *
     * @param authorizer
     *            the new authorizer
     */
	public void setAuthorizer(final Authorizer authorizer) {
		this.authorizer = authorizer;
	}

	/**
     * Sets the logged in user id.
     *
     * @param loggedInUserId
     *            the new logged in user id
     */
	public void setLoggedInUserId(final int loggedInUserId) {
		this.loggedInUserId = loggedInUserId;
	}
}
