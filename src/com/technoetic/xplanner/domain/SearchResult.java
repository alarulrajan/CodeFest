package com.technoetic.xplanner.domain;

import com.technoetic.xplanner.util.StringUtilities;

/**
 * Created by IntelliJ IDEA. User: tkmower Date: Nov 29, 2004 Time: 4:47:59 PM
 */
public class SearchResult extends Object {
	private String matchPrefix = "";
	private String matchSuffix = "";
	private String matchingText = "";
	private final String resultType;
	private final Nameable matchingObject;
	private boolean matchInDescription = false;
	private String attachedToId;
	private String attachedToDomainType;

	public SearchResult(final Nameable matchingObject, final String resultType) {
		this.matchingObject = matchingObject;
		this.resultType = resultType;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SearchResult)) {
			return false;
		}

		final SearchResult searchResult = (SearchResult) o;

		if (this.getDomainObjectId() != searchResult.getDomainObjectId()) {
			return false;
		}

		return true;
	}

	public String getMatchPrefix() {
		return this.matchPrefix;
	}

	public void setMatchPrefix(final String matchPrefix) {
		this.matchPrefix = matchPrefix;
	}

	public String getMatchSuffix() {
		return this.matchSuffix;
	}

	public void setMatchSuffix(final String matchSuffix) {
		this.matchSuffix = matchSuffix;
	}

	public Object getMatchingObject() {
		return this.matchingObject;
	}

	public String getMatchingText() {
		return this.matchingText;
	}

	public void setMatchingText(final String matchingText) {
		this.matchingText = matchingText;
	}

	public String getResultType() {
		return this.resultType;
	}

	public String getTitle() {
		return this.matchingObject.getName();
	}

	@Override
	public int hashCode() {
		return this.getDomainObjectId();
	}

	public int getDomainObjectId() {
		return this.matchingObject.getId();
	}

	public boolean isMatchInDescription() {
		return this.matchInDescription;
	}

	public void setMatchInDescription(final boolean matchInDescription) {
		this.matchInDescription = matchInDescription;
	}

	public void populate(final String searchCriteria,
			final int desiredDescriptionLines, final int maxSuffixLength) {
		if (this.getLocationOfCriteria(searchCriteria) < 0) {
			this.populateResultWithNoDescriptionMatch(desiredDescriptionLines);
		} else {
			this.populateResultWithPrefixSuffixAndResult(searchCriteria,
					maxSuffixLength);
		}
	}

	private int getLocationOfCriteria(final String searchCriteria) {
		return this.matchingObject.getDescription().toLowerCase()
				.indexOf(searchCriteria.toLowerCase());
	}

	private void populateResultWithNoDescriptionMatch(
			final int desiredDescriptionLines) {
		this.setMatchingText(StringUtilities.getFirstNLines(
				this.matchingObject.getDescription(), desiredDescriptionLines));
	}

	private void populateResultWithPrefixSuffixAndResult(
			final String searchCriteria, final int maxSuffixLength) {
		this.populate(searchCriteria, maxSuffixLength);
	}

	public void populate(final String searchCriteria, final int maxSuffixLength) {
		this.setMatchInDescription(true);
		final String description = this.matchingObject.getDescription();
		this.setMatchPrefix(StringUtilities.computePrefix(description,
				searchCriteria, maxSuffixLength));

		final int beginIndex = this.getLocationOfCriteria(searchCriteria);
		this.setMatchingText(description.substring(beginIndex, beginIndex
				+ searchCriteria.length()));

		this.setMatchSuffix(StringUtilities.computeSuffix(description,
				searchCriteria, maxSuffixLength));
	}

	public void setAttachedToId(final String attachedToId) {
		this.attachedToId = attachedToId;
	}

	public String getAttachedToId() {
		return this.attachedToId;
	}

	public void setAttachedToDomainType(final String attachedToDomainType) {
		this.attachedToDomainType = attachedToDomainType;
	}

	public String getAttachedToDomainType() {
		return this.attachedToDomainType;
	}
}
