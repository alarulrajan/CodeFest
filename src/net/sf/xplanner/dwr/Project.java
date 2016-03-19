package net.sf.xplanner.dwr;

import java.util.List;

import net.sf.xplanner.dao.ProjectDao;
import net.sf.xplanner.dao.UserStoryDao;

/**
 * The Class Project.
 */
public class Project {
    
    /** The project dao. */
    private ProjectDao projectDao;
    
    /** The user story dao. */
    private UserStoryDao userStoryDao;

    /**
     * Sets the project dao.
     *
     * @param projectDao
     *            the new project dao
     */
    public void setProjectDao(final ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    /**
     * Gets the all projects.
     *
     * @return the all projects
     */
    public List<net.sf.xplanner.domain.Project> getAllProjects() {
        return this.projectDao.getAllProjects(null);
    }

    /**
     * Gets the all user stories.
     *
     * @param iterationId
     *            the iteration id
     * @return the all user stories
     */
    public List<net.sf.xplanner.domain.UserStory> getAllUserStories(
            final int iterationId) {
        return this.userStoryDao.getAllUserStrories(iterationId);
    }

}
