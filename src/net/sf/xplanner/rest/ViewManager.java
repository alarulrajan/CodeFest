package net.sf.xplanner.rest;

import java.util.List;

import net.sf.xplanner.dao.ViewDao;
import net.sf.xplanner.domain.view.ProjectView;
import net.sf.xplanner.domain.view.UserStoryView;

/**
 * The Class ViewManager.
 */
public class ViewManager {
	
	/** The view dao. */
	private ViewDao viewDao;

	/**
     * Gets the project.
     *
     * @param id
     *            the id
     * @return the project
     */
	public ProjectView getProject(final Integer id) {
		return (ProjectView) this.viewDao.getById(ProjectView.class, id);
	}

	/**
     * Sets the view dao.
     *
     * @param viewDao
     *            the new view dao
     */
	public void setViewDao(final ViewDao viewDao) {
		this.viewDao = viewDao;
	}

	/**
     * Gets the user stories.
     *
     * @param id
     *            the id
     * @return the user stories
     */
	public List<UserStoryView> getUserStories(final Integer id) {
		return this.viewDao.getUserStories(id);
	}

}
