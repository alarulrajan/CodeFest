package com.technoetic.xplanner.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

public class DateFormat extends AbstractFormat {
	private java.text.DateFormat format = null;

	public DateFormat(final HttpServletRequest request) {
		final String format = AbstractFormat.getFormat(request, "format.date");
		if (format != null) {
			this.format = new SimpleDateFormat(format);
		} else {
			this.format = java.text.DateFormat.getDateInstance(
					java.text.DateFormat.SHORT, request.getLocale());
		}
	}

	public String format(final Date value) {
		return this.format.format(value);
	}

	public Date parse(final String value) throws ParseException {
		return this.format.parse(value);
	}

	public static String format(final PageContext pageContext, final Date value) {
		return new DateFormat((HttpServletRequest) pageContext.getRequest())
				.format(value);
	}

	public static String format(final HttpServletRequest request,
			final Date value) {
		return new DateFormat(request).format(value);
	}

	public static Date parse(final PageContext pageContext,
			final String dateString) throws ParseException {
		return new DateFormat((HttpServletRequest) pageContext.getRequest())
				.parse(dateString);
	}

}