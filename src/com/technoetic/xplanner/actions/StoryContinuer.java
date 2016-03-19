package com.technoetic.xplanner.actions;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;

/**
 * The Class StoryContinuer.
 */
public class StoryContinuer extends Continuer {
	
	/** The task continuer. */
	private TaskContinuer taskContinuer;

	// DEBT 3LAYERCONTEXT remove dependency on request and move this class into
	/**
     * Inits the.
     *
     * @param session
     *            the session
     * @param request
     *            the request
     * @throws AuthenticationException
     *             the authentication exception
     */
	// the session context.
	public void init(final Session session, final HttpServletRequest request)
			throws AuthenticationException {
		this.init(session,
				(MessageResources) request.getAttribute(Globals.MESSAGES_KEY),
				SecurityHelper.getRemoteUserId(request));
		this.taskContinuer.init(session,
				(MessageResources) request.getAttribute(Globals.MESSAGES_KEY),
				this.currentUserId);
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.Continuer#doContinueObject(net.sf.xplanner.domain.DomainObject, net.sf.xplanner.domain.DomainObject, net.sf.xplanner.domain.DomainObject)
	 */
	@Override
	protected void doContinueObject(final DomainObject fromObject,
			final DomainObject toParent, final DomainObject toObject)
			throws HibernateException {

		final UserStory fromStory = (UserStory) fromObject;
		final Iteration toIteration = (Iteration) toParent;
		final UserStory toStory = (UserStory) toObject;

		fromStory.postponeRemainingHours();

		toStory.setIteration(toIteration);
		toStory.setEstimatedHoursField(fromStory.getTaskBasedRemainingHours());
		toStory.setDisposition(this
				.determineContinuedStoryDisposition(toIteration));
		toStory.setEstimatedOriginalHours(0);
		toStory.setTasks(new ArrayList<Task>());
		this.continueTasks(fromStory, toStory,
				this.determineTaskDisposition(toIteration));
	}

	/**
     * Determine task disposition.
     *
     * @param iteration
     *            the iteration
     * @return the task disposition
     */
	private TaskDisposition determineTaskDisposition(final Iteration iteration) {
		return iteration.isActive() ? TaskDisposition.ADDED
				: TaskDisposition.CARRIED_OVER;
	}

	/**
     * Continue tasks.
     *
     * @param fromStory
     *            the from story
     * @param toStory
     *            the to story
     * @param taskDisposition
     *            the task disposition
     * @throws HibernateException
     *             the hibernate exception
     */
	private void continueTasks(final UserStory fromStory,
			final UserStory toStory, final TaskDisposition taskDisposition)
			throws HibernateException {
		final Iterator taskIterator = fromStory.getTasks().iterator();
		this.taskContinuer.setDispositionOfContinuedTasks(taskDisposition);
		while (taskIterator.hasNext()) {
			final Task task = (Task) taskIterator.next();
			if (!task.isCompleted()) {
				this.taskContinuer.continueObject(task, fromStory, toStory);
			}
		}
	}

	/**
     * Determine continued story disposition.
     *
     * @param iteration
     *            the iteration
     * @return the story disposition
     */
	public StoryDisposition determineContinuedStoryDisposition(
			final Iteration iteration) {
		return iteration.isActive() ? StoryDisposition.ADDED
				: StoryDisposition.CARRIED_OVER;
	}

	/**
     * Sets the task continuer.
     *
     * @param taskContinuer
     *            the new task continuer
     */
	public void setTaskContinuer(final TaskContinuer taskContinuer) {
		this.taskContinuer = taskContinuer;
	}

	/**
     * Gets the task continuer.
     *
     * @return the task continuer
     */
	public TaskContinuer getTaskContinuer() {
		return this.taskContinuer;
	}
}
