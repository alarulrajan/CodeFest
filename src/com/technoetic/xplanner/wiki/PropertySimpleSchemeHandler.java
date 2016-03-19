package com.technoetic.xplanner.wiki;

import java.util.Properties;

/**
 * The Class PropertySimpleSchemeHandler.
 */
public class PropertySimpleSchemeHandler extends SimpleSchemeHandler {
	
	/** The key. */
	private String key = null;
	
	/** The Constant NEW_TARGET. */
	private static final String NEW_TARGET = "_new";

	/**
     * Instantiates a new property simple scheme handler.
     *
     * @param key
     *            the key
     */
	public PropertySimpleSchemeHandler(final String key) {
		this.key = key;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.wiki.SimpleSchemeHandler#getTarget()
	 */
	@Override
	protected String getTarget() {
		return PropertySimpleSchemeHandler.NEW_TARGET;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.wiki.SimpleSchemeHandler#getPattern(java.util.Properties)
	 */
	@Override
	protected String getPattern(final Properties properties) {
		return properties.getProperty(this.key);
	}

}
