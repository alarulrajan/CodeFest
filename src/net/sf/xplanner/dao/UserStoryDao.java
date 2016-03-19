package net.sf.xplanner.dao;

import java.util.List;

import net.sf.xplanner.domain.UserStory;

/**
 * The Interface UserStoryDao.
 */
public interface UserStoryDao extends Dao<UserStory> {
	
	/**
     * Gets the all user strories.
     *
     * @param iterationId
     *            the iteration id
     * @return the all user strories
     */
	List<UserStory> getAllUserStrories(int iterationId);

	/**
     * Gets the stories for person where customer.
     *
     * @param personId
     *            the person id
     * @return the stories for person where customer
     */
	List<UserStory> getStoriesForPersonWhereCustomer(int personId);

	/**
     * Gets the stories for person where tracker.
     *
     * @param personId
     *            the person id
     * @return the stories for person where tracker
     */
	List<UserStory> getStoriesForPersonWhereTracker(int personId);
}
