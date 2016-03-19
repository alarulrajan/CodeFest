package com.technoetic.xplanner.tags;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

/**
 * The Class FormatDateTag.
 */
public class FormatDateTag extends TagSupport {
	
	/** The date formatters. */
	private static HashMap dateFormatters = new HashMap();
	
	/** The Constant DEFAULT_DATE_FORMAT. */
	private final static String DEFAULT_DATE_FORMAT = "EEE MMM dd k:mm:ss z";
	
	/** The property. */
	private String property;
	
	/** The scope. */
	private String scope;
	
	/** The name. */
	private String name;
	
	/** The format. */
	private String format;
	
	/** The format key. */
	private String formatKey;
	
	/** The locale. */
	private String locale;
	
	/** The value. */
	private Date value;
	
	/** The bundle. */
	private String bundle;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		Object object = null;
		if (this.value == null) {
			object = RequestUtils.lookup(this.pageContext, this.name,
					this.property, this.scope);
		} else {
			object = this.value;
		}

		if (object == null) {
			return Tag.SKIP_BODY; // Nothing to output
		}

		Date dateValue = null;
		if (object instanceof Date) {
			dateValue = (Date) object;
		} else {
			throw new JspException("date value must be a java.util.Date");
		}

		String dateFormat = null;
		if (this.format != null) {
			dateFormat = this.format;
		} else if (this.formatKey != null) {
			dateFormat = RequestUtils.message(this.pageContext, this.bundle,
					this.locale, this.formatKey);
		} else {
			dateFormat = FormatDateTag.DEFAULT_DATE_FORMAT;
		}

		DateFormat dateFormatter = (DateFormat) FormatDateTag.dateFormatters
				.get(dateFormat);
		if (dateFormatter == null) {
			dateFormatter = new SimpleDateFormat(dateFormat, this.pageContext
					.getRequest().getLocale());
			// This should really be keyed off of both format and locale
			FormatDateTag.dateFormatters.put(dateFormat, dateFormatter);
		}

		ResponseUtils.write(this.pageContext, dateFormatter.format(dateValue));

		return Tag.SKIP_BODY;
	}

	/**
     * Sets the property.
     *
     * @param property
     *            the new property
     */
	public void setProperty(final String property) {
		this.property = property;
	}

	/**
     * Gets the property.
     *
     * @return the property
     */
	public String getProperty() {
		return this.property;
	}

	/**
     * Sets the scope.
     *
     * @param scope
     *            the new scope
     */
	public void setScope(final String scope) {
		this.scope = scope;
	}

	/**
     * Gets the scope.
     *
     * @return the scope
     */
	public String getScope() {
		return this.scope;
	}

	/**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
	public void setName(final String name) {
		this.name = name;
	}

	/**
     * Gets the name.
     *
     * @return the name
     */
	public String getName() {
		return this.name;
	}

	/**
     * Sets the format.
     *
     * @param format
     *            the new format
     */
	public void setFormat(final String format) {
		this.format = format;
	}

	/**
     * Gets the format.
     *
     * @return the format
     */
	public String getFormat() {
		return this.format;
	}

	/**
     * Sets the format key.
     *
     * @param formatKey
     *            the new format key
     */
	public void setFormatKey(final String formatKey) {
		this.formatKey = formatKey;
	}

	/**
     * Gets the format key.
     *
     * @return the format key
     */
	public String getFormatKey() {
		return this.formatKey;
	}

	/**
     * Sets the value.
     *
     * @param value
     *            the new value
     */
	public void setValue(final java.util.Date value) {
		this.value = value;
	}

	/**
     * Gets the value.
     *
     * @return the value
     */
	public java.util.Date getValue() {
		return this.value;
	}

	/**
     * Sets the locale.
     *
     * @param locale
     *            the new locale
     */
	public void setLocale(final String locale) {
		this.locale = locale;
	}

	/**
     * Sets the bundle.
     *
     * @param bundle
     *            the new bundle
     */
	public void setBundle(final String bundle) {
		this.bundle = bundle;
	}

	/**
     * Gets the bundle.
     *
     * @return the bundle
     */
	public String getBundle() {
		return this.bundle;
	}
}
