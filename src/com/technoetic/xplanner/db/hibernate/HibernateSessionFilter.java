package com.technoetic.xplanner.db.hibernate;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Get the session created by spring filter and set it into the HibernateHelper
 * session instance.
 *
 * @deprecated This is only as a transition to fully move to spring injected
 *             hibernate session management
 */
@Deprecated
public class HibernateSessionFilter extends GenericFilterBean {
    
    /**
     * Lookup session factory.
     *
     * @return the session factory
     */
    protected SessionFactory lookupSessionFactory() {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Using session factory '"
                    + this.getSessionFactoryBeanName() + "'");
        }
        final WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(this.getServletContext());
        return (SessionFactory) wac.getBean(this.getSessionFactoryBeanName());
    }

    /**
     * Gets the session factory bean name.
     *
     * @return the session factory bean name
     */
    private String getSessionFactoryBeanName() {
        return "sessionFactory";
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(final ServletRequest request,
            final ServletResponse aResponse, final FilterChain aChain)
            throws IOException, ServletException {

        try {
            final Session session = (Session) SessionFactoryUtils.getSession(
                    this.lookupSessionFactory(), false);
            this.logger.debug("Retrieved connection: " + session);
            if (session == null) {
                throw new IllegalStateException(
                        "OpenSessionInViewFilter should be before this filter in order to create the session");
            }
            // DEBT(HB) Should only use the ThreadSession
            HibernateHelper.setSession(request, session);
            ThreadSession.set(session);
            aChain.doFilter(request, aResponse);
        } finally {
            HibernateHelper.setSession(request, null);
            ThreadSession.set(null);
        }
    }
}