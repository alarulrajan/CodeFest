package net.sf.xplanner.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.tags.DomainContext;

/**
 * XplannerPlus, agile planning software.
 *
 * @author Maksym_Chyrkov. Copyright (C) 2009 Maksym Chyrkov This program is
 *         free software: you can redistribute it and/or modify it under the
 *         terms of the GNU General Public License as published by the Free
 *         Software Foundation, either version 3 of the License, or (at your
 *         option) any later version.
 * 
 *         This program is distributed in the hope that it will be useful, but
 *         WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with this program. If not, see <http://www.gnu.org/licenses/>
 */

@Entity
@Table(name = "note")
public class Note extends DomainObject implements java.io.Serializable,
		Nameable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4379309425634770729L;
	
	/** The attached to id. */
	private int attachedToId;
	
	/** The author id. */
	private Integer authorId;
	
	/** The subject. */
	private String subject;
	
	/** The body. */
	private String body;
	
	/** The submission time. */
	private Date submissionTime;
	
	/** The file. */
	private File file;
	
	/** The Constant ATTACHED_NOTES_QUERY. */
	public static final String ATTACHED_NOTES_QUERY = "AttachedNotesQuery";

	/**
     * Instantiates a new note.
     */
	public Note() {
	}

	/**
     * Gets the attached to id.
     *
     * @return the attached to id
     */
	@Column(name = "attachedTo_id")
	public int getAttachedToId() {
		return this.attachedToId;
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
     * Gets the author id.
     *
     * @return the author id
     */
	@Column(name = "author_id")
	public Integer getAuthorId() {
		return this.authorId;
	}

	/**
     * Sets the author id.
     *
     * @param authorId
     *            the new author id
     */
	public void setAuthorId(final Integer authorId) {
		this.authorId = authorId;
	}

	/**
     * Gets the subject.
     *
     * @return the subject
     */
	@Column(name = "subject")
	public String getSubject() {
		return this.subject;
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
     * Gets the body.
     *
     * @return the body
     */
	@Column(name = "body", length = 65535)
	public String getBody() {
		return this.body;
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
     * Gets the submission time.
     *
     * @return the submission time
     */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "submission_time", length = 19)
	public Date getSubmissionTime() {
		return this.submissionTime;
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
     * Gets the file.
     *
     * @return the file
     */
	@OneToOne
	@JoinColumn(name = "attachment_id")
	public File getFile() {
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

	/**
     * Gets the attachment count.
     *
     * @return the attachment count
     * @throws HibernateException
     *             the hibernate exception
     */
	@Transient
	@Deprecated
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
	@Transient
	@Deprecated
	public DomainObject getParent() {
		// DEBT: Remove the cycle. Note should not depends on a web tier
		// operation
		return DomainContext.getNoteTarget(this.getAttachedToId());
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.Nameable#getName()
	 */
	@Transient
	@Override
	public String getName() {
		return this.getSubject();
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.Nameable#getDescription()
	 */
	@Transient
	@Override
	public String getDescription() {
		return this.getBody();
	}

}
