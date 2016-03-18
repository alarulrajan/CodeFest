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

public class UserStoryRepository {
	private final Session session;
	private final int personId;
	private final Authorizer authorizer;
	private final IterationRepository iterationRepository;
	protected static final String USER_STORY_WE_CAN_MOVE_TASKS_TO_QUERY = "com.technoetic.xplanner.domain.StoriesOfCurrentAndFutureIterationOfAllVisibleProjects";

	public UserStoryRepository(final Session session,
			final Authorizer authorizer, final int personId) {
		this.session = session;
		this.personId = personId;
		this.authorizer = authorizer;
		this.iterationRepository = new IterationRepository(session, authorizer,
				personId);
	}

	public Iteration getIteration(final UserStory story)
			throws HibernateException {
		return this.iterationRepository.getIterationForStory(story);
	}

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

	public UserStory getUserStory(final int storyId) throws HibernateException {
		return (UserStory) this.session.get(UserStory.class, new Integer(
				storyId));
	}

	private boolean accept(final UserStory story)
			throws AuthenticationException, HibernateException {
		return this.authorizer.hasPermission(this.getIteration(story)
				.getProject().getId(), this.personId, story, "edit");
	}

	protected Session getSession() {
		return this.session;
	}

}