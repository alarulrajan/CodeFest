package com.technoetic.xplanner.domain.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.util.TimeGenerator;

//DOORDIE Test THIS!

/**
 * The Class UserStoryRepository.
 */
public class UserStoryRepository {
    
    /** The session. */
    private final Session session;
    
    /** The person id. */
    private final int personId;
    
    /** The authorizer. */
    private final Authorizer authorizer;
    
    /** The iteration repository. */
    private final IterationRepository iterationRepository;
    
    /** The Constant USER_STORY_WE_CAN_MOVE_TASKS_TO_QUERY. */
    protected static final String USER_STORY_WE_CAN_MOVE_TASKS_TO_QUERY = "com.technoetic.xplanner.domain.StoriesOfCurrentAndFutureIterationOfAllVisibleProjects";

    /**
     * Instantiates a new user story repository.
     *
     * @param session
     *            the session
     * @param authorizer
     *            the authorizer
     * @param personId
     *            the person id
     */
    public UserStoryRepository(final Session session,
            final Authorizer authorizer, final int personId) {
        this.session = session;
        this.personId = personId;
        this.authorizer = authorizer;
        this.iterationRepository = new IterationRepository(session, authorizer,
                personId);
    }

    /**
     * Gets the iteration.
     *
     * @param story
     *            the story
     * @return the iteration
     * @throws HibernateException
     *             the hibernate exception
     */
    public Iteration getIteration(final UserStory story)
            throws HibernateException {
        return this.iterationRepository.getIterationForStory(story);
    }

    /**
     * Fetch stories we can move tasks to.
     *
     * @param actualStoryId
     *            the actual story id
     * @return the list
     * @throws HibernateException
     *             the hibernate exception
     * @throws AuthenticationException
     *             the authentication exception
     */
    public List fetchStoriesWeCanMoveTasksTo(final int actualStoryId)
            throws HibernateException, AuthenticationException {
        final TimeGenerator timeGenerator = new TimeGenerator();
        final Date currentDate = timeGenerator.getTodaysMidnight();
        final Query query = this.getSession().getNamedQuery(
                UserStoryRepository.USER_STORY_WE_CAN_MOVE_TASKS_TO_QUERY);
        query.setParameter("currentDate", currentDate);
        query.setParameter("actualStoryId", new Integer(actualStoryId));
        final List allStories = query.list();
        final List acceptedStories = new ArrayList();
        for (int i = 0; i < allStories.size(); i++) {
            final UserStory it = (UserStory) allStories.get(i);
            if (this.accept(it)) {
                acceptedStories.add(it);
            }
        }
        return acceptedStories;
    }

    /**
     * Gets the user story.
     *
     * @param storyId
     *            the story id
     * @return the user story
     * @throws HibernateException
     *             the hibernate exception
     */
    public UserStory getUserStory(final int storyId) throws HibernateException {
        return (UserStory) this.session.get(UserStory.class, new Integer(
                storyId));
    }

    /**
     * Accept.
     *
     * @param story
     *            the story
     * @return true, if successful
     * @throws AuthenticationException
     *             the authentication exception
     * @throws HibernateException
     *             the hibernate exception
     */
    private boolean accept(final UserStory story)
            throws AuthenticationException, HibernateException {
        return this.authorizer.hasPermission(this.getIteration(story)
                .getProject().getId(), this.personId, story, "edit");
    }

    /**
     * Gets the session.
     *
     * @return the session
     */
    protected Session getSession() {
        return this.session;
    }

}