package com.technoetic.xplanner.tags;

import net.sf.xplanner.domain.UserStory;

//FIXME Test this

/**
 * The Class StoryModel.
 */
public class StoryModel {
	
	/** The iteration model. */
	private final IterationModel iterationModel;
	
	/** The story. */
	private final UserStory story;

	/**
     * Instantiates a new story model.
     *
     * @param iterationModel
     *            the iteration model
     * @param story
     *            the story
     */
	public StoryModel(final IterationModel iterationModel, final UserStory story) {
		this.iterationModel = iterationModel;
		this.story = story;
	}

	/**
     * Gets the name.
     *
     * @return the name
     */
	public String getName() {
		return this.iterationModel.getName() + " :: " + this.story.getName();
	}

	/**
     * Gets the id.
     *
     * @return the id
     */
	public int getId() {
		return this.story.getId();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof StoryModel)) {
			return false;
		}

		final StoryModel storyModel = (StoryModel) o;

		if (!this.iterationModel.equals(storyModel.iterationModel)) {
			return false;
		}
		if (!this.story.equals(storyModel.story)) {
			return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.story.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StoryModel{" + "iterationModel=" + this.iterationModel
				+ ", story=" + this.story + "}";
	}

	/**
     * Gets the user story.
     *
     * @return the user story
     */
	public UserStory getUserStory() {
		return this.story;
	}

	/**
     * Gets the iteration model.
     *
     * @return the iteration model
     */
	public IterationModel getIterationModel() {
		return this.iterationModel;
	}

}
