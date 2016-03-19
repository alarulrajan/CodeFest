package net.sf.xplanner.domain;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.technoetic.xplanner.domain.Describable;
import com.technoetic.xplanner.domain.NoteAttachable;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.domain.TaskStatus;

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
@Table(name = "task")
public class Task extends NamedObject implements java.io.Serializable,
		Describable, NoteAttachable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6196936706046250433L;
	
	/** The type. */
	private String type;
	
	/** The acceptor id. */
	private int acceptorId;
	
	/** The created date. */
	private Date createdDate;
	
	/** The estimated hours. */
	private double estimatedHours;
	
	/** The original estimate. */
	private double originalEstimate;
	
	/** The completed. */
	private boolean completed;
	
	/** The disposition. */
	private char disposition = TaskDisposition.PLANNED.getCode();
	
	/** The time entries. */
	private List<TimeEntry> timeEntries = new ArrayList<TimeEntry>();
	
	/** The user story. */
	private UserStory userStory;
	
	/** The postponed hours. */
	private double postponedHours;
	
	/** The Constant ADDED_ORIGINAL_HOURS. */
	public static final String ADDED_ORIGINAL_HOURS = Task
			.getValidProperty("addedOriginalHours");
	
	/** The Constant ESTIMATED_ORIGINAL_HOURS. */
	public static final String ESTIMATED_ORIGINAL_HOURS = Task
			.getValidProperty("estimatedOriginalHours");
	
	/** The Constant ITERATION_START_ESTIMATED_HOURS. */
	public static final String ITERATION_START_ESTIMATED_HOURS = Task
			.getValidProperty("iterationStartEstimatedHours");

	/**
     * Gets the valid property.
     *
     * @param property
     *            the property
     * @return the valid property
     */
	private static String getValidProperty(final String property) {
		return Task.getValidProperty(Task.class, property);
	}

	/**
     * Instantiates a new task.
     */
	public Task() {
	}

	/**
     * Gets the type.
     *
     * @return the type
     */
	@Column(name = "type")
	public String getType() {
		return this.type;
	}

	/**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
	public void setType(final String type) {
		this.type = type;
	}

	/**
     * Gets the acceptor id.
     *
     * @return the acceptor id
     */
	@Column(name = "acceptor_id")
	public int getAcceptorId() {
		return this.acceptorId;
	}

	/**
     * Sets the acceptor id.
     *
     * @param acceptorId
     *            the new acceptor id
     */
	public void setAcceptorId(final int acceptorId) {
		this.acceptorId = acceptorId;
	}

	/**
     * Gets the created date.
     *
     * @return the created date
     */
	@Temporal(TemporalType.DATE)
	@Column(name = "created_date", length = 10)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	/**
     * Sets the created date.
     *
     * @param createdDate
     *            the new created date
     */
	public void setCreatedDate(final Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
     * Gets the estimated hours.
     *
     * @return the estimated hours
     */
	@Column(name = "estimated_hours", precision = 22, scale = 0)
	public double getEstimatedHours() {
		return this.estimatedHours;
	}

	/**
     * Sets the estimated hours.
     *
     * @param estimatedHours
     *            the new estimated hours
     */
	public void setEstimatedHours(final double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	/**
     * Gets the original estimate.
     *
     * @return the original estimate
     */
	@Column(name = "original_estimate", precision = 22, scale = 0)
	public double getOriginalEstimate() {
		return this.originalEstimate;
	}

	/**
     * Sets the original estimate.
     *
     * @param originalEstimate
     *            the new original estimate
     */
	public void setOriginalEstimate(final double originalEstimate) {
		this.originalEstimate = originalEstimate;
	}

	/**
     * Gets the completed.
     *
     * @return the completed
     */
	@Column(name = "is_complete")
	public boolean getCompleted() {
		return this.completed;
	}

	/**
     * Sets the completed.
     *
     * @param isComplete
     *            the new completed
     */
	public void setCompleted(final boolean isComplete) {
		this.completed = isComplete;
	}

	/**
     * Gets the disposition.
     *
     * @return the disposition
     */
	@Column(name = "disposition", nullable = false, length = 1)
	public char getDisposition() {
		return this.disposition;
	}

	/**
     * Sets the disposition.
     *
     * @param disposition
     *            the new disposition
     */
	public void setDisposition(final char disposition) {
		this.disposition = disposition;
	}

	/**
     * Gets the time entries.
     *
     * @return the time entries
     */
	@OrderBy("reportDate")
	@OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE)
	public List<TimeEntry> getTimeEntries() {
		return this.timeEntries;
	}

	/**
     * Sets the time entries.
     *
     * @param timeEntries
     *            the new time entries
     */
	public void setTimeEntries(final List<TimeEntry> timeEntries) {
		this.timeEntries = timeEntries;
	}

	/**
     * Gets the user story.
     *
     * @return the user story
     */
	@ManyToOne
	@JoinColumn(name = "story_id")
	public UserStory getUserStory() {
		return this.userStory;
	}

	/**
     * Sets the user story.
     *
     * @param userStory
     *            the new user story
     */
	public void setUserStory(final UserStory userStory) {
		this.userStory = userStory;
	}

	/**
     * Gets the actual hours.
     *
     * @return the actual hours
     */
	@Deprecated
	@Transient
	public double getActualHours() {
		double actualHours = 0.0;
		if (this.timeEntries != null && this.timeEntries.size() > 0) {
			for (final TimeEntry entry : this.timeEntries) {
				actualHours += entry.getEffort();
			}
		}
		return actualHours;
	}

	/**
     * Gets the estimated original hours.
     *
     * @return the estimated original hours
     */
	@Deprecated
	@Transient
	public double getEstimatedOriginalHours() {
		if (this.isStarted()) {
			return this.getOriginalEstimate();
		}
		return this.getEstimatedHours();
	}

	/**
     * Checks if is started.
     *
     * @return true, if is started
     */
	@Transient
	public boolean isStarted() {
		return this.originalEstimate > 0;
	}

	/**
     * Gets the iteration start estimated hours.
     *
     * @return the iteration start estimated hours
     */
	@Transient
	@Deprecated
	public double getIterationStartEstimatedHours() {
		final TaskDisposition taskDisposition = TaskDisposition
				.fromCode(this.disposition);
		if (!taskDisposition.isOriginal()) {
			return 0;
		}
		return this.getEstimatedOriginalHours();
	}

	/**
     * Gets the adjusted estimated hours.
     *
     * @return the adjusted estimated hours
     */
	@Transient
	@Deprecated
	public double getAdjustedEstimatedHours() {
		if (this.isCompleted()) {
			return this.getActualHours();
		}
		return Math.max(this.getEstimatedHours(), this.getActualHours());
	}

	/**
     * Checks if is completed.
     *
     * @return true, if is completed
     */
	@Transient
	public boolean isCompleted() {
		return this.completed;
	}

	/**
     * Gets the completed original hours.
     *
     * @return the completed original hours
     */
	@Transient
	@Deprecated
	public double getCompletedOriginalHours() {
		return this.isCompleted() ? this.getEstimatedOriginalHours() : 0;
	}

	/**
     * Gets the completed hours.
     *
     * @return the completed hours
     */
	@Transient
	@Deprecated
	public double getCompletedHours() {
		return this.isCompleted() ? this.getActualHours() : 0;
	}

	/**
     * Gets the valid property.
     *
     * @param beanClass
     *            the bean class
     * @param property
     *            the property
     * @return the valid property
     */
	@Deprecated
	protected static String getValidProperty(final Class beanClass,
			final String property) {
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(beanClass);
		} catch (final IntrospectionException e) {
			throw new RuntimeException("could not introspect " + beanClass, e);
		}
		final PropertyDescriptor[] properties = beanInfo
				.getPropertyDescriptors();
		boolean found = false;
		for (int i = 0; i < properties.length; i++) {
			if (properties[i].getName().equals(property)) {
				found = true;
				break;
			}
		}
		if (!found) {
			throw new RuntimeException("Could not find property " + property
					+ " in " + beanClass);
		}

		return property;

	}

	/**
     * Gets the remaining hours.
     *
     * @return the remaining hours
     */
	@Transient
	@Deprecated
	public double getRemainingHours() {
		return this.isCompleted() ? 0.0 : Math.max(this.getEstimatedHours()
				- this.getActualHours() - this.getPostponedHours(), 0.0);
	}

	/**
     * Gets the postponed hours.
     *
     * @return the postponed hours
     */
	@Transient
	@Deprecated
	public double getPostponedHours() {
		return this.postponedHours;
	}

	/**
     * Sets the postponed hours.
     *
     * @param postponedHours
     *            the new postponed hours
     */
	public void setPostponedHours(final double postponedHours) {
		this.postponedHours = postponedHours;
	}

	/**
     * Postpone.
     */
	@Transient
	@Deprecated
	public void postpone() {
		this.postponeRemainingHours();
		this.setCompleted(false);
	}

	/**
     * Postpone remaining hours.
     */
	@Transient
	public void postponeRemainingHours() {
		this.setPostponedHours(this.getRemainingHours());
	}

	/**
     * Sets the disposition.
     *
     * @param taskDisposition
     *            the new disposition
     */
	public void setDisposition(final TaskDisposition taskDisposition) {
		this.setDisposition(taskDisposition.getCode());
	}

	/**
     * Gets the disposition name.
     *
     * @return the disposition name
     */
	@Transient
	public String getDispositionName() {
		return TaskDisposition.fromCode(this.disposition).getName();
	}

	/**
     * Sets the estimated original hours.
     *
     * @param d
     *            the new estimated original hours
     */
	@Deprecated
	public void setEstimatedOriginalHours(final double d) {
		this.setOriginalEstimate(d);
	}

	/**
     * Sets the estimated original hours field.
     *
     * @param d
     *            the new estimated original hours field
     */
	public void setEstimatedOriginalHoursField(final double d) {
		this.setOriginalEstimate(d);
	}

	/**
     * Start.
     */
	public void start() {
		if (!this.isStarted()) {
			this.setEstimatedOriginalHours(this.getEstimatedHours());
		}
	}

	/**
     * Checks if is currently active.
     *
     * @param personId
     *            the person id
     * @return true, if is currently active
     */
	public boolean isCurrentlyActive(final int personId) {
		if (this.timeEntries != null && this.timeEntries.size() > 0) {
			final Iterator itr = this.timeEntries.iterator();
			while (itr.hasNext()) {
				final TimeEntry entry = (TimeEntry) itr.next();
				if (entry.isCurrentlyActive(personId)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
     * Gets the estimated original hours field.
     *
     * @return the estimated original hours field
     */
	@Transient
	public double getEstimatedOriginalHoursField() {
		return this.getOriginalEstimate();
	}

	/**
     * Gets the status.
     *
     * @return the status
     */
	@Transient
	public TaskStatus getStatus() {
		TaskStatus status;
		if (this.isCompleted()) {
			status = TaskStatus.COMPLETED;
		} else if (this.getActualHours() > 0.0) {
			status = TaskStatus.STARTED;
		} else {
			status = TaskStatus.NON_STARTED;
		}
		return status;
	}

	/**
     * Gets the new status.
     *
     * @return the new status
     */
	@Transient
	public net.sf.xplanner.domain.enums.TaskStatus getNewStatus() {
		if (this.isCompleted()) {
			return net.sf.xplanner.domain.enums.TaskStatus.COMPLETED;
		} else if (this.isStarted()) {
			return net.sf.xplanner.domain.enums.TaskStatus.STARTED;
		}
		return net.sf.xplanner.domain.enums.TaskStatus.NON_STARTED;
	}

	/**
     * Gets the estimated hours based on actuals.
     *
     * @return the estimated hours based on actuals
     */
	@Transient
	public double getEstimatedHoursBasedOnActuals() {
		return this.getActualHours() + this.getRemainingHours();
	}

	/**
     * Gets the added hours.
     *
     * @return the added hours
     */
	@Transient
	public double getAddedHours() {
		return this.isAdded() ? this.getEstimatedHours() : 0;
	}

	/**
     * Checks if is added.
     *
     * @return true, if is added
     */
	@Transient
	private boolean isAdded() {
		return this.getDisposition() == TaskDisposition.ADDED.getCode();
	}

	/**
     * Gets the disposition name key.
     *
     * @return the disposition name key
     */
	@Transient
	public String getDispositionNameKey() {
		return TaskDisposition.fromCode(this.disposition).getNameKey();
	}

	/**
     * Gets the added original hours.
     *
     * @return the added original hours
     */
	@Transient
	public double getAddedOriginalHours() {
		return TaskDisposition.fromCode(this.getDisposition()).isOriginal() ? 0
				: this.getEstimatedOriginalHours();
	}

	/**
     * Gets the overestimated original hours.
     *
     * @return the overestimated original hours
     */
	@Transient
	@Deprecated
	public double getOverestimatedOriginalHours() {
		if (this.isOverestimated(this.getEstimatedOriginalHours())) {
			return this.getEstimatedOriginalHours() - this.getActualHours();
		}
		return 0.0;
	}

	/**
     * Checks if is overestimated.
     *
     * @param estimatedHours
     *            the estimated hours
     * @return true, if is overestimated
     */
	private boolean isOverestimated(final double estimatedHours) {
		return this.isCompleted() && this.getActualHours() < estimatedHours;
	}

	/**
     * Gets the underestimated original hours.
     *
     * @return the underestimated original hours
     */
	@Transient
	public double getUnderestimatedOriginalHours() {
		return this.isUnderestimated(this.getEstimatedOriginalHours()) ? this
				.getActualHours() - this.getEstimatedOriginalHours() : 0.0;
	}

	/**
     * Checks if is underestimated.
     *
     * @param estimatedHours
     *            the estimated hours
     * @return true, if is underestimated
     */
	private boolean isUnderestimated(final double estimatedHours) {
		return this.getActualHours() > estimatedHours;
	}

	/**
     * Gets the completed remaining hours.
     *
     * @return the completed remaining hours
     */
	@Transient
	@Deprecated
	public double getCompletedRemainingHours() {
		return this.isCompleted() ? 0.0 : this.getEstimatedOriginalHours();
	}

	/**
     * Gets the overestimated hours.
     *
     * @return the overestimated hours
     */
	@Transient
	@Deprecated
	public double getOverestimatedHours() {
		return this.isOverestimated(this.getEstimatedHours()) ? this
				.getEstimatedHours() - this.getActualHours() : 0.0;
	}

	/**
     * Gets the underestimated hours.
     *
     * @return the underestimated hours
     */
	@Transient
	public double getUnderestimatedHours() {
		if (this.isDiscovered()) {
			final double result = this.isCompleted() ? 0.0 : Math.max(
					this.getEstimatedHours() - this.getActualHours(), 0.0);
			return this.getActualHours() + result;
		}
		return this.isUnderestimated(this.getEstimatedHours()) ? this
				.getActualHours() - this.getEstimatedHours() : 0.0;
	}

	/**
     * Checks if is discovered.
     *
     * @return true, if is discovered
     */
	@Transient
	private boolean isDiscovered() {
		return TaskDisposition.DISCOVERED.getCode() == this.getDisposition();
	}

}
