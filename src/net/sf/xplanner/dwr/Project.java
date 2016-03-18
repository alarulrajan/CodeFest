package net.sf.xplanner.dwr;

import java.util.List;

import net.sf.xplanner.dao.ProjectDao;
import net.sf.xplanner.dao.UserStoryDao;

public class Project {
	private ProjectDao projectDao;
	private UserStoryDao userStoryDao;

	public void setProjectDao(final ProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	public List<net.sf.xplanner.domain.Project> getAllProjects() {
		return this.projectDao.getAllProjects(null);
	}

	public List<net.sf.xplanner.domain.UserStory> getAllUserStories(
			final int iterationId) {
		return this.userStoryDao.getAllUserStrories(iterationId);
	}

}
