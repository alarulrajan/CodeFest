package com.technoetic.xplanner.forms;

import javax.servlet.ServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * User: Mateusz Prokopowicz Date: Jan 10, 2005 Time: 3:05:49 PM.
 */
public class IterationStatusEditorForm extends ActionForm {
	
	/** The operation. */
	private String operation;
	
	/** The oid. */
	private String oid;
	
	/** The Constant SAVE_TIME_ATTR. */
	public static final String SAVE_TIME_ATTR = "saveTime";
	
	/** The close iterations. */
	private boolean closeIterations;
	
	/** The iteration start confirmed. */
	private boolean iterationStartConfirmed;

	/**
     * Gets the operation.
     *
     * @return the operation
     */
	public String getOperation() {
		return this.operation;
	}

	/**
     * Sets the operation.
     *
     * @param operation
     *            the new operation
     */
	public void setOperation(final String operation) {
		this.operation = operation;
	}

	/**
     * Gets the oid.
     *
     * @return the oid
     */
	public String getOid() {
		return this.oid;
	}

	/**
     * Sets the oid.
     *
     * @param oid
     *            the new oid
     */
	public void setOid(final String oid) {
		this.oid = oid;
	}

	/**
     * Checks if is close iterations.
     *
     * @return true, if is close iterations
     */
	public boolean isCloseIterations() {
		return this.closeIterations;
	}

	/**
     * Sets the close iterations.
     *
     * @param closeIterations
     *            the new close iterations
     */
	public void setCloseIterations(final boolean closeIterations) {
		this.closeIterations = closeIterations;
	}

	/**
     * Checks if is iteration start confirmed.
     *
     * @return true, if is iteration start confirmed
     */
	public boolean isIterationStartConfirmed() {
		return this.iterationStartConfirmed;
	}

	/**
     * Sets the iteration start confirmed.
     *
     * @param iterationStartConfirmed
     *            the new iteration start confirmed
     */
	public void setIterationStartConfirmed(final boolean iterationStartConfirmed) {
		this.iterationStartConfirmed = iterationStartConfirmed;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.ServletRequest)
	 */
	@Override
	public void reset(final ActionMapping mapping, final ServletRequest request) {
		super.reset(mapping, request);
		this.closeIterations = false;
		this.iterationStartConfirmed = false;
	}
}
