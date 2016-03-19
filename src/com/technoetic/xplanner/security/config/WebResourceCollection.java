package com.technoetic.xplanner.security.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * The Class WebResourceCollection.
 */
public class WebResourceCollection {
    
    /** The url patterns. */
    private final ArrayList urlPatterns = new ArrayList();
    
    /** The name. */
    private String name;

    /**
     * Adds the url pattern.
     *
     * @param pattern
     *            the pattern
     */
    public void addUrlPattern(final String pattern) {
        this.urlPatterns.add(pattern);
    }

    /**
     * Gets the url patterns.
     *
     * @return the url patterns
     */
    public Collection getUrlPatterns() {
        return this.urlPatterns;
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
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Matches.
     *
     * @param request
     *            the request
     * @return true, if successful
     */
    public boolean matches(final HttpServletRequest request) {
        final Iterator urlPatterns = this.getUrlPatterns().iterator();
        while (urlPatterns.hasNext()) {
            final String urlPattern = (String) urlPatterns.next();
            if (this.isMatchingPathInfo(request, urlPattern)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if is matching path info.
     *
     * @param request
     *            the request
     * @param urlPattern
     *            the url pattern
     * @return true, if is matching path info
     */
    private boolean isMatchingPathInfo(final HttpServletRequest request,
            final String urlPattern) {
        final String path = request.getServletPath()
                + (request.getPathInfo() == null ? "" : request.getPathInfo());
        return urlPattern.equals("/")
                && path.equals("/")
                || urlPattern.length() > 2
                && urlPattern.startsWith("*.")
                && path != null
                && path.endsWith(urlPattern.substring(2))
                || urlPattern.endsWith("*")
                && path != null
                && path.startsWith(urlPattern.substring(0,
                        urlPattern.length() - 1))
                || StringUtils.equals(urlPattern, path);
    }

}
