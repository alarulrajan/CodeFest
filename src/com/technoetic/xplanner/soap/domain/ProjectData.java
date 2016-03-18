package com.technoetic.xplanner.soap.domain;

import net.sf.xplanner.domain.Project;

public class ProjectData extends DomainData {
	private String name;
	private String description;

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static Class getInternalClass() {
		return Project.class;
	}
}