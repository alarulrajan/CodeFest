package com.technoetic.xplanner.tags;

import net.sf.xplanner.domain.UserStory;

//FIXME Test this

public class StoryModel {
	private final IterationModel iterationModel;
	private final UserStory story;

	public StoryModel(final IterationModel iterationModel, final UserStory story) {
		this.iterationModel = iterationModel;
		this.story = story;
	}

	public String getName() {
		return this.iterationModel.getName() + " :: " + this.story.getName();
	}

	public int getId() {
		return this.story.getId();
	}

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

	@Override
	public int hashCode() {
		return this.story.hashCode();
	}

	@Override
	public String toString() {
		return "StoryModel{" + "iterationModel=" + this.iterationModel
				+ ", story=" + this.story + "}";
	}

	public UserStory getUserStory() {
		return this.story;
	}

	public IterationModel getIterationModel() {
		return this.iterationModel;
	}

}
