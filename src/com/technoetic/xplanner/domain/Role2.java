package com.technoetic.xplanner.domain;

import java.io.Serializable;
import java.security.Principal;

import net.sf.xplanner.domain.NamedObject;

public class Role2 extends NamedObject implements Principal, Serializable {
	public static final String SYSADMIN = "sysadmin";
	public static final String ADMIN = "admin";
	public static final String EDITOR = "editor";
	public static final String VIEWER = "viewer";
	private int left;
	private int right;

	Role2() {
		// for Hibernate
	}

	public Role2(final String name) {
		this.setName(name);
	}

	@Override
	public String toString() {
		return this.getName();
	}

	public int getLeft() {
		return this.left;
	}

	public void setLeft(final int left) {
		this.left = left;
	}

	public int getRight() {
		return this.right;
	}

	public void setRight(final int right) {
		this.right = right;
	}

	public boolean isSysadmin() {
		return this.getName().equals(Role2.SYSADMIN);
	}

	@Override
	public String getDescription() {
		return "";
	}
}
