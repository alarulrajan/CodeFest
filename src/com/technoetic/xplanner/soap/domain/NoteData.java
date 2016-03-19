package com.technoetic.xplanner.soap.domain;

import java.util.Calendar;

import net.sf.xplanner.domain.Note;

/**
 * The Class NoteData.
 */
public class NoteData extends DomainData {
    
    /** The attached to id. */
    private int attachedToId;
    
    /** The author id. */
    private int authorId;
    
    /** The subject. */
    private String subject;
    
    /** The body. */
    private String body;
    
    /** The submission time. */
    private Calendar submissionTime;
    
    /** The attachment id. */
    private int attachmentId;

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
     * Sets the submission time.
     *
     * @param submissionTime
     *            the new submission time
     */
    public void setSubmissionTime(final Calendar submissionTime) {
        this.submissionTime = submissionTime;
    }

    /**
     * Gets the submission time.
     *
     * @return the submission time
     */
    public Calendar getSubmissionTime() {
        return this.submissionTime;
    }

    /**
     * Gets the internal class.
     *
     * @return the internal class
     */
    public static Class getInternalClass() {
        return Note.class;
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
}
