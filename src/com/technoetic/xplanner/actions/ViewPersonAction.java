package com.technoetic.xplanner.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.dao.TaskDao;
import net.sf.xplanner.dao.UserStoryDao;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Task;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.domain.repository.TaskRepository;

/**
 * User: Mateusz Prokopowicz Date: Jul 5, 2005 Time: 2:45:56 PM.
 */
public class ViewPersonAction extends ViewObjectAction<Person> {
	
	/** The task dao. */
	private TaskDao taskDao;
	
	/** The user story dao. */
	private UserStoryDao userStoryDao;

	/** The task repository. */
	TaskRepository taskRepository;

	/**
     * Sets the task repository.
     *
     * @param taskRepository
     *            the new task repository
     */
	public void setTaskRepository(final TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.ViewObjectAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward doExecute(final ActionMapping actionMapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse reply) throws Exception {

		final int personId = Integer.parseInt(request.getParameter("oid"));

		// grab a list of all active tasks
		final List<Task> tasks = this.taskDao
				.getCurrentTasksForPerson(personId);
		final Person person = this.getCommonDao().getById(Person.class,
				personId);
		request.setAttribute("person", person);
		// use the utility methods on the task repository to filter the results
		request.setAttribute("currentActiveTasksForPerson",
				this.taskRepository.getCurrentActiveTasks(tasks));
		request.setAttribute("currentPendingTasksForPerson",
				this.taskRepository.getCurrentPendingTasks(tasks));
		request.setAttribute("currentCompletedTasksForPerson",
				this.taskRepository.getCurrentCompletedTasks(tasks));
		request.setAttribute("futureTasksForPerson",
				this.taskRepository.getFutureTasksForPerson(personId));
		request.setAttribute("storiesForCustomer",
				this.userStoryDao.getStoriesForPersonWhereCustomer(personId));
		request.setAttribute("storiesForTracker",
				this.userStoryDao.getStoriesForPersonWhereTracker(personId));

		return super.doExecute(actionMapping, form, request, reply);
	}

	/**
     * Sets the task dao.
     *
     * @param taskDao
     *            the new task dao
     */
	public void setTaskDao(final TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	/**
     * Sets the user story dao.
     *
     * @param userStoryDao
     *            the new user story dao
     */
	public void setUserStoryDao(final UserStoryDao userStoryDao) {
		this.userStoryDao = userStoryDao;
	}

}
