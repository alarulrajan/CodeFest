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

public class ActivityLogFilter implements Filter {

	public static final Logger LOG = LogUtil.getLogger();

	// TODO: Once we migrate to 1.5, use String.format to extract the logging
	// pattern into the web.xml filter init param.

	@Override
	public void init(final FilterConfig config) throws ServletException {
	}

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

	@Override
	public void destroy() {
	}

}
