package com.technoetic.xplanner.security;

import java.io.Serializable;
import java.security.Principal;

import net.sf.xplanner.domain.Person;

/**
 * The Class PersonPrincipal.
 */
public class PersonPrincipal implements Principal, Serializable {
	
	/** The person. */
	private final Person person;

	/**
     * Instantiates a new person principal.
     *
     * @param person
     *            the person
     */
	public PersonPrincipal(final Person person) {
		this.person = person;
	}

	/* (non-Javadoc)
	 * @see java.security.Principal#getName()
	 */
	@Override
	public String getName() {
		return this.person.getUserId();
	}

	/**
     * Gets the person.
     *
     * @return the person
     */
	public Person getPerson() {
		return this.person;
	}
}
