package com.technoetic.xplanner.wiki;

import com.technoetic.xplanner.XPlannerProperties;

public class ExternalTwikiAdapter extends GenericWikiAdapter {
	private String defaultSubwiki;

	@Override
	protected void initialize(final XPlannerProperties properties) {
		super.initialize(properties);
		this.defaultSubwiki = properties
				.getProperty("twiki.wikiadapter.subwiki.default");
		if (this.defaultSubwiki == null) {
			this.defaultSubwiki = "Main";
		}
	}

	@Override
	protected String formatLinkToCreateTopic(final String wikiWord) {
		final String subwiki = this.getSubWiki(wikiWord);
		final String word = this.getWord(wikiWord);
		final String url = this.substitute(subwiki, word,
				this.getNewTopicUrlPattern());
		return word + "<a href='" + url + "'>?</a>";
	}

	@Override
	protected String formatUrl(final String wikiWord, final String urlPattern) {
		final String subwiki = this.getSubWiki(wikiWord);
		final String word = this.getWord(wikiWord);
		return this.substitute(subwiki, word, urlPattern);
	}

	private String substitute(final String subwiki, final String word,
			final String urlPattern) {
		String url = urlPattern;
		url = url.replaceAll("\\$\\{subwiki\\}", subwiki + "/");
		url = url.replaceAll("\\$\\{word\\}", word);
		return url;
	}

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

	private String getWord(final String wikiWord) {
		final int periodOffset = wikiWord.lastIndexOf(".");
		return periodOffset != -1 ? wikiWord.substring(periodOffset + 1,
				wikiWord.length()) : wikiWord;
	}

	@Override
	protected String formatLinkToExistingTopic(final String wikiWord) {
		final String subwiki = this.getSubWiki(wikiWord);
		final String word = this.getWord(wikiWord);
		final String url = this.substitute(subwiki, word,
				this.getExistingTopicUrlPattern());
		return "<a href='" + url + "'>" + word + "</a>";
	}
}
