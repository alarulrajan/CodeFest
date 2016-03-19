package com.technoetic.xplanner.security.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.technoetic.xplanner.security.SecurityHelper;

/**
 * The Class SecurityConstraint.
 */
public class SecurityConstraint {
	
	/** The web resource collections. */
	private final ArrayList webResourceCollections = new ArrayList();
	
	/** The auth constraints. */
	private final ArrayList authConstraints = new ArrayList();
	
	/** The display name. */
	private String displayName;

	/**
     * Adds the web resource collection.
     *
     * @param collection
     *            the collection
     */
	public void addWebResourceCollection(final WebResourceCollection collection) {
		this.webResourceCollections.add(collection);
	}

	/**
     * Adds the auth constraint.
     *
     * @param authConstraint
     *            the auth constraint
     */
	public void addAuthConstraint(final AuthConstraint authConstraint) {
		this.authConstraints.add(authConstraint);
	}

	/**
     * Gets the display name.
     *
     * @return the display name
     */
	public String getDisplayName() {
		return this.displayName;
	}

	/**
     * Sets the display name.
     *
     * @param displayName
     *            the new display name
     */
	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	/**
     * Gets the web resource collection.
     *
     * @return the web resource collection
     */
	public Collection getWebResourceCollection() {
		return this.webResourceCollections;
	}

	/**
     * Gets the auth constraints.
     *
     * @return the auth constraints
     */
	public Collection getAuthConstraints() {
		return this.authConstraints;
	}

	/**
     * Checks if is applicable.
     *
     * @param request
     *            the request
     * @return true, if is applicable
     */
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

	/**
     * Checks if is authorized.
     *
     * @param request
     *            the request
     * @return true, if is authorized
     */
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
