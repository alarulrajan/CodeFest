package com.technoetic.xplanner.soap.domain;

import java.io.Serializable;
import java.util.Calendar;

import com.technoetic.xplanner.domain.Identifiable;

/**
 * The Class DomainData.
 */
public abstract class DomainData implements Identifiable, Serializable {
	
	/** The last update time. */
	private Calendar lastUpdateTime;
	
	/** The id. */
	private int id;

	/**
     * Gets the last update time.
     *
     * @return the last update time
     */
	public Calendar getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	/**
     * Sets the last update time.
     *
     * @param lastUpdateTime
     *            the new last update time
     */
	public void setLastUpdateTime(final Calendar lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.Identifiable#getId()
	 */
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
