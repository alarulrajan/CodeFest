package com.technoetic.xplanner.domain.repository;

import java.util.Collection;
import java.util.Date;

/**
 * The Interface TaskRepository.
 */
/*
 * A repository that can be used to access collections of tasks
 * based on certain criteria.
 * 
 * @author James Beard
 */
public interface TaskRepository extends ObjectRepository {

	/**
     * Gets the current tasks for person.
     *
     * @param personId
     *            the person id
     * @return the current tasks for person
     */
	/*
	 * Returns a collection of tasks in current iterations where personId is the
	 * acceptor.
	 * 
	 * @param personId the id of the acceptor
	 * 
	 * @return the collection of tasks
	 */
	public Collection getCurrentTasksForPerson(int personId);

	/**
     * Gets the current active tasks for person.
     *
     * @param personId
     *            the person id
     * @return the current active tasks for person
     */
	/*
	 * Returns a collection of tasks in current iterations where personId is the
	 * acceptor, and the task has been started.
	 * 
	 * @param personId the id of the acceptor
	 * 
	 * @return the collection of tasks
	 */
	public Collection getCurrentActiveTasksForPerson(int personId);

	/**
     * Gets the current active tasks.
     *
     * @param tasks
     *            the tasks
     * @return the current active tasks
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
	public Collection getCurrentActiveTasks(Collection tasks);

	/**
     * Gets the current pending tasks for person.
     *
     * @param personId
     *            the person id
     * @return the current pending tasks for person
     */
	/*
	 * Returns a collection of tasks in current iterations where personId is the
	 * acceptor, and the task hasn't been started yet.
	 * 
	 * @param personId the id of the acceptor
	 * 
	 * @return the collection of tasks
	 */
	public Collection getCurrentPendingTasksForPerson(int personId);

	/**
     * Gets the current pending tasks.
     *
     * @param tasks
     *            the tasks
     * @return the current pending tasks
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
	public Collection getCurrentPendingTasks(Collection tasks);

	/**
     * Gets the current completed tasks for person.
     *
     * @param personId
     *            the person id
     * @return the current completed tasks for person
     */
	/*
	 * Returns a collection of tasks in current iterations where personId is the
	 * acceptor, and the task has already been completed.
	 * 
	 * @param personId the id of the acceptor
	 * 
	 * @return the collection of tasks
	 */
	public Collection getCurrentCompletedTasksForPerson(int personId);

	/**
     * Gets the current completed tasks.
     *
     * @param tasks
     *            the tasks
     * @return the current completed tasks
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
	public Collection getCurrentCompletedTasks(Collection tasks);

	/**
     * Gets the future tasks for person.
     *
     * @param personId
     *            the person id
     * @return the future tasks for person
     */
	/*
	 * Returns a collection of tasks in future iterations where personId is the
	 * acceptor, and the task has not been completed.
	 * 
	 * @param personId the id of the acceptor
	 * 
	 * @return the collection of tasks
	 */
	public Collection getFutureTasksForPerson(int personId);

	/**
     * Gets the project leads email notification.
     *
     * @param date
     *            the date
     * @return the project leads email notification
     */
	public Collection getProjectLeadsEmailNotification(Date date);

}
