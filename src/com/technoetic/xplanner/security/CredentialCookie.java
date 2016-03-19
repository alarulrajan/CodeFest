package com.technoetic.xplanner.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.technoetic.xplanner.util.CookieSupport;

/**
 * The Class CredentialCookie.
 */
public class CredentialCookie {
	
	/** The Constant USERID_COOKIE_NAME. */
	private static final String USERID_COOKIE_NAME = "userid";
	
	/** The Constant PASSWORD_COOKIE_NAME. */
	private static final String PASSWORD_COOKIE_NAME = "password";
	
	/** The response. */
	private final HttpServletResponse response;
	
	/** The user id. */
	private final Cookie userId;
	
	/** The password. */
	private final Cookie password;

	/**
     * Instantiates a new credential cookie.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     */
	public CredentialCookie(final HttpServletRequest request,
			final HttpServletResponse response) {
		this.response = response;
		this.userId = CookieSupport.getCookie(
				CredentialCookie.USERID_COOKIE_NAME, request);
		this.password = CookieSupport.getCookie(
				CredentialCookie.PASSWORD_COOKIE_NAME, request);
	}

	/**
     * Sets the.
     *
     * @param userId
     *            the user id
     * @param password
     *            the password
     */
	public void set(final String userId, final String password) {
		CookieSupport.createCookie(CredentialCookie.USERID_COOKIE_NAME, userId,
				this.response);
		CookieSupport.createCookie(
				CredentialCookie.PASSWORD_COOKIE_NAME,
				new String(com.sabre.security.jndi.util.Base64.encode(password
						.getBytes())), this.response);
	}

	/**
     * Removes the.
     */
	public void remove() {
		CookieSupport.deleteCookie(CredentialCookie.USERID_COOKIE_NAME,
				this.response);
		CookieSupport.deleteCookie(CredentialCookie.PASSWORD_COOKIE_NAME,
				this.response);
	}

	/**
     * Checks if is present.
     *
     * @return true, if is present
     */
	public boolean isPresent() {
		return this.userId != null && this.password != null;
	}

	/**
     * Gets the user id.
     *
     * @return the user id
     */
	public String getUserId() {
		return this.userId != null ? this.userId.getValue() : null;
	}

	/**
     * Gets the password.
     *
     * @return the password
     */
	public String getPassword() {
		return this.password != null ? new String(
				com.sabre.security.jndi.util.Base64.decode(this.password
						.getValue().getBytes())) : null;
	}

}
