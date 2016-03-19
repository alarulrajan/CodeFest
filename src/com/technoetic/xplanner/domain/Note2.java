package com.technoetic.xplanner.domain;

import java.util.Date;
import java.util.List;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.File;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.tags.DomainContext;

/**
 * The Class Note2.
 */
public class Note2 extends DomainObject implements Nameable {
	
	/** The attached to id. */
	private int attachedToId;
	
	/** The author id. */
	private int authorId;
	
	/** The subject. */
	private String subject;
	
	/** The body. */
	private String body;
	
	/** The submission time. */
	private Date submissionTime = new Date();
	
	/** The file. */
	private File file;
	
	/** The Constant ATTACHED_NOTES_QUERY. */
	public static final String ATTACHED_NOTES_QUERY = "AttachedNotesQuery";

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
     * Sets the file.
     *
     * @param file
     *            the new file
     */
	public void setFile(final File file) {
		this.file = file;
	}

	/**
     * Gets the file.
     *
     * @return the file
     */
	public File getFile() {
		return this.file;
	}

	/**
     * Sets the submission time.
     *
     * @param submissionTime
     *            the new submission time
     */
	public void setSubmissionTime(final Date submissionTime) {
		this.submissionTime = submissionTime;
	}

	/**
     * Gets the submission time.
     *
     * @return the submission time
     */
	public Date getSubmissionTime() {
		return this.submissionTime;
	}

	/* (non-Javadoc)
	 * @see net.sf.xplanner.domain.DomainObject#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/* (non-Javadoc)
	 * @see net.sf.xplanner.domain.DomainObject#equals(java.lang.Object)
	 */
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

	/**
     * Checks if is filename equal.
     *
     * @param otherFile
     *            the other file
     * @return true, if is filename equal
     */
	private boolean isFilenameEqual(final File otherFile) {
		return otherFile != null && this.file.getName() != null ? !this.file
				.getName().equals(otherFile.getName()) : false;
	}

	/* (non-Javadoc)
	 * @see net.sf.xplanner.domain.DomainObject#toString()
	 */
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

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.Nameable#getName()
	 */
	@Override
	public String getName() {
		return this.getSubject();
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.Nameable#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.getBody();
	}

	/**
     * Gets the attachment count.
     *
     * @return the attachment count
     * @throws HibernateException
     *             the hibernate exception
     */
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

	/**
     * Gets the parent.
     *
     * @return the parent
     */
	public DomainObject getParent() {
		// DEBT: Remove the cycle. Note should not depends on a web tier
		// operation
		return DomainContext.getNoteTarget(this.getAttachedToId());
	}
}
