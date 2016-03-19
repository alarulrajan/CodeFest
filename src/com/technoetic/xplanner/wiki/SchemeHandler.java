package com.technoetic.xplanner.wiki;

import java.util.Properties;

/**
 * The Interface SchemeHandler.
 */
public interface SchemeHandler {
	
	/**
     * Translate.
     *
     * @param properties
     *            the properties
     * @param scheme
     *            the scheme
     * @param location
     *            the location
     * @param linkText
     *            the link text
     * @return the string
     */
	String translate(Properties properties, String scheme, String location,
			String linkText);
}
