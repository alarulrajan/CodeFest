package com.technoetic.xplanner.actions;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.File;
import net.sf.xplanner.domain.Note;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.classic.Session;

public class DownloadAttachmentAction extends AbstractAction {
	private static Logger logger = Logger
			.getLogger(DownloadAttachmentAction.class);
	private static final int BUFFER_SIZE = 4000;

	protected Note locateNote(final Session session, final int id) {
		Note result = null;

		try {
			try {
				final Class objectClass = Note.class;
				String query = "from " + objectClass.getName();
				query += " where id=" + id;

				final List objects = session.find(query);
				if (objects != null && objects.size() > 0) {
					result = (Note) objects.get(0);
				}
			} catch (final Exception ex) {
				DownloadAttachmentAction.logger.error("error loading objects",
						ex);
				return null;
			}
		} catch (final Exception ex) {
			DownloadAttachmentAction.logger.error("error", ex);
			return null;
		}

		return result;
	}

	@Override
	protected ActionForward doExecute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		int noteId = -1;
		try {
			noteId = Integer.parseInt(request.getParameter("oid"));
		} catch (final NumberFormatException e) {
			DownloadAttachmentAction.logger.error(
					"Exception: " + e.getMessage(), e);
		}

		final Note currentNote = this.locateNote(this.getSession(request),
				noteId);
		if (currentNote != null) {
			final File file = currentNote.getFile();
			DownloadAttachmentAction.logger.debug("Note : " + file.getName());
			DownloadAttachmentAction.logger.debug("Note attachment size : "
					+ file.getFileSize());
			DownloadAttachmentAction.logger.debug("Note contentType : "
					+ file.getContentType());

			response.setContentType(file.getContentType());
			response.setHeader("Content-disposition",
					"note;filename=\"" + file.getName() + "\"");
			response.addHeader("Content-description", file.getName());

			final ServletOutputStream stream = response.getOutputStream();
			this.writeAttachment(currentNote, stream);
			stream.close();
		}

		return null;
	}

	private void writeAttachment(final Note currentNote,
			final ServletOutputStream stream) throws SQLException, IOException {
		final InputStream attachmentStream = currentNote.getFile().getData()
				.getBinaryStream();
		final byte[] buffer = new byte[DownloadAttachmentAction.BUFFER_SIZE];
		int n = attachmentStream.read(buffer);
		while (n > 0) {
			stream.write(buffer, 0, n);
			n = attachmentStream.read(buffer);
		}
		stream.flush();
	}
}
