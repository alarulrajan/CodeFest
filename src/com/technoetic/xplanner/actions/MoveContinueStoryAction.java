package com.technoetic.xplanner.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.domain.RelationshipConvertor;
import com.technoetic.xplanner.domain.RelationshipMappingRegistry;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.forms.MoveContinueStoryForm;

/**
 * The Class MoveContinueStoryAction.
 */
public class MoveContinueStoryAction extends EditObjectAction {
	
	/** The Constant CONTINUE_ACTION. */
	public static final String CONTINUE_ACTION = "Continue";
	
	/** The Constant MOVE_ACTION. */
	public static final String MOVE_ACTION = "Move";

	/** The move continue story. */
	private MoveContinueStory moveContinueStory;

	/**
     * Sets the move continue story.
     *
     * @param moveContinueStory
     *            the new move continue story
     */
	public void setMoveContinueStory(final MoveContinueStory moveContinueStory) {
		this.moveContinueStory = moveContinueStory;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.EditObjectAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward doExecute(final ActionMapping actionMapping,
			final ActionForm actionForm, final HttpServletRequest request,
			final HttpServletResponse reply) throws Exception {
		final Session session = this.getSession(request);
		final MoveContinueStoryForm storyForm = (MoveContinueStoryForm) actionForm;
		if (storyForm.isSubmitted()) {
			this.saveForm(storyForm, session, request);
			final String returnto = request
					.getParameter(EditObjectAction.RETURNTO_PARAM);
			if (returnto != null) {
				return new ActionForward(returnto, true);
			} else {
				return actionMapping.findForward("view/projects");
			}
		} else {
			this.populateForm(storyForm, session);
			return new ActionForward(actionMapping.getInput());
		}
	}

	/**
     * Save form.
     *
     * @param storyForm
     *            the story form
     * @param session
     *            the session
     * @param request
     *            the request
     * @throws Exception
     *             the exception
     */
	private void saveForm(final MoveContinueStoryForm storyForm,
			final Session session, final HttpServletRequest request)
			throws Exception {
		final UserStory story = this.getStory(Integer.parseInt(storyForm
				.getOid()));
		this.populateObject(request, story, storyForm);
		final Iteration originalIteration = this.getIteration(story
				.getIteration().getId());
		final Iteration targetIteration = this.getIteration(storyForm
				.getTargetIterationId());

		if (storyForm.getAction().equals(MoveContinueStoryAction.MOVE_ACTION)) {
			this.moveContinueStory.moveStory(story, targetIteration,
					originalIteration, request, session);
		} else if (storyForm.getAction().equals(
				MoveContinueStoryAction.CONTINUE_ACTION)) {
			this.moveContinueStory.continueStory(story, originalIteration,
					targetIteration, request, session);
		} else {
			throw new ServletException("Unknown editor action: "
					+ storyForm.getAction());
		}
		this.moveContinueStory.reorderIterationStories(originalIteration);
		this.moveContinueStory.reorderIterationStories(targetIteration);
		storyForm.setAction(null);
	}

	/**
     * Gets the story.
     *
     * @param id
     *            the id
     * @return the story
     * @throws RepositoryException
     *             the repository exception
     */
	private UserStory getStory(final int id) throws RepositoryException {
		return (UserStory) this.getCommonDao().getById(UserStory.class, id);
	}

	/**
     * Populate form.
     *
     * @param storyForm
     *            the story form
     * @param session
     *            the session
     * @throws Exception
     *             the exception
     */
	private void populateForm(final MoveContinueStoryForm storyForm,
			final Session session) throws Exception {
		final String oid = storyForm.getOid();
		if (oid != null) {
			final UserStory story = (UserStory) session.load(UserStory.class,
					new Integer(oid));
			final Iteration iteration = story.getIteration();
			storyForm.setFutureIteration(iteration.isFuture());
			storyForm.setIterationId(iteration.getId());
			PropertyUtils.copyProperties(storyForm, story);
			this.populateManyToOneIds(storyForm, story);
		}
	}

	/**
     * Populate many to one ids.
     *
     * @param form
     *            the form
     * @param story
     *            the story
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws NoSuchMethodException
     *             the no such method exception
     * @throws InvocationTargetException
     *             the invocation target exception
     */
	private void populateManyToOneIds(final ActionForm form,
			final UserStory story) throws IllegalAccessException,
			NoSuchMethodException, InvocationTargetException {
		final Collection mappings = RelationshipMappingRegistry.getInstance()
				.getRelationshipMappings(story);
		for (final Iterator iterator = mappings.iterator(); iterator.hasNext();) {
			final RelationshipConvertor convertor = (RelationshipConvertor) iterator
					.next();
			convertor.populateAdapter(form, story);
		}
	}

}
