package com.technoetic.xplanner.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

/**
 * The Class RequestCharacterEncodingFilter.
 */
public class RequestCharacterEncodingFilter implements Filter {

	/** The log. */
	private final Logger log = Logger.getLogger(this.getClass());

	/** The Constant REQUEST_CHARACTER_ENCODING. */
	public static final String REQUEST_CHARACTER_ENCODING = "requestCharacterEncoding";

	/** The encoding. */
	private String encoding = null;

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		this.encoding = filterConfig
				.getInitParameter(RequestCharacterEncodingFilter.REQUEST_CHARACTER_ENCODING);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		request.setCharacterEncoding(this.encoding);
		chain.doFilter(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

}
