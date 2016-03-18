package com.technoetic.xplanner.wiki;

import java.util.Properties;

import org.apache.oro.text.perl.Perl5Util;

public class SimpleSchemeHandler implements SchemeHandler {
	private final Perl5Util perl = new Perl5Util();
	private final String pattern;
	private static final String TARGET_TOP = "_top";

	public SimpleSchemeHandler() {
		this.pattern = null;
	}

	public SimpleSchemeHandler(final String pattern) {
		this.pattern = pattern;
	}

	@Override
	public String translate(final Properties properties, final String scheme,
			String location, final String linkText) {
		final String pattern = this.getPattern(properties);
		location = this.perl.substitute("s|(/)|\\\\$1|g", location);
		final String url = this.perl.substitute("s/\\$1/" + location + "/go",
				pattern);
		return "<a href=\"" + url + "\" target=\"" + this.getTarget() + "\">"
				+ (linkText != null ? linkText : scheme + ":" + location)
				+ "</a>";
	}

	protected String getPattern(final Properties properties) {
		return this.pattern;
	}

	protected String getTarget() {
		return SimpleSchemeHandler.TARGET_TOP;
	}

	@Override
	public String toString() {
		return "[" + this.getClass().getName() + " pattern=" + this.pattern
				+ "]";
	}
}
