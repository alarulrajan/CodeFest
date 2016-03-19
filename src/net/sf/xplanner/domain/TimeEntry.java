package net.sf.xplanner.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.domain.Nameable;

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
@Table(name = "time_entry")
public class TimeEntry extends DomainObject implements java.io.Serializable,
		Nameable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3275141598175122473L;
	
	/** The start time. */
	private Date startTime;
	
	/** The end time. */
	private Date endTime;
	
	/** The duration. */
	private double duration;
	
	/** The person1 id. */
	private int person1Id;
	
	/** The person2 id. */
	private int person2Id;
	
	/** The task. */
	private Task task;
	
	/** The report date. */
	private Date reportDate;
	
	/** The description. */
	private String description;

	/**
     * Instantiates a new time entry.
     */
	public TimeEntry() {
	}

	/**
     * Gets the start time.
     *
     * @return the start time
     */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time", length = 19)
	public Date getStartTime() {
		return this.startTime;
	}

	/**
     * Sets the start time.
     *
     * @param startTime
     *            the new start time
     */
	public void setStartTime(final Date startTime) {
		this.startTime = startTime;
	}

	/**
     * Gets the end time.
     *
     * @return the end time
     */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time", length = 19)
	public Date getEndTime() {
		return this.endTime;
	}

	/**
     * Sets the end time.
     *
     * @param endTime
     *            the new end time
     */
	public void setEndTime(final Date endTime) {
		this.endTime = endTime;
	}

	/**
     * Gets the duration.
     *
     * @return the duration
     */
	@Column(name = "duration", precision = 22, scale = 0)
	public double getDuration() {
		if (this.startTime != null && this.endTime != null) {
			this.duration = (this.endTime.getTime() - this.startTime.getTime()) / 3600000.0;
		}
		return this.duration;
	}

	/**
     * Sets the duration.
     *
     * @param duration
     *            the new duration
     */
	public void setDuration(final double duration) {
		this.duration = duration;
	}

	/**
     * Gets the person1 id.
     *
     * @return the person1 id
     */
	@Column(name = "person1_id")
	public int getPerson1Id() {
		return this.person1Id;
	}

	/**
     * Sets the person1 id.
     *
     * @param person1Id
     *            the new person1 id
     */
	public void setPerson1Id(final int person1Id) {
		this.person1Id = person1Id;
	}

	/**
     * Gets the person2 id.
     *
     * @return the person2 id
     */
	@Column(name = "person2_id")
	public int getPerson2Id() {
		return this.person2Id;
	}

	/**
     * Sets the person2 id.
     *
     * @param person2Id
     *            the new person2 id
     */
	public void setPerson2Id(final int person2Id) {
		this.person2Id = person2Id;
	}

	/**
     * Gets the task.
     *
     * @return the task
     */
	@ManyToOne
	@JoinColumn(name = "task_id")
	public Task getTask() {
		return this.task;
	}

	/**
     * Sets the task.
     *
     * @param task
     *            the new task
     */
	public void setTask(final Task task) {
		this.task = task;
	}

	/**
     * Gets the report date.
     *
     * @return the report date
     */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "report_date", length = 19)
	public Date getReportDate() {
		return this.reportDate;
	}

	/**
     * Sets the report date.
     *
     * @param reportDate
     *            the new report date
     */
	public void setReportDate(final Date reportDate) {
		this.reportDate = reportDate;
	}

	/**
	 * The classic way for XPlanner to calculate "effort" is in idea wall clock
	 * time or pair-programming hours. Some teams would like to do labor
	 * tracking using XPlanner and want to double the effort measured for a
	 * paired time entry.
	 * 
	 * This behavior is configurable in xplanner.properties.
	 * 
	 * @return measured effort
	 */
	@Transient
	@Deprecated
	public double getEffort() {
		final boolean adjustHoursForPairing = "double"
				.equalsIgnoreCase(new XPlannerProperties().getProperty(
						"xplanner.pairprogramming", "single"));
		final boolean isPairedEntry = this.person1Id != 0
				&& this.person2Id != 0;
		return adjustHoursForPairing && isPairedEntry ? this.getDuration() * 2
				: this.getDuration();
	}

	/**
     * Checks if is currently active.
     *
     * @param personId
     *            the person id
     * @return true, if is currently active
     */
	@Deprecated
	public boolean isCurrentlyActive(final int personId) {
		return this.startTime != null && this.endTime == null
				&& this.duration == 0
				&& (personId == this.person1Id || personId == this.person2Id);
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.Nameable#getDescription()
	 */
	@Override
	@Column(name = "description", length = 65535)
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

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.Nameable#getName()
	 */
	@Transient
	@Override
	public String getName() {
		// ChangeSoon 
		return null;
	}

}
