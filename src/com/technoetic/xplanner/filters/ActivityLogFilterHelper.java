package com.technoetic.xplanner.filters;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.technoetic.xplanner.format.PrintfFormat;
import com.technoetic.xplanner.security.PersonPrincipal;
import com.technoetic.xplanner.security.SecurityHelper;

public class ActivityLogFilterHelper {

	public static final String LOG_LINE_PATTERN = "%-20.20s %-10.10s %-14.14s %-25.25s %-6.6s %-12.12s %s";

	public static final String ACTION_START = "START";
	public static final String ACTION_END = "END";
	public static final String ACTION_PREFIX = "/do/";
	public static final String DATE_FORMAT = "MM/dd HH:mm:ss.SSSS";
	public static final String ACTION_PERIOD_PATTERN = "{0,number,#.####}";
	public static final String NO_USER_MSG = "";
	public static final String NO_QUERY_MSG = "";

	private String userId = null;
	private String remoteAddr = null;
	private String actionName = null;
	private String queryString = null;
	private Date startDate = null;
	private Date endDate = null;

	public void doHelperSetUp(final ServletRequest request) {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		this.userId = this.getUserId(httpRequest);
		this.remoteAddr = httpRequest.getRemoteAddr();
		this.actionName = this.getActionName(httpRequest);
		this.queryString = this.getQueryString(httpRequest);
	}

	public String getStartLogRecord() {
		this.startDate = new Date();
		final Object[] elements = new Object[] {
				this.getFormatedTime(this.startDate), this.userId,
				this.remoteAddr, this.actionName,
				ActivityLogFilterHelper.ACTION_START, "", this.queryString };
		return this.getLogLine(elements);
	}

	public String getEndLogRecord() {
		this.endDate = new Date();
		final Object[] elements = new Object[] {
				this.getFormatedTime(this.endDate), this.userId,
				this.remoteAddr, this.actionName,
				ActivityLogFilterHelper.ACTION_END, this.getActionPeriod(),
				this.queryString };
		return this.getLogLine(elements);
	}

	public String getFormatedStartDate() {
		return this.getFormatedTime(this.startDate);
	}

	public String getFormatedEndDate() {
		return this.getFormatedTime(this.endDate);
	}

	private String getFormatedTime(final Date date) {
		final SimpleDateFormat formater = new SimpleDateFormat(
				ActivityLogFilterHelper.DATE_FORMAT);
		return formater.format(date);
	}

	private String getUserId(final HttpServletRequest httpRequest) {
		final Subject subject = (Subject) httpRequest.getSession()
				.getAttribute(SecurityHelper.SECURITY_SUBJECT_KEY);
		if (subject != null) {
			final Set pricipalSet = subject.getPrincipals();
			final Iterator iterator = pricipalSet.iterator();
			return ((PersonPrincipal) iterator.next()).getPerson().getUserId();
		} else {
			return ActivityLogFilterHelper.NO_USER_MSG;
		}
	}

	private String getQueryString(final HttpServletRequest httpRequest) {
		if (httpRequest.getQueryString() != null) {
			return httpRequest.getQueryString();
		} else {
			return ActivityLogFilterHelper.NO_QUERY_MSG;
		}
	}

	private String getActionName(final HttpServletRequest httpRequest) {
		final String url = httpRequest.getRequestURI();
		final int index = url.indexOf(ActivityLogFilterHelper.ACTION_PREFIX);
		return url.substring(index
				+ ActivityLogFilterHelper.ACTION_PREFIX.length());
	}

	private String getLogLine(final Object[] elements) {
		final PrintfFormat formater = new PrintfFormat(
				ActivityLogFilterHelper.LOG_LINE_PATTERN);
		return formater.sprintf(elements);
	}

	public String getActionPeriod() {
		final double mseconds = this.endDate.getTime()
				- this.startDate.getTime();
		final double seconds = mseconds / 1000;
		final Object[] param = new Object[] { new Double(seconds) };
		return MessageFormat.format(
				ActivityLogFilterHelper.ACTION_PERIOD_PATTERN, param);
	}

}
