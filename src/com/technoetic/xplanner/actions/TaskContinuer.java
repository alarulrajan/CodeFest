package com.technoetic.xplanner.actions;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.domain.TaskDisposition;

/**
 * The Class TaskContinuer.
 */
public class TaskContinuer extends Continuer {
	
	/** The disposition of continued tasks. */
	private TaskDisposition dispositionOfContinuedTasks;

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.Continuer#doContinueObject(net.sf.xplanner.domain.DomainObject, net.sf.xplanner.domain.DomainObject, net.sf.xplanner.domain.DomainObject)
	 */
	@Override
	protected void doContinueObject(final DomainObject fromObject,
			final DomainObject toParent, final DomainObject toObject)
			throws HibernateException {

		final Task fromTask = (Task) fromObject;
		final UserStory toStory = (UserStory) toParent;
		final Task toTask = (Task) toObject;

		toTask.setCreatedDate(this.when);
		toTask.setDisposition(TaskDisposition.fromNameKey(this
				.getDispositionOfContinuedTasks(toStory).getNameKey()));
		toTask.setAcceptorId(0);
		toTask.setEstimatedHours(fromTask.getRemainingHours());
		toTask.setEstimatedOriginalHoursField(0.0);
		toTask.setTimeEntries(null);
		fromTask.postpone();
	}

	/**
     * Sets the disposition of continued tasks.
     *
     * @param dispositionOfContinuedTasks
     *            the new disposition of continued tasks
     */
	public void setDispositionOfContinuedTasks(
			final TaskDisposition dispositionOfContinuedTasks) {
		this.dispositionOfContinuedTasks = dispositionOfContinuedTasks;
	}

	/**
     * Gets the disposition of continued tasks.
     *
     * @param toStory
     *            the to story
     * @return the disposition of continued tasks
     * @throws HibernateException
     *             the hibernate exception
     */
	public TaskDisposition getDispositionOfContinuedTasks(
			final UserStory toStory) throws HibernateException {
		if (this.dispositionOfContinuedTasks == null) {
			this.determineTaskDisposition(toStory);
		}
		return this.dispositionOfContinuedTasks;
	}

	/**
     * Determine task disposition.
     *
     * @param toStory
     *            the to story
     * @throws HibernateException
     *             the hibernate exception
     */
	private void determineTaskDisposition(final UserStory toStory)
			throws HibernateException {
		final Iteration iteration = (Iteration) this.metaDataRepository
				.getParent(toStory);
		this.dispositionOfContinuedTasks = iteration.isActive() ? TaskDisposition.ADDED
				: TaskDisposition.CARRIED_OVER;
	}
}
