package com.technoetic.xplanner.security;

import java.io.Serializable;
import java.security.Principal;

import net.sf.xplanner.domain.Person;

public class PersonPrincipal implements Principal, Serializable {
	private final Person person;

	public PersonPrincipal(final Person person) {
		this.person = person;
	}

	@Override
	public String getName() {
		return this.person.getUserId();
	}

	public Person getPerson() {
		return this.person;
	}
}
