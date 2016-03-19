package net.sf.xplanner.dao;

import java.io.Serializable;
import java.util.List;

import net.sf.xplanner.domain.view.UserStoryView;

import com.technoetic.xplanner.domain.Identifiable;

/**
 * The Interface ViewDao.
 */
public interface ViewDao {
	
	/**
     * Gets the by id.
     *
     * @param objectClass
     *            the object class
     * @param id
     *            the id
     * @return the by id
     */
	Object getById(Class<? extends Identifiable> objectClass, Serializable id);

	/**
     * Gets the user stories.
     *
     * @param iterationId
     *            the iteration id
     * @return the user stories
     */
	public List<UserStoryView> getUserStories(Integer iterationId);
}
