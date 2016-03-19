package com.technoetic.xplanner.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import net.sf.xplanner.domain.Task;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

/**
 * The Class CurrentTaskQuery.
 */
public class CurrentTaskQuery {
    
    /** The log. */
    private final Logger log = Logger.getLogger(this.getClass());
    
    /** The query. */
    private static String query;
    
    /** The tasks in progress. */
    private Collection tasksInProgress;
    
    /** The completed tasks. */
    private Collection completedTasks;
    
    /** The tasks. */
    private Collection tasks;
    
    /** The person id. */
    private int personId;

    /**
     * Gets the tasks.
     *
     * @return the tasks
     * @throws QueryException
     *             the query exception
     */
    private java.util.Collection getTasks() throws QueryException {
        if (this.personId == 0) {
            throw new QueryException("no person specified for query");
        }
        final Session session = ThreadSession.get();
        if (this.tasks == null) {
            try {
                if (session == null) {
                    this.log.error("no Hibernate session provided, ignoring "
                            + this);
                    return Collections.EMPTY_LIST;
                }
                try {
                    if (CurrentTaskQuery.query == null) {
                        CurrentTaskQuery.query = "select distinct task "
                                + " from task in class net.sf.xplanner.domain.Task, "
                                + " time_entry in class net.sf.xplanner.domain.TimeEntry, "
                                + " iteration in class net.sf.xplanner.domain.Iteration, "
                                + " story in class net.sf.xplanner.domain.UserStory,"
                                + " person in class net.sf.xplanner.domain.Person "
                                + " where task.id = time_entry.task.id and"
                                + " task.userStory.id = story.id and story.iteration.id = iteration.id and"
                                + " (iteration.startDate <= ? and iteration.endDate >= ?) and"
                                + " (time_entry.person1Id = person.id or time_entry.person2Id = person.id)"
                                + " and person.id = ?";
                    }
                    final Date now = new Date();
                    this.tasks = session
                            .find(CurrentTaskQuery.query, new Object[] { now,
                                    now, new Integer(this.personId) },
                                    new Type[] { Hibernate.DATE,
                                            Hibernate.DATE, Hibernate.INTEGER });
                } catch (final Exception ex) {
                    this.log.error("query error", ex);
                } finally {
                    session.connection().rollback();
                }
            } catch (final Exception ex) {
                this.log.error("error in CurrentTaskQuery", ex);
            }
        }
        return this.tasks;
    }

    /**
     * Sets the person id.
     *
     * @param personId
     *            the new person id
     */
    public void setPersonId(final int personId) {
        this.personId = personId;
    }

    /**
     * Gets the person id.
     *
     * @return the person id
     */
    public int getPersonId() {
        return this.personId;
    }

    /**
     * Gets the completed tasks.
     *
     * @return the completed tasks
     * @throws Exception
     *             the exception
     */
    public Collection getCompletedTasks() throws Exception {
        if (this.completedTasks == null) {
            this.completedTasks = new ArrayList();
            final Iterator taskItr = this.getTasks().iterator();
            while (taskItr.hasNext()) {
                final Task task = (Task) taskItr.next();
                if (task.isCompleted()) {
                    this.completedTasks.add(task);
                }
            }
        }
        return this.completedTasks;
    }

    /**
     * Gets the tasks in progress.
     *
     * @return the tasks in progress
     * @throws QueryException
     *             the query exception
     */
    public Collection getTasksInProgress() throws QueryException {
        if (this.tasksInProgress == null) {
            this.tasksInProgress = new ArrayList();
            final Iterator taskItr = this.getTasks().iterator();
            while (taskItr.hasNext()) {
                final Task task = (Task) taskItr.next();
                if (!task.isCompleted()) {
                    this.tasksInProgress.add(task);
                }
            }
        }
        return this.tasksInProgress;
    }
}
