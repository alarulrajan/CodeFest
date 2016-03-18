package com.technoetic.xplanner.tags;

import java.util.ArrayList;
import java.util.List;

import net.sf.xplanner.domain.UserStory;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.domain.repository.UserStoryRepository;
import com.technoetic.xplanner.security.AuthenticationException;

public class StoryOptionsTag extends OptionsTag {
	private int actualStoryId;

	public void setActualStoryId(final int actualStoryId) {
		this.actualStoryId = actualStoryId;
	}

	protected UserStoryRepository getUserStoryRepository() {
		return new UserStoryRepository(this.getSession(), this.getAuthorizer(),
				this.getLoggedInUserId());
	}

	@Override
	protected List getOptions() throws HibernateException,
			AuthenticationException {
		final UserStoryRepository userStoryRepository = this
				.getUserStoryRepository();
		final List stories = userStoryRepository
				.fetchStoriesWeCanMoveTasksTo(this.actualStoryId);
		final List options = new ArrayList();
		for (int i = 0; i < stories.size(); i++) {
			final UserStory s = (UserStory) stories.get(i);
			options.add(new StoryModel(new IterationModel(userStoryRepository
					.getIteration(s)), s));
		}
		return options;
	}
}
