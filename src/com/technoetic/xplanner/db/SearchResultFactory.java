package com.technoetic.xplanner.db;

import java.util.HashMap;
import java.util.Map;

import net.sf.xplanner.domain.Note;

import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.domain.SearchResult;

/**
 * A factory for creating SearchResult objects.
 */
public class SearchResultFactory {
	// DEBT factor the type mapping in the RelationshipMappingRegistry or
	/** The types. */
	// similar metadata registry.
	private Map types = new HashMap();
	
	/** The desired description lines. */
	private int desiredDescriptionLines = 3;
	
	/** The max suffix length. */
	private int maxSuffixLength = 20;

	/**
     * Instantiates a new search result factory.
     *
     * @param types
     *            the types
     */
	public SearchResultFactory(final Map types) {
		this.types = types;
	}

	/**
     * Convert object to search result.
     *
     * @param titled
     *            the titled
     * @param searchCriteria
     *            the search criteria
     * @return the search result
     * @throws Exception
     *             the exception
     */
	public SearchResult convertObjectToSearchResult(final Nameable titled,
			final String searchCriteria) throws Exception {
		final String resultType = this.convertClassToType(titled.getClass()
				.getName());

		final SearchResult searchResult = new SearchResult(titled, resultType);
		if ("note".equals(resultType)) {
			final Note note = (Note) titled;
			searchResult
					.setAttachedToId(String.valueOf(note.getAttachedToId()));
			Object objectToWhichImAttached = null;
			objectToWhichImAttached = note.getParent();
			final Class classOfAttachedTo = objectToWhichImAttached.getClass();
			searchResult.setAttachedToDomainType(this
					.convertClassToType(classOfAttachedTo.getName()));
		}
		searchResult.populate(searchCriteria, this.desiredDescriptionLines,
				this.maxSuffixLength);
		return searchResult;
	}

	/**
     * Convert class to type.
     *
     * @param className
     *            the class name
     * @return the string
     */
	private String convertClassToType(final String className) {
		return (String) this.types.get(className);
	}

	/**
     * Sets the desired description lines.
     *
     * @param desiredDescriptionLines
     *            the new desired description lines
     */
	public void setDesiredDescriptionLines(final int desiredDescriptionLines) {
		this.desiredDescriptionLines = desiredDescriptionLines;
	}

	/**
     * Sets the max suffix length.
     *
     * @param maxSuffixLength
     *            the new max suffix length
     */
	public void setMaxSuffixLength(final int maxSuffixLength) {
		this.maxSuffixLength = maxSuffixLength;
	}
}
