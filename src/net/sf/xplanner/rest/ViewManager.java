package net.sf.xplanner.rest;

import java.util.List;

import net.sf.xplanner.dao.ViewDao;
import net.sf.xplanner.domain.view.ProjectView;
import net.sf.xplanner.domain.view.UserStoryView;

public class ViewManager {
	private ViewDao viewDao;

	public ProjectView getProject(final Integer id) {
		return (ProjectView) this.viewDao.getById(ProjectView.class, id);
	}

	public void setViewDao(final ViewDao viewDao) {
		this.viewDao = viewDao;
	}

	public List<UserStoryView> getUserStories(final Integer id) {
		return this.viewDao.getUserStories(id);
	}

}
