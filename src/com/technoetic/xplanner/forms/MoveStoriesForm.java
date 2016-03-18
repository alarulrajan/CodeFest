package com.technoetic.xplanner.forms;

import java.util.ArrayList;
import java.util.List;

public class MoveStoriesForm extends AbstractEditorForm {
	private final List<String> storyIds = new ArrayList<String>();
	private int targetIterationId;
	private String iterationId;

	public List<String> getStoryIds() {
		return this.storyIds;
	}

	public void setSelected(final int index, final String storyId) {
		this.storyIds.add(storyId);
	}

	public int getTargetIterationId() {
		return this.targetIterationId;
	}

	public void setTargetIterationId(final int targetIterationId) {
		this.targetIterationId = targetIterationId;
	}

	public String getIterationId() {
		return this.iterationId;
	}

	public void setIterationId(final String iterationId) {
		this.iterationId = iterationId;
	}
}
