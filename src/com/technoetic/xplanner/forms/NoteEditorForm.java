package com.technoetic.xplanner.forms;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.File;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class NoteEditorForm extends AbstractEditorForm {
	private int authorId;
	private String subject;
	private String body;
	private FormFile formFile;
	private String attachedToType;
	private int attachedToId;
	private int attachmentId;
	private File file;

	public String getContainerId() {
		return Integer.toString(this.getAttachedToId());
	}

	@Override
	public ActionErrors validate(final ActionMapping mapping,
			final HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();
		if (this.isSubmitted()) {
			AbstractEditorForm.require(errors, this.subject,
					"note.editor.missing_subject");
			AbstractEditorForm.require(errors, this.authorId,
					"note.editor.missing_author");
			if (this.formFile == null
					|| StringUtils.isEmpty(this.formFile.getFileName())) {
				AbstractEditorForm.require(errors, this.body,
						"note.editor.missing_body");
			}
		}
		return errors;
	}

	@Override
	public void reset(final ActionMapping mapping,
			final HttpServletRequest request) {
		super.reset(mapping, request);
		this.subject = null;
		this.body = null;
		this.formFile = null;
		this.authorId = 0;
		this.attachedToId = 0;
		this.attachedToType = null;
	}

	public void setAuthorId(final int authorId) {
		this.authorId = authorId;
	}

	public int getAuthorId() {
		return this.authorId;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	public String getBody() {
		return this.body;
	}

	public void setFormFile(final FormFile formFile) {
		this.formFile = formFile;
	}

	public FormFile getFormFile() {
		return this.formFile;
	}

	public void setAttachedToType(final String attachedToType) {
		this.attachedToType = attachedToType;
	}

	public String getAttachedToType() {
		return this.attachedToType;
	}

	public void setAttachedToId(final int attachedToId) {
		this.attachedToId = attachedToId;
	}

	public int getAttachedToId() {
		return this.attachedToId;
	}

	public int getAttachmentId() {
		return this.attachmentId;
	}

	public void setAttachmentId(final int attachmentId) {
		this.attachmentId = attachmentId;
	}

	public File getAttachedFile() {
		return this.file;
	}

	public void setFile(final File file) {
		this.file = file;
	}
}
