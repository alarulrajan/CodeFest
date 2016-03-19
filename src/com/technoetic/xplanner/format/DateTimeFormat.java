package com.technoetic.xplanner.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * The Class DateTimeFormat.
 */
public class DateTimeFormat extends AbstractFormat {
    
    /** The formatter. */
    private SimpleDateFormat formatter = null;

    /**
     * Instantiates a new date time format.
     *
     * @param request
     *            the request
     */
    public DateTimeFormat(final HttpServletRequest request) {
        final String format = AbstractFormat.getFormat(request,
                "format.datetime");
        if (format != null) {
            this.formatter = new SimpleDateFormat(format);
        } else {
            this.formatter = new SimpleDateFormat();
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
        return value != null ? this.formatter.format(value) : "";
    }

    /**
     * Parses the.
     *
     * @param date
     *            the date
     * @return the date
     * @throws ParseException
     *             the parse exception
     */
    public Date parse(final String date) throws ParseException {
        return this.formatter.parse(date);
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
        return new DateTimeFormat((HttpServletRequest) pageContext.getRequest())
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
        return new DateTimeFormat(request).format(value);
    }
}