package com.technoetic.xplanner.domain;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.UserStory;

public class Feature extends DomainObject {
	private String name;
	private String description;
	private UserStory story;

	public UserStory getUserStory() {
		return this.story;
	}

	// TODO: add management of the inverse relationship tasks
	public void setStory(final UserStory story) {
		this.story = story;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}
