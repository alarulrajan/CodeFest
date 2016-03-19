package com.technoetic.xplanner.filters;

import javax.servlet.http.HttpServletRequest;

/**
 * The Class ThreadServletRequest.
 */
public class ThreadServletRequest {
	
	/** The request. */
	private static ThreadLocal request = new ThreadLocal();

	/**
     * Gets the.
     *
     * @return the http servlet request
     */
	public static HttpServletRequest get() {
		return (HttpServletRequest) ThreadServletRequest.request.get();
	}

	/**
     * Sets the.
     *
     * @param request
     *            the request
     */
	public static void set(final HttpServletRequest request) {
		ThreadServletRequest.request.set(request);
	}
}
