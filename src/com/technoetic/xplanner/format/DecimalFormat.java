package com.technoetic.xplanner.format;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

public class DecimalFormat extends AbstractFormat {
	private java.text.DecimalFormat formatter = null;
	private NumberFormat parser = null;

	public DecimalFormat(final HttpServletRequest request) {
		this(request.getLocale(), AbstractFormat.getFormat(request,
				"format.decimal"));
	}

	public DecimalFormat(final Locale locale, final String format) {
		this.formatter = (java.text.DecimalFormat) NumberFormat
				.getNumberInstance(locale);
		if (format != null) {
			this.formatter.applyPattern(format);
		}
		this.parser = this.getParser(locale);
	}

	private NumberFormat getParser(final Locale locale) {
		return NumberFormat.getInstance(locale);
	}

	public String format(final double value) {
		return this.formatter.format(value);
	}

	public double parse(String value) throws ParseException {
		value = value.trim();
		return this.parser.parse(value).doubleValue();
	}

	public static String format(final PageContext pageContext,
			final double value) {
		return DecimalFormat.format(
				(HttpServletRequest) pageContext.getRequest(), value);
	}

	public static String format(final HttpServletRequest request,
			final double value) {
		return DecimalFormat.format(request, value, null);
	}

	public static String format(final PageContext pageContext,
			final double value, final String zeroString) {
		return value == 0.0 && zeroString != null ? zeroString
				: new DecimalFormat(
						(HttpServletRequest) pageContext.getRequest())
						.format(value);
	}

	public static String format(final HttpServletRequest request,
			final double value, final String zeroString) {
		return value == 0.0 && zeroString != null ? zeroString
				: new DecimalFormat(request).format(value);
	}
}