package com.technoetic.xplanner.tags;

import java.util.ArrayList;
import java.util.List;

import net.sf.xplanner.domain.UserStory;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.domain.repository.UserStoryRepository;
import com.technoetic.xplanner.security.AuthenticationException;

/**
 * The Class StoryOptionsTag.
 */
public class StoryOptionsTag extends OptionsTag {
	
	/** The actual story id. */
	private int actualStoryId;

	/**
     * Sets the actual story id.
     *
     * @param actualStoryId
     *            the new actual story id
     */
	public void setActualStoryId(final int actualStoryId) {
		this.actualStoryId = actualStoryId;
	}

	/**
     * Gets the user story repository.
     *
     * @return the user story repository
     */
	protected UserStoryRepository getUserStoryRepository() {
		return new UserStoryRepository(this.getSession(), this.getAuthorizer(),
				this.getLoggedInUserId());
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.tags.OptionsTag#getOptions()
	 */
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
