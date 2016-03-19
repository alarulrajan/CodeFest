package com.technoetic.xplanner.wiki;

import com.technoetic.xplanner.XPlannerProperties;

/**
 * The Class SimpleWikiAdapter.
 */
public class SimpleWikiAdapter implements ExternalWikiAdapter {
    
    /* (non-Javadoc)
     * @see com.technoetic.xplanner.wiki.ExternalWikiAdapter#formatWikiWord(java.lang.String)
     */
    @Override
    public String formatWikiWord(final String wikiWord) {
        String url = new XPlannerProperties()
                .getProperty(XPlannerProperties.WIKI_URL_KEY);
        if (url != null) {
            url = url.replaceAll("\\$1", wikiWord);
            return "<a href='" + url + "'>" + wikiWord + "</a>";
        } else {
            return wikiWord;
        }
    }
}
