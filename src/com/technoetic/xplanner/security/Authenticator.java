package com.technoetic.xplanner.security;

import javax.servlet.http.HttpServletRequest;

/**
 * The Interface Authenticator.
 */
public interface Authenticator {
	
	/**
     * Authenticate.
     *
     * @param request
     *            the request
     * @param userId
     *            the user id
     * @param password
     *            the password
     * @throws AuthenticationException
     *             the authentication exception
     */
	void authenticate(HttpServletRequest request, String userId, String password)
			throws AuthenticationException;

	/**
     * Logout.
     *
     * @param request
     *            the request
     * @param principalId
     *            the principal id
     * @throws AuthenticationException
     *             the authentication exception
     */
	void logout(HttpServletRequest request, int principalId)
			throws AuthenticationException;
}
