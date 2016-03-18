package com.technoetic.xplanner.security.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.config.SecurityConfiguration;

public abstract class AbstractSecurityFilter implements Filter {
	protected Logger log = Logger.getLogger(this.getClass());
	private SecurityConfiguration securityConfiguration;

	@Override
	public final void init(final FilterConfig filterConfig)
			throws ServletException {
		try {
			this.doInit(filterConfig);
			final String filename = filterConfig
					.getInitParameter("securityConfiguration");
			// do-before-release unit test for null filename
			if (filename != null) {
				final InputStream configurationStream = filterConfig
						.getServletContext().getResourceAsStream(filename);
				if (configurationStream == null) {
					throw new ServletException(
							"could not load security configuration: "
									+ filename);
				}
				this.securityConfiguration = SecurityConfiguration
						.load(configurationStream);
			}
		} catch (final Exception e) {
			throw new ServletException(e);
		}
	}

	protected abstract void doInit(FilterConfig filterConfig)
			throws ServletException;

	@Override
	public final void doFilter(final ServletRequest servletRequest,
			final ServletResponse servletResponse, final FilterChain filterChain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		boolean continueFilterChain = true;
		if (this.isSecureRequest(request)
				&& !this.isAuthenticated(request, response)) {
			continueFilterChain = this.onAuthenticationFailure(request,
					response);
		}
		if (continueFilterChain) {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	// do-before-release unit test
	private boolean isSecureRequest(final HttpServletRequest request) {
		return this.securityConfiguration == null
				|| this.securityConfiguration.isSecureRequest(request);
	}

	protected abstract boolean isAuthenticated(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException;

	protected abstract boolean onAuthenticationFailure(
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;

	@Override
	public void destroy() {
		// empty
	}

	public void setSecurityConfiguration(
			final SecurityConfiguration securityConfiguration) {
		this.securityConfiguration = securityConfiguration;
	}

	protected boolean isSubjectInSession(final HttpServletRequest request) {
		return SecurityHelper.isUserAuthenticated(request);
	}
}
