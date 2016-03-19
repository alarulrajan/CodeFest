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
    
    /** The search criteria. */
    protected String searchCriteria;
    
    /** The search results. */
    private List searchResults;
    
    /** The searchable classes. */
    private final Class[] searchableClasses = new Class[] { Project.class,
            UserStory.class, Task.class, Note.class, Iteration.class };
    
    /** The search result factory. */
    private final SearchResultFactory searchResultFactory;
    
    /** The query. */
    private final SearchContentQuery query;

    /**
     * Instantiates a new content search helper.
     *
     * @param searchResultFactory
     *            the search result factory
     * @param query
     *            the query
     */
    public ContentSearchHelper(final SearchResultFactory searchResultFactory,
            final SearchContentQuery query) {
        this.searchResultFactory = searchResultFactory;
        this.query = query;
    }

    /**
     * Gets the search results.
     *
     * @return the search results
     */
    public List getSearchResults() {
        return this.searchResults;
    }

    /**
     * Search.
     *
     * @param searchCriteria
     *            the search criteria
     * @param userId
     *            the user id
     * @param restrictedProjectId
     *            the restricted project id
     * @throws RepositoryException
     *             the repository exception
     */
    public void search(final String searchCriteria, final int userId,
            final int restrictedProjectId) throws RepositoryException {
        this.searchCriteria = searchCriteria;
        this.query.setRestrictedProjectId(restrictedProjectId);
        final List results = this.findMatchingResults(searchCriteria);
        this.excludeResultsBasedOnUserPermissions(results, userId);
        this.searchResults = results;
    }

    /**
     * Find matching results.
     *
     * @param searchCriteria
     *            the search criteria
     * @return the list
     * @throws RepositoryException
     *             the repository exception
     */
    private List findMatchingResults(final String searchCriteria)
            throws RepositoryException {
        final List results = new LinkedList();
        for (int i = 0; i < this.searchableClasses.length; i++) {
            this.convertMatchesToResults(this.findMatchingObjects(
                    this.searchableClasses[i], searchCriteria), results);
        }
        return results;
    }

    /**
     * Find objects matching.
     *
     * @param searchableClasses
     *            the searchable classes
     * @param searchCriteria
     *            the search criteria
     * @return the list
     * @throws RepositoryException
     *             the repository exception
     */
    protected List findObjectsMatching(final Class[] searchableClasses,
            final String searchCriteria) throws RepositoryException {
        final List results = new LinkedList();
        for (int i = 0; i < searchableClasses.length; i++) {
            this.convertMatchesToResults(this.findMatchingObjects(
                    searchableClasses[i], searchCriteria), results);
        }
        return results;
    }

    /**
     * Find matching objects.
     *
     * @param objectClass
     *            the object class
     * @param searchCriteria
     *            the search criteria
     * @return the list
     * @throws RepositoryException
     *             the repository exception
     */
    private List findMatchingObjects(final Class objectClass,
            final String searchCriteria) throws RepositoryException {
        return this.query.findWhereNameOrDescriptionContains(searchCriteria,
                objectClass);
    }

    /**
     * Exclude results based on user permissions.
     *
     * @param results
     *            the results
     * @param userId
     *            the user id
     */
    private void excludeResultsBasedOnUserPermissions(final List results,
            final int userId) {
        final Predicate predicate = this.getAuthorizationPredicate(userId);
        CollectionUtils.filter(results, predicate);
    }

    /**
     * Gets the authorization predicate.
     *
     * @param userId
     *            the user id
     * @return the authorization predicate
     */
    protected Predicate getAuthorizationPredicate(final int userId) {
        return new SearchResultAuthorizationPredicate(userId);
    }

    /**
     * Convert matches to results.
     *
     * @param matches
     *            the matches
     * @param results
     *            the results
     */
    private void convertMatchesToResults(final List matches, final List results) {
        for (final Iterator iterator = matches.iterator(); iterator.hasNext();) {
            try {
                results.add(this.searchResultFactory.convertObjectToSearchResult(
                        (Nameable) iterator.next(), this.searchCriteria));
            } catch (final Exception ex) {
            }
        }
    }

    /**
     * Convert to search result.
     *
     * @param nameable
     *            the nameable
     * @param searchCriteria
     *            the search criteria
     * @return the search result
     */
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
