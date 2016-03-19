package com.technoetic.xplanner.db;

import java.util.Collection;
import java.util.Date;

/**
 * ChangeSoon move to task repository.
 * 
 * @see com.technoetic.xplanner.domain.repository.TaskRepositoryHibernate
 */
public class TaskQueryHelper {
	
	/** The Constant EMAIL_TO_LEADS_QUERY. */
	public static final String EMAIL_TO_LEADS_QUERY = "com.technoetic.xplanner.domain.TimeEntryEmailNotificationToProjectSpecificLeads";
	
	/** The Constant EMAIL_TO_ACCEPTORS_QUERY. */
	public static final String EMAIL_TO_ACCEPTORS_QUERY = "com.technoetic.xplanner.domain.TimeEntryEmailNotificationToAcceptors";

	/** The cache. */
	private final Collection cache = null;
	
	/** The person id. */
	private int personId;

	/** The task query. */
	private TaskQuery taskQuery;

	/**
     * Instantiates a new task query helper.
     */
	public TaskQueryHelper() {
	}

	/**
     * Instantiates a new task query helper.
     *
     * @param taskQuery
     *            the task query
     */
	public TaskQueryHelper(final TaskQuery taskQuery) {
		this.taskQuery = taskQuery;
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
     * Gets the current active tasks for person.
     *
     * @return the current active tasks for person
     */
	public Collection getCurrentActiveTasksForPerson() {
		return this.taskQuery.query(this.cache, this.personId, Boolean.FALSE,
				Boolean.TRUE);
	}

	/**
     * Gets the current pending tasks for person.
     *
     * @return the current pending tasks for person
     */
	public Collection getCurrentPendingTasksForPerson() {
		return this.taskQuery.query(this.cache, this.personId, Boolean.FALSE,
				Boolean.FALSE);
	}

	/**
     * Gets the current completed tasks for person.
     *
     * @return the current completed tasks for person
     */
	public Collection getCurrentCompletedTasksForPerson() {
		return this.taskQuery.query(this.cache, this.personId, Boolean.TRUE,
				null);
	}

	/**
     * Gets the future tasks for person.
     *
     * @return the future tasks for person
     */
	public Collection getFutureTasksForPerson() {
		return this.taskQuery.queryTasks("tasks.planned.future", this.personId);
	}

	/**
     * Gets the task acceptors email notification.
     *
     * @param date
     *            the date
     * @return the task acceptors email notification
     */
	public Collection getTaskAcceptorsEmailNotification(final Date date) {
		return this.taskQuery.queryTasks(
				TaskQueryHelper.EMAIL_TO_ACCEPTORS_QUERY, date);
	}

	/**
     * Gets the project leeds email notification.
     *
     * @param date
     *            the date
     * @return the project leeds email notification
     */
	public Collection getProjectLeedsEmailNotification(final Date date) {
		return this.taskQuery.queryTasks(TaskQueryHelper.EMAIL_TO_LEADS_QUERY,
				date);
	}

	/**
     * Sets the task query.
     *
     * @param taskQuery
     *            the new task query
     */
	public void setTaskQuery(final TaskQuery taskQuery) {
		this.taskQuery = taskQuery;
	}
}
