package com.technoetic.xplanner.tags;

import java.util.List;

import javax.servlet.jsp.JspException;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.Authorizer;

/**
 * The Class OptionsTag.
 */
public abstract class OptionsTag extends
        org.apache.struts.taglib.html.OptionsTag {
    
    /** The default label property. */
    public final String DEFAULT_LABEL_PROPERTY = "name";
    
    /** The default property. */
    public final String DEFAULT_PROPERTY = "id";
    
    /** The context initiator. */
    protected ContextInitiator contextInitiator;

    /* (non-Javadoc)
     * @see org.apache.struts.taglib.html.OptionsTag#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        this.createContextInitiator();
        return super.doStartTag();
    }

    /**
     * Creates the context initiator.
     */
    private void createContextInitiator() {
        this.contextInitiator = new ContextInitiator(this.pageContext);
        this.contextInitiator.initStaticContext();
    }

    /* (non-Javadoc)
     * @see org.apache.struts.taglib.html.OptionsTag#doEndTag()
     */
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

    /**
     * Gets the options.
     *
     * @return the options
     * @throws HibernateException
     *             the hibernate exception
     * @throws AuthenticationException
     *             the authentication exception
     */
    protected abstract List getOptions() throws HibernateException,
            AuthenticationException;

    /**
     * Gets the session.
     *
     * @return the session
     */
    public Session getSession() {
        return this.contextInitiator.getSession();
    }

    /**
     * Gets the authorizer.
     *
     * @return the authorizer
     */
    public Authorizer getAuthorizer() {
        return this.contextInitiator.getAuthorizer();
    }

    /**
     * Gets the logged in user id.
     *
     * @return the logged in user id
     */
    public int getLoggedInUserId() {
        return this.contextInitiator.getLoggedInUserId();
    }

    /**
     * Sets the session.
     *
     * @param session
     *            the new session
     */
    public void setSession(final Session session) {
        if (this.contextInitiator == null) {
            this.createContextInitiator();
        }
        this.contextInitiator.setSession(session);
    }

    /**
     * Sets the authorizer.
     *
     * @param authorizer
     *            the new authorizer
     */
    public void setAuthorizer(final Authorizer authorizer) {
        if (this.contextInitiator == null) {
            this.createContextInitiator();
        }
        this.contextInitiator.setAuthorizer(authorizer);
    }

    /**
     * Sets the logged in user id.
     *
     * @param loggedInUserId
     *            the new logged in user id
     */
    public void setLoggedInUserId(final int loggedInUserId) {
        if (this.contextInitiator == null) {
            this.createContextInitiator();
        }
        this.contextInitiator.setLoggedInUserId(loggedInUserId);
    }
}