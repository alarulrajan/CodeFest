package net.sf.xplanner.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.technoetic.xplanner.domain.Nameable;

/**
 * XplannerPlus, agile planning software.
 *
 * @author Maksym_Chyrkov. Copyright (C) 2009 Maksym Chyrkov This program is
 *         free software: you can redistribute it and/or modify it under the
 *         terms of the GNU General Public License as published by the Free
 *         Software Foundation, either version 3 of the License, or (at your
 *         option) any later version.
 * 
 *         This program is distributed in the hope that it will be useful, but
 *         WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with this program. If not, see <http://www.gnu.org/licenses/>
 */

@Entity
@Table(name = "person", uniqueConstraints = @UniqueConstraint(columnNames = "userId"))
public class Person extends DomainObject implements java.io.Serializable,
		Nameable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8209945280001173802L;
	
	/** The Constant USER_ID. */
	public static final String USER_ID = "userId";
	
	/** The email. */
	private String email;
	
	/** The phone. */
	private String phone;
	
	/** The initials. */
	private String initials;
	
	/** The user id. */
	private String userId;
	
	/** The password. */
	private String password;
	
	/** The is hidden. */
	private Boolean isHidden;
	
	/** The name. */
	private String name;

	/**
     * Instantiates a new person.
     */
	public Person() {
	}

	/**
     * Public constructor.
     *
     * @param userId
     *            user identity
     */
	public Person(final String userId) {
		this.userId = userId;
	}

	/**
     * Gets the email.
     *
     * @return the email
     */
	@Column(name = "email")
	public String getEmail() {
		return this.email;
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
     * Gets the phone.
     *
     * @return the phone
     */
	@Column(name = "phone")
	public String getPhone() {
		return this.phone;
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
     * Gets the initials.
     *
     * @return the initials
     */
	@Column(name = "initials")
	public String getInitials() {
		return this.initials;
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
     * Gets the user id.
     *
     * @return the user id
     */
	@Column(name = "userId", unique = true)
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
     * Gets the password.
     *
     * @return the password
     */
	@Column(name = "password")
	public String getPassword() {
		return this.password;
	}

	/**
     * Sets the password.
     *
     * @param password
     *            the new password
     */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
     * Gets the hidden.
     *
     * @return the hidden
     */
	@Column(name = "is_hidden")
	public Boolean getHidden() {
		return this.isHidden;
	}

	/**
     * Sets the hidden.
     *
     * @param isHidden
     *            the new hidden
     */
	public void setHidden(final Boolean isHidden) {
		this.isHidden = isHidden;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.Nameable#getName()
	 */
	@Override
	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	/**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
	public void setName(final String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.Nameable#getDescription()
	 */
	@Override
	@Transient
	public String getDescription() {
		return null;
	}

}
