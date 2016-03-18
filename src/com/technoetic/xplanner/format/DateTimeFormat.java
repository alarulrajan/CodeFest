package com.technoetic.xplanner.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

public class DateTimeFormat extends AbstractFormat {
	private SimpleDateFormat formatter = null;

	public DateTimeFormat(final HttpServletRequest request) {
		final String format = AbstractFormat.getFormat(request,
				"format.datetime");
		if (format != null) {
			this.formatter = new SimpleDateFormat(format);
		} else {
			this.formatter = new SimpleDateFormat();
		}
	}

	public String format(final Date value) {
		return value != null ? this.formatter.format(value) : "";
	}

	public Date parse(final String date) throws ParseException {
		return this.formatter.parse(date);
	}

	public static String format(final PageContext pageContext, final Date value) {
		return new DateTimeFormat((HttpServletRequest) pageContext.getRequest())
				.format(value);
	}

	public static String format(final HttpServletRequest request,
			final Date value) {
		return new DateTimeFormat(request).format(value);
	}
}