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

/**
 * The Class BoxedListTag.
 */
public class BoxedListTag extends BoxTag {
    
    /** The key values. */
    private Map keyValues;
    
    /** The Constant NAME_STYLE_CLASS. */
    public static final String NAME_STYLE_CLASS = "propertyName";
    
    /** The Constant VALUE_STYLE_CLASS. */
    public static final String VALUE_STYLE_CLASS = "propertyValue";

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.tags.util.BoxTag#renderBody(java.lang.StringBuffer)
     */
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

    /**
     * Render row.
     *
     * @param results
     *            the results
     * @param name
     *            the name
     */
    private void renderRow(final StringBuffer results, final String name) {
        final String key = StringUtilities.htmlEncode(name);
        final Object rawValue = this.keyValues.get(key);
        results.append("<li>" + BoxedListTag.renderProperty(key, rawValue)
                + "</li>");
    }

    /**
     * Render property.
     *
     * @param key
     *            the key
     * @param rawValue
     *            the raw value
     * @return the string
     */
    public static String renderProperty(final String key, final Object rawValue) {
        return BoxedListTag.span(key, BoxedListTag.NAME_STYLE_CLASS)
                + ": "
                + BoxedListTag.span(BoxedListTag.getStringValue(rawValue),
                        BoxedListTag.VALUE_STYLE_CLASS);
    }

    /**
     * Gets the string value.
     *
     * @param rawValue
     *            the raw value
     * @return the string value
     */
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

    /**
     * Span.
     *
     * @param key
     *            the key
     * @param styleClass
     *            the style class
     * @return the string
     */
    private static String span(final String key, final String styleClass) {
        return "<span class=\"" + styleClass + "\">" + key + "</span>";
    }

    /**
     * Gets the request attribute map.
     *
     * @param request
     *            the request
     * @return the request attribute map
     */
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

    /**
     * Sets the key values.
     *
     * @param keyValues
     *            the new key values
     */
    public void setKeyValues(final Map keyValues) {
        this.keyValues = keyValues;
    }
}