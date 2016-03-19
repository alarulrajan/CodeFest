package com.technoetic.xplanner.security.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.Subject;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Person;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.security.PersonPrincipal;
import com.technoetic.xplanner.security.SecurityHelper;

/**
 * The Class NullSecurityFilter.
 */
public class NullSecurityFilter extends AbstractSecurityFilter {
    
    /** The default userid key. */
    public final String DEFAULT_USERID_KEY = "defaultUserId";
    
    /** The authentication url key. */
    public final String AUTHENTICATION_URL_KEY = "authenticatorUrl";
    
    /** The authenticator url. */
    private String authenticatorUrl;
    
    /** The default user id. */
    private String defaultUserId;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.filter.AbstractSecurityFilter#doInit(javax.servlet.FilterConfig)
     */
    @Override
    protected void doInit(final FilterConfig filterConfig)
            throws ServletException {
        this.authenticatorUrl = this.getInitParameter(filterConfig,
                this.AUTHENTICATION_URL_KEY, null);
        this.defaultUserId = this.getInitParameter(filterConfig,
                this.DEFAULT_USERID_KEY, "sysadmin");
    }

    /**
     * Gets the inits the parameter.
     *
     * @param filterConfig
     *            the filter config
     * @param parameterName
     *            the parameter name
     * @param defaultValue
     *            the default value
     * @return the inits the parameter
     * @throws ServletException
     *             the servlet exception
     */
    private String getInitParameter(final FilterConfig filterConfig,
            final String parameterName, final String defaultValue)
            throws ServletException {
        String value = filterConfig.getInitParameter(parameterName);
        if (StringUtils.isEmpty(value)) {
            if (defaultValue == null) {
                throw new ServletException(this.getClass().getName() + ": "
                        + parameterName + " is required");
            } else {
                value = defaultValue;
            }
        }
        return value;
    }

    /**
     * Sets the authenticator url.
     *
     * @param authenticatorUrl
     *            the new authenticator url
     */
    public void setAuthenticatorUrl(final String authenticatorUrl) {
        this.authenticatorUrl = authenticatorUrl;
    }

    /**
     * Sets the default user id.
     *
     * @param defaultUserId
     *            the new default user id
     */
    public void setDefaultUserId(final String defaultUserId) {
        this.defaultUserId = defaultUserId;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.filter.AbstractSecurityFilter#isAuthenticated(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected boolean isAuthenticated(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        // This could also be implemented in a special login module
        if (!this.isSubjectInSession(request)) {
            try {
                final Person person = this.getPerson(ThreadSession.get(),
                        this.defaultUserId);
                final Subject subject = new Subject();
                subject.getPrincipals().add(new PersonPrincipal(person));
                SecurityHelper.setSubject(request, subject);
            } catch (final HibernateException e) {
                throw new ServletException(e);
            }
        }
        return true;
    }

    /**
     * Gets the person.
     *
     * @param session
     *            the session
     * @param userId
     *            the user id
     * @return the person
     * @throws HibernateException
     *             the hibernate exception
     */
    // todo - these methods should be moved to an object repository
    private Person getPerson(final Session session, final String userId)
            throws HibernateException {
        final List people = session.find(
                "from person in class " + Person.class.getName()
                        + " where userid = ?", userId, Hibernate.STRING);
        Person person = null;
        final Iterator peopleIterator = people.iterator();
        if (peopleIterator.hasNext()) {
            person = (Person) peopleIterator.next();
        }
        return person;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.filter.AbstractSecurityFilter#onAuthenticationFailure(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public boolean onAuthenticationFailure(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        // Should never happen
        this.log.error("default authentication failed!");
        final String redirectUrl = this.authenticatorUrl;
        this.log.debug(request.getRequestURL() + " being redirected to "
                + redirectUrl);
        response.sendRedirect(request.getContextPath() + redirectUrl);
        return false;
    }
}
