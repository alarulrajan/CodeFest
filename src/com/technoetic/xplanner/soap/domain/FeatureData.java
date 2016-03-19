package com.technoetic.xplanner.soap.domain;

import com.technoetic.xplanner.domain.Feature;

/**
 * The Class FeatureData.
 */
public class FeatureData extends DomainData {
	
	/** The story id. */
	private int storyId;
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;

	/**
     * Sets the story id.
     *
     * @param storyId
     *            the new story id
     */
	public void setStoryId(final int storyId) {
		this.storyId = storyId;
	}

	/**
     * Gets the story id.
     *
     * @return the story id
     */
	public int getStoryId() {
		return this.storyId;
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

	/**
     * Gets the internal class.
     *
     * @return the internal class
     */
	public static Class getInternalClass() {
		return Feature.class;
	}
}