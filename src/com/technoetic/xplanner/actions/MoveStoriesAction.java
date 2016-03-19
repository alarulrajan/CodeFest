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

/**
 * The Class MoveStoriesAction.
 */
public class MoveStoriesAction extends AbstractAction<UserStory> {
    
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

    /**
     * Gets the move continue story.
     *
     * @return the move continue story
     */
    public MoveContinueStory getMoveContinueStory() {
        return this.moveContinueStory;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.actions.AbstractAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
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
        return this.getCommonDao().getById(UserStory.class, id);
    }
}
