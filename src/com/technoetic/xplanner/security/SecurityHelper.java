package com.technoetic.xplanner.security;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import net.sf.xplanner.domain.Role;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.security.module.LoginModuleLoader;

public class SecurityHelper {
	public static final String SECURITY_SUBJECT_KEY = "SECURITY_SUBJECT";
	private static final String SAVED_URL_KEY = "SAVED_URL";

	public static boolean isUserAuthenticated(final HttpServletRequest request) {
		try {
			return SecurityHelper.getSubject(request) != null
					&& ((PersonPrincipal) SecurityHelper
							.getUserPrincipal(SecurityHelper
									.getSubject(request))).getPerson().getId() != 0;
		} catch (final Exception e) {
			return false;
		}
	}

	public static int getRemoteUserId(final HttpServletRequest request)
			throws AuthenticationException {
		return ((PersonPrincipal) SecurityHelper.getUserPrincipal(request))
				.getPerson().getId();
	}

	public static int getRemoteUserId(final PageContext context)
			throws AuthenticationException {
		return SecurityHelper.getRemoteUserId((HttpServletRequest) context
				.getRequest());
	}

	public static boolean isUserInRole(final HttpServletRequest request,
			final String roleName) {
		final Subject subject = SecurityHelper.getSubject(request);
		if (subject != null) {
			final Iterator roles = subject.getPrincipals(Role.class).iterator();
			while (roles.hasNext()) {
				final Role role = (Role) roles.next();
				if (role.getName().equals(roleName)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void setSubject(final HttpServletRequest request,
			final Subject subject) {
		request.getSession(true).setAttribute(
				SecurityHelper.SECURITY_SUBJECT_KEY, subject);
	}

	public static Subject getSubject(final HttpServletRequest request) {
		return (Subject) request.getSession().getAttribute(
				SecurityHelper.SECURITY_SUBJECT_KEY);
	}

	public static Subject getSubject(final PageContext context) {
		return SecurityHelper.getSubject((HttpServletRequest) context
				.getRequest());
	}

	public static Principal getUserPrincipal(final Subject subject)
			throws AuthenticationException {
		if (subject != null) {
			final Iterator people = subject
					.getPrincipals(PersonPrincipal.class).iterator();
			if (people.hasNext()) {
				return (PersonPrincipal) people.next();
			}
		}
		throw new AuthenticationException("no user principal in session");
	}

	public static void saveUrl(final HttpServletRequest request) {
		request.getSession().setAttribute(
				SecurityHelper.SAVED_URL_KEY,
				request.getRequestURL().toString() + "?"
						+ request.getQueryString());
	}

	public static String getSavedUrl(final HttpServletRequest request) {
		return (String) request.getSession().getAttribute(
				SecurityHelper.SAVED_URL_KEY);
	}

	public static Principal getUserPrincipal(final HttpServletRequest request)
			throws AuthenticationException {
		return SecurityHelper.getUserPrincipal(SecurityHelper
				.getSubject(request));
	}

	public static Subject addRolesToSubject(Subject subject,
			final ArrayList roles) {
		// This approach is required because some servlet ISP's set up
		// security so that Subject cannot be modified even if it is not
		// in read-only mode.
		final java.util.HashSet principals = new java.util.HashSet();
		principals.addAll(subject.getPrincipals());
		principals.addAll(roles);
		subject = new Subject(true, principals, subject.getPublicCredentials(),
				subject.getPrivateCredentials());
		return subject;
	}

	// FIXME: Is it right that the authentication be case sensitive if at least
	// one module is?
	public static boolean isAuthenticationCaseSensitive() {
		final XPlannerProperties properties = new XPlannerProperties();
		final Iterator propertiesIterator = properties.getPropertyNames();
		while (propertiesIterator.hasNext()) {
			final String property = (String) propertiesIterator.next();
			if (property != null
					&& property
							.startsWith(LoginModuleLoader.LOGIN_MODULE_PROPERTY_PREFIX
									+ "[")
					&& property.endsWith("].option.userIdCaseSensitive")) {
				if (new Boolean(properties.getProperty(property).trim())
						.booleanValue()) {
					return true;
				}
			}
		}
		return false;
	}

}
