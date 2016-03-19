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
@Table(name = "patches")
public class Patches implements java.io.Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3888790427234669567L;
	
	/** The system name. */
	private String systemName;
	
	/** The patch level. */
	private int patchLevel;
	
	/** The patch date. */
	private Date patchDate;
	
	/** The patch in progress. */
	private char patchInProgress;

	/**
     * Instantiates a new patches.
     */
	public Patches() {
	}

	/**
     * Instantiates a new patches.
     *
     * @param systemName
     *            the system name
     * @param patchLevel
     *            the patch level
     * @param patchDate
     *            the patch date
     * @param patchInProgress
     *            the patch in progress
     */
	public Patches(final String systemName, final int patchLevel,
			final Date patchDate, final char patchInProgress) {
		this.systemName = systemName;
		this.patchLevel = patchLevel;
		this.patchDate = patchDate;
		this.patchInProgress = patchInProgress;
	}

	/**
     * Gets the system name.
     *
     * @return the system name
     */
	@Id
	@Column(name = "system_name", unique = true, nullable = false, length = 30)
	public String getSystemName() {
		return this.systemName;
	}

	/**
     * Sets the system name.
     *
     * @param systemName
     *            the new system name
     */
	public void setSystemName(final String systemName) {
		this.systemName = systemName;
	}

	/**
     * Gets the patch level.
     *
     * @return the patch level
     */
	@Column(name = "patch_level", nullable = false)
	public int getPatchLevel() {
		return this.patchLevel;
	}

	/**
     * Sets the patch level.
     *
     * @param patchLevel
     *            the new patch level
     */
	public void setPatchLevel(final int patchLevel) {
		this.patchLevel = patchLevel;
	}

	/**
     * Gets the patch date.
     *
     * @return the patch date
     */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "patch_date", nullable = false, length = 19)
	public Date getPatchDate() {
		return this.patchDate;
	}

	/**
     * Sets the patch date.
     *
     * @param patchDate
     *            the new patch date
     */
	public void setPatchDate(final Date patchDate) {
		this.patchDate = patchDate;
	}

	/**
     * Gets the patch in progress.
     *
     * @return the patch in progress
     */
	@Column(name = "patch_in_progress", nullable = false, length = 1)
	public char getPatchInProgress() {
		return this.patchInProgress;
	}

	/**
     * Sets the patch in progress.
     *
     * @param patchInProgress
     *            the new patch in progress
     */
	public void setPatchInProgress(final char patchInProgress) {
		this.patchInProgress = patchInProgress;
	}

}
