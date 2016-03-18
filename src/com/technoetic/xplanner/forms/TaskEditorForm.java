package com.technoetic.xplanner.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.UserStory;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class TaskEditorForm extends AbstractEditorForm {
	private static SimpleDateFormat dateConverter;
	private String name;
	private String description;
	private Date createdDate;
	private String createdDateString;
	private int userStoryId;
	private int targetStoryId;
	private int acceptorId;
	private double estimatedHours;
	private double actualHours;
	private String type;
	private String dispositionName;
	private boolean completed;

	public String getContainerId() {
		return Integer.toString(this.getUserStoryId());
	}

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

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setUserStoryId(final int storyId) {
		if (this.targetStoryId == 0) {
			this.targetStoryId = storyId;
		}
		this.userStoryId = storyId;
	}

	public int getUserStoryId() {
		return this.userStoryId;
	}

	public void setUserStory(final UserStory story) {
		this.userStoryId = story == null ? 0 : story.getId();
	}

	public void setEstimatedHours(final double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	public double getEstimatedHours() {
		return this.estimatedHours;
	}

	public void setActualHours(final double actualHours) {
		this.actualHours = actualHours;
	}

	public double getActualHours() {
		return this.actualHours;
	}

	public void setAcceptorId(final int acceptorId) {
		this.acceptorId = acceptorId;
	}

	public int getAcceptorId() {
		return this.acceptorId;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setDispositionName(final String dispositionName) {
		this.dispositionName = dispositionName;
	}

	public String getDispositionName() {
		return this.dispositionName;
	}

	public void setCompleted(final boolean flag) {
		this.completed = flag;
	}

	public boolean isCompleted() {
		return this.completed;
	}

	public void setCreatedDateString(final String createdDateString) {
		this.createdDateString = createdDateString;
	}

	public String getCreatedDateString() {
		return this.createdDateString;
	}

	public Date getCreatedDate() {
		if (this.createdDate == null) {
			this.setCreatedDate(new Date());
		}

		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		if (createdDate == null) {
			createdDate = new Date();
		}

		this.createdDate = createdDate;
		this.createdDateString = TaskEditorForm.dateConverter
				.format(createdDate);
	}

	public int getTargetStoryId() {
		return this.targetStoryId;
	}

	public void setTargetStoryId(final int targetStoryId) {
		this.targetStoryId = targetStoryId;
	}

}
