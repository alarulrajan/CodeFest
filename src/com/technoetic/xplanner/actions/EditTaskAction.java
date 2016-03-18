package com.technoetic.xplanner.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.util.StringUtils;

import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.forms.TaskEditorForm;
import com.technoetic.xplanner.mail.EmailFormatterImpl;
import com.technoetic.xplanner.mail.EmailNotificationSupport;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;

public class EditTaskAction extends EditObjectAction<Task> {
	private EmailNotificationSupport emailNotificationSupport;

	@Override
	protected ActionForward doExecute(final ActionMapping actionMapping,
			final ActionForm actionForm, final HttpServletRequest request,
			final HttpServletResponse reply) throws Exception {
		final TaskEditorForm form = (TaskEditorForm) actionForm;
		final String action = form.getAction();
		Task oldTask = null;
		if (StringUtils.hasText(form.getOid())) {
			oldTask = new Task();
			this.copyProperties(
					oldTask,
					this.getCommonDao().getById(Task.class,
							Integer.parseInt(form.getOid())));
		}
		final boolean isSubmitted = form.isSubmitted();
		final ActionForward forward = super.doExecute(actionMapping,
				actionForm, request, reply);
		this.sendNotification(forward, oldTask, request, action);
		if (!isSubmitted) {
			this.setTaskDisposition(form, request);
		}
		return forward;
	}

	private void sendNotification(final ActionForward forward,
			final Task oldTask, final HttpServletRequest request,
			final String action) throws NumberFormatException,
			AuthenticationException {
		if (forward.getRedirect()) {
			final Task task = (Task) request
					.getAttribute(AbstractAction.TARGET_OBJECT);
			final int remoteUserId = SecurityHelper.getRemoteUserId(request);

			if (remoteUserId == 0 || task == null) {
				return;
			}

			Person acceptor = null;
			Person oldAcceptor = null;

			if (task.getAcceptorId() != 0) {
				acceptor = this.getCommonDao().getById(Person.class,
						task.getAcceptorId());
				if (oldTask == null
						|| task.getAcceptorId() == oldTask.getAcceptorId()) {
					oldAcceptor = acceptor;
				}
			}
			if (oldAcceptor == null && task.getAcceptorId() != 0) {
				oldAcceptor = this.getCommonDao().getById(Person.class,
						task.getAcceptorId());
			}

			final Person editor = this.getCommonDao().getById(Person.class,
					remoteUserId);
			int storyId = NumberUtils.toInt(request.getParameter("fkey"), 0);
			if (storyId == 0) {
				if (task.getUserStory() != null) {
					storyId = task.getUserStory().getId();
				} else {
					return;
				}
			}
			final UserStory story = this.getCommonDao().getById(
					UserStory.class, storyId);
			final Map<Integer, List<Object>> notificationEmails = new HashMap<Integer, List<Object>>();
			this.emailNotificationSupport.compileEmail(notificationEmails,
					remoteUserId, acceptor, task, story);
			if (acceptor != null) {
				this.emailNotificationSupport.compileEmail(notificationEmails,
						acceptor.getId(), editor, task, story);
			}
			final Map<String, Object> params = new HashMap<String, Object>();
			if (action.equals(EditObjectAction.CREATE_ACTION)) {
				params.put(EmailFormatterImpl.TEMPLATE,
						"com/technoetic/xplanner/mail/velocity/email_notification_task_created.vm");
				params.put(EmailFormatterImpl.SUBJECT, "task.created.subject");
				params.put(EmailFormatterImpl.TITLE, "task.created.title");
				params.put(EmailFormatterImpl.HEADER, "task.created.header");
				params.put(EmailFormatterImpl.FOOTER, "task.created.footer");
			} else {
				if (oldAcceptor != null
						&& (acceptor == null || oldAcceptor.equals(acceptor))) {
					this.emailNotificationSupport.compileEmail(
							notificationEmails, oldAcceptor.getId(), editor,
							task, story);
				}
				params.put(EmailFormatterImpl.TEMPLATE,
						"com/technoetic/xplanner/mail/velocity/email_notification_task_updated.vm");
				params.put(EmailFormatterImpl.SUBJECT, "task.updated.subject");
				params.put(EmailFormatterImpl.TITLE, "task.updated.title");
				params.put(EmailFormatterImpl.HEADER, "task.updated.header");
				params.put(EmailFormatterImpl.FOOTER, "task.updated.footer");
				params.put("oldTask", oldTask);
			}
			if (acceptor != null) {
				params.put("asignee", acceptor.getName());
			} else {
				params.put("asignee", "Unassigned");
			}
			if (oldAcceptor != null) {
				params.put("oldAsignee", oldAcceptor.getName());
			} else {
				params.put("oldAsignee", "Unassigned");
			}

			params.put("creator", editor.getName());
			params.put("task", task);
			this.emailNotificationSupport.sendNotifications(notificationEmails,
					params);
			System.out.println("Email was sent to:");
			System.out.println(acceptor);
			System.out.println(oldAcceptor);
			System.out.println(editor);
		}
	}

	private void setTaskDisposition(final TaskEditorForm form,
			final HttpServletRequest request) throws RepositoryException {
		if (!form.isSubmitted()) {
			final String oid = form.getOid();
			if (oid == null) {
				final int storyId = Integer.parseInt(request
						.getParameter("fkey"));
				;
				final UserStory story = this.getCommonDao().getById(
						UserStory.class, storyId);
				form.setDispositionName(this.getTaskDisposition(story));
			}
		}
	}

	private String getTaskDisposition(final UserStory story)
			throws RepositoryException {
		final Iteration iteration = this.getCommonDao().getById(
				Iteration.class, story.getIteration().getId());
		return iteration.getNewTaskDispositionName(story);
	}

	public void setEmailNotificationSupport(
			final EmailNotificationSupport emailNotificationSupport) {
		this.emailNotificationSupport = emailNotificationSupport;
	}

	@Override
	protected void populateObject(final HttpServletRequest request,
			final Object object, final ActionForm actionForm) throws Exception {
		super.populateObject(request, object, actionForm);
		final TaskEditorForm taskEditorForm = (TaskEditorForm) actionForm;
		final int storyId = taskEditorForm.getUserStoryId();
		if (storyId != 0) {
			final UserStory story = this.getCommonDao().getById(
					UserStory.class, storyId);
			((Task) object).setUserStory(story);
		}
	}
}
