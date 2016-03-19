/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.util.LogUtil;

/**
 * The Class ActivityLogFilter.
 */
public class ActivityLogFilter implements Filter {

	/** The Constant LOG. */
	public static final Logger LOG = LogUtil.getLogger();

	// ChangeSoon: Once we migrate to 1.5, use String.format to extract the logging
	// pattern into the web.xml filter init param.

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(final FilterConfig config) throws ServletException {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {

		final ActivityLogFilterHelper helper = new ActivityLogFilterHelper();
		helper.doHelperSetUp(request);

		ActivityLogFilter.LOG.info(helper.getStartLogRecord());

		filterChain.doFilter(request, response);

		ActivityLogFilter.LOG.info(helper.getEndLogRecord());

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

}
