package net.sf.xplanner.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "person_role")
public class PersonRole implements java.io.Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4005621505117736352L;
	
	/** The id. */
	private PersonRoleId id;

	/**
     * Instantiates a new person role.
     */
	public PersonRole() {
	}

	/**
     * Instantiates a new person role.
     *
     * @param id
     *            the id
     */
	public PersonRole(final PersonRoleId id) {
		this.id = id;
	}

	/**
     * Instantiates a new person role.
     *
     * @param projectId
     *            the project id
     * @param personId
     *            the person id
     * @param roleId
     *            the role id
     */
	public PersonRole(final int projectId, final int personId, final int roleId) {
		this.id = new PersonRoleId(roleId, personId, projectId);
	}

	/**
     * Gets the id.
     *
     * @return the id
     */
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "roleId", column = @Column(name = "role_id", nullable = false)),
			@AttributeOverride(name = "personId", column = @Column(name = "person_id", nullable = false)),
			@AttributeOverride(name = "projectId", column = @Column(name = "project_id", nullable = false)) })
	public PersonRoleId getId() {
		return this.id;
	}

	/**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
	public void setId(final PersonRoleId id) {
		this.id = id;
	}

	/**
     * Gets the role id.
     *
     * @return the role id
     */
	@Transient
	public int getRoleId() {
		return this.id.getRoleId();
	}

	/**
     * Gets the person id.
     *
     * @return the person id
     */
	@Transient
	public int getPersonId() {
		return this.id.getPersonId();
	}

	/**
     * Gets the project id.
     *
     * @return the project id
     */
	@Transient
	public int getProjectId() {
		return this.getProjectId();
	}

}
