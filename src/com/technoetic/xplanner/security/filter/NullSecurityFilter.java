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

public class NullSecurityFilter extends AbstractSecurityFilter {
	public final String DEFAULT_USERID_KEY = "defaultUserId";
	public final String AUTHENTICATION_URL_KEY = "authenticatorUrl";
	private String authenticatorUrl;
	private String defaultUserId;

	@Override
	protected void doInit(final FilterConfig filterConfig)
			throws ServletException {
		this.authenticatorUrl = this.getInitParameter(filterConfig,
				this.AUTHENTICATION_URL_KEY, null);
		this.defaultUserId = this.getInitParameter(filterConfig,
				this.DEFAULT_USERID_KEY, "sysadmin");
	}

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

	public void setAuthenticatorUrl(final String authenticatorUrl) {
		this.authenticatorUrl = authenticatorUrl;
	}

	public void setDefaultUserId(final String defaultUserId) {
		this.defaultUserId = defaultUserId;
	}

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
