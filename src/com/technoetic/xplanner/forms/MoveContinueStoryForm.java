package com.technoetic.xplanner.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.XPlannerProperties;

/**
 * The Class MoveContinueStoryForm.
 */
public class MoveContinueStoryForm extends AbstractEditorForm {
	
	/** The name. */
	private String name;
	
	/** The iteration id. */
	private int iterationId;
	
	/** The target iteration id. */
	private int targetIterationId;
	
	/** The customer id. */
	private int customerId;
	
	/** The is future iteration. */
	private boolean isFutureIteration;

	/** The Constant MISSING_NAME_ERROR_KEY. */
	static final String MISSING_NAME_ERROR_KEY = "story.editor.missing_name";
	
	/** The Constant SAME_ITERATION_ERROR_KEY. */
	static final String SAME_ITERATION_ERROR_KEY = "story.editor.same_iteration";

	/**
     * Gets the container id.
     *
     * @return the container id
     */
	public String getContainerId() {
		return Integer.toString(this.getIterationId());
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
						MoveContinueStoryForm.MISSING_NAME_ERROR_KEY);
			} else {
				AbstractEditorForm.require(errors,
						this.targetIterationId != this.iterationId,
						MoveContinueStoryForm.SAME_ITERATION_ERROR_KEY);
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
		this.name = null;
		this.iterationId = 0;
		this.targetIterationId = 0;
		this.customerId = 0;
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
     * Sets the iteration id.
     *
     * @param iterationId
     *            the new iteration id
     */
	public void setIterationId(final int iterationId) {
		if (this.targetIterationId == 0) {
			this.targetIterationId = iterationId;
		}
		this.iterationId = iterationId;
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
     * Gets the customer id.
     *
     * @return the customer id
     */
	public int getCustomerId() {
		return this.customerId;
	}

	/**
     * Sets the customer id.
     *
     * @param customerId
     *            the new customer id
     */
	public void setCustomerId(final int customerId) {
		this.customerId = customerId;
	}

	/**
     * Checks if is future iteration.
     *
     * @return true, if is future iteration
     */
	public boolean isFutureIteration() {
		return this.isFutureIteration;
	}

	/**
     * Sets the future iteration.
     *
     * @param futureIteration
     *            the new future iteration
     */
	public void setFutureIteration(final boolean futureIteration) {
		this.isFutureIteration = futureIteration;
	}
}