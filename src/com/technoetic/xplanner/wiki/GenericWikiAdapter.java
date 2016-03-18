package com.technoetic.xplanner.wiki;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.XPlannerProperties;

public class GenericWikiAdapter implements ExternalWikiAdapter {
	private final Logger log = Logger.getLogger(this.getClass());
	private static HashSet existingTopics = new HashSet();
	private String existingTopicUrlPattern;
	private String newTopicUrlPattern;
	public String newTopicPattern;

	public GenericWikiAdapter() {
		final XPlannerProperties properties = new XPlannerProperties();
		this.initialize(properties);
	}

	protected void initialize(final XPlannerProperties properties) {
		this.existingTopicUrlPattern = properties
				.getProperty("twiki.wikiadapter.topic.url.existing");
		this.newTopicUrlPattern = properties
				.getProperty("twiki.wikiadapter.topic.url.new");
		this.newTopicPattern = properties
				.getProperty("twiki.wikiadapter.topic.newpattern");
	}

	@Override
	public String formatWikiWord(final String wikiWord) {
		return this.isTopicExisting(wikiWord) ? this
				.formatLinkToExistingTopic(wikiWord) : this
				.formatLinkToCreateTopic(wikiWord);
	}

	protected String formatLinkToCreateTopic(final String wikiWord) {
		return wikiWord + "<a href='"
				+ this.formatUrl(wikiWord, this.newTopicUrlPattern) + "'>?</a>";
	}

	protected String formatLinkToExistingTopic(final String wikiWord) {
		return "<a href='"
				+ this.formatUrl(wikiWord, this.existingTopicUrlPattern) + "'>"
				+ wikiWord + "</a>";
	}

	protected String formatUrl(final String wikiWord, final String urlPattern) {
		return this.substituteWikiWord(wikiWord, urlPattern);
	}

	private String substituteWikiWord(final String wikiWord, final String text) {
		return text.replaceAll("\\$\\{word\\}", wikiWord);
	}

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

	public String getExistingTopicUrlPattern() {
		return this.existingTopicUrlPattern;
	}

	public String getNewTopicUrlPattern() {
		return this.newTopicUrlPattern;
	}

	public String getNewTopicPattern() {
		return this.newTopicPattern;
	}
}
