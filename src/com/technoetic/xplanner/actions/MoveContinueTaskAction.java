package com.technoetic.xplanner.actions;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.domain.RelationshipConvertor;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.forms.MoveContinueTaskForm;
import com.technoetic.xplanner.security.SecurityHelper;

/**
 * The Class MoveContinueTaskAction.
 */
public class MoveContinueTaskAction extends EditObjectAction<Task> {
	
	/** The log. */
	private final Logger log = Logger.getLogger(this.getClass());
	
	/** The Constant TARGET_STORY_ID_CONVERTOR. */
	public static final RelationshipConvertor TARGET_STORY_ID_CONVERTOR = new RelationshipConvertor(
			"targetStoryId", "userStory");
	
	/** The Constant CONTINUE_ACTION. */
	public static final String CONTINUE_ACTION = "Continue";
	
	/** The Constant MOVE_ACTION. */
	public static final String MOVE_ACTION = "Move";

	/** The task continuer. */
	private TaskContinuer taskContinuer;

	/**
     * Gets the task continuer.
     *
     * @return the task continuer
     */
	public TaskContinuer getTaskContinuer() {
		return this.taskContinuer;
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

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.AbstractAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(final ActionMapping actionMapping,
			final ActionForm actionForm, final HttpServletRequest request,
			final HttpServletResponse reply) throws Exception {
		final MoveContinueTaskForm taskForm = (MoveContinueTaskForm) actionForm;
		final Session session = this.getSession(request);
		try {
			if (taskForm.isSubmitted()) {
				this.saveForm(taskForm, actionMapping, session, request,
						actionForm, reply);
				final String returnto = request
						.getParameter(EditObjectAction.RETURNTO_PARAM);
				if (returnto != null) {
					return new ActionForward(returnto, true);
				} else {
					return actionMapping.findForward("view/projects");
				}
			} else {
				this.populateForm(taskForm, session);
				return new ActionForward(actionMapping.getInput());
			}
		} catch (final Exception ex) {
			this.log.error("error during task move/continue action", ex);
			session.connection().rollback();
			throw new ServletException(ex);
		}
	}

	/**
     * Save form.
     *
     * @param taskForm
     *            the task form
     * @param actionMapping
     *            the action mapping
     * @param session
     *            the session
     * @param request
     *            the request
     * @param actionForm
     *            the action form
     * @param reply
     *            the reply
     * @throws Exception
     *             the exception
     */
	private void saveForm(final MoveContinueTaskForm taskForm,
			final ActionMapping actionMapping, final Session session,
			final HttpServletRequest request, final ActionForm actionForm,
			final HttpServletResponse reply) throws Exception {
		session.connection().setAutoCommit(false);
		final MessageResources messageResources = (MessageResources) request
				.getAttribute(Globals.MESSAGES_KEY);
		final String oid = taskForm.getOid();
		final int targetStoryId = taskForm.getTargetStoryId();
		final Class objectClass = this.getObjectType(actionMapping, request);
		Task task;
		if (taskForm.getAction().equals(MoveContinueTaskAction.CONTINUE_ACTION)) {
			task = (Task) session.load(objectClass, new Integer(oid));
			this.taskContinuer.init(session, (MessageResources) request
					.getAttribute(Globals.MESSAGES_KEY), SecurityHelper
					.getRemoteUserId(request));
			final UserStory fromStory = this.getCommonDao().getById(
					UserStory.class, task.getUserStory().getId());
			final UserStory toStory = this.getCommonDao().getById(
					UserStory.class, taskForm.getTargetStoryId());
			this.taskContinuer.continueObject(task, fromStory, toStory);
		} else if (taskForm.getAction().equals(
				MoveContinueTaskAction.MOVE_ACTION)) {
			task = (Task) session.load(objectClass, new Integer(oid));
			final UserStory origStory = task.getUserStory();
			final UserStory targetStory = (UserStory) session.load(
					UserStory.class, new Integer(targetStoryId));
			this.historySupport.saveEvent(
					task,
					History.MOVED,
					messageResources.getMessage("task.moved.from.to",
							origStory.getName(), targetStory.getName()),
					SecurityHelper.getRemoteUserId(request), new Date());
			this.historySupport.saveEvent(
					origStory,
					History.MOVED_OUT,
					messageResources.getMessage("task.moved.out.to",
							task.getName(), targetStory.getName()),
					SecurityHelper.getRemoteUserId(request), new Date());

			final Iteration targetIteration = targetStory.getIteration();

			final boolean isTheSameIteration = origStory.getIteration() == targetStory
					.getIteration();

			if (!isTheSameIteration) {
				if (targetIteration.isFuture() && !targetIteration.isActive()) {
					if (targetStory.getDisposition() == StoryDisposition.ADDED) {
						task.setDisposition(TaskDisposition.DISCOVERED);
					} else if (targetStory.getDisposition() == StoryDisposition.CARRIED_OVER) {
						task.setDisposition(TaskDisposition.CARRIED_OVER);
					} else if (targetStory.getDisposition() == StoryDisposition.PLANNED) {
						task.setDisposition(TaskDisposition.PLANNED);
					}
				} else {
					task.setDisposition(TaskDisposition.ADDED);
				}
			}

			MoveContinueTaskAction.TARGET_STORY_ID_CONVERTOR
					.populateDomainObject(task, taskForm, this.getCommonDao());

			this.historySupport.saveEvent(
					targetStory,
					History.MOVED_IN,
					messageResources.getMessage("task.moved.in.from",
							task.getName(), origStory.getName()),
					SecurityHelper.getRemoteUserId(request), new Date());
		} else {
			throw new ServletException("Unknown task editor form action: "
					+ taskForm.getAction());
		}
		taskForm.setAction(null);
		session.flush();
		session.connection().commit();
		this.afterObjectCommit(actionMapping, actionForm, request, reply);
	}

	/**
     * Populate form.
     *
     * @param taskEditorForm
     *            the task editor form
     * @param session
     *            the session
     * @throws Exception
     *             the exception
     */
	private void populateForm(final MoveContinueTaskForm taskEditorForm,
			final Session session) throws Exception {
		final String oid = taskEditorForm.getOid();
		if (oid != null) {
			final Task object = (Task) session.load(Task.class,
					new Integer(oid));
			this.populateForm(taskEditorForm, object);
		}
	}
}