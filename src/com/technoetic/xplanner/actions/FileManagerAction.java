package com.technoetic.xplanner.actions;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Directory;
import net.sf.xplanner.domain.File;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.file.FileSystem;
import com.technoetic.xplanner.forms.FileManagerForm;

/**
 * EXPERIMENTAL - File Management Action.
 */
public class FileManagerAction extends AbstractAction {
    
    /** The buffer size. */
    private final int BUFFER_SIZE = 4000;
    
    /** The file system. */
    private FileSystem fileSystem;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.actions.AbstractAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ActionForward doExecute(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final Session hibernateSession = this.getSession(request);
        final FileManagerForm fform = (FileManagerForm) form;
        try {
            hibernateSession.connection().setAutoCommit(false);
            if (fform.getAction() == null) {
                fform.setAction("list");
                fform.setDirectoryId(Integer.toString(this.fileSystem
                        .getRootDirectory().getId()));
            }
            if (fform.getAction().equals("upload")) {
                final FormFile formFile = fform.getFormFile();
                this.fileSystem.createFile(hibernateSession,
                        Integer.parseInt(fform.getDirectoryId()),
                        formFile.getFileName(), formFile.getContentType(),
                        formFile.getFileSize(), formFile.getInputStream());
            } else if (fform.getAction().equals("download")) {
                final File file = this.fileSystem.getFile(hibernateSession,
                        Integer.parseInt(fform.getFileId()));
                this.writeFileToResponse(response, file);
            } else if (fform.getAction().equals("delete")) {
                this.fileSystem.deleteFile(hibernateSession,
                        Integer.parseInt(fform.getFileId()));
            } else if (fform.getAction().equals("mkdir")) {
                final int parentDirectoryId = Integer.parseInt(fform
                        .getDirectoryId());
                this.fileSystem.createDirectory(hibernateSession,
                        parentDirectoryId, fform.getName());
            } else if (fform.getAction().equals("rmdir")) {
                final Directory directory = this.fileSystem.getDirectory(
                        hibernateSession,
                        Integer.parseInt(fform.getDirectoryId()));
                Directory parent = directory.getParent();
                if (parent == null) {
                    parent = this.fileSystem.getRootDirectory();
                }
                fform.setDirectoryId(Integer.toString(parent.getId()));
                this.fileSystem.deleteDirectory(hibernateSession,
                        directory.getId());
            }
            hibernateSession.flush();
            hibernateSession.connection().commit();
            if (fform.getDirectoryId() != null) {
                final Directory directory = this.fileSystem.getDirectory(
                        hibernateSession,
                        Integer.parseInt(fform.getDirectoryId()));
                request.setAttribute("directory", directory);
            }
            request.setAttribute("root", this.fileSystem.getRootDirectory());
            return mapping.findForward("display");
        } catch (final ObjectNotFoundException ex) {
            request.setAttribute("exception", ex);
            return mapping.findForward("error/objectNotFound");
        } catch (final Exception e) {
            hibernateSession.connection().rollback();
            throw e;
        }
    }

    /**
     * Write file to response.
     *
     * @param response
     *            the response
     * @param file
     *            the file
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws SQLException
     *             the SQL exception
     */
    private void writeFileToResponse(final HttpServletResponse response,
            final File file) throws IOException, SQLException {
        response.setContentType(file.getContentType());
        response.setHeader("Content-disposition",
                "note;filename=\"" + file.getName() + "\"");
        response.addHeader("Content-description", file.getName());
        final ServletOutputStream stream = response.getOutputStream();
        final InputStream attachmentStream = file.getData().getBinaryStream();
        final byte[] buffer = new byte[this.BUFFER_SIZE];
        int n = attachmentStream.read(buffer);
        while (n > 0) {
            stream.write(buffer, 0, n);
            n = attachmentStream.read(buffer);
        }
        stream.flush();
        stream.close();
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
