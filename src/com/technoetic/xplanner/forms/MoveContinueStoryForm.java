package com.technoetic.xplanner.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.XPlannerProperties;

public class MoveContinueStoryForm extends AbstractEditorForm {
	private String name;
	private int iterationId;
	private int targetIterationId;
	private int customerId;
	private boolean isFutureIteration;

	static final String MISSING_NAME_ERROR_KEY = "story.editor.missing_name";
	static final String SAME_ITERATION_ERROR_KEY = "story.editor.same_iteration";

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
						MoveContinueStoryForm.MISSING_NAME_ERROR_KEY);
			} else {
				AbstractEditorForm.require(errors,
						this.targetIterationId != this.iterationId,
						MoveContinueStoryForm.SAME_ITERATION_ERROR_KEY);
			}

		}
		return errors;
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
		this.iterationId = 0;
		this.targetIterationId = 0;
		this.customerId = 0;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
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

	public int getTargetIterationId() {
		return this.targetIterationId;
	}

	public void setTargetIterationId(final int targetIterationId) {
		this.targetIterationId = targetIterationId;
	}

	public int getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(final int customerId) {
		this.customerId = customerId;
	}

	public boolean isFutureIteration() {
		return this.isFutureIteration;
	}

	public void setFutureIteration(final boolean futureIteration) {
		this.isFutureIteration = futureIteration;
	}
}