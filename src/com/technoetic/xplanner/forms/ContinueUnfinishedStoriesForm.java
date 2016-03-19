package com.technoetic.xplanner.forms;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.actions.ContinueUnfinishedStoriesAction;

/**
 * The Class ContinueUnfinishedStoriesForm.
 */
public class ContinueUnfinishedStoriesForm extends AbstractEditorForm {
	
	/** The iteration id. */
	private int iterationId;
	
	/** The target iteration id. */
	private int targetIterationId;
	
	/** The project id. */
	private int projectId;
	
	/** The start date. */
	private Date startDate;
	
	/** The Constant SAME_ITERATION_ERROR_KEY. */
	private static final String SAME_ITERATION_ERROR_KEY = "iteration.status.editor.continue_in_same_iteration";

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
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

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.forms.AbstractEditorForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void reset(final ActionMapping mapping,
			final HttpServletRequest request) {
		final XPlannerProperties props = new XPlannerProperties();
		this.reset(mapping, request, props);
	}

	/**
     * Reset.
     *
     * @param mapping
     *            the mapping
     * @param request
     *            the request
     * @param props
     *            the props
     */
	public void reset(final ActionMapping mapping,
			final HttpServletRequest request, final XPlannerProperties props) {
		super.reset(mapping, request);
		this.iterationId = 0;
		this.targetIterationId = 0;
		this.projectId = 0;
	}

	/**
     * Gets the iteration id.
     *
     * @return the iteration id
     */
	public int getIterationId() {
		return this.iterationId;
	}

	/**
     * Sets the iteration id.
     *
     * @param iterationId
     *            the new iteration id
     */
	public void setIterationId(final int iterationId) {
		this.iterationId = iterationId;
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
     * Sets the project id.
     *
     * @param projectId
     *            the new project id
     */
	public void setProjectId(final int projectId) {
		this.projectId = projectId;
	}

	/**
     * Gets the project id.
     *
     * @return the project id
     */
	public int getProjectId() {
		return this.projectId;
	}

	/**
     * Gets the start date.
     *
     * @return the start date
     */
	public Date getStartDate() {
		return this.startDate;
	}

	/**
     * Sets the start date.
     *
     * @param startDate
     *            the new start date
     */
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}
}
