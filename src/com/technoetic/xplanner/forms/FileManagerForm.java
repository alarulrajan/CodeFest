package com.technoetic.xplanner.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class FileManagerForm extends ActionForm {
	private String action;
	private FormFile formFile;
	private String name;
	private String directoryId;
	private String fileId;

	public String getAction() {
		return this.action;
	}

	public void setAction(final String action) {
		this.action = action;
	}

	public FormFile getFormFile() {
		return this.formFile;
	}

	public void setFormFile(final FormFile formFile) {
		this.formFile = formFile;
	}

	public String getDirectoryId() {
		return this.directoryId;
	}

	public void setDirectoryId(final String directoryId) {
		this.directoryId = directoryId;
	}

	public String getFileId() {
		return this.fileId;
	}

	public void setFileId(final String fileId) {
		this.fileId = fileId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public void reset(final ActionMapping mapping,
			final HttpServletRequest request) {
		this.name = null;
		this.directoryId = null;
		this.fileId = null;
		this.action = null;
		this.formFile = null;
		super.reset(mapping, request);
	}
}
