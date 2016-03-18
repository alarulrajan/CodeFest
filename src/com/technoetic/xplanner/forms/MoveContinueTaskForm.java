package com.technoetic.xplanner.forms;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.UserStory;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class MoveContinueTaskForm extends AbstractEditorForm {
	private String name;
	private int storyId;
	private int targetStoryId;
	private int taskId;
	private UserStory userStory = new UserStory();
	static final String SAME_STORY_ERROR_KEY = "task.editor.same_story";

	public String getContainerId() {
		return Integer.toString(this.getStoryId());
	}

	@Override
	public ActionErrors validate(final ActionMapping mapping,
			final HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();

		if (this.isSubmitted()) {
			if (!this.isMerge()) {
				AbstractEditorForm.require(errors, this.name,
						"task.editor.missing_name");
			} else {
				AbstractEditorForm.require(errors,
						this.targetStoryId != this.storyId,
						MoveContinueTaskForm.SAME_STORY_ERROR_KEY);
			}
		}
		return errors;
	}

	@Override
	public void reset(final ActionMapping mapping,
			final HttpServletRequest request) {
		super.reset(mapping, request);
		this.storyId = 0;
		this.targetStoryId = 0;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setStoryId(final int storyId) {
		if (this.targetStoryId == 0) {
			this.targetStoryId = storyId;
		}
		this.storyId = storyId;
	}

	public int getStoryId() {
		return this.userStory.getId();
	}

	public void setStory(final UserStory story) {
		this.userStory = story;
	}

	public int getTargetStoryId() {
		return this.targetStoryId;
	}

	public void setTargetStoryId(final int targetStoryId) {
		this.targetStoryId = targetStoryId;
	}

	public int getTaskId() {
		return this.taskId;
	}

	public void setTaskId(final int taskId) {
		this.taskId = taskId;
	}

	public void setUserStory(final UserStory userStory) {
		this.userStory = userStory;
	}

	public UserStory getUserStory() {
		return this.userStory;
	}
}
