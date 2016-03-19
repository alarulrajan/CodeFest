package com.technoetic.xplanner.wiki;

import java.util.Properties;

/**
 * The Interface WikiFormat.
 */
public interface WikiFormat {
    
    /** The escape brackets key. */
    String ESCAPE_BRACKETS_KEY = "xplanner.escape.brackets";

    /**
     * Format.
     *
     * @param text
     *            the text
     * @return the string
     */
    String format(String text);

    /**
     * Sets the properties.
     *
     * @param properties
     *            the new properties
     */
    void setProperties(Properties properties);
}
