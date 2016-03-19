package net.sf.xplanner.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "attribute")
public class Attribute implements java.io.Serializable, Identifiable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7560899355285382322L;
	
	/** The id. */
	private AttributeId id;
	
	/** The value. */
	private String value;

	/**
     * Instantiates a new attribute.
     */
	public Attribute() {
	}

	/**
     * Instantiates a new attribute.
     *
     * @param id
     *            the id
     */
	public Attribute(final AttributeId id) {
		this.id = id;
	}

	/**
     * Instantiates a new attribute.
     *
     * @param id
     *            the id
     * @param value
     *            the value
     */
	public Attribute(final AttributeId id, final String value) {
		this.id = id;
		this.value = value;
	}

	/**
     * Instantiates a new attribute.
     *
     * @param targetId
     *            the target id
     * @param attributeName
     *            the attribute name
     * @param attributeValue
     *            the attribute value
     */
	public Attribute(final int targetId, final String attributeName,
			final String attributeValue) {
		this(new AttributeId(targetId, attributeName), attributeValue);
	}

	/**
     * Gets the attribute id.
     *
     * @return the attribute id
     */
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "targetId", column = @Column(name = "targetId", nullable = false)),
			@AttributeOverride(name = "name", column = @Column(name = "name", nullable = false)) })
	public AttributeId getAttributeId() {
		return this.id;
	}

	/**
     * Sets the attribute id.
     *
     * @param id
     *            the new attribute id
     */
	public void setAttributeId(final AttributeId id) {
		this.id = id;
	}

	/**
     * Gets the value.
     *
     * @return the value
     */
	@Column(name = "\"value\"")
	public String getValue() {
		return this.value;
	}

	/**
     * Sets the value.
     *
     * @param value
     *            the new value
     */
	public void setValue(final String value) {
		this.value = value;
	}

	/**
     * Gets the name.
     *
     * @return the name
     */
	@Transient
	public String getName() {
		return this.id.getName();
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.Identifiable#getId()
	 */
	@Override
	@Transient
	public int getId() {
		return 0;
	}
}
