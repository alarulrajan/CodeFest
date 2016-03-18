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

public class FormatDateTag extends TagSupport {
	private static HashMap dateFormatters = new HashMap();
	private final static String DEFAULT_DATE_FORMAT = "EEE MMM dd k:mm:ss z";
	private String property;
	private String scope;
	private String name;
	private String format;
	private String formatKey;
	private String locale;
	private Date value;
	private String bundle;

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

	public void setProperty(final String property) {
		this.property = property;
	}

	public String getProperty() {
		return this.property;
	}

	public void setScope(final String scope) {
		this.scope = scope;
	}

	public String getScope() {
		return this.scope;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setFormat(final String format) {
		this.format = format;
	}

	public String getFormat() {
		return this.format;
	}

	public void setFormatKey(final String formatKey) {
		this.formatKey = formatKey;
	}

	public String getFormatKey() {
		return this.formatKey;
	}

	public void setValue(final java.util.Date value) {
		this.value = value;
	}

	public java.util.Date getValue() {
		return this.value;
	}

	public void setLocale(final String locale) {
		this.locale = locale;
	}

	public void setBundle(final String bundle) {
		this.bundle = bundle;
	}

	public String getBundle() {
		return this.bundle;
	}
}
