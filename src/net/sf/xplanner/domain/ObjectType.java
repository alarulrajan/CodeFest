/**
 *    XplannerPlus, agile planning software
 *    @author Maksym_Chyrkov.
 *    Copyright (C) 2009  Maksym Chyrkov
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */

package net.sf.xplanner.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.technoetic.xplanner.domain.Identifiable;

/**
 * The Class ObjectType.
 *
 * @author Maksym
 */
@Entity
@Table(name = "object_type")
public class ObjectType extends DomainObject implements Identifiable {
	
	/** The name. */
	@Column(unique = true, length = 32)
	private String name;

	/** The description. */
	@Column(length = 255)
	private String description;

	/**
     * Gets the description.
     *
     * @return the description
     */
	public String getDescription() {
		return this.description;
	}

	/**
     * Sets the description.
     *
     * @param description
     *            the new description
     */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
     * Gets the name.
     *
     * @return the name
     */
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
	 * @see net.sf.xplanner.domain.DomainObject#toString()
	 */
	@Override
	public String toString() {
		return "net.sf.xplanner.domain.ObjectType[name=" + this.name + "]";
	}

}
