package com.technoetic.xplanner.domain;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.UserStory;

/**
 * The Class Feature.
 */
public class Feature extends DomainObject {
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/** The story. */
	private UserStory story;

	/**
     * Gets the user story.
     *
     * @return the user story
     */
	public UserStory getUserStory() {
		return this.story;
	}

	/**
     * Sets the story.
     *
     * @param story
     *            the new story
     */
	// ChangeSoon: add management of the inverse relationship tasks
	public void setStory(final UserStory story) {
		this.story = story;
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
     * Gets the name.
     *
     * @return the name
     */
	public String getName() {
		return this.name;
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
}
