package com.technoetic.xplanner.soap.domain;

import com.technoetic.xplanner.domain.Feature;

public class FeatureData extends DomainData {
	private int storyId;
	private String name;
	private String description;

	public void setStoryId(final int storyId) {
		this.storyId = storyId;
	}

	public int getStoryId() {
		return this.storyId;
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

	public static Class getInternalClass() {
		return Feature.class;
	}
}