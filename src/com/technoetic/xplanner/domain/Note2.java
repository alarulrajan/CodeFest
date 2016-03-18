package com.technoetic.xplanner.domain;

import java.util.Date;
import java.util.List;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.File;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.tags.DomainContext;

public class Note2 extends DomainObject implements Nameable {
	private int attachedToId;
	private int authorId;
	private String subject;
	private String body;
	private Date submissionTime = new Date();
	private File file;
	public static final String ATTACHED_NOTES_QUERY = "AttachedNotesQuery";

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

	public void setFile(final File file) {
		this.file = file;
	}

	public File getFile() {
		return this.file;
	}

	public void setSubmissionTime(final Date submissionTime) {
		this.submissionTime = submissionTime;
	}

	public Date getSubmissionTime() {
		return this.submissionTime;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Note2)) {
			return false;
		}
		// todo Get rid of this hack checking id == 0. Jacques will add an
		// abstract fieldsEquals member to DomainObject that all inheriting
		// classes will implement.
		if (this.getId() != 0 && !super.equals(o)) {
			return false;
		}

		final Note2 note = (Note2) o;

		if (this.attachedToId != note.attachedToId) {
			return false;
		}
		if (this.authorId != note.authorId) {
			return false;
		}
		if (this.body != null ? !this.body.equals(note.body)
				: note.body != null) {
			return false;
		}
		if (this.file != null ? this.isFilenameEqual(note.file)
				: note.file != null) {
			return false;
		}
		if (this.subject != null ? !this.subject.equals(note.subject)
				: note.subject != null) {
			return false;
		}
		return !(this.submissionTime != null ? !this.submissionTime
				.equals(note.submissionTime) : note.submissionTime != null);

	}

	private boolean isFilenameEqual(final File otherFile) {
		return otherFile != null && this.file.getName() != null ? !this.file
				.getName().equals(otherFile.getName()) : false;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("Note - ");
		sb.append("filename: "
				+ (this.file != null ? this.file.getName() : "none") + "\n");
		sb.append("file: " + this.file + "\n");
		sb.append("attachedToId: " + this.attachedToId + "\n");
		sb.append("authorId: " + this.authorId + "\n");
		sb.append("body: " + this.body + "\n");
		sb.append("subject: " + this.subject + "\n");
		sb.append("submissionTime: " + this.submissionTime + "\n");

		return sb.toString();
	}

	@Override
	public String getName() {
		return this.getSubject();
	}

	@Override
	public String getDescription() {
		return this.getBody();
	}

	public int getAttachmentCount() throws HibernateException {
		List noteList = null;
		noteList = ThreadSession.get().find(
				"select note from Note note where note.file.id="
						+ this.getFile().getId());
		if (noteList != null) {
			return noteList.size();
		} else {
			return 0;
		}
	}

	public DomainObject getParent() {
		// DEBT: Remove the cycle. Note should not depends on a web tier
		// operation
		return DomainContext.getNoteTarget(this.getAttachedToId());
	}
}
