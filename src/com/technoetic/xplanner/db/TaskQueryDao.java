package com.technoetic.xplanner.db;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sf.xplanner.domain.Task;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * User: Mateusz Prokopowicz Date: Aug 24, 2005 Time: 6:22:01 PM
 */
public class TaskQueryDao extends HibernateDaoSupport implements TaskQuery {

	@Override
	public Collection query(final Collection cachedTasks, final int personId,
			final Boolean completed, final Boolean active) {
		return CollectionUtils.select(
				this.getCurrentTasks(cachedTasks, personId),
				new TaskStatusFilter(completed, active));
	}

	private Collection getCurrentTasks(Collection cache, final int personId) {
		if (cache == null) {
			cache = this.queryTasks("tasks.current.accepted", personId);
			cache.addAll(this.queryTasks("tasks.current.worked", personId));
		}
		return cache;
	}

	@Override
	public List queryTasks(final String queryName, final int personId) {
		return this.getHibernateTemplate().findByNamedQueryAndNamedParam(
				queryName, new String[] { "now", "personId" },
				new Object[] { new Date(), new Integer(personId) });
	}

	@Override
	public List queryTasks(final String queryName, final Date date) {
		return this.getHibernateTemplate().findByNamedQuery(queryName, date);
	}

	private class TaskStatusFilter implements Predicate {
		Boolean isCompleted;
		Boolean isActive;

		public TaskStatusFilter(final Boolean isCompleted,
				final Boolean isActive) {
			this.isCompleted = isCompleted;
			this.isActive = isActive;
		}

		@Override
		public boolean evaluate(final Object o) {
			final Task task = (Task) o;
			return (this.isCompleted == null || this.isCompleted.booleanValue() == task
					.isCompleted())
					&& (this.isActive == null || this.isActive.booleanValue() == task
							.getTimeEntries().size() > 0);
		}
	}
}
