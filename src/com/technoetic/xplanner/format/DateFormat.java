package com.technoetic.xplanner.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * The Class DateFormat.
 */
public class DateFormat extends AbstractFormat {
	
	/** The format. */
	private java.text.DateFormat format = null;

	/**
     * Instantiates a new date format.
     *
     * @param request
     *            the request
     */
	public DateFormat(final HttpServletRequest request) {
		final String format = AbstractFormat.getFormat(request, "format.date");
		if (format != null) {
			this.format = new SimpleDateFormat(format);
		} else {
			this.format = java.text.DateFormat.getDateInstance(
					java.text.DateFormat.SHORT, request.getLocale());
		}
	}

	/**
     * Format.
     *
     * @param value
     *            the value
     * @return the string
     */
	public String format(final Date value) {
		return this.format.format(value);
	}

	/**
     * Parses the.
     *
     * @param value
     *            the value
     * @return the date
     * @throws ParseException
     *             the parse exception
     */
	public Date parse(final String value) throws ParseException {
		return this.format.parse(value);
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
	public static String format(final PageContext pageContext, final Date value) {
		return new DateFormat((HttpServletRequest) pageContext.getRequest())
				.format(value);
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
			final Date value) {
		return new DateFormat(request).format(value);
	}

	/**
     * Parses the.
     *
     * @param pageContext
     *            the page context
     * @param dateString
     *            the date string
     * @return the date
     * @throws ParseException
     *             the parse exception
     */
	public static Date parse(final PageContext pageContext,
			final String dateString) throws ParseException {
		return new DateFormat((HttpServletRequest) pageContext.getRequest())
				.parse(dateString);
	}

}