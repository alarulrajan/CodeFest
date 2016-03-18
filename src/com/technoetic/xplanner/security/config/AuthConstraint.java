package com.technoetic.xplanner.security.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthConstraint {
	private final List<String> roleNames = new ArrayList<String>();

	public void addRoleName(final String roleName) {
		this.roleNames.add(roleName);
	}

	public Collection<String> getRoleNames() {
		return this.roleNames;
	}
}
