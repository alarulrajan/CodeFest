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

/**
 * The Class ContinueUnfinishedStoriesAction.
 */
public class ContinueUnfinishedStoriesAction extends
        EditObjectAction<UserStory> {
    
    /** The Constant OK_ACTION. */
    public static final String OK_ACTION = "Ok";
    
    /** The Constant CANCEL_ACTION. */
    public static final String CANCEL_ACTION = "Cancel";
    
    /** The story continuer. */
    private StoryContinuer storyContinuer;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.actions.EditObjectAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
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

    /**
     * Populate form.
     *
     * @param continueUnfinishedStoriesForm
     *            the continue unfinished stories form
     * @param session
     *            the session
     * @throws HibernateException
     *             the hibernate exception
     * @throws NoSuchMethodException
     *             the no such method exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     */
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

    /**
     * Save form.
     *
     * @param selectionForm
     *            the selection form
     * @param mapping
     *            the mapping
     * @param session
     *            the session
     * @param request
     *            the request
     * @param form
     *            the form
     * @param response
     *            the response
     * @throws Exception
     *             the exception
     */
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

    /**
     * Continue iteration.
     *
     * @param currentIterationId
     *            the current iteration id
     * @param targetIterationId
     *            the target iteration id
     * @param request
     *            the request
     * @param session
     *            the session
     * @throws Exception
     *             the exception
     */
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

    /**
     * Continue unfinished stories in iteration.
     *
     * @param request
     *            the request
     * @param session
     *            the session
     * @param originalIteration
     *            the original iteration
     * @param targetIteration
     *            the target iteration
     * @throws Exception
     *             the exception
     */
    protected void continueUnfinishedStoriesInIteration(
            final HttpServletRequest request, final Session session,
            final Iteration originalIteration, final Iteration targetIteration)
            throws Exception {
        this.storyContinuer.init(session, request);
        if (originalIteration != null) {
            this.continueIteration(originalIteration, targetIteration);
        }
    }

    /**
     * Continue iteration.
     *
     * @param originalIteration
     *            the original iteration
     * @param targetIteration
     *            the target iteration
     * @throws HibernateException
     *             the hibernate exception
     */
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

    /**
     * Continue complete story.
     *
     * @param userStory
     *            the user story
     * @param originalIteration
     *            the original iteration
     * @param targetIteration
     *            the target iteration
     * @throws HibernateException
     *             the hibernate exception
     */
    private void continueCompleteStory(final UserStory userStory,
            final Iteration originalIteration, final Iteration targetIteration)
            throws HibernateException {
        if (!userStory.isCompleted()) {
            this.continueStory(userStory, originalIteration, targetIteration);
        }
    }

    /**
     * Continue story.
     *
     * @param userStory
     *            the user story
     * @param originalIteration
     *            the original iteration
     * @param targetIteration
     *            the target iteration
     * @throws HibernateException
     *             the hibernate exception
     */
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

    /**
     * Sets the story continuer.
     *
     * @param storyContinuer
     *            the new story continuer
     */
    public void setStoryContinuer(final StoryContinuer storyContinuer) {
        this.storyContinuer = storyContinuer;
    }
}
