package com.technoetic.xplanner.domain;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import net.sf.xplanner.domain.NamedObject;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

/**
 * The Class Task2.
 */
// ChangeSoon management of original/current estimate should be done through a status
public class Task2 extends NamedObject implements Nameable, NoteAttachable,
		Describable {
	// ------------------------------ FIELDS ------------------------------

	/** The acceptor id. */
	private int acceptorId;
	
	/** The completion flag. */
	private int completionFlag;
	
	/** The type. */
	private String type;
	
	/** The disposition. */
	private TaskDisposition disposition = TaskDisposition.PLANNED;
	
	/** The estimated original hours. */
	private double estimatedOriginalHours;
	
	/** The estimated hours. */
	private double estimatedHours;
	
	/** The postponed hours. */
	private double postponedHours;
	
	/** The created date. */
	private Date createdDate;
	
	/** The story. */
	private UserStory story;
	
	/** The time entries. */
	private Collection timeEntries = new HashSet();
	
	/** The Constant ADDED_ORIGINAL_HOURS. */
	public static final String ADDED_ORIGINAL_HOURS = Task2
			.getValidProperty("addedOriginalHours");
	
	/** The Constant ESTIMATED_ORIGINAL_HOURS. */
	public static final String ESTIMATED_ORIGINAL_HOURS = Task2
			.getValidProperty("estimatedOriginalHours");
	
	/** The Constant ITERATION_START_ESTIMATED_HOURS. */
	public static final String ITERATION_START_ESTIMATED_HOURS = Task2
			.getValidProperty("iterationStartEstimatedHours");

	/**
     * Gets the valid property.
     *
     * @param property
     *            the property
     * @return the valid property
     */
	private static String getValidProperty(final String property) {
		return Task2.getValidProperty(Task.class, property);
	}

	// --------------------- GETTER / SETTER METHODS ---------------------

	/**
     * Gets the acceptor id.
     *
     * @return the acceptor id
     */
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
     * Gets the actual hours.
     *
     * @return the actual hours
     */
	public double getActualHours() {
		double actualHours = 0.0;
		if (this.timeEntries != null && this.timeEntries.size() > 0) {
			final Iterator itr = this.timeEntries.iterator();
			while (itr.hasNext()) {
				final TimeEntry2 entry = (TimeEntry2) itr.next();
				actualHours += entry.getEffort();
			}
		}
		return actualHours;
	}

	/**
     * Gets the created date.
     *
     * @return the created date
     */
	public Date getCreatedDate() {
		return this.createdDate;
	}

	/**
     * Sets the created date.
     *
     * @param createdDate
     *            the new created date
     */
	public void setCreatedDate(final java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
     * Gets the disposition.
     *
     * @return the disposition
     */
	public TaskDisposition getDisposition() {
		return this.disposition;
	}

	/**
     * Sets the disposition.
     *
     * @param disposition
     *            the new disposition
     */
	public void setDisposition(final TaskDisposition disposition) {
		this.disposition = disposition;
	}

	/**
     * Gets the estimated hours.
     *
     * @return the estimated hours
     */
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
     * Gets the status.
     *
     * @return the status
     */
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
     * Checks if is completed.
     *
     * @return true, if is completed
     */
	public boolean isCompleted() {
		return this.completionFlag == 1;
	}

	/**
     * Gets the user story.
     *
     * @return the user story
     */
	public UserStory getUserStory() {
		return this.story;
	}

	/**
     * Sets the story.
     *
     * @param story
     *            the new story
     */
	// ChangeSoon: add management of the inverse relationship tasks
	public void setStory(final UserStory story) {
		this.story = story;
	}

	/**
     * Gets the time entries.
     *
     * @return the time entries
     */
	public Collection getTimeEntries() {
		return this.timeEntries;
	}

	/**
     * Sets the time entries.
     *
     * @param timeEntries
     *            the new time entries
     */
	public void setTimeEntries(final Collection timeEntries) {
		this.timeEntries = timeEntries;
	}

	/**
     * Gets the type.
     *
     * @return the type
     */
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
     * Sets the estimated original hours.
     *
     * @param estimatedOriginalHours
     *            the new estimated original hours
     */
	public void setEstimatedOriginalHours(final double estimatedOriginalHours) {
		this.estimatedOriginalHours = estimatedOriginalHours;
	}

	// ------------------------ CANONICAL METHODS ------------------------

	/* (non-Javadoc)
	 * @see net.sf.xplanner.domain.NamedObject#toString()
	 */
	@Override
	public String toString() {
		return "Task(id="
				+ this.getId()
				+ ", userStoryId="
				+ (this.getUserStory() == null ? "null" : ""
						+ this.getUserStory().getId()) + ", name="
				+ this.getName() + ", acceptorId=" + this.getAcceptorId()
				+ ", createDate=" + this.getCreatedDate() + ", desc="
				+ this.getDescription() + ", title=" + this.getName();
	}

	// -------------------------- OTHER METHODS --------------------------

	/**
     * Gets the disposition name.
     *
     * @return the disposition name
     */
	public String getDispositionName() {
		return this.disposition != null ? this.disposition.getName() : null;
	}

	/**
     * Sets the disposition name.
     *
     * @param dispositionName
     *            the new disposition name
     */
	public void setDispositionName(final String dispositionName) {
		this.disposition = TaskDisposition.fromName(dispositionName);
	}

	/**
     * Gets the added hours.
     *
     * @return the added hours
     */
	public double getAddedHours() {
		return this.isAdded() ? this.getEstimatedHours() : 0;
	}

	/**
     * Checks if is added.
     *
     * @return true, if is added
     */
	private boolean isAdded() {
		return this.getDisposition().equals(TaskDisposition.ADDED);
	}

	/**
     * Gets the adjusted estimated hours.
     *
     * @return the adjusted estimated hours
     */
	public double getAdjustedEstimatedHours() {
		if (this.isCompleted()) {
			return this.getActualHours();
		} else {
			return Math.max(this.getEstimatedHours(), this.getActualHours());
		}
	}

	/**
     * Gets the completed hours.
     *
     * @return the completed hours
     */
	public double getCompletedHours() {
		return this.isCompleted() ? this.getActualHours() : 0;
	}

	/**
     * Gets the estimated hours based on actuals.
     *
     * @return the estimated hours based on actuals
     */
	public double getEstimatedHoursBasedOnActuals() {
		return this.getActualHours() + this.getRemainingHours();
	}

	/**
     * Gets the completed remaining hours.
     *
     * @return the completed remaining hours
     */
	public double getCompletedRemainingHours() {
		return this.isCompleted() ? 0.0 : this.getEstimatedOriginalHours();
	}

	/**
     * Gets the remaining hours.
     *
     * @return the remaining hours
     */
	public double getRemainingHours() {
		return this.isCompleted() ? 0.0 : Math.max(this.getEstimatedHours()
				- this.getActualHours() - this.getPostponedHours(), 0.0);
	}

	/**
     * Gets the added original hours.
     *
     * @return the added original hours
     */
	public double getAddedOriginalHours() {
		return this.getDisposition().isOriginal() ? 0 : this
				.getEstimatedOriginalHours();
	}

	/**
     * Gets the estimated original hours.
     *
     * @return the estimated original hours
     */
	public double getEstimatedOriginalHours() {
		if (this.isStarted()) {
			return this.estimatedOriginalHours;
		} else {
			return this.getEstimatedHours();
		}
	}

	/**
     * Sets the estimated original hours field.
     *
     * @param estimatedOriginalHours
     *            the new estimated original hours field
     */
	public void setEstimatedOriginalHoursField(
			final double estimatedOriginalHours) {
		this.estimatedOriginalHours = estimatedOriginalHours;
	}

	/**
     * Gets the estimated original hours field.
     *
     * @return the estimated original hours field
     */
	public double getEstimatedOriginalHoursField() {
		return this.estimatedOriginalHours;
	}

	/**
     * Gets the completed original hours.
     *
     * @return the completed original hours
     */
	public double getCompletedOriginalHours() {
		return this.isCompleted() ? this.getEstimatedOriginalHours() : 0;
	}

	/**
     * Gets the overestimated original hours.
     *
     * @return the overestimated original hours
     */
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
     * Gets the overestimated hours.
     *
     * @return the overestimated hours
     */
	public double getOverestimatedHours() {
		return this.isOverestimated(this.getEstimatedHours()) ? this
				.getEstimatedHours() - this.getActualHours() : 0.0;
	}

	/**
     * Gets the underestimated hours.
     *
     * @return the underestimated hours
     */
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
     * Gets the iteration start estimated hours.
     *
     * @return the iteration start estimated hours
     */
	public double getIterationStartEstimatedHours() {
		if (!this.disposition.isOriginal()) {
			return 0;
		}
		return this.getEstimatedOriginalHours();
	}

	/**
     * Checks if is discovered.
     *
     * @return true, if is discovered
     */
	private boolean isDiscovered() {
		return TaskDisposition.DISCOVERED.equals(this.getDisposition());
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
				final TimeEntry2 entry = (TimeEntry2) itr.next();
				if (entry.isCurrentlyActive(personId)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
     * Sets the completed.
     *
     * @param flag
     *            the new completed
     */
	public void setCompleted(final boolean flag) {
		this.completionFlag = flag ? 1 : 0;
	}

	/**
     * Gets the postponed hours.
     *
     * @return the postponed hours
     */
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
     * Postpone remaining hours.
     */
	public void postponeRemainingHours() {
		this.setPostponedHours(this.getRemainingHours());
	}

	/**
     * Postpone.
     */
	public void postpone() {
		this.postponeRemainingHours();
		this.setCompleted(false);
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
     * Checks if is started.
     *
     * @return true, if is started
     */
	public boolean isStarted() {
		return this.estimatedOriginalHours > 0;
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
}