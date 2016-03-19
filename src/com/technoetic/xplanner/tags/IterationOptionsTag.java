package com.technoetic.xplanner.tags;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.security.AuthenticationException;

/**
 * The Class IterationOptionsTag.
 */
public class IterationOptionsTag extends OptionsTag {
	
	/** The project id. */
	private int projectId;
	
	/** The only current project. */
	private boolean onlyCurrentProject;
	
	/** The start date. */
	private Date startDate;
	
	/** The iteration loader. */
	private IterationLoader iterationLoader;

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.tags.OptionsTag#getOptions()
	 */
	@Override
	protected List getOptions() throws HibernateException,
			AuthenticationException {
		this.iterationLoader = new IterationLoader();
		this.iterationLoader.setPageContext(this.pageContext);

		return this.iterationLoader.getIterationOptions(this.projectId,
				this.onlyCurrentProject, this.startDate);
	}

	/**
     * Gets the project id.
     *
     * @return the project id
     */
	public int getProjectId() {
		return this.projectId;
	}

	/**
     * Sets the project id.
     *
     * @param projectId
     *            the new project id
     */
	public void setProjectId(final int projectId) {
		this.projectId = projectId;
	}

	/**
     * Checks if is only current project.
     *
     * @return true, if is only current project
     */
	public boolean isOnlyCurrentProject() {
		return this.onlyCurrentProject;
	}

	/**
     * Sets the only current project.
     *
     * @param onlyCurrentProject
     *            the new only current project
     */
	public void setOnlyCurrentProject(final boolean onlyCurrentProject) {
		this.onlyCurrentProject = onlyCurrentProject;
	}

	/**
     * Sets the start date.
     *
     * @param startDate
     *            the new start date
     */
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}
}
