package com.technoetic.xplanner.forms;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.UserStory;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * The Class MoveContinueTaskForm.
 */
public class MoveContinueTaskForm extends AbstractEditorForm {
    
    /** The name. */
    private String name;
    
    /** The story id. */
    private int storyId;
    
    /** The target story id. */
    private int targetStoryId;
    
    /** The task id. */
    private int taskId;
    
    /** The user story. */
    private UserStory userStory = new UserStory();
    
    /** The Constant SAME_STORY_ERROR_KEY. */
    static final String SAME_STORY_ERROR_KEY = "task.editor.same_story";

    /**
     * Gets the container id.
     *
     * @return the container id
     */
    public String getContainerId() {
        return Integer.toString(this.getStoryId());
    }

    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
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

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.forms.AbstractEditorForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void reset(final ActionMapping mapping,
            final HttpServletRequest request) {
        super.reset(mapping, request);
        this.storyId = 0;
        this.targetStoryId = 0;
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
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the story id.
     *
     * @param storyId
     *            the new story id
     */
    public void setStoryId(final int storyId) {
        if (this.targetStoryId == 0) {
            this.targetStoryId = storyId;
        }
        this.storyId = storyId;
    }

    /**
     * Gets the story id.
     *
     * @return the story id
     */
    public int getStoryId() {
        return this.userStory.getId();
    }

    /**
     * Sets the story.
     *
     * @param story
     *            the new story
     */
    public void setStory(final UserStory story) {
        this.userStory = story;
    }

    /**
     * Gets the target story id.
     *
     * @return the target story id
     */
    public int getTargetStoryId() {
        return this.targetStoryId;
    }

    /**
     * Sets the target story id.
     *
     * @param targetStoryId
     *            the new target story id
     */
    public void setTargetStoryId(final int targetStoryId) {
        this.targetStoryId = targetStoryId;
    }

    /**
     * Gets the task id.
     *
     * @return the task id
     */
    public int getTaskId() {
        return this.taskId;
    }

    /**
     * Sets the task id.
     *
     * @param taskId
     *            the new task id
     */
    public void setTaskId(final int taskId) {
        this.taskId = taskId;
    }

    /**
     * Sets the user story.
     *
     * @param userStory
     *            the new user story
     */
    public void setUserStory(final UserStory userStory) {
        this.userStory = userStory;
    }

    /**
     * Gets the user story.
     *
     * @return the user story
     */
    public UserStory getUserStory() {
        return this.userStory;
    }
}
