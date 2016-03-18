package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.forms.MoveStoriesForm;

public class MoveStoriesAction extends AbstractAction<UserStory> {
	public static final String MOVE_ACTION = "Move";

	private MoveContinueStory moveContinueStory;

	public void setMoveContinueStory(final MoveContinueStory moveContinueStory) {
		this.moveContinueStory = moveContinueStory;
	}

	public MoveContinueStory getMoveContinueStory() {
		return this.moveContinueStory;
	}

	@Override
	protected ActionForward doExecute(final ActionMapping actionMapping,
			final ActionForm actionForm, final HttpServletRequest request,
			final HttpServletResponse reply) throws Exception {

		final MoveStoriesForm moveStoriesForm = (MoveStoriesForm) actionForm;
		final int targetIterationId = moveStoriesForm.getTargetIterationId();

		if (targetIterationId > 0) {
			final Session session = this.getSession(request);
			final Iteration targetIteration = this
					.getIteration(targetIterationId);
			final Iteration iteration = this.getIteration(Integer
					.parseInt(moveStoriesForm.getIterationId()));

			if (targetIteration.getId() != iteration.getId()) {
				for (final String storyId : moveStoriesForm.getStoryIds()) {
					final UserStory story = this.getStory(Integer
							.parseInt(storyId));
					this.moveContinueStory.moveStory(story, targetIteration,
							iteration, request, session);
				}
				this.moveContinueStory.reorderIterationStories(iteration);
				this.moveContinueStory.reorderIterationStories(targetIteration);
			}

			final ActionForward actionForward = new ActionForward(
					"/do/view/iteration?oid=" + iteration.getId());
			actionForward.setRedirect(true);
			return actionForward;
		} else {
			return new ActionForward(actionMapping.getInput());
		}
	}

	private UserStory getStory(final int id) throws RepositoryException {
		return this.getCommonDao().getById(UserStory.class, id);
	}
}
