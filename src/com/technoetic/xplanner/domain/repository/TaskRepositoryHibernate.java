package com.technoetic.xplanner.domain.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sf.xplanner.domain.Task;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.HibernateException;

/**
 * The Class TaskRepositoryHibernate.
 */
/*
 * A Hibernate implementation of the TaskRepository interface.
 * 
 * Implementation is based pretty heavily on (and should 
 * eventually supercede) TaskQueryHelper.
 * 
 * @author James Beard
 * @see    com.technoetic.xplanner.db.TaskQueryHelper
 */
public class TaskRepositoryHibernate extends HibernateObjectRepository
        implements TaskRepository {
    
    /** The Constant EMAIL_TO_LEADS_QUERY. */
    public static final String EMAIL_TO_LEADS_QUERY = "net.sf.xplanner.domain.TimeEntryEmailNotificationToProjectSpecificLeads";
    
    /** The Constant EMAIL_TO_ACCEPTORS_QUERY. */
    public static final String EMAIL_TO_ACCEPTORS_QUERY = "net.sf.xplanner.domain.TimeEntryEmailNotificationToAcceptors";

    /**
     * The Class TaskStatusFilter.
     */
    /*
     * A implementation of the Predicate interface to filter collections of
     * tasks based on whether they are completed and/or active.
     * 
     * @author James Beard
     */
    private class TaskStatusFilter implements Predicate {
        
        /** The is completed. */
        Boolean isCompleted;
        
        /** The is active. */
        Boolean isActive;

        /**
         * Instantiates a new task status filter.
         *
         * @param isCompleted
         *            the is completed
         * @param isActive
         *            the is active
         */
        public TaskStatusFilter(final Boolean isCompleted,
                final Boolean isActive) {
            this.isCompleted = isCompleted;
            this.isActive = isActive;
        }

        /* (non-Javadoc)
         * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
         */
        @Override
        public boolean evaluate(final Object o) {
            final Task task = (Task) o;
            return (this.isCompleted == null || this.isCompleted.booleanValue() == task
                    .isCompleted())
                    && (this.isActive == null || this.isActive.booleanValue() == task
                            .getTimeEntries().size() > 0);
        }
    }

    /**
     * Instantiates a new task repository hibernate.
     *
     * @throws HibernateException
     *             the hibernate exception
     */
    public TaskRepositoryHibernate() throws HibernateException {
        super(Task.class);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.TaskRepository#getCurrentActiveTasksForPerson(int)
     */
    /*
     * Returns a collection of tasks in current iterations where personId is the
     * acceptor, and the task has been started.
     * 
     * @param personId the id of the acceptor
     * 
     * @return the collection of tasks
     */
    @Override
    public Collection getCurrentActiveTasksForPerson(final int personId) {
        return this.getCurrentActiveTasks(this
                .getCurrentTasksForPerson(personId));
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.TaskRepository#getCurrentActiveTasks(java.util.Collection)
     */
    /*
     * Filters a collection of tasks for those in current iterations that have
     * been started.
     * 
     * Can be used to further filter a cached Collection of all the tasks for a
     * particular person.
     * 
     * @param tasks the collection of tasks
     * 
     * @return the filtered collection of tasks
     */
    @Override
    public Collection getCurrentActiveTasks(final Collection tasks) {
        return CollectionUtils.select(tasks, new TaskStatusFilter(
                Boolean.FALSE, Boolean.TRUE));
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.TaskRepository#getCurrentPendingTasksForPerson(int)
     */
    /*
     * Returns a collection of tasks in current iterations where personId is the
     * acceptor, and the task hasn't been started yet.
     * 
     * @param personId the id of the acceptor
     * 
     * @return the collection of tasks
     */
    @Override
    public Collection getCurrentPendingTasksForPerson(final int personId) {
        return this.getCurrentPendingTasks(this
                .getCurrentTasksForPerson(personId));
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.TaskRepository#getCurrentPendingTasks(java.util.Collection)
     */
    /*
     * Filters a collection of tasks for those in current iterations that
     * haven't been started yet.
     * 
     * Can be used to further filter a cached Collection of all the tasks for a
     * particular person.
     * 
     * @param tasks the collection of tasks
     * 
     * @return the filtered collection of tasks
     */
    @Override
    public Collection getCurrentPendingTasks(final Collection tasks) {
        return CollectionUtils.select(tasks, new TaskStatusFilter(
                Boolean.FALSE, Boolean.FALSE));
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.TaskRepository#getCurrentCompletedTasksForPerson(int)
     */
    /*
     * Returns a collection of tasks in current iterations where personId is the
     * acceptor, and the task has already been completed.
     * 
     * @param personId the id of the acceptor
     * 
     * @return the collection of tasks
     */
    @Override
    public Collection getCurrentCompletedTasksForPerson(final int personId) {
        return this.getCurrentCompletedTasks(this
                .getCurrentTasksForPerson(personId));
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.TaskRepository#getCurrentCompletedTasks(java.util.Collection)
     */
    /*
     * Filters a collection of tasks for those in current iterations that have
     * already been completed.
     * 
     * Can be used to further filter a cached Collection of all the tasks for a
     * particular person.
     * 
     * @param tasks the collection of tasks
     * 
     * @return the filtered collection of tasks
     */
    @Override
    public Collection getCurrentCompletedTasks(final Collection tasks) {
        return CollectionUtils.select(tasks, new TaskStatusFilter(Boolean.TRUE,
                null));
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.TaskRepository#getFutureTasksForPerson(int)
     */
    /*
     * Returns a collection of tasks in future iterations where personId is the
     * acceptor, and the task has not been completed.
     * 
     * @param personId the id of the acceptor
     * 
     * @return the collection of tasks
     */
    @Override
    public Collection getFutureTasksForPerson(final int personId) {
        return this.queryTasks("tasks.planned.future", personId);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.TaskRepository#getProjectLeadsEmailNotification(java.util.Date)
     */
    @Override
    public Collection getProjectLeadsEmailNotification(final Date date) {
        return this.getHibernateTemplate().findByNamedQuery(
                TaskRepositoryHibernate.EMAIL_TO_LEADS_QUERY, date);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.TaskRepository#getCurrentTasksForPerson(int)
     */
    /*
     * Returns a collection of tasks in current iterations where personId is the
     * acceptor.
     * 
     * @param personId the id of the acceptor
     * 
     * @return the collection of tasks
     */
    @Override
    public Collection getCurrentTasksForPerson(final int personId) {
        final Collection currentTasks = this.queryTasks(
                "tasks.current.accepted", personId);
        currentTasks.addAll(this.queryTasks("tasks.current.worked", personId));

        return currentTasks;
    }

    /**
     * Query tasks.
     *
     * @param queryName
     *            the query name
     * @param personId
     *            the person id
     * @return the list
     */
    private List queryTasks(final String queryName, final int personId) {
        return this.getHibernateTemplate().findByNamedQueryAndNamedParam(
                queryName, new String[] { "now", "personId" },
                new Object[] { new Date(), new Integer(personId) });
    }

}
