package com.technoetic.xplanner.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.Task;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

public class PlannedTaskQuery {
	private final Logger log = Logger.getLogger(this.getClass());
	private static String query;
	private static String futureQuery;
	private final java.util.Collection tasks = new ArrayList();
	private int personId;

	public boolean hasCurrentTasks() throws QueryException {
		return this.getTasks().size() > 0;
	}

	public java.util.Collection getTasks() throws QueryException {
		if (this.personId == 0) {
			throw new QueryException("no person specified for query");
		}
		final Session session = ThreadSession.get();
		try {
			if (session == null) {
				this.log.error("no Hibernate session provided, ignoring "
						+ this);
				return Collections.EMPTY_LIST;
			}
			try {
				if (PlannedTaskQuery.query == null) {
					PlannedTaskQuery.query = "select distinct task "
							+ " from task in class net.sf.xplanner.domain.Task, "
							+ " iteration in class net.sf.xplanner.domain.Iteration, "
							+ " story in class net.sf.xplanner.domain.UserStory,"
							+ " person in class net.sf.xplanner.domain.Person "
							+ " where task.userStory.id = story.id and story.iteration.id = iteration.id and"
							+ " (iteration.startDate <= ? and iteration.endDate >= ?) and"
							+ " task.acceptorId = person.id and person.id = ? and task.completed = 'false'";
				}
				final Date now = new Date();
				final List acceptedTasks = session.find(PlannedTaskQuery.query,
						new Object[] { now, now, new Integer(this.personId) },
						new Type[] { Hibernate.DATE, Hibernate.DATE,
								Hibernate.INTEGER });
				final Iterator itr = acceptedTasks.iterator();
				this.tasks.clear();
				while (itr.hasNext()) {
					final Task task = (Task) itr.next();
					if (task.getTimeEntries().size() == 0) {
						this.tasks.add(task);
					}
				}
			} catch (final Exception ex) {
				this.log.error("query error", ex);
			} finally {
				session.connection().rollback();
			}
		} catch (final Exception ex) {
			this.log.error("error in PlannedTaskQuery", ex);
		}
		return this.tasks;
	}

	public void setPersonId(final int personId) {
		this.personId = personId;
	}

	public int getPersonId() {
		return this.personId;
	}

	public boolean hasFutureTasks() throws QueryException {
		return this.getFutureTasks().size() > 0;
	}

	public java.util.Collection getFutureTasks() throws QueryException {
		if (this.personId == 0) {
			throw new QueryException("no person specified for query");
		}
		final Session session = ThreadSession.get();
		try {
			if (session == null) {
				this.log.error("no Hibernate session provided, ignoring "
						+ this);
				return Collections.EMPTY_LIST;
			}
			try {
				if (PlannedTaskQuery.futureQuery == null) {
					PlannedTaskQuery.futureQuery = "select distinct task "
							+ " from task in class net.sf.xplanner.domain.Task, "
							+ " iteration in class net.sf.xplanner.domain.Iteration, "
							+ " story in class net.sf.xplanner.domain.UserStory,"
							+ " person in class net.sf.xplanner.domain.Person "
							+ " where task.userStory.id = story.id and story.iteration.id = iteration.id and"
							+ " iteration.startDate > ? and"
							+ " task.acceptorId = person.id and"
							+ " person.id = ? and task.completed = 'false'";
				}
				final Date now = new Date();
				return session.find(PlannedTaskQuery.futureQuery, new Object[] {
						now, new Integer(this.personId) }, new Type[] {
						Hibernate.DATE, Hibernate.INTEGER });
			} catch (final Exception ex) {
				this.log.error("query error", ex);
			} finally {
				session.connection().rollback();
			}
		} catch (final Exception ex) {
			this.log.error("error in PlannedTaskQuery", ex);
		}
		return Collections.EMPTY_LIST;
	}
}
