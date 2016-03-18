package com.technoetic.xplanner.filters;

import javax.servlet.http.HttpServletRequest;

public class ThreadServletRequest {
	private static ThreadLocal request = new ThreadLocal();

	public static HttpServletRequest get() {
		return (HttpServletRequest) ThreadServletRequest.request.get();
	}

	public static void set(final HttpServletRequest request) {
		ThreadServletRequest.request.set(request);
	}
}
