/**
 *    XplannerPlus, agile planning software
 *    @author Maksym_Chyrkov. 
 *    Copyright (C) 2010  Maksym Chyrkov
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
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.sf.xplanner.domain.enums.SettingScope;

/**
 * The Class Setting.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "object_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "setting")
public class Setting extends NamedObject {
	
	/** The category. */
	private String category;
	
	/** The default value. */
	private String defaultValue;
	
	/** The scope. */
	private SettingScope scope;
	
	/** The object type. */
	private ObjectType objectType;

	/**
     * Gets the object type.
     *
     * @return the object type
     */
	@ManyToOne(optional = false)
	public ObjectType getObjectType() {
		return this.objectType;
	}

	/**
     * Gets the category.
     *
     * @return the category
     */
	@Column(name = "category", length = 255)
	public String getCategory() {
		return this.category;
	}

	/**
     * Gets the default value.
     *
     * @return the default value
     */
	@Column(name = "defaultValue", length = 255)
	public String getDefaultValue() {
		return this.defaultValue;
	}

	/**
     * Gets the scope.
     *
     * @return the scope
     */
	@Column(name = "setting_scope")
	@Enumerated(EnumType.ORDINAL)
	public SettingScope getScope() {
		return this.scope;
	}

	/**
     * Sets the category.
     *
     * @param category
     *            the new category
     */
	public void setCategory(final String category) {
		this.category = category;
	}

	/**
     * Sets the default value.
     *
     * @param defaultValue
     *            the new default value
     */
	public void setDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
     * Sets the scope.
     *
     * @param scope
     *            the new scope
     */
	public void setScope(final SettingScope scope) {
		this.scope = scope;
	}

	/**
     * Sets the object type.
     *
     * @param objectType
     *            the new object type
     */
	public void setObjectType(final ObjectType objectType) {
		this.objectType = objectType;
	}

}
