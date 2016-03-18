package com.technoetic.xplanner.soap.domain;

import java.util.Calendar;

import net.sf.xplanner.domain.Note;

public class NoteData extends DomainData {
	private int attachedToId;
	private int authorId;
	private String subject;
	private String body;
	private Calendar submissionTime;
	private int attachmentId;

	public void setAttachedToId(final int attachedToId) {
		this.attachedToId = attachedToId;
	}

	public int getAttachedToId() {
		return this.attachedToId;
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

	public void setSubmissionTime(final Calendar submissionTime) {
		this.submissionTime = submissionTime;
	}

	public Calendar getSubmissionTime() {
		return this.submissionTime;
	}

	public static Class getInternalClass() {
		return Note.class;
	}

	public int getAttachmentId() {
		return this.attachmentId;
	}

	public void setAttachmentId(final int attachmentId) {
		this.attachmentId = attachmentId;
	}
}
