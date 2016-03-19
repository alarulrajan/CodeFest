package com.technoetic.xplanner.security.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The Class AuthConstraint.
 */
public class AuthConstraint {
	
	/** The role names. */
	private final List<String> roleNames = new ArrayList<String>();

	/**
     * Adds the role name.
     *
     * @param roleName
     *            the role name
     */
	public void addRoleName(final String roleName) {
		this.roleNames.add(roleName);
	}

	/**
     * Gets the role names.
     *
     * @return the role names
     */
	public Collection<String> getRoleNames() {
		return this.roleNames;
	}
}
