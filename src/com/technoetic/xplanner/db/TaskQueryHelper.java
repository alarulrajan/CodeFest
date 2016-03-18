package com.technoetic.xplanner.db;

import java.util.Collection;
import java.util.Date;

/**
 * TODO move to task repository.
 * 
 * @see com.technoetic.xplanner.domain.repository.TaskRepositoryHibernate
 */
public class TaskQueryHelper {
	public static final String EMAIL_TO_LEADS_QUERY = "com.technoetic.xplanner.domain.TimeEntryEmailNotificationToProjectSpecificLeads";
	public static final String EMAIL_TO_ACCEPTORS_QUERY = "com.technoetic.xplanner.domain.TimeEntryEmailNotificationToAcceptors";

	private final Collection cache = null;
	private int personId;

	private TaskQuery taskQuery;

	public TaskQueryHelper() {
	}

	public TaskQueryHelper(final TaskQuery taskQuery) {
		this.taskQuery = taskQuery;
	}

	public void setPersonId(final int personId) {
		this.personId = personId;
	}

	public Collection getCurrentActiveTasksForPerson() {
		return this.taskQuery.query(this.cache, this.personId, Boolean.FALSE,
				Boolean.TRUE);
	}

	public Collection getCurrentPendingTasksForPerson() {
		return this.taskQuery.query(this.cache, this.personId, Boolean.FALSE,
				Boolean.FALSE);
	}

	public Collection getCurrentCompletedTasksForPerson() {
		return this.taskQuery.query(this.cache, this.personId, Boolean.TRUE,
				null);
	}

	public Collection getFutureTasksForPerson() {
		return this.taskQuery.queryTasks("tasks.planned.future", this.personId);
	}

	public Collection getTaskAcceptorsEmailNotification(final Date date) {
		return this.taskQuery.queryTasks(
				TaskQueryHelper.EMAIL_TO_ACCEPTORS_QUERY, date);
	}

	public Collection getProjectLeedsEmailNotification(final Date date) {
		return this.taskQuery.queryTasks(TaskQueryHelper.EMAIL_TO_LEADS_QUERY,
				date);
	}

	public void setTaskQuery(final TaskQuery taskQuery) {
		this.taskQuery = taskQuery;
	}
}
