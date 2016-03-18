package com.technoetic.xplanner.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.forms.ContinueUnfinishedStoriesForm;

public class ContinueUnfinishedStoriesAction extends
		EditObjectAction<UserStory> {
	public static final String OK_ACTION = "Ok";
	public static final String CANCEL_ACTION = "Cancel";
	private StoryContinuer storyContinuer;

	@Override
	protected ActionForward doExecute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		ContinueUnfinishedStoriesForm continueUnfinishedStoriesForm;
		continueUnfinishedStoriesForm = (ContinueUnfinishedStoriesForm) form;
		final Session session = this.getSession(request);
		try {
			if (continueUnfinishedStoriesForm.isSubmitted()) {
				this.saveForm(continueUnfinishedStoriesForm, mapping, session,
						request, form, response);
				final String returnto = request
						.getParameter(EditObjectAction.RETURNTO_PARAM);
				if (returnto != null) {
					return new ActionForward(returnto, true);
				} else {
					return mapping.findForward("view/projects");
				}
			} else {
				this.populateForm(continueUnfinishedStoriesForm, session);
				return new ActionForward(mapping.getInput());
			}
		} catch (final Exception ex) {
			session.connection().rollback();
			throw new ServletException(ex);
		}
	}

	private void populateForm(
			final ContinueUnfinishedStoriesForm continueUnfinishedStoriesForm,
			final Session session) throws HibernateException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		final int iterationId = continueUnfinishedStoriesForm.getIterationId();
		if (iterationId != 0) {
			final Iteration iteration = (Iteration) session.load(
					Iteration.class, new Integer(iterationId));
			PropertyUtils.copyProperties(continueUnfinishedStoriesForm,
					iteration);
			this.populateManyToOneIds(continueUnfinishedStoriesForm, iteration);
		}
	}

	protected void saveForm(final ContinueUnfinishedStoriesForm selectionForm,
			final ActionMapping mapping, final Session session,
			final HttpServletRequest request, final ActionForm form,
			final HttpServletResponse response) throws Exception {
		final String action = selectionForm.getAction();
		if (action.equals(ContinueUnfinishedStoriesAction.OK_ACTION)) {
			final int currentIterationId = selectionForm.getIterationId();
			final int targetIterationId = selectionForm.getTargetIterationId();
			this.continueIteration(currentIterationId, targetIterationId,
					request, session);
		}
	}

	private void continueIteration(final int currentIterationId,
			final int targetIterationId, final HttpServletRequest request,
			final Session session) throws Exception {
		final Iteration originalIteration = this.getCommonDao().getById(
				Iteration.class, currentIterationId);
		final Iteration targetIteration = this.getCommonDao().getById(
				Iteration.class, targetIterationId);
		this.continueUnfinishedStoriesInIteration(request, session,
				originalIteration, targetIteration);
	}

	protected void continueUnfinishedStoriesInIteration(
			final HttpServletRequest request, final Session session,
			final Iteration originalIteration, final Iteration targetIteration)
			throws Exception {
		this.storyContinuer.init(session, request);
		if (originalIteration != null) {
			this.continueIteration(originalIteration, targetIteration);
		}
	}

	private void continueIteration(final Iteration originalIteration,
			final Iteration targetIteration) throws HibernateException {
		originalIteration.setStatusKey(IterationStatus.INACTIVE_KEY);
		final Collection stories = originalIteration.getUserStories();
		if (targetIteration.getId() != 0) {
			for (final Iterator iterator = stories.iterator(); iterator
					.hasNext();) {
				this.continueCompleteStory((UserStory) iterator.next(),
						originalIteration, targetIteration);
			}
		}
	}

	private void continueCompleteStory(final UserStory userStory,
			final Iteration originalIteration, final Iteration targetIteration)
			throws HibernateException {
		if (!userStory.isCompleted()) {
			this.continueStory(userStory, originalIteration, targetIteration);
		}
	}

	private void continueStory(final UserStory userStory,
			final Iteration originalIteration, final Iteration targetIteration)
			throws HibernateException {
		final UserStory continuedUserStory = (UserStory) this.storyContinuer
				.continueObject(userStory, originalIteration, targetIteration);
		final List<UserStory> targetIterationStories = targetIteration
				.getUserStories();
		if (!targetIterationStories.contains(continuedUserStory)) {
			targetIterationStories.add(continuedUserStory);
		}
		targetIteration.setUserStories(targetIterationStories);
	}

	public void setStoryContinuer(final StoryContinuer storyContinuer) {
		this.storyContinuer = storyContinuer;
	}
}
