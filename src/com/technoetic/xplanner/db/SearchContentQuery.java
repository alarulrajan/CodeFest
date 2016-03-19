package com.technoetic.xplanner.db;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.repository.RepositoryException;

/**
 * The Class SearchContentQuery.
 */
public class SearchContentQuery {
    
    /** The restricted project id. */
    private int restrictedProjectId = 0;

    /**
     * Find where name or description contains.
     *
     * @param searchCriteria
     *            the search criteria
     * @param objectClass
     *            the object class
     * @return the list
     * @throws RepositoryException
     *             the repository exception
     */
    public List findWhereNameOrDescriptionContains(final String searchCriteria,
            final Class objectClass) throws RepositoryException {
        try {
            return this.runSearchQuery(searchCriteria, objectClass);
        } catch (final HibernateException e) {
            throw new RepositoryException(e);
        }
    }

    /**
     * Sets the restricted project id.
     *
     * @param restrictedProjectId
     *            the new restricted project id
     */
    public void setRestrictedProjectId(final int restrictedProjectId) {
        this.restrictedProjectId = restrictedProjectId;
    }

    /**
     * Run search query.
     *
     * @param searchCriteria
     *            the search criteria
     * @param objectClass
     *            the object class
     * @return the list
     * @throws HibernateException
     *             the hibernate exception
     */
    private List runSearchQuery(final String searchCriteria,
            final Class objectClass) throws HibernateException {
        final Query query = ThreadSession.get().getNamedQuery(
                this.getQueryName(objectClass));
        query.setString("contents", "%" + searchCriteria + "%");
        if (this.restrictedProjectId > 0) {
            query.setInteger("projectId", this.restrictedProjectId);
        }
        return this.copyResults(query.list());
    }

    /**
     * Gets the query name.
     *
     * @param objectClass
     *            the object class
     * @return the query name
     */
    private String getQueryName(final Class objectClass) {
        return objectClass.getName()
                + (this.restrictedProjectId == 0 ? "SearchQuery"
                        : "RestrictedSearchQuery");
    }

    /**
     * Copy results.
     *
     * @param results
     *            the results
     * @return the list
     */
    private List copyResults(final List results) {
        final ArrayList returnValue = new ArrayList();
        if (results != null) {
            returnValue.addAll(results);
        }
        return returnValue;
    }
}
