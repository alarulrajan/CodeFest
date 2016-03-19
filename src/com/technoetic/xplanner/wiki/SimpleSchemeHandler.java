package com.technoetic.xplanner.wiki;

import java.util.Properties;

import org.apache.oro.text.perl.Perl5Util;

/**
 * The Class SimpleSchemeHandler.
 */
public class SimpleSchemeHandler implements SchemeHandler {
    
    /** The perl. */
    private final Perl5Util perl = new Perl5Util();
    
    /** The pattern. */
    private final String pattern;
    
    /** The Constant TARGET_TOP. */
    private static final String TARGET_TOP = "_top";

    /**
     * Instantiates a new simple scheme handler.
     */
    public SimpleSchemeHandler() {
        this.pattern = null;
    }

    /**
     * Instantiates a new simple scheme handler.
     *
     * @param pattern
     *            the pattern
     */
    public SimpleSchemeHandler(final String pattern) {
        this.pattern = pattern;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.wiki.SchemeHandler#translate(java.util.Properties, java.lang.String, java.lang.String, java.lang.String)
     */
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

    /**
     * Gets the pattern.
     *
     * @param properties
     *            the properties
     * @return the pattern
     */
    protected String getPattern(final Properties properties) {
        return this.pattern;
    }

    /**
     * Gets the target.
     *
     * @return the target
     */
    protected String getTarget() {
        return SimpleSchemeHandler.TARGET_TOP;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + this.getClass().getName() + " pattern=" + this.pattern
                + "]";
    }
}
