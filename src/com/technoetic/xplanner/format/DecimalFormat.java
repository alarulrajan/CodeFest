package com.technoetic.xplanner.format;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * The Class DecimalFormat.
 */
public class DecimalFormat extends AbstractFormat {
	
	/** The formatter. */
	private java.text.DecimalFormat formatter = null;
	
	/** The parser. */
	private NumberFormat parser = null;

	/**
     * Instantiates a new decimal format.
     *
     * @param request
     *            the request
     */
	public DecimalFormat(final HttpServletRequest request) {
		this(request.getLocale(), AbstractFormat.getFormat(request,
				"format.decimal"));
	}

	/**
     * Instantiates a new decimal format.
     *
     * @param locale
     *            the locale
     * @param format
     *            the format
     */
	public DecimalFormat(final Locale locale, final String format) {
		this.formatter = (java.text.DecimalFormat) NumberFormat
				.getNumberInstance(locale);
		if (format != null) {
			this.formatter.applyPattern(format);
		}
		this.parser = this.getParser(locale);
	}

	/**
     * Gets the parser.
     *
     * @param locale
     *            the locale
     * @return the parser
     */
	private NumberFormat getParser(final Locale locale) {
		return NumberFormat.getInstance(locale);
	}

	/**
     * Format.
     *
     * @param value
     *            the value
     * @return the string
     */
	public String format(final double value) {
		return this.formatter.format(value);
	}

	/**
     * Parses the.
     *
     * @param value
     *            the value
     * @return the double
     * @throws ParseException
     *             the parse exception
     */
	public double parse(String value) throws ParseException {
		value = value.trim();
		return this.parser.parse(value).doubleValue();
	}

	/**
     * Format.
     *
     * @param pageContext
     *            the page context
     * @param value
     *            the value
     * @return the string
     */
	public static String format(final PageContext pageContext,
			final double value) {
		return DecimalFormat.format(
				(HttpServletRequest) pageContext.getRequest(), value);
	}

	/**
     * Format.
     *
     * @param request
     *            the request
     * @param value
     *            the value
     * @return the string
     */
	public static String format(final HttpServletRequest request,
			final double value) {
		return DecimalFormat.format(request, value, null);
	}

	/**
     * Format.
     *
     * @param pageContext
     *            the page context
     * @param value
     *            the value
     * @param zeroString
     *            the zero string
     * @return the string
     */
	public static String format(final PageContext pageContext,
			final double value, final String zeroString) {
		return value == 0.0 && zeroString != null ? zeroString
				: new DecimalFormat(
						(HttpServletRequest) pageContext.getRequest())
						.format(value);
	}

	/**
     * Format.
     *
     * @param request
     *            the request
     * @param value
     *            the value
     * @param zeroString
     *            the zero string
     * @return the string
     */
	public static String format(final HttpServletRequest request,
			final double value, final String zeroString) {
		return value == 0.0 && zeroString != null ? zeroString
				: new DecimalFormat(request).format(value);
	}
}