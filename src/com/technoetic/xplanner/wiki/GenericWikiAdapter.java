package com.technoetic.xplanner.wiki;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.XPlannerProperties;

/**
 * The Class GenericWikiAdapter.
 */
public class GenericWikiAdapter implements ExternalWikiAdapter {
    
    /** The log. */
    private final Logger log = Logger.getLogger(this.getClass());
    
    /** The existing topics. */
    private static HashSet existingTopics = new HashSet();
    
    /** The existing topic url pattern. */
    private String existingTopicUrlPattern;
    
    /** The new topic url pattern. */
    private String newTopicUrlPattern;
    
    /** The new topic pattern. */
    public String newTopicPattern;

    /**
     * Instantiates a new generic wiki adapter.
     */
    public GenericWikiAdapter() {
        final XPlannerProperties properties = new XPlannerProperties();
        this.initialize(properties);
    }

    /**
     * Initialize.
     *
     * @param properties
     *            the properties
     */
    protected void initialize(final XPlannerProperties properties) {
        this.existingTopicUrlPattern = properties
                .getProperty("twiki.wikiadapter.topic.url.existing");
        this.newTopicUrlPattern = properties
                .getProperty("twiki.wikiadapter.topic.url.new");
        this.newTopicPattern = properties
                .getProperty("twiki.wikiadapter.topic.newpattern");
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.wiki.ExternalWikiAdapter#formatWikiWord(java.lang.String)
     */
    @Override
    public String formatWikiWord(final String wikiWord) {
        return this.isTopicExisting(wikiWord) ? this
                .formatLinkToExistingTopic(wikiWord) : this
                .formatLinkToCreateTopic(wikiWord);
    }

    /**
     * Format link to create topic.
     *
     * @param wikiWord
     *            the wiki word
     * @return the string
     */
    protected String formatLinkToCreateTopic(final String wikiWord) {
        return wikiWord + "<a href='"
                + this.formatUrl(wikiWord, this.newTopicUrlPattern) + "'>?</a>";
    }

    /**
     * Format link to existing topic.
     *
     * @param wikiWord
     *            the wiki word
     * @return the string
     */
    protected String formatLinkToExistingTopic(final String wikiWord) {
        return "<a href='"
                + this.formatUrl(wikiWord, this.existingTopicUrlPattern) + "'>"
                + wikiWord + "</a>";
    }

    /**
     * Format url.
     *
     * @param wikiWord
     *            the wiki word
     * @param urlPattern
     *            the url pattern
     * @return the string
     */
    protected String formatUrl(final String wikiWord, final String urlPattern) {
        return this.substituteWikiWord(wikiWord, urlPattern);
    }

    /**
     * Substitute wiki word.
     *
     * @param wikiWord
     *            the wiki word
     * @param text
     *            the text
     * @return the string
     */
    private String substituteWikiWord(final String wikiWord, final String text) {
        return text.replaceAll("\\$\\{word\\}", wikiWord);
    }

    /**
     * Checks if is topic existing.
     *
     * @param wikiWord
     *            the wiki word
     * @return true, if is topic existing
     */
    private boolean isTopicExisting(final String wikiWord) {
        // This is a somewhat inefficient, but general approach
        if (GenericWikiAdapter.existingTopics.contains(wikiWord)) {
            return true;
        }
        try {
            final URL url = new URL(this.formatUrl(wikiWord,
                    this.existingTopicUrlPattern));
            final InputStream page = url.openStream();
            final BufferedReader reader = new BufferedReader(
                    new InputStreamReader(page));
            try {
                String line = reader.readLine();
                while (line != null) {
                    if (line.matches(this.substituteWikiWord(wikiWord,
                            this.newTopicPattern))) {
                        return false;
                    }
                    line = reader.readLine();
                }
            } finally {
                reader.close();
            }
        } catch (final java.io.IOException e) {
            // ignored - return false
            return false;
        }
        GenericWikiAdapter.existingTopics.add(wikiWord);
        return true;
    }

    /**
     * Gets the existing topic url pattern.
     *
     * @return the existing topic url pattern
     */
    public String getExistingTopicUrlPattern() {
        return this.existingTopicUrlPattern;
    }

    /**
     * Gets the new topic url pattern.
     *
     * @return the new topic url pattern
     */
    public String getNewTopicUrlPattern() {
        return this.newTopicUrlPattern;
    }

    /**
     * Gets the new topic pattern.
     *
     * @return the new topic pattern
     */
    public String getNewTopicPattern() {
        return this.newTopicPattern;
    }
}
