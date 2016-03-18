package com.technoetic.xplanner.db;

import java.util.HashMap;
import java.util.Map;

import net.sf.xplanner.domain.Note;

import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.domain.SearchResult;

public class SearchResultFactory {
	// DEBT factor the type mapping in the RelationshipMappingRegistry or
	// similar metadata registry.
	private Map types = new HashMap();
	private int desiredDescriptionLines = 3;
	private int maxSuffixLength = 20;

	public SearchResultFactory(final Map types) {
		this.types = types;
	}

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

	private String convertClassToType(final String className) {
		return (String) this.types.get(className);
	}

	public void setDesiredDescriptionLines(final int desiredDescriptionLines) {
		this.desiredDescriptionLines = desiredDescriptionLines;
	}

	public void setMaxSuffixLength(final int maxSuffixLength) {
		this.maxSuffixLength = maxSuffixLength;
	}
}
