package com.technoetic.xplanner.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.RequestUtils;

/**
 * This filter saves the HTTP request for SOAP purposes. The SOAP adapters don't
 * have access to the HTTP execution context.
 */
public class ServletRequestFilter implements Filter {
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {
		try {
			final HttpServletRequest servletRequest = (HttpServletRequest) request;
			ThreadServletRequest.set(servletRequest);
			final StringBuffer requestURL = servletRequest.getRequestURL();
			if (StringUtils.isNotBlank(servletRequest.getQueryString())) {
				requestURL.append("?").append(servletRequest.getQueryString());
			}
			request.setAttribute("currentPageUrl", requestURL);
			request.setAttribute("appPath",
					RequestUtils.absoluteURL(servletRequest, "/"));
			filterChain.doFilter(request, response);
		} finally {
			ThreadServletRequest.set(null);
		}
	}

	@Override
	public void destroy() {
	}

}
