/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Jan 1, 2006
 * Time: 10:59:22 PM
 */
package com.technoetic.xplanner.tags.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

import com.technoetic.xplanner.util.StringUtilities;

public class BoxedListTag extends BoxTag {
	private Map keyValues;
	public static final String NAME_STYLE_CLASS = "propertyName";
	public static final String VALUE_STYLE_CLASS = "propertyValue";

	@Override
	protected void renderBody(final StringBuffer results) {
		results.append("<ul>");
		if (this.bodyContent == null) {
			for (final Iterator i = this.keyValues.keySet().iterator(); i
					.hasNext();) {
				this.renderRow(results, (String) i.next());
			}
		} else {
			results.append(this.bodyContent.getString());
		}
		results.append("</ul>");
	}

	private void renderRow(final StringBuffer results, final String name) {
		final String key = StringUtilities.htmlEncode(name);
		final Object rawValue = this.keyValues.get(key);
		results.append("<li>" + BoxedListTag.renderProperty(key, rawValue)
				+ "</li>");
	}

	public static String renderProperty(final String key, final Object rawValue) {
		return BoxedListTag.span(key, BoxedListTag.NAME_STYLE_CLASS)
				+ ": "
				+ BoxedListTag.span(BoxedListTag.getStringValue(rawValue),
						BoxedListTag.VALUE_STYLE_CLASS);
	}

	private static String getStringValue(final Object rawValue) {
		String value = "";
		if (rawValue instanceof String) {
			value = StringUtilities.htmlEncode((String) rawValue);
		} else if (rawValue instanceof String[]) {
			final String[] values = (String[]) rawValue;
			if (values.length > 1) {
				value = "{";
			}
			for (int i = 0; i < values.length; i++) {
				value += StringUtilities.htmlEncode(values[i]);
				if (i < values.length - 1) {
					value += ", ";
				}
			}
			if (values.length > 1) {
				value += "}";
			}
		}
		return value;
	}

	private static String span(final String key, final String styleClass) {
		return "<span class=\"" + styleClass + "\">" + key + "</span>";
	}

	public static Map getRequestAttributeMap(final ServletRequest request) {
		final Map map = new TreeMap();
		final Enumeration attributeNames = request.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			final String name = (String) attributeNames.nextElement();
			map.put(name, StringUtilities.htmlEncode(request.getAttribute(name)
					.toString()));
		}
		return map;
	}

	public void setKeyValues(final Map keyValues) {
		this.keyValues = keyValues;
	}
}