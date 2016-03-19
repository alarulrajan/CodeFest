package com.technoetic.xplanner.domain;

import com.technoetic.xplanner.util.StringUtilities;

/**
 * Created by IntelliJ IDEA. User: tkmower Date: Nov 29, 2004 Time: 4:47:59 PM
 */
public class SearchResult extends Object {
	
	/** The match prefix. */
	private String matchPrefix = "";
	
	/** The match suffix. */
	private String matchSuffix = "";
	
	/** The matching text. */
	private String matchingText = "";
	
	/** The result type. */
	private final String resultType;
	
	/** The matching object. */
	private final Nameable matchingObject;
	
	/** The match in description. */
	private boolean matchInDescription = false;
	
	/** The attached to id. */
	private String attachedToId;
	
	/** The attached to domain type. */
	private String attachedToDomainType;

	/**
     * Instantiates a new search result.
     *
     * @param matchingObject
     *            the matching object
     * @param resultType
     *            the result type
     */
	public SearchResult(final Nameable matchingObject, final String resultType) {
		this.matchingObject = matchingObject;
		this.resultType = resultType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	/**
     * Gets the match prefix.
     *
     * @return the match prefix
     */
	public String getMatchPrefix() {
		return this.matchPrefix;
	}

	/**
     * Sets the match prefix.
     *
     * @param matchPrefix
     *            the new match prefix
     */
	public void setMatchPrefix(final String matchPrefix) {
		this.matchPrefix = matchPrefix;
	}

	/**
     * Gets the match suffix.
     *
     * @return the match suffix
     */
	public String getMatchSuffix() {
		return this.matchSuffix;
	}

	/**
     * Sets the match suffix.
     *
     * @param matchSuffix
     *            the new match suffix
     */
	public void setMatchSuffix(final String matchSuffix) {
		this.matchSuffix = matchSuffix;
	}

	/**
     * Gets the matching object.
     *
     * @return the matching object
     */
	public Object getMatchingObject() {
		return this.matchingObject;
	}

	/**
     * Gets the matching text.
     *
     * @return the matching text
     */
	public String getMatchingText() {
		return this.matchingText;
	}

	/**
     * Sets the matching text.
     *
     * @param matchingText
     *            the new matching text
     */
	public void setMatchingText(final String matchingText) {
		this.matchingText = matchingText;
	}

	/**
     * Gets the result type.
     *
     * @return the result type
     */
	public String getResultType() {
		return this.resultType;
	}

	/**
     * Gets the title.
     *
     * @return the title
     */
	public String getTitle() {
		return this.matchingObject.getName();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getDomainObjectId();
	}

	/**
     * Gets the domain object id.
     *
     * @return the domain object id
     */
	public int getDomainObjectId() {
		return this.matchingObject.getId();
	}

	/**
     * Checks if is match in description.
     *
     * @return true, if is match in description
     */
	public boolean isMatchInDescription() {
		return this.matchInDescription;
	}

	/**
     * Sets the match in description.
     *
     * @param matchInDescription
     *            the new match in description
     */
	public void setMatchInDescription(final boolean matchInDescription) {
		this.matchInDescription = matchInDescription;
	}

	/**
     * Populate.
     *
     * @param searchCriteria
     *            the search criteria
     * @param desiredDescriptionLines
     *            the desired description lines
     * @param maxSuffixLength
     *            the max suffix length
     */
	public void populate(final String searchCriteria,
			final int desiredDescriptionLines, final int maxSuffixLength) {
		if (this.getLocationOfCriteria(searchCriteria) < 0) {
			this.populateResultWithNoDescriptionMatch(desiredDescriptionLines);
		} else {
			this.populateResultWithPrefixSuffixAndResult(searchCriteria,
					maxSuffixLength);
		}
	}

	/**
     * Gets the location of criteria.
     *
     * @param searchCriteria
     *            the search criteria
     * @return the location of criteria
     */
	private int getLocationOfCriteria(final String searchCriteria) {
		return this.matchingObject.getDescription().toLowerCase()
				.indexOf(searchCriteria.toLowerCase());
	}

	/**
     * Populate result with no description match.
     *
     * @param desiredDescriptionLines
     *            the desired description lines
     */
	private void populateResultWithNoDescriptionMatch(
			final int desiredDescriptionLines) {
		this.setMatchingText(StringUtilities.getFirstNLines(
				this.matchingObject.getDescription(), desiredDescriptionLines));
	}

	/**
     * Populate result with prefix suffix and result.
     *
     * @param searchCriteria
     *            the search criteria
     * @param maxSuffixLength
     *            the max suffix length
     */
	private void populateResultWithPrefixSuffixAndResult(
			final String searchCriteria, final int maxSuffixLength) {
		this.populate(searchCriteria, maxSuffixLength);
	}

	/**
     * Populate.
     *
     * @param searchCriteria
     *            the search criteria
     * @param maxSuffixLength
     *            the max suffix length
     */
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

	/**
     * Sets the attached to id.
     *
     * @param attachedToId
     *            the new attached to id
     */
	public void setAttachedToId(final String attachedToId) {
		this.attachedToId = attachedToId;
	}

	/**
     * Gets the attached to id.
     *
     * @return the attached to id
     */
	public String getAttachedToId() {
		return this.attachedToId;
	}

	/**
     * Sets the attached to domain type.
     *
     * @param attachedToDomainType
     *            the new attached to domain type
     */
	public void setAttachedToDomainType(final String attachedToDomainType) {
		this.attachedToDomainType = attachedToDomainType;
	}

	/**
     * Gets the attached to domain type.
     *
     * @return the attached to domain type
     */
	public String getAttachedToDomainType() {
		return this.attachedToDomainType;
	}
}
