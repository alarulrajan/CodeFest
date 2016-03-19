package net.sf.xplanner.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@Table(name = "integration")
public class Integration implements java.io.Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6948499568758432723L;
	
	/** The id. */
	private int id;
	
	/** The project id. */
	private Integer projectId;
	
	/** The last update. */
	private Date lastUpdate;
	
	/** The person id. */
	private Integer personId;
	
	/** The when started. */
	private Date whenStarted;
	
	/** The when requested. */
	private Date whenRequested;
	
	/** The when complete. */
	private Date whenComplete;
	
	/** The state. */
	private Character state;
	
	/** The comments. */
	private String comments;

	/**
     * Instantiates a new integration.
     */
	public Integration() {
	}

	/**
     * Instantiates a new integration.
     *
     * @param id
     *            the id
     */
	public Integration(final int id) {
		this.id = id;
	}

	/**
     * Instantiates a new integration.
     *
     * @param id
     *            the id
     * @param projectId
     *            the project id
     * @param lastUpdate
     *            the last update
     * @param personId
     *            the person id
     * @param whenStarted
     *            the when started
     * @param whenRequested
     *            the when requested
     * @param whenComplete
     *            the when complete
     * @param state
     *            the state
     * @param comments
     *            the comments
     */
	public Integration(final int id, final Integer projectId,
			final Date lastUpdate, final Integer personId,
			final Date whenStarted, final Date whenRequested,
			final Date whenComplete, final Character state,
			final String comments) {
		this.id = id;
		this.projectId = projectId;
		this.lastUpdate = lastUpdate;
		this.personId = personId;
		this.whenStarted = whenStarted;
		this.whenRequested = whenRequested;
		this.whenComplete = whenComplete;
		this.state = state;
		this.comments = comments;
	}

	/**
     * Gets the id.
     *
     * @return the id
     */
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	/**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
	public void setId(final int id) {
		this.id = id;
	}

	/**
     * Gets the project id.
     *
     * @return the project id
     */
	@Column(name = "project_id")
	public Integer getProjectId() {
		return this.projectId;
	}

	/**
     * Sets the project id.
     *
     * @param projectId
     *            the new project id
     */
	public void setProjectId(final Integer projectId) {
		this.projectId = projectId;
	}

	/**
     * Gets the last update.
     *
     * @return the last update
     */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_update", length = 19)
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	/**
     * Sets the last update.
     *
     * @param lastUpdate
     *            the new last update
     */
	public void setLastUpdate(final Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
     * Gets the person id.
     *
     * @return the person id
     */
	@Column(name = "person_id")
	public Integer getPersonId() {
		return this.personId;
	}

	/**
     * Sets the person id.
     *
     * @param personId
     *            the new person id
     */
	public void setPersonId(final Integer personId) {
		this.personId = personId;
	}

	/**
     * Gets the when started.
     *
     * @return the when started
     */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "when_started", length = 19)
	public Date getWhenStarted() {
		return this.whenStarted;
	}

	/**
     * Sets the when started.
     *
     * @param whenStarted
     *            the new when started
     */
	public void setWhenStarted(final Date whenStarted) {
		this.whenStarted = whenStarted;
	}

	/**
     * Gets the when requested.
     *
     * @return the when requested
     */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "when_requested", length = 19)
	public Date getWhenRequested() {
		return this.whenRequested;
	}

	/**
     * Sets the when requested.
     *
     * @param whenRequested
     *            the new when requested
     */
	public void setWhenRequested(final Date whenRequested) {
		this.whenRequested = whenRequested;
	}

	/**
     * Gets the when complete.
     *
     * @return the when complete
     */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "when_complete", length = 19)
	public Date getWhenComplete() {
		return this.whenComplete;
	}

	/**
     * Sets the when complete.
     *
     * @param whenComplete
     *            the new when complete
     */
	public void setWhenComplete(final Date whenComplete) {
		this.whenComplete = whenComplete;
	}

	/**
     * Gets the state.
     *
     * @return the state
     */
	@Column(name = "state", length = 1)
	public Character getState() {
		return this.state;
	}

	/**
     * Sets the state.
     *
     * @param state
     *            the new state
     */
	public void setState(final Character state) {
		this.state = state;
	}

	/**
     * Gets the comments.
     *
     * @return the comments
     */
	@Column(name = "comments")
	public String getComments() {
		return this.comments;
	}

	/**
     * Sets the comments.
     *
     * @param comments
     *            the new comments
     */
	public void setComments(final String comments) {
		this.comments = comments;
	}

}
