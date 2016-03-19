package com.technoetic.xplanner.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.UserStory;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * The Class TaskEditorForm.
 */
public class TaskEditorForm extends AbstractEditorForm {
	
	/** The date converter. */
	private static SimpleDateFormat dateConverter;
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/** The created date. */
	private Date createdDate;
	
	/** The created date string. */
	private String createdDateString;
	
	/** The user story id. */
	private int userStoryId;
	
	/** The target story id. */
	private int targetStoryId;
	
	/** The acceptor id. */
	private int acceptorId;
	
	/** The estimated hours. */
	private double estimatedHours;
	
	/** The actual hours. */
	private double actualHours;
	
	/** The type. */
	private String type;
	
	/** The disposition name. */
	private String dispositionName;
	
	/** The completed. */
	private boolean completed;

	/**
     * Gets the container id.
     *
     * @return the container id
     */
	public String getContainerId() {
		return Integer.toString(this.getUserStoryId());
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public ActionErrors validate(final ActionMapping mapping,
			final HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();

		if (TaskEditorForm.dateConverter == null) {
			final String format = AbstractEditorForm.getResources(request)
					.getMessage("format.date");
			TaskEditorForm.dateConverter = new SimpleDateFormat(format);
		}

		// Set created date to be now
		// if (createdDate == null) {
		// setCreatedDate(new Date());
		// }

		if (this.isSubmitted()) {
			if (this.createdDateString != null) {
				this.createdDate = null;
				try {
					this.createdDate = TaskEditorForm.dateConverter
							.parse(this.createdDateString);
				} catch (final ParseException ex) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"task.editor.bad_created_date"));
				}
			}

			if (!this.isMerge()) {
				AbstractEditorForm.require(errors, this.name,
						"task.editor.missing_name");
				AbstractEditorForm.require(errors, this.estimatedHours >= 0.0,
						"task.editor.negative_estimated_hours");
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
		this.name = null;
		this.description = null;
		this.userStoryId = 0;
		this.targetStoryId = 0;
		this.completed = false;
		this.acceptorId = 0;
		this.estimatedHours = 0;
		this.actualHours = 0;
		this.type = null;
		this.dispositionName = null;
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
     * Sets the user story id.
     *
     * @param storyId
     *            the new user story id
     */
	public void setUserStoryId(final int storyId) {
		if (this.targetStoryId == 0) {
			this.targetStoryId = storyId;
		}
		this.userStoryId = storyId;
	}

	/**
     * Gets the user story id.
     *
     * @return the user story id
     */
	public int getUserStoryId() {
		return this.userStoryId;
	}

	/**
     * Sets the user story.
     *
     * @param story
     *            the new user story
     */
	public void setUserStory(final UserStory story) {
		this.userStoryId = story == null ? 0 : story.getId();
	}

	/**
     * Sets the estimated hours.
     *
     * @param estimatedHours
     *            the new estimated hours
     */
	public void setEstimatedHours(final double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	/**
     * Gets the estimated hours.
     *
     * @return the estimated hours
     */
	public double getEstimatedHours() {
		return this.estimatedHours;
	}

	/**
     * Sets the actual hours.
     *
     * @param actualHours
     *            the new actual hours
     */
	public void setActualHours(final double actualHours) {
		this.actualHours = actualHours;
	}

	/**
     * Gets the actual hours.
     *
     * @return the actual hours
     */
	public double getActualHours() {
		return this.actualHours;
	}

	/**
     * Sets the acceptor id.
     *
     * @param acceptorId
     *            the new acceptor id
     */
	public void setAcceptorId(final int acceptorId) {
		this.acceptorId = acceptorId;
	}

	/**
     * Gets the acceptor id.
     *
     * @return the acceptor id
     */
	public int getAcceptorId() {
		return this.acceptorId;
	}

	/**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
	public void setType(final String type) {
		this.type = type;
	}

	/**
     * Gets the type.
     *
     * @return the type
     */
	public String getType() {
		return this.type;
	}

	/**
     * Sets the disposition name.
     *
     * @param dispositionName
     *            the new disposition name
     */
	public void setDispositionName(final String dispositionName) {
		this.dispositionName = dispositionName;
	}

	/**
     * Gets the disposition name.
     *
     * @return the disposition name
     */
	public String getDispositionName() {
		return this.dispositionName;
	}

	/**
     * Sets the completed.
     *
     * @param flag
     *            the new completed
     */
	public void setCompleted(final boolean flag) {
		this.completed = flag;
	}

	/**
     * Checks if is completed.
     *
     * @return true, if is completed
     */
	public boolean isCompleted() {
		return this.completed;
	}

	/**
     * Sets the created date string.
     *
     * @param createdDateString
     *            the new created date string
     */
	public void setCreatedDateString(final String createdDateString) {
		this.createdDateString = createdDateString;
	}

	/**
     * Gets the created date string.
     *
     * @return the created date string
     */
	public String getCreatedDateString() {
		return this.createdDateString;
	}

	/**
     * Gets the created date.
     *
     * @return the created date
     */
	public Date getCreatedDate() {
		if (this.createdDate == null) {
			this.setCreatedDate(new Date());
		}

		return this.createdDate;
	}

	/**
     * Sets the created date.
     *
     * @param createdDate
     *            the new created date
     */
	public void setCreatedDate(Date createdDate) {
		if (createdDate == null) {
			createdDate = new Date();
		}

		this.createdDate = createdDate;
		this.createdDateString = TaskEditorForm.dateConverter
				.format(createdDate);
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

}
