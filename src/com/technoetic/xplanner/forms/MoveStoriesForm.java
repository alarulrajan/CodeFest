package com.technoetic.xplanner.forms;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MoveStoriesForm.
 */
public class MoveStoriesForm extends AbstractEditorForm {
	
	/** The story ids. */
	private final List<String> storyIds = new ArrayList<String>();
	
	/** The target iteration id. */
	private int targetIterationId;
	
	/** The iteration id. */
	private String iterationId;

	/**
     * Gets the story ids.
     *
     * @return the story ids
     */
	public List<String> getStoryIds() {
		return this.storyIds;
	}

	/**
     * Sets the selected.
     *
     * @param index
     *            the index
     * @param storyId
     *            the story id
     */
	public void setSelected(final int index, final String storyId) {
		this.storyIds.add(storyId);
	}

	/**
     * Gets the target iteration id.
     *
     * @return the target iteration id
     */
	public int getTargetIterationId() {
		return this.targetIterationId;
	}

	/**
     * Sets the target iteration id.
     *
     * @param targetIterationId
     *            the new target iteration id
     */
	public void setTargetIterationId(final int targetIterationId) {
		this.targetIterationId = targetIterationId;
	}

	/**
     * Gets the iteration id.
     *
     * @return the iteration id
     */
	public String getIterationId() {
		return this.iterationId;
	}

	/**
     * Sets the iteration id.
     *
     * @param iterationId
     *            the new iteration id
     */
	public void setIterationId(final String iterationId) {
		this.iterationId = iterationId;
	}
}
