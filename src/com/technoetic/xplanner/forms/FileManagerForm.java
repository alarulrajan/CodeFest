package com.technoetic.xplanner.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * The Class FileManagerForm.
 */
public class FileManagerForm extends ActionForm {
	
	/** The action. */
	private String action;
	
	/** The form file. */
	private FormFile formFile;
	
	/** The name. */
	private String name;
	
	/** The directory id. */
	private String directoryId;
	
	/** The file id. */
	private String fileId;

	/**
     * Gets the action.
     *
     * @return the action
     */
	public String getAction() {
		return this.action;
	}

	/**
     * Sets the action.
     *
     * @param action
     *            the new action
     */
	public void setAction(final String action) {
		this.action = action;
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
     * Sets the form file.
     *
     * @param formFile
     *            the new form file
     */
	public void setFormFile(final FormFile formFile) {
		this.formFile = formFile;
	}

	/**
     * Gets the directory id.
     *
     * @return the directory id
     */
	public String getDirectoryId() {
		return this.directoryId;
	}

	/**
     * Sets the directory id.
     *
     * @param directoryId
     *            the new directory id
     */
	public void setDirectoryId(final String directoryId) {
		this.directoryId = directoryId;
	}

	/**
     * Gets the file id.
     *
     * @return the file id
     */
	public String getFileId() {
		return this.fileId;
	}

	/**
     * Sets the file id.
     *
     * @param fileId
     *            the new file id
     */
	public void setFileId(final String fileId) {
		this.fileId = fileId;
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

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
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
