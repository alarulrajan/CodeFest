package com.technoetic.xplanner.actions;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.history.HistorySupport;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;

public class MoveContinueStory {
	private StoryContinuer storyContinuer;
	private HistorySupport historySupport;

	public void setStoryContinuer(final StoryContinuer storyContinuer) {
		this.storyContinuer = storyContinuer;
	}

	public void setHistorySupport(final HistorySupport historySupport) {
		this.historySupport = historySupport;
	}

	public void moveStory(final UserStory story,
			final Iteration targetIteration, final Iteration originalIteration,
			final HttpServletRequest request, final Session session)
			throws AuthenticationException {

		originalIteration.getUserStories().remove(story);
		story.moveTo(targetIteration);
		this.updateStoryOrderNoInTargetIteration(story, originalIteration,
				targetIteration);
		this.saveMoveHistory(story, originalIteration, targetIteration,
				session, SecurityHelper.getRemoteUserId(request));
	}

	public void continueStory(final UserStory story,
			final Iteration originalIteration, final Iteration targetIteration,
			final HttpServletRequest request, final Session session)
			throws AuthenticationException, HibernateException {

		this.storyContinuer.init(session, request);
		final UserStory targetStory = (UserStory) this.storyContinuer
				.continueObject(story, originalIteration, targetIteration);
		this.updateStoryOrderNoInTargetIteration(targetStory,
				originalIteration, targetIteration);
	}

	public void reorderIterationStories(final Iteration iteration)
			throws Exception {
		final Collection stories = iteration.getUserStories();
		iteration.modifyStoryOrder(DomainOrderer
				.buildStoryIdNewOrderArray(stories));
	}

	private void updateStoryOrderNoInTargetIteration(final UserStory story,
			final Iteration originalIteration, final Iteration targetIteration) {

		if (originalIteration.getStartDate().compareTo(
				targetIteration.getStartDate()) <= 0) {
			story.setOrderNo(0);
		} else {
			story.setOrderNo(Integer.MAX_VALUE);
		}
	}

	private void saveMoveHistory(final UserStory story,
			final Iteration originIteration, final Iteration targetIteration,
			final Session session, final int currentUserId) {

		final Date now = new Date();
		this.historySupport.saveEvent(
				story,
				History.MOVED,
				"from " + originIteration.getName() + " to "
						+ targetIteration.getName(), currentUserId, now);
		this.historySupport.saveEvent(originIteration, History.MOVED_OUT,
				story.getName() + " to " + targetIteration.getName(),
				currentUserId, now);
		this.historySupport.saveEvent(targetIteration, History.MOVED_IN,
				story.getName() + " from " + originIteration.getName(),
				currentUserId, now);
	}
}
