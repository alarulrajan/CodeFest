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

// TODO management of original/current estimate should be done through a status
public class Task2 extends NamedObject implements Nameable, NoteAttachable,
		Describable {
	// ------------------------------ FIELDS ------------------------------

	private int acceptorId;
	private int completionFlag;
	private String type;
	private TaskDisposition disposition = TaskDisposition.PLANNED;
	private double estimatedOriginalHours;
	private double estimatedHours;
	private double postponedHours;
	private Date createdDate;
	private UserStory story;
	private Collection timeEntries = new HashSet();
	public static final String ADDED_ORIGINAL_HOURS = Task2
			.getValidProperty("addedOriginalHours");
	public static final String ESTIMATED_ORIGINAL_HOURS = Task2
			.getValidProperty("estimatedOriginalHours");
	public static final String ITERATION_START_ESTIMATED_HOURS = Task2
			.getValidProperty("iterationStartEstimatedHours");

	private static String getValidProperty(final String property) {
		return Task2.getValidProperty(Task.class, property);
	}

	// --------------------- GETTER / SETTER METHODS ---------------------

	public int getAcceptorId() {
		return this.acceptorId;
	}

	public void setAcceptorId(final int acceptorId) {
		this.acceptorId = acceptorId;
	}

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

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(final java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	public TaskDisposition getDisposition() {
		return this.disposition;
	}

	public void setDisposition(final TaskDisposition disposition) {
		this.disposition = disposition;
	}

	public double getEstimatedHours() {
		return this.estimatedHours;
	}

	public void setEstimatedHours(final double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

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

	public boolean isCompleted() {
		return this.completionFlag == 1;
	}

	public UserStory getUserStory() {
		return this.story;
	}

	// TODO: add management of the inverse relationship tasks
	public void setStory(final UserStory story) {
		this.story = story;
	}

	public Collection getTimeEntries() {
		return this.timeEntries;
	}

	public void setTimeEntries(final Collection timeEntries) {
		this.timeEntries = timeEntries;
	}

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public void setEstimatedOriginalHours(final double estimatedOriginalHours) {
		this.estimatedOriginalHours = estimatedOriginalHours;
	}

	// ------------------------ CANONICAL METHODS ------------------------

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

	public String getDispositionName() {
		return this.disposition != null ? this.disposition.getName() : null;
	}

	public void setDispositionName(final String dispositionName) {
		this.disposition = TaskDisposition.fromName(dispositionName);
	}

	public double getAddedHours() {
		return this.isAdded() ? this.getEstimatedHours() : 0;
	}

	private boolean isAdded() {
		return this.getDisposition().equals(TaskDisposition.ADDED);
	}

	public double getAdjustedEstimatedHours() {
		if (this.isCompleted()) {
			return this.getActualHours();
		} else {
			return Math.max(this.getEstimatedHours(), this.getActualHours());
		}
	}

	public double getCompletedHours() {
		return this.isCompleted() ? this.getActualHours() : 0;
	}

	public double getEstimatedHoursBasedOnActuals() {
		return this.getActualHours() + this.getRemainingHours();
	}

	public double getCompletedRemainingHours() {
		return this.isCompleted() ? 0.0 : this.getEstimatedOriginalHours();
	}

	public double getRemainingHours() {
		return this.isCompleted() ? 0.0 : Math.max(this.getEstimatedHours()
				- this.getActualHours() - this.getPostponedHours(), 0.0);
	}

	public double getAddedOriginalHours() {
		return this.getDisposition().isOriginal() ? 0 : this
				.getEstimatedOriginalHours();
	}

	public double getEstimatedOriginalHours() {
		if (this.isStarted()) {
			return this.estimatedOriginalHours;
		} else {
			return this.getEstimatedHours();
		}
	}

	public void setEstimatedOriginalHoursField(
			final double estimatedOriginalHours) {
		this.estimatedOriginalHours = estimatedOriginalHours;
	}

	public double getEstimatedOriginalHoursField() {
		return this.estimatedOriginalHours;
	}

	public double getCompletedOriginalHours() {
		return this.isCompleted() ? this.getEstimatedOriginalHours() : 0;
	}

	public double getOverestimatedOriginalHours() {
		if (this.isOverestimated(this.getEstimatedOriginalHours())) {
			return this.getEstimatedOriginalHours() - this.getActualHours();
		}
		return 0.0;
	}

	private boolean isOverestimated(final double estimatedHours) {
		return this.isCompleted() && this.getActualHours() < estimatedHours;
	}

	public double getUnderestimatedOriginalHours() {
		return this.isUnderestimated(this.getEstimatedOriginalHours()) ? this
				.getActualHours() - this.getEstimatedOriginalHours() : 0.0;
	}

	private boolean isUnderestimated(final double estimatedHours) {
		return this.getActualHours() > estimatedHours;
	}

	public double getOverestimatedHours() {
		return this.isOverestimated(this.getEstimatedHours()) ? this
				.getEstimatedHours() - this.getActualHours() : 0.0;
	}

	public double getUnderestimatedHours() {
		if (this.isDiscovered()) {
			final double result = this.isCompleted() ? 0.0 : Math.max(
					this.getEstimatedHours() - this.getActualHours(), 0.0);
			return this.getActualHours() + result;
		}
		return this.isUnderestimated(this.getEstimatedHours()) ? this
				.getActualHours() - this.getEstimatedHours() : 0.0;
	}

	public double getIterationStartEstimatedHours() {
		if (!this.disposition.isOriginal()) {
			return 0;
		}
		return this.getEstimatedOriginalHours();
	}

	private boolean isDiscovered() {
		return TaskDisposition.DISCOVERED.equals(this.getDisposition());
	}

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

	public void setCompleted(final boolean flag) {
		this.completionFlag = flag ? 1 : 0;
	}

	public double getPostponedHours() {
		return this.postponedHours;
	}

	public void setPostponedHours(final double postponedHours) {
		this.postponedHours = postponedHours;
	}

	public void postponeRemainingHours() {
		this.setPostponedHours(this.getRemainingHours());
	}

	public void postpone() {
		this.postponeRemainingHours();
		this.setCompleted(false);
	}

	public void start() {
		if (!this.isStarted()) {
			this.setEstimatedOriginalHours(this.getEstimatedHours());
		}
	}

	public boolean isStarted() {
		return this.estimatedOriginalHours > 0;
	}

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