package net.sf.xplanner.domain;

import java.security.Principal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.technoetic.xplanner.domain.Identifiable;

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
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(columnNames = "role"))
public class Role implements java.io.Serializable, Principal, Identifiable {
	
	/** The Constant SYSADMIN. */
	public static final String SYSADMIN = "sysadmin";
	
	/** The Constant ADMIN. */
	public static final String ADMIN = "admin";
	
	/** The Constant EDITOR. */
	public static final String EDITOR = "editor";
	
	/** The Constant VIEWER. */
	public static final String VIEWER = "viewer";
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4921403899778522202L;
	
	/** The left. */
	private Integer left;
	
	/** The right. */
	private Integer right;
	
	/** The name. */
	private String name;
	
	/** The id. */
	private int id;

	/**
     * Instantiates a new role.
     */
	public Role() {
	}

	/**
     * Instantiates a new role.
     *
     * @param name
     *            the name
     */
	public Role(final String name) {
		this.setName(name);
	}

	/**
     * Gets the left.
     *
     * @return the left
     */
	@Column(name = "lft")
	public Integer getLeft() {
		return this.left;
	}

	/**
     * Sets the left.
     *
     * @param lft
     *            the new left
     */
	public void setLeft(final Integer lft) {
		this.left = lft;
	}

	/**
     * Gets the right.
     *
     * @return the right
     */
	@Column(name = "rgt")
	public Integer getRight() {
		return this.right;
	}

	/**
     * Sets the right.
     *
     * @param rgt
     *            the new right
     */
	public void setRight(final Integer rgt) {
		this.right = rgt;
	}

	/* (non-Javadoc)
	 * @see java.security.Principal#getName()
	 */
	@Override
	@Column(name = "role", nullable = false)
	public String getName() {
		return this.name;
	}

	/**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
	public void setName(final String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.Identifiable#getId()
	 */
	@Id
	@GeneratedValue(generator = "commonId")
	@GenericGenerator(name = "commonId", strategy = "com.technoetic.xplanner.db.hibernate.HibernateIdentityGenerator")
	@Column(name = "id", unique = true, nullable = false)
	@Override
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

}
