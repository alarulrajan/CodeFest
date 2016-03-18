package com.technoetic.xplanner.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.technoetic.xplanner.util.CookieSupport;

public class CredentialCookie {
	private static final String USERID_COOKIE_NAME = "userid";
	private static final String PASSWORD_COOKIE_NAME = "password";
	private final HttpServletResponse response;
	private final Cookie userId;
	private final Cookie password;

	public CredentialCookie(final HttpServletRequest request,
			final HttpServletResponse response) {
		this.response = response;
		this.userId = CookieSupport.getCookie(
				CredentialCookie.USERID_COOKIE_NAME, request);
		this.password = CookieSupport.getCookie(
				CredentialCookie.PASSWORD_COOKIE_NAME, request);
	}

	public void set(final String userId, final String password) {
		CookieSupport.createCookie(CredentialCookie.USERID_COOKIE_NAME, userId,
				this.response);
		CookieSupport.createCookie(
				CredentialCookie.PASSWORD_COOKIE_NAME,
				new String(com.sabre.security.jndi.util.Base64.encode(password
						.getBytes())), this.response);
	}

	public void remove() {
		CookieSupport.deleteCookie(CredentialCookie.USERID_COOKIE_NAME,
				this.response);
		CookieSupport.deleteCookie(CredentialCookie.PASSWORD_COOKIE_NAME,
				this.response);
	}

	public boolean isPresent() {
		return this.userId != null && this.password != null;
	}

	public String getUserId() {
		return this.userId != null ? this.userId.getValue() : null;
	}

	public String getPassword() {
		return this.password != null ? new String(
				com.sabre.security.jndi.util.Base64.decode(this.password
						.getValue().getBytes())) : null;
	}

}
