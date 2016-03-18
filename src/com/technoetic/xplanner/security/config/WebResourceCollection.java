package com.technoetic.xplanner.security.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class WebResourceCollection {
	private final ArrayList urlPatterns = new ArrayList();
	private String name;

	public void addUrlPattern(final String pattern) {
		this.urlPatterns.add(pattern);
	}

	public Collection getUrlPatterns() {
		return this.urlPatterns;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean matches(final HttpServletRequest request) {
		final Iterator urlPatterns = this.getUrlPatterns().iterator();
		while (urlPatterns.hasNext()) {
			final String urlPattern = (String) urlPatterns.next();
			if (this.isMatchingPathInfo(request, urlPattern)) {
				return true;
			}
		}
		return false;
	}

	private boolean isMatchingPathInfo(final HttpServletRequest request,
			final String urlPattern) {
		final String path = request.getServletPath()
				+ (request.getPathInfo() == null ? "" : request.getPathInfo());
		return urlPattern.equals("/")
				&& path.equals("/")
				|| urlPattern.length() > 2
				&& urlPattern.startsWith("*.")
				&& path != null
				&& path.endsWith(urlPattern.substring(2))
				|| urlPattern.endsWith("*")
				&& path != null
				&& path.startsWith(urlPattern.substring(0,
						urlPattern.length() - 1))
				|| StringUtils.equals(urlPattern, path);
	}

}
