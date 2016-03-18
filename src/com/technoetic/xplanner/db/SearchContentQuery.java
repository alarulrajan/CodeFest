package com.technoetic.xplanner.db;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.repository.RepositoryException;

public class SearchContentQuery {
	private int restrictedProjectId = 0;

	public List findWhereNameOrDescriptionContains(final String searchCriteria,
			final Class objectClass) throws RepositoryException {
		try {
			return this.runSearchQuery(searchCriteria, objectClass);
		} catch (final HibernateException e) {
			throw new RepositoryException(e);
		}
	}

	public void setRestrictedProjectId(final int restrictedProjectId) {
		this.restrictedProjectId = restrictedProjectId;
	}

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

	private String getQueryName(final Class objectClass) {
		return objectClass.getName()
				+ (this.restrictedProjectId == 0 ? "SearchQuery"
						: "RestrictedSearchQuery");
	}

	private List copyResults(final List results) {
		final ArrayList returnValue = new ArrayList();
		if (results != null) {
			returnValue.addAll(results);
		}
		return returnValue;
	}
}
