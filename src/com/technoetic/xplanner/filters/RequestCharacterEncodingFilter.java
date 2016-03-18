package com.technoetic.xplanner.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

public class RequestCharacterEncodingFilter implements Filter {

	private final Logger log = Logger.getLogger(this.getClass());

	public static final String REQUEST_CHARACTER_ENCODING = "requestCharacterEncoding";

	private String encoding = null;

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		this.encoding = filterConfig
				.getInitParameter(RequestCharacterEncodingFilter.REQUEST_CHARACTER_ENCODING);
	}

	@Override
	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		request.setCharacterEncoding(this.encoding);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}
