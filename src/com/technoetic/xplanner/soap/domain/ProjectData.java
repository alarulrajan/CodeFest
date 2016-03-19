package com.technoetic.xplanner.soap.domain;

import net.sf.xplanner.domain.Project;

/**
 * The Class ProjectData.
 */
public class ProjectData extends DomainData {
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;

	/**
     * Gets the name.
     *
     * @return the name
     */
	public String getName() {
		return this.name;
	}

	/**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
	public void setName(final String name) {
		this.name = name;
	}

	/**
     * Sets the description.
     *
     * @param description
     *            the new description
     */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
     * Gets the description.
     *
     * @return the description
     */
	public String getDescription() {
		return this.description;
	}

	/**
     * Gets the internal class.
     *
     * @return the internal class
     */
	public static Class getInternalClass() {
		return Project.class;
	}
}