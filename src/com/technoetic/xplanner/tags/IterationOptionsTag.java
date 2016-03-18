package com.technoetic.xplanner.tags;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.security.AuthenticationException;

public class IterationOptionsTag extends OptionsTag {
	private int projectId;
	private boolean onlyCurrentProject;
	private Date startDate;
	private IterationLoader iterationLoader;

	@Override
	protected List getOptions() throws HibernateException,
			AuthenticationException {
		this.iterationLoader = new IterationLoader();
		this.iterationLoader.setPageContext(this.pageContext);

		return this.iterationLoader.getIterationOptions(this.projectId,
				this.onlyCurrentProject, this.startDate);
	}

	public int getProjectId() {
		return this.projectId;
	}

	public void setProjectId(final int projectId) {
		this.projectId = projectId;
	}

	public boolean isOnlyCurrentProject() {
		return this.onlyCurrentProject;
	}

	public void setOnlyCurrentProject(final boolean onlyCurrentProject) {
		this.onlyCurrentProject = onlyCurrentProject;
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}
}
