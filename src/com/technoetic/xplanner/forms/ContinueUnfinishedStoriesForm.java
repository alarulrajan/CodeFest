package com.technoetic.xplanner.forms;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.actions.ContinueUnfinishedStoriesAction;

public class ContinueUnfinishedStoriesForm extends AbstractEditorForm {
	private int iterationId;
	private int targetIterationId;
	private int projectId;
	private Date startDate;
	private static final String SAME_ITERATION_ERROR_KEY = "iteration.status.editor.continue_in_same_iteration";

	@Override
	public ActionErrors validate(final ActionMapping mapping,
			final HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();
		if (this.isSubmitted()
				&& this.getAction().equals(
						ContinueUnfinishedStoriesAction.OK_ACTION)) {
			AbstractEditorForm.require(errors,
					this.targetIterationId != this.iterationId,
					ContinueUnfinishedStoriesForm.SAME_ITERATION_ERROR_KEY);
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
		this.iterationId = 0;
		this.targetIterationId = 0;
		this.projectId = 0;
	}

	public int getIterationId() {
		return this.iterationId;
	}

	public void setIterationId(final int iterationId) {
		this.iterationId = iterationId;
	}

	public int getTargetIterationId() {
		return this.targetIterationId;
	}

	public void setTargetIterationId(final int targetIterationId) {
		this.targetIterationId = targetIterationId;
	}

	public void setProjectId(final int projectId) {
		this.projectId = projectId;
	}

	public int getProjectId() {
		return this.projectId;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}
}
