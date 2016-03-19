package com.technoetic.xplanner.soap.domain;

import net.sf.xplanner.domain.Person;

/**
 * The Class PersonData.
 */
public class PersonData extends DomainData {
	
	/** The name. */
	private String name;
	
	/** The email. */
	private String email;
	
	/** The phone. */
	private String phone;
	
	/** The initials. */
	private String initials;
	
	/** The user id. */
	private String userId;

	/**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
	public void setName(final String name) {
		this.name = name;
	}

	/**
     * Gets the name.
     *
     * @return the name
     */
	public String getName() {
		return this.name;
	}

	/**
     * Sets the email.
     *
     * @param email
     *            the new email
     */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
     * Gets the email.
     *
     * @return the email
     */
	public String getEmail() {
		return this.email;
	}

	/**
     * Sets the phone.
     *
     * @param phone
     *            the new phone
     */
	public void setPhone(final String phone) {
		this.phone = phone;
	}

	/**
     * Gets the phone.
     *
     * @return the phone
     */
	public String getPhone() {
		return this.phone;
	}

	/**
     * Sets the initials.
     *
     * @param initials
     *            the new initials
     */
	public void setInitials(final String initials) {
		this.initials = initials;
	}

	/**
     * Gets the initials.
     *
     * @return the initials
     */
	public String getInitials() {
		return this.initials;
	}

	/**
     * Gets the user id.
     *
     * @return the user id
     */
	public String getUserId() {
		return this.userId;
	}

	/**
     * Sets the user id.
     *
     * @param userId
     *            the new user id
     */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
     * Gets the internal class.
     *
     * @return the internal class
     */
	public static Class getInternalClass() {
		return Person.class;
	}
}