package com.technoetic.xplanner.wiki;

import com.technoetic.xplanner.XPlannerProperties;

/**
 * The Class ExternalTwikiAdapter.
 */
public class ExternalTwikiAdapter extends GenericWikiAdapter {
    
    /** The default subwiki. */
    private String defaultSubwiki;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.wiki.GenericWikiAdapter#initialize(com.technoetic.xplanner.XPlannerProperties)
     */
    @Override
    protected void initialize(final XPlannerProperties properties) {
        super.initialize(properties);
        this.defaultSubwiki = properties
                .getProperty("twiki.wikiadapter.subwiki.default");
        if (this.defaultSubwiki == null) {
            this.defaultSubwiki = "Main";
        }
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.wiki.GenericWikiAdapter#formatLinkToCreateTopic(java.lang.String)
     */
    @Override
    protected String formatLinkToCreateTopic(final String wikiWord) {
        final String subwiki = this.getSubWiki(wikiWord);
        final String word = this.getWord(wikiWord);
        final String url = this.substitute(subwiki, word,
                this.getNewTopicUrlPattern());
        return word + "<a href='" + url + "'>?</a>";
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.wiki.GenericWikiAdapter#formatUrl(java.lang.String, java.lang.String)
     */
    @Override
    protected String formatUrl(final String wikiWord, final String urlPattern) {
        final String subwiki = this.getSubWiki(wikiWord);
        final String word = this.getWord(wikiWord);
        return this.substitute(subwiki, word, urlPattern);
    }

    /**
     * Substitute.
     *
     * @param subwiki
     *            the subwiki
     * @param word
     *            the word
     * @param urlPattern
     *            the url pattern
     * @return the string
     */
    private String substitute(final String subwiki, final String word,
            final String urlPattern) {
        String url = urlPattern;
        url = url.replaceAll("\\$\\{subwiki\\}", subwiki + "/");
        url = url.replaceAll("\\$\\{word\\}", word);
        return url;
    }

    /**
     * Gets the sub wiki.
     *
     * @param wikiWord
     *            the wiki word
     * @return the sub wiki
     */
    private String getSubWiki(final String wikiWord) {
        final int periodOffset = wikiWord.lastIndexOf(".");
        String subwiki;
        if (periodOffset != -1) {
            subwiki = wikiWord.substring(0, periodOffset);
        } else {
            subwiki = this.defaultSubwiki;
        }
        return subwiki;
    }

    /**
     * Gets the word.
     *
     * @param wikiWord
     *            the wiki word
     * @return the word
     */
    private String getWord(final String wikiWord) {
        final int periodOffset = wikiWord.lastIndexOf(".");
        return periodOffset != -1 ? wikiWord.substring(periodOffset + 1,
                wikiWord.length()) : wikiWord;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.wiki.GenericWikiAdapter#formatLinkToExistingTopic(java.lang.String)
     */
    @Override
    protected String formatLinkToExistingTopic(final String wikiWord) {
        final String subwiki = this.getSubWiki(wikiWord);
        final String word = this.getWord(wikiWord);
        final String url = this.substitute(subwiki, word,
                this.getExistingTopicUrlPattern());
        return "<a href='" + url + "'>" + word + "</a>";
    }
}
