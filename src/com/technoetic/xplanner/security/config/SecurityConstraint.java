package com.technoetic.xplanner.security.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.technoetic.xplanner.security.SecurityHelper;

public class SecurityConstraint {
	private final ArrayList webResourceCollections = new ArrayList();
	private final ArrayList authConstraints = new ArrayList();
	private String displayName;

	public void addWebResourceCollection(final WebResourceCollection collection) {
		this.webResourceCollections.add(collection);
	}

	public void addAuthConstraint(final AuthConstraint authConstraint) {
		this.authConstraints.add(authConstraint);
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	public Collection getWebResourceCollection() {
		return this.webResourceCollections;
	}

	public Collection getAuthConstraints() {
		return this.authConstraints;
	}

	public boolean isApplicable(final HttpServletRequest request) {
		final Iterator webResourceCollections = this.getWebResourceCollection()
				.iterator();
		while (webResourceCollections.hasNext()) {
			final WebResourceCollection webResourceCollection = (WebResourceCollection) webResourceCollections
					.next();
			if (webResourceCollection.matches(request)) {
				return true;
			}
		}
		return false;
	}

	public boolean isAuthorized(final HttpServletRequest request) {
		final Iterator authConstraints = this.getAuthConstraints().iterator();
		while (authConstraints.hasNext()) {
			final AuthConstraint authConstraint = (AuthConstraint) authConstraints
					.next();
			final Iterator roleNames = authConstraint.getRoleNames().iterator();
			while (roleNames.hasNext()) {
				final String role = (String) roleNames.next();
				if (SecurityHelper.getSubject(request) != null
						&& role.equals("*")
						|| SecurityHelper.isUserInRole(request, role)) {
					return true;
				}
			}
		}
		return false;
	}
}
