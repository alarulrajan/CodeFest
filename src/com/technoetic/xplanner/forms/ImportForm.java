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
 * User: Mateusz Prokopowicz Date: Jun 7, 2005 Time: 12:02:31 PM
 */
public class ImportForm extends AbstractEditorForm {
	protected FormFile formFile;
	protected String action = null;
	protected List results = null;
	private File file;
	static final String NO_IMPORT_FILE_KEY = "import.status.no_import_file";

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

	@Override
	public void reset(final ActionMapping mapping,
			final HttpServletRequest request) {
		super.reset(mapping, request);
		this.results = new ArrayList();
		this.formFile = null;
		this.action = null;
	}

	public void setFormFile(final FormFile formFile) {
		this.formFile = formFile;
	}

	public FormFile getFormFile() {
		return this.formFile;
	}

	public File getAttachedFile() {
		return this.file;
	}

	public void setFile(final File file) {
		this.file = file;
	}

	@Override
	public boolean isSubmitted() {
		return this.action != null && !this.action.equals("")
				&& !this.action.equals("null");
	}

	@Override
	public String getAction() {
		return this.action;
	}

	@Override
	public void setAction(final String action) {
		this.action = action;
	}

	public List getResults() {
		return this.results;
	}

	public void setResults(final List results) {
		this.results = results;
	}
}
