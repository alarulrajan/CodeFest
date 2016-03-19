package com.technoetic.xplanner.actions;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Directory;
import net.sf.xplanner.domain.File;
import net.sf.xplanner.domain.Note;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

import com.technoetic.xplanner.file.FileSystem;
import com.technoetic.xplanner.forms.NoteEditorForm;

/**
 * The Class EditNoteAction.
 */
public class EditNoteAction extends EditObjectAction<Note> {
	
	/** The file system. */
	private FileSystem fileSystem;

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.EditObjectAction#populateObject(javax.servlet.http.HttpServletRequest, java.lang.Object, org.apache.struts.action.ActionForm)
	 */
	@Override
	protected void populateObject(final HttpServletRequest request,
			final Object object, final ActionForm form) throws Exception {
		Logger.getLogger(EditNoteAction.class).debug("Populating...");

		super.populateObject(request, object, form);

		final NoteEditorForm noteForm = (NoteEditorForm) form;

		final FormFile formFile = noteForm.getFormFile();
		if (formFile != null) {
			final String filename = formFile.getFileName();
			if (StringUtils.isNotEmpty(filename)) {
				final String contentType = formFile.getContentType();
				final InputStream input = formFile.getInputStream();
				final int fileSize = formFile.getFileSize();
				final int projectId = request.getParameter("projectId") != null ? Integer
						.parseInt(request.getParameter("projectId")) : 0;
				final Directory directory = this.fileSystem
						.getDirectory("/attachments/project/" + projectId);
				final File file = this.fileSystem.createFile(
						this.getSession(request), directory, filename,
						contentType, fileSize, input);
				final Note note = (Note) object;
				note.setFile(file);

				Logger.getLogger(EditNoteAction.class).debug(
						"Saving note: filename=" + filename + ", fileSize="
								+ fileSize + ", contentType=" + contentType);

			}
		}
	}

	/**
     * Sets the file system.
     *
     * @param fileSystem
     *            the new file system
     */
	public void setFileSystem(final FileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}
}
