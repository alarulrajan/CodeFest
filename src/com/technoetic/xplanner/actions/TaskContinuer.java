package com.technoetic.xplanner.actions;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.domain.TaskDisposition;

public class TaskContinuer extends Continuer {
	private TaskDisposition dispositionOfContinuedTasks;

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

	public void setDispositionOfContinuedTasks(
			final TaskDisposition dispositionOfContinuedTasks) {
		this.dispositionOfContinuedTasks = dispositionOfContinuedTasks;
	}

	public TaskDisposition getDispositionOfContinuedTasks(
			final UserStory toStory) throws HibernateException {
		if (this.dispositionOfContinuedTasks == null) {
			this.determineTaskDisposition(toStory);
		}
		return this.dispositionOfContinuedTasks;
	}

	private void determineTaskDisposition(final UserStory toStory)
			throws HibernateException {
		final Iteration iteration = (Iteration) this.metaDataRepository
				.getParent(toStory);
		this.dispositionOfContinuedTasks = iteration.isActive() ? TaskDisposition.ADDED
				: TaskDisposition.CARRIED_OVER;
	}
}
