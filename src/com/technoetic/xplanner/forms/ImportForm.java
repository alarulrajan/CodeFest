package com.technoetic.xplanner.forms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.File;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * User: Mateusz Prokopowicz Date: Jun 7, 2005 Time: 12:02:31 PM.
 */
public class ImportForm extends AbstractEditorForm {
	
	/** The form file. */
	protected FormFile formFile;
	
	/** The action. */
	protected String action = null;
	
	/** The results. */
	protected List results = null;
	
	/** The file. */
	private File file;
	
	/** The Constant NO_IMPORT_FILE_KEY. */
	static final String NO_IMPORT_FILE_KEY = "import.status.no_import_file";

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public ActionErrors validate(final ActionMapping mapping,
			final HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();
		if (this.isSubmitted()) {
			if (this.formFile == null
					|| StringUtils.isEmpty(this.formFile.getFileName())) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						ImportForm.NO_IMPORT_FILE_KEY));
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
		this.results = new ArrayList();
		this.formFile = null;
		this.action = null;
	}

	/**
     * Sets the form file.
     *
     * @param formFile
     *            the new form file
     */
	public void setFormFile(final FormFile formFile) {
		this.formFile = formFile;
	}

	/**
     * Gets the form file.
     *
     * @return the form file
     */
	public FormFile getFormFile() {
		return this.formFile;
	}

	/**
     * Gets the attached file.
     *
     * @return the attached file
     */
	public File getAttachedFile() {
		return this.file;
	}

	/**
     * Sets the file.
     *
     * @param file
     *            the new file
     */
	public void setFile(final File file) {
		this.file = file;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.forms.AbstractEditorForm#isSubmitted()
	 */
	@Override
	public boolean isSubmitted() {
		return this.action != null && !this.action.equals("")
				&& !this.action.equals("null");
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.forms.AbstractEditorForm#getAction()
	 */
	@Override
	public String getAction() {
		return this.action;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.forms.AbstractEditorForm#setAction(java.lang.String)
	 */
	@Override
	public void setAction(final String action) {
		this.action = action;
	}

	/**
     * Gets the results.
     *
     * @return the results
     */
	public List getResults() {
		return this.results;
	}

	/**
     * Sets the results.
     *
     * @param results
     *            the new results
     */
	public void setResults(final List results) {
		this.results = results;
	}
}
