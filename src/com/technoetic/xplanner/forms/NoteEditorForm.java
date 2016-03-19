package com.technoetic.xplanner.forms;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.File;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * The Class NoteEditorForm.
 */
public class NoteEditorForm extends AbstractEditorForm {
    
    /** The author id. */
    private int authorId;
    
    /** The subject. */
    private String subject;
    
    /** The body. */
    private String body;
    
    /** The form file. */
    private FormFile formFile;
    
    /** The attached to type. */
    private String attachedToType;
    
    /** The attached to id. */
    private int attachedToId;
    
    /** The attachment id. */
    private int attachmentId;
    
    /** The file. */
    private File file;

    /**
     * Gets the container id.
     *
     * @return the container id
     */
    public String getContainerId() {
        return Integer.toString(this.getAttachedToId());
    }

    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
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

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.forms.AbstractEditorForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
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

    /**
     * Sets the author id.
     *
     * @param authorId
     *            the new author id
     */
    public void setAuthorId(final int authorId) {
        this.authorId = authorId;
    }

    /**
     * Gets the author id.
     *
     * @return the author id
     */
    public int getAuthorId() {
        return this.authorId;
    }

    /**
     * Sets the subject.
     *
     * @param subject
     *            the new subject
     */
    public void setSubject(final String subject) {
        this.subject = subject;
    }

    /**
     * Gets the subject.
     *
     * @return the subject
     */
    public String getSubject() {
        return this.subject;
    }

    /**
     * Sets the body.
     *
     * @param body
     *            the new body
     */
    public void setBody(final String body) {
        this.body = body;
    }

    /**
     * Gets the body.
     *
     * @return the body
     */
    public String getBody() {
        return this.body;
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
     * Sets the attached to type.
     *
     * @param attachedToType
     *            the new attached to type
     */
    public void setAttachedToType(final String attachedToType) {
        this.attachedToType = attachedToType;
    }

    /**
     * Gets the attached to type.
     *
     * @return the attached to type
     */
    public String getAttachedToType() {
        return this.attachedToType;
    }

    /**
     * Sets the attached to id.
     *
     * @param attachedToId
     *            the new attached to id
     */
    public void setAttachedToId(final int attachedToId) {
        this.attachedToId = attachedToId;
    }

    /**
     * Gets the attached to id.
     *
     * @return the attached to id
     */
    public int getAttachedToId() {
        return this.attachedToId;
    }

    /**
     * Gets the attachment id.
     *
     * @return the attachment id
     */
    public int getAttachmentId() {
        return this.attachmentId;
    }

    /**
     * Sets the attachment id.
     *
     * @param attachmentId
     *            the new attachment id
     */
    public void setAttachmentId(final int attachmentId) {
        this.attachmentId = attachmentId;
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
}
