package com.technoetic.xplanner.forms;

import javax.servlet.ServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * User: Mateusz Prokopowicz Date: Jan 10, 2005 Time: 3:05:49 PM
 */
public class IterationStatusEditorForm extends ActionForm {
	private String operation;
	private String oid;
	public static final String SAVE_TIME_ATTR = "saveTime";
	private boolean closeIterations;
	private boolean iterationStartConfirmed;

	public String getOperation() {
		return this.operation;
	}

	public void setOperation(final String operation) {
		this.operation = operation;
	}

	public String getOid() {
		return this.oid;
	}

	public void setOid(final String oid) {
		this.oid = oid;
	}

	public boolean isCloseIterations() {
		return this.closeIterations;
	}

	public void setCloseIterations(final boolean closeIterations) {
		this.closeIterations = closeIterations;
	}

	public boolean isIterationStartConfirmed() {
		return this.iterationStartConfirmed;
	}

	public void setIterationStartConfirmed(final boolean iterationStartConfirmed) {
		this.iterationStartConfirmed = iterationStartConfirmed;
	}

	@Override
	public void reset(final ActionMapping mapping, final ServletRequest request) {
		super.reset(mapping, request);
		this.closeIterations = false;
		this.iterationStartConfirmed = false;
	}
}
