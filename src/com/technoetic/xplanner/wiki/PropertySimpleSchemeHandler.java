package com.technoetic.xplanner.wiki;

import java.util.Properties;

public class PropertySimpleSchemeHandler extends SimpleSchemeHandler {
	private String key = null;
	private static final String NEW_TARGET = "_new";

	public PropertySimpleSchemeHandler(final String key) {
		this.key = key;
	}

	@Override
	protected String getTarget() {
		return PropertySimpleSchemeHandler.NEW_TARGET;
	}

	@Override
	protected String getPattern(final Properties properties) {
		return properties.getProperty(this.key);
	}

}
