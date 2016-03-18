package com.technoetic.xplanner.db;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.technoetic.xplanner.actions.SearchResultAuthorizationPredicate;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.domain.SearchResult;
import com.technoetic.xplanner.domain.repository.RepositoryException;

/**
 * Created by IntelliJ IDEA. User: tkmower Date: Dec 14, 2004 Time: 2:58:40 PM
 */
public class ContentSearchHelper {
	protected String searchCriteria;
	private List searchResults;
	private final Class[] searchableClasses = new Class[] { Project.class,
			UserStory.class, Task.class, Note.class, Iteration.class };
	private final SearchResultFactory searchResultFactory;
	private final SearchContentQuery query;

	public ContentSearchHelper(final SearchResultFactory searchResultFactory,
			final SearchContentQuery query) {
		this.searchResultFactory = searchResultFactory;
		this.query = query;
	}

	public List getSearchResults() {
		return this.searchResults;
	}

	public void search(final String searchCriteria, final int userId,
			final int restrictedProjectId) throws RepositoryException {
		this.searchCriteria = searchCriteria;
		this.query.setRestrictedProjectId(restrictedProjectId);
		final List results = this.findMatchingResults(searchCriteria);
		this.excludeResultsBasedOnUserPermissions(results, userId);
		this.searchResults = results;
	}

	private List findMatchingResults(final String searchCriteria)
			throws RepositoryException {
		final List results = new LinkedList();
		for (int i = 0; i < this.searchableClasses.length; i++) {
			this.convertMatchesToResults(this.findMatchingObjects(
					this.searchableClasses[i], searchCriteria), results);
		}
		return results;
	}

	protected List findObjectsMatching(final Class[] searchableClasses,
			final String searchCriteria) throws RepositoryException {
		final List results = new LinkedList();
		for (int i = 0; i < searchableClasses.length; i++) {
			this.convertMatchesToResults(this.findMatchingObjects(
					searchableClasses[i], searchCriteria), results);
		}
		return results;
	}

	private List findMatchingObjects(final Class objectClass,
			final String searchCriteria) throws RepositoryException {
		return this.query.findWhereNameOrDescriptionContains(searchCriteria,
				objectClass);
	}

	private void excludeResultsBasedOnUserPermissions(final List results,
			final int userId) {
		final Predicate predicate = this.getAuthorizationPredicate(userId);
		CollectionUtils.filter(results, predicate);
	}

	protected Predicate getAuthorizationPredicate(final int userId) {
		return new SearchResultAuthorizationPredicate(userId);
	}

	private void convertMatchesToResults(final List matches, final List results) {
		for (final Iterator iterator = matches.iterator(); iterator.hasNext();) {
			try {
				results.add(this.searchResultFactory.convertObjectToSearchResult(
						(Nameable) iterator.next(), this.searchCriteria));
			} catch (final Exception ex) {
			}
		}
	}

	public SearchResult convertToSearchResult(final Nameable nameable,
			final String searchCriteria) {
		try {
			return this.searchResultFactory.convertObjectToSearchResult(
					nameable, searchCriteria);
		} catch (final Exception e) {
		}
		return null;
	}

}
