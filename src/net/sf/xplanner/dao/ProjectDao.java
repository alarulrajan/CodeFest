package net.sf.xplanner.dao;

import java.util.List;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;

/**
 * The Interface ProjectDao.
 */
public interface ProjectDao extends Dao<Project> {
	
	/**
     * Gets the all projects.
     *
     * @param user
     *            the user
     * @return the all projects
     */
	List<Project> getAllProjects(Person user);
}
