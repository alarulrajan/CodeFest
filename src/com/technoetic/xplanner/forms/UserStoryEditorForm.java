package com.technoetic.xplanner.forms;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Person;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.XPlannerProperties;

public class UserStoryEditorForm extends AbstractEditorForm {
	private String name;
	private String description;
	private int trackerId;
	private int iterationId;
	private int targetIterationId;
	private double estimatedHoursField;
	private double taskBasedEstimatedHours;
	private int priority;
	private int orderNo;
	private String dispositionName;
	private String statusName;
	private int customerId;
	/* package */
	static final int DEFAULT_PRIORITY = 4;
	static final String DEFAULT_PRIORITY_KEY = "xplanner.story.defaultpriority";
	static final String INVALID_PRIORITY_ERROR_KEY = "story.editor.invalid_priority";
	static final String NEGATIVE_ESTIMATED_HOURS_ERROR_KEY = "story.editor.negative_estimated_hours";
	static final String MISSING_NAME_ERROR_KEY = "story.editor.missing_name";
	static final String SAME_ITERATION_ERROR_KEY = "story.editor.same_iteration";
	static final String PRIORITY_PARAM = "priority";

	public String getContainerId() {
		return Integer.toString(this.getIterationId());
	}

	@Override
	public ActionErrors validate(final ActionMapping mapping,
			final HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();
		if (this.isSubmitted()) {
			if (!this.isMerge()) {
				AbstractEditorForm.require(errors, this.name,
						UserStoryEditorForm.MISSING_NAME_ERROR_KEY);
				AbstractEditorForm.require(errors,
						this.estimatedHoursField >= 0,
						UserStoryEditorForm.NEGATIVE_ESTIMATED_HOURS_ERROR_KEY);
				this.validateIsNumber(errors, request,
						UserStoryEditorForm.PRIORITY_PARAM,
						UserStoryEditorForm.INVALID_PRIORITY_ERROR_KEY);
			} else {
				AbstractEditorForm.require(errors,
						this.targetIterationId != this.iterationId,
						UserStoryEditorForm.SAME_ITERATION_ERROR_KEY);
			}

		}
		return errors;
	}

	private void validateIsNumber(final ActionErrors errors,
			final HttpServletRequest request, final String param,
			final String errorKey) {
		final String priority = request.getParameter(param);
		if (priority == null) {
			return;
		}
		try {
			Integer.parseInt(priority);
		} catch (final NumberFormatException e) {
			AbstractEditorForm.error(errors, errorKey);
		}
	}

	@Override
	public void reset(final ActionMapping mapping,
			final HttpServletRequest request) {
		final XPlannerProperties props = new XPlannerProperties();
		this.reset(mapping, request, props);
	}

	public void reset(final ActionMapping mapping,
			final HttpServletRequest request, final XPlannerProperties props) {
		super.reset(mapping, request);
		this.name = null;
		this.description = null;
		this.trackerId = 0;
		this.iterationId = 0;
		this.estimatedHoursField = 0;
		this.taskBasedEstimatedHours = 0;
		this.targetIterationId = 0;
		this.customerId = 0;
		this.orderNo = 0;
		this.resetPriority(props);
	}

	public void resetPriority(final XPlannerProperties props) {
		if (props.getProperty(UserStoryEditorForm.DEFAULT_PRIORITY_KEY) != null) {
			this.priority = Integer.parseInt(props
					.getProperty(UserStoryEditorForm.DEFAULT_PRIORITY_KEY));
		} else {
			this.priority = UserStoryEditorForm.DEFAULT_PRIORITY;
		}
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

	public void setIterationId(final int iterationId) {
		if (this.targetIterationId == 0) {
			this.targetIterationId = iterationId;
		}
		this.iterationId = iterationId;
	}

	public int getIterationId() {
		return this.iterationId;
	}

	public void setTrackerId(final int trackerId) {
		this.trackerId = trackerId;
	}

	public int getTrackerId() {
		return this.trackerId;
	}

	public void setEstimatedHoursField(final double estimatedHours) {
		this.estimatedHoursField = estimatedHours;
	}

	public double getEstimatedHoursField() {
		return this.estimatedHoursField;
	}

	public void setPriority(final int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return this.priority;
	}

	public String getDispositionName() {
		return this.dispositionName;
	}

	public void setDispositionName(final String dispositionName) {
		this.dispositionName = dispositionName;
	}

	public String getStatusName() {
		return this.statusName;
	}

	public void setStatusName(final String statusName) {
		this.statusName = statusName;
	}

	public void setCustomerId(final int customerId) {
		this.customerId = customerId;
	}

	public int getCustomerId() {
		return this.customerId;
	}

	public double getTaskBasedEstimatedHours() {
		return this.taskBasedEstimatedHours;
	}

	public void setTaskBasedEstimatedHours(final double hours) {
		this.taskBasedEstimatedHours = hours;
	}

	public int getTargetIterationId() {
		return this.targetIterationId;
	}

	public void setTargetIterationId(final int targetIterationId) {
		this.targetIterationId = targetIterationId;
	}

	public int getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(final int orderNo) {
		this.orderNo = orderNo;
	}

	public void setCustomer(final Person customer) {
		if (customer != null) {
			this.setCustomerId(customer.getId());
		}
	}
}