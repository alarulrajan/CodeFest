package com.sabre.security.jndi;

import java.util.List;

/**
 * The Class User.
 */
public class User {
	
	/** The username. */
	String username = null;
	
	/** The dn. */
	String dn = null;
	
	/** The password. */
	String password = null;
	
	/** The roles. */
	List roles = null;

	/**
	 * Instantiates a new user.
	 *
	 * @param username the username
	 * @param dn the dn
	 * @param password the password
	 * @param roles the roles
	 */
	public User(final String username, final String dn, final String password,
			final List roles) {
		this.username = username;
		this.dn = dn;
		this.password = password;
		this.roles = roles;
	}

}
