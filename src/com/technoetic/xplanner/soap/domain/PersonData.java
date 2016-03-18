package com.technoetic.xplanner.soap.domain;

import net.sf.xplanner.domain.Person;

public class PersonData extends DomainData {
	private String name;
	private String email;
	private String phone;
	private String initials;
	private String userId;

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setInitials(final String initials) {
		this.initials = initials;
	}

	public String getInitials() {
		return this.initials;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public static Class getInternalClass() {
		return Person.class;
	}
}