package net.sf.xplanner.domain;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.technoetic.xplanner.domain.Describable;
import com.technoetic.xplanner.domain.Feature;
import com.technoetic.xplanner.domain.NoteAttachable;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.StoryStatus;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.util.CollectionUtils;
import com.technoetic.xplanner.util.CollectionUtils.DoubleFilter;
import com.technoetic.xplanner.util.CollectionUtils.DoublePropertyFilter;

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
@Table(name = "story")
public class UserStory extends NamedObject implements java.io.Serializable,
		NoteAttachable, Describable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5603178238283216187L;
	
	/** The iteration. */
	private Iteration iteration;
	
	/** The tracker id. */
	private int trackerId;
	
	/** The estimated hours field. */
	private double estimatedHoursField;
	
	/** The priority. */
	private int priority;
	
	/** The customer. */
	private Person customer;
	
	/** The status. */
	private char status = StoryStatus.DRAFT.getCode();
	
	/** The original estimated hours. */
	private Double originalEstimatedHours;
	
	/** The disposition code. */
	private char dispositionCode = StoryDisposition.PLANNED.getCode();
	
	/** The postponed hours. */
	private double postponedHours;
	
	/** The iteration start estimated hours. */
	private double iterationStartEstimatedHours;
	
	/** The order no. */
	private int orderNo;
	
	/** The actual hours. */
	private double actualHours;
	
	/** The tasks. */
	private List<Task> tasks = new ArrayList<Task>();
	
	/** The previous order no. */
	private int previousOrderNo;

	/** The Constant ITERATION_START_ESTIMATED_HOURS. */
	public static final String ITERATION_START_ESTIMATED_HOURS = UserStory
			.getValidProperty("iterationStartEstimatedHours");
	
	/** The Constant TASK_BASED_ESTIMATED_ORIGINAL_HOURS. */
	public static final String TASK_BASED_ESTIMATED_ORIGINAL_HOURS = UserStory
			.getValidProperty("taskBasedEstimatedOriginalHours");
	
	/** The Constant TASK_BASED_COMPLETED_ORIGINAL_HOURS. */
	public static final String TASK_BASED_COMPLETED_ORIGINAL_HOURS = UserStory
			.getValidProperty("taskBasedCompletedOriginalHours");
	
	/** The Constant TASK_BASED_ADDED_ORIGINAL_HOURS. */
	public static final String TASK_BASED_ADDED_ORIGINAL_HOURS = UserStory
			.getValidProperty("taskBasedAddedOriginalHours");
	// public static final String TASK_BASED_REMAINING_ORIGINAL_HOURS =
	// getValidProperty("taskBasedRemainingOriginalHours");
	// public static final String TASK_BASED_POSTPONED_ORIGINAL_HOURS =
	/** The Constant TASK_BASED_OVERESTIMATED_ORIGINAL_HOURS. */
	// getValidProperty("taskBasedPostponedOriginalHours");
	public static final String TASK_BASED_OVERESTIMATED_ORIGINAL_HOURS = UserStory
			.getValidProperty("taskBasedOverestimatedOriginalHours");
	
	/** The Constant TASK_BASED_UNDERESTIMATED_ORIGINAL_HOURS. */
	public static final String TASK_BASED_UNDERESTIMATED_ORIGINAL_HOURS = UserStory
			.getValidProperty("taskBasedUnderestimatedOriginalHours");
	
	/** The Constant TASK_BASED_ESTIMATED_HOURS. */
	public static final String TASK_BASED_ESTIMATED_HOURS = UserStory
			.getValidProperty("estimatedHours");
	
	/** The Constant TASK_BASED_ADJUSTED_ESTIMATED_HOURS. */
	public static final String TASK_BASED_ADJUSTED_ESTIMATED_HOURS = UserStory
			.getValidProperty("adjustedEstimatedHours");
	
	/** The Constant TASK_BASED_COMPLETED_HOURS. */
	public static final String TASK_BASED_COMPLETED_HOURS = UserStory
			.getValidProperty("completedTaskHours");
	
	/** The Constant CACHED_TASK_BASED_ACTUAL_HOURS. */
	public static final String CACHED_TASK_BASED_ACTUAL_HOURS = UserStory
			.getValidProperty("cachedActualHours");
	
	/** The Constant TASK_BASED_ACTUAL_HOURS. */
	public static final String TASK_BASED_ACTUAL_HOURS = UserStory
			.getValidProperty("actualHours");
	
	/** The Constant TASK_BASED_ADDED_HOURS. */
	public static final String TASK_BASED_ADDED_HOURS = UserStory
			.getValidProperty("estimatedHoursOfAddedTasks");
	
	/** The Constant TASK_BASED_POSTPONED_HOURS. */
	public static final String TASK_BASED_POSTPONED_HOURS = UserStory
			.getValidProperty("postponedHours");
	
	/** The Constant TASK_BASED_REMAINING_HOURS. */
	public static final String TASK_BASED_REMAINING_HOURS = UserStory
			.getValidProperty("taskBasedRemainingHours");
	
	/** The Constant TASK_BASED_COMPLETED_REMAINING_HOURS. */
	public static final String TASK_BASED_COMPLETED_REMAINING_HOURS = UserStory
			.getValidProperty("taskBasedCompletedRemainingHours");
	
	/** The Constant TASK_BASED_OVERESTIMATED_HOURS. */
	public static final String TASK_BASED_OVERESTIMATED_HOURS = UserStory
			.getValidProperty("overestimatedHours");
	
	/** The Constant TASK_BASED_UNDERESTIMATED_HOURS. */
	public static final String TASK_BASED_UNDERESTIMATED_HOURS = UserStory
			.getValidProperty("underestimatedHours");
	
	/** The Constant ADJUSTED_ESTIMATED_HOURS. */
	public static final String ADJUSTED_ESTIMATED_HOURS = UserStory
			.getValidProperty("adjustedEstimatedHours");
	
	/** The Constant ESTIMATED_HOURS. */
	public static final String ESTIMATED_HOURS = UserStory
			.getValidProperty("estimatedHoursField");
	
	/** The Constant STORY_ESTIMATED_HOURS. */
	public static final String STORY_ESTIMATED_HOURS = UserStory
			.getValidProperty("totalHours");
	
	/** The Constant REMAINING_HOURS. */
	public static final String REMAINING_HOURS = UserStory
			.getValidProperty("remainingHours");
	
	/** The Constant COMPLETED_HOURS. */
	public static final String COMPLETED_HOURS = UserStory
			.getValidProperty("completedHours");
	
	/** The Constant TOTAL_HOURS. */
	public static final String TOTAL_HOURS = UserStory
			.getValidProperty("totalHours");
	
	/** The Constant TASK_BASED_TOTAL_HOURS. */
	public static final String TASK_BASED_TOTAL_HOURS = UserStory
			.getValidProperty("taskBasedTotalHours");
	
	/** The Constant STORY_ESTIMATED_ORIGINAL_HOURS. */
	public static final String STORY_ESTIMATED_ORIGINAL_HOURS = UserStory
			.getValidProperty("nonTaskBasedOriginalHours");
	
	/** The Constant TASK_ESTIMATED_ORIGINAL_HOURS. */
	public static final String TASK_ESTIMATED_ORIGINAL_HOURS = UserStory
			.getValidProperty("taskEstimatedOriginalHours");
	
	/** The Constant TASK_BASED_ORIGINAL_HOURS. */
	public static final String TASK_BASED_ORIGINAL_HOURS = UserStory
			.getValidProperty("taskBasedOriginalHours");
	
	/** The Constant TASK_ESTIMATED_HOURS_IF_STORY_ADDED. */
	public static final String TASK_ESTIMATED_HOURS_IF_STORY_ADDED = UserStory
			.getValidProperty("taskEstimatedHoursIfStoryAdded");
	
	/** The Constant TASK_ESTIMATED_HOURS_IF_ORIGINAL_STORY. */
	public static final String TASK_ESTIMATED_HOURS_IF_ORIGINAL_STORY = UserStory
			.getValidProperty("taskEstimatedHoursIfOriginalStory");
	
	/** The Constant STORY_ESTIMATED_HOURS_IF_STORY_ADDED. */
	public static final String STORY_ESTIMATED_HOURS_IF_STORY_ADDED = UserStory
			.getValidProperty("storyEstimatedHoursIfStoryAdded");
	
	/** The Constant POSTPONED_STORY_HOURS. */
	public static final String POSTPONED_STORY_HOURS = UserStory
			.getValidProperty("postponedStoryHours");

	/**
     * Gets the valid property.
     *
     * @param property
     *            the property
     * @return the valid property
     */
	private static String getValidProperty(final String property) {
		return UserStory.getValidProperty(UserStory.class, property);
	}

	/**
     * Gets the iteration.
     *
     * @return the iteration
     */
	@ManyToOne
	@JoinColumn(name = "iteration_id")
	public Iteration getIteration() {
		return this.iteration;
	}

	/**
     * Sets the iteration.
     *
     * @param iteration
     *            the new iteration
     */
	public void setIteration(final Iteration iteration) {
		this.iteration = iteration;
	}

	/**
     * Gets the tracker id.
     *
     * @return the tracker id
     */
	@Column(name = "tracker_id")
	public int getTrackerId() {
		return this.trackerId;
	}

	/**
     * Sets the tracker id.
     *
     * @param trackerId
     *            the new tracker id
     */
	public void setTrackerId(final int trackerId) {
		this.trackerId = trackerId;
	}

	/**
     * Gets the estimated hours field.
     *
     * @return the estimated hours field
     */
	@Column(name = "estimated_hours", precision = 22, scale = 0)
	public double getEstimatedHoursField() {
		return this.estimatedHoursField;
	}

	/**
     * Sets the estimated hours field.
     *
     * @param estimatedHours
     *            the new estimated hours field
     */
	public void setEstimatedHoursField(final double estimatedHours) {
		this.estimatedHoursField = estimatedHours;
	}

	/**
     * Gets the estimated hours.
     *
     * @return the estimated hours
     */
	@Transient
	public double getEstimatedHours() {
		if (this.tasks.size() > 0) {
			return this.getTaskBasedEstimatedHours();
		}
		return this.getEstimatedHoursField();
	}

	/**
     * Gets the priority.
     *
     * @return the priority
     */
	@Column(name = "priority")
	public int getPriority() {
		return this.priority;
	}

	/**
     * Sets the priority.
     *
     * @param priority
     *            the new priority
     */
	public void setPriority(final int priority) {
		this.priority = priority;
	}

	/**
     * Gets the customer.
     *
     * @return the customer
     */
	@ManyToOne(cascade = CascadeType.REFRESH, optional = true)
	@JoinColumn(name = "customer_id")
	public Person getCustomer() {
		return this.customer;
	}

	/**
     * Sets the customer.
     *
     * @param person
     *            the new customer
     */
	public void setCustomer(final Person person) {
		this.customer = person;
	}

	/**
     * Gets the status.
     *
     * @return the status
     */
	@Column(name = "status", nullable = false, length = 1)
	public char getStatus() {
		return this.status;
	}

	/**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
	public void setStatus(final char status) {
		this.status = status;
	}

	/**
     * Gets the original estimated hours.
     *
     * @return the original estimated hours
     */
	@Column(name = "original_estimated_hours", precision = 22, scale = 0)
	public Double getOriginalEstimatedHours() {
		return this.originalEstimatedHours;
	}

	/**
     * Sets the original estimated hours.
     *
     * @param originalEstimatedHours
     *            the new original estimated hours
     */
	public void setOriginalEstimatedHours(final Double originalEstimatedHours) {
		this.originalEstimatedHours = originalEstimatedHours;
	}

	/**
     * Gets the disposition code.
     *
     * @return the disposition code
     */
	@Column(name = "disposition", nullable = false, length = 1)
	public char getDispositionCode() {
		return this.dispositionCode;
	}

	/**
     * Gets the disposition.
     *
     * @return the disposition
     */
	@Transient
	public StoryDisposition getDisposition() {
		return StoryDisposition.fromCode(this.dispositionCode);
	}

	/**
     * Sets the disposition code.
     *
     * @param disposition
     *            the new disposition code
     */
	public void setDispositionCode(final char disposition) {
		this.dispositionCode = disposition;
	}

	/**
     * Gets the postponed hours.
     *
     * @return the postponed hours
     */
	@Column(name = "postponed_hours", precision = 22, scale = 0)
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
     * Gets the it start estimated hours.
     *
     * @return the it start estimated hours
     */
	@Column(name = "it_start_estimated_hours", precision = 22, scale = 0)
	public double getItStartEstimatedHours() {
		return this.iterationStartEstimatedHours;
	}

	/**
     * Sets the it start estimated hours.
     *
     * @param itStartEstimatedHours
     *            the new it start estimated hours
     */
	public void setItStartEstimatedHours(final double itStartEstimatedHours) {
		this.iterationStartEstimatedHours = itStartEstimatedHours;
	}

	/**
     * Gets the order no.
     *
     * @return the order no
     */
	@Column(name = "orderNo")
	public int getOrderNo() {
		return this.orderNo;
	}

	/**
     * Sets the order no.
     *
     * @param orderNo
     *            the new order no
     */
	public void setOrderNo(final int orderNo) {
		this.previousOrderNo = this.orderNo;
		this.orderNo = orderNo;
	}

	/**
     * Gets the tasks.
     *
     * @return the tasks
     */
	@OneToMany(mappedBy = "userStory", cascade = CascadeType.REMOVE)
	public List<Task> getTasks() {
		return this.tasks;
	}

	/**
     * Sets the tasks.
     *
     * @param tasks
     *            the new tasks
     */
	public void setTasks(final List<Task> tasks) {
		this.tasks = tasks;
	}

	/**
     * Gets the cached actual hours.
     *
     * @return the cached actual hours
     */
	@Transient
	@Deprecated
	public double getCachedActualHours() {
		if (this.actualHours == 0) {
			this.actualHours = this.getActualHours();
		}
		return this.actualHours;
	}

	/**
     * Gets the actual hours.
     *
     * @return the actual hours
     */
	@Transient
	@Deprecated
	public double getActualHours() {
		return CollectionUtils.sum(this.getTasks(), new DoubleFilter() {
			@Override
			public double filter(final Object o) {
				return ((Task) o).getActualHours();
			}
		});
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
     * Gets the task based completed original hours.
     *
     * @return the task based completed original hours
     */
	@Transient
	@Deprecated
	public double getTaskBasedCompletedOriginalHours() {
		return CollectionUtils.sum(this.getTasks(), new DoubleFilter() {
			@Override
			public double filter(final Object o) {
				return ((Task) o).getCompletedOriginalHours();
			}
		});
	}

	/**
     * Gets the completed task hours.
     *
     * @return the completed task hours
     */
	@Transient
	@Deprecated
	public double getCompletedTaskHours() {
		return CollectionUtils.sum(this.getTasks(), new DoubleFilter() {
			@Override
			public double filter(final Object o) {
				return ((Task) o).getCompletedHours();
			}
		});
	}

	/**
     * Gets the task based estimated original hours.
     *
     * @return the task based estimated original hours
     */
	@Transient
	@Deprecated
	public double getTaskBasedEstimatedOriginalHours() {
		return CollectionUtils.sum(this.tasks, new DoublePropertyFilter(
				Task.ITERATION_START_ESTIMATED_HOURS));
	}

	/**
     * Gets the task based remaining hours.
     *
     * @return the task based remaining hours
     */
	@Transient
	@Deprecated
	public double getTaskBasedRemainingHours() {
		if (this.tasks.size() == 0) {
			return this.estimatedHoursField;
		}

		double remainingHours = 0;
		boolean isTaskEstimatePresent = false;
		final Iterator<Task> itr = this.tasks.iterator();
		while (itr.hasNext()) {
			final Task task = itr.next();
			remainingHours += task.getRemainingHours();
			isTaskEstimatePresent |= task.getEstimatedHours() > 0;
		}
		return isTaskEstimatePresent ? remainingHours
				: this.estimatedHoursField;
	}

	/**
     * Checks if is completed.
     *
     * @return true, if is completed
     */
	@Transient
	@Deprecated
	public boolean isCompleted() {
		if (this.tasks.size() > 0) {
			final Iterator<Task> itr = this.tasks.iterator();
			while (itr.hasNext()) {
				final Task task = itr.next();
				if (!task.isCompleted()) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
     * Gets the adjusted estimated hours.
     *
     * @return the adjusted estimated hours
     */
	@Transient
	@Deprecated
	public double getAdjustedEstimatedHours() {
		double adjustedEstimatedHours = 0;
		if (!this.tasks.isEmpty()) {
			final Iterator<Task> itr = this.tasks.iterator();
			while (itr.hasNext()) {
				final Task task = itr.next();
				adjustedEstimatedHours += task.getAdjustedEstimatedHours();
			}
			if (adjustedEstimatedHours == 0) {
				adjustedEstimatedHours = this.estimatedHoursField;
			}
		} else {
			adjustedEstimatedHours = this.getEstimatedHours();
		}
		return adjustedEstimatedHours;
	}

	/**
     * Checks if is started.
     *
     * @return true, if is started
     */
	@Transient
	public boolean isStarted() {
		return this.originalEstimatedHours != null;
	}

	/**
     * Gets the estimated original hours.
     *
     * @return the estimated original hours
     */
	@Transient
	public double getEstimatedOriginalHours() {
		if (this.isStarted()) {
			return this.originalEstimatedHours;
		}
		return this.getTaskBasedEstimatedOriginalHours();
	}

	/**
     * Gets the estimated original hours field.
     *
     * @return the estimated original hours field
     */
	@Transient
	public double getEstimatedOriginalHoursField() {
		return this.getOriginalEstimatedHours();
	}

	/**
     * Sets the estimated original hours.
     *
     * @param double1
     *            the new estimated original hours
     */
	@Deprecated
	public void setEstimatedOriginalHours(final double double1) {
		this.setOriginalEstimatedHours(double1);
	}

	/**
     * Sets the disposition.
     *
     * @param added
     *            the new disposition
     */
	@Deprecated
	public void setDisposition(final StoryDisposition added) {
		this.setDispositionCode(added.getCode());
	}

	/**
     * Start.
     */
	public void start() {
		if (!this.isStarted()) {
			this.setEstimatedOriginalHours(new Double(this
					.getTaskBasedEstimatedOriginalHours()));
		}
		for (final Iterator<Task> iterator = this.tasks.iterator(); iterator
				.hasNext();) {
			final Task task = iterator.next();
			task.start();
		}
	}

	/**
     * Sets the features.
     *
     * @param features
     *            the new features
     */
	// FIXME: implement
	public void setFeatures(final List features) {
		// ChangeSoon 

	}

	/**
     * Postpone remaining hours.
     */
	public void postponeRemainingHours() {
		this.setPostponedHours(this.getPostponedHours()
				+ this.getTaskBasedRemainingHours());
	}

	/**
     * Move to.
     *
     * @param targetIteration
     *            the target iteration
     */
	@Deprecated
	public void moveTo(final Iteration targetIteration) {
		this.setIteration(targetIteration);
		targetIteration.getUserStories().add(this);

		if (targetIteration.isActive() && !this.isStarted()) {
			this.start();
		}

		if (targetIteration.isActive()) {
			this.setDisposition(StoryDisposition.ADDED);
			this.setTasksDisposition(TaskDisposition.ADDED);
		} else {
			this.setDisposition(StoryDisposition.PLANNED);
			this.setTasksDisposition(TaskDisposition.PLANNED);
		}
	}

	/**
     * Sets the tasks disposition.
     *
     * @param disposition
     *            the new tasks disposition
     */
	@Deprecated
	private void setTasksDisposition(final TaskDisposition disposition) {
		final Iterator<Task> iterator = this.tasks.iterator();
		while (iterator.hasNext()) {
			final Task task = iterator.next();
			task.setDisposition(disposition);
		}
	}

	/**
     * Gets the task based estimated hours.
     *
     * @return the task based estimated hours
     */
	@Transient
	public double getTaskBasedEstimatedHours() {
		double taskBasedEstimatedHours = 0;
		final Iterator<Task> itr = this.tasks.iterator();
		while (itr.hasNext()) {
			final Task task = itr.next();
			taskBasedEstimatedHours += task.getEstimatedHoursBasedOnActuals();
		}
		return taskBasedEstimatedHours;
	}

	/**
     * Gets the previous order no.
     *
     * @return the previous order no
     */
	@Transient
	public int getPreviousOrderNo() {
		return this.previousOrderNo;
	}

	/**
     * Gets the features.
     *
     * @return the features
     */
	// FIXME: Implement
	@Transient
	public List<Feature> getFeatures() {
		return null;
	}

	/**
     * Gets the estimated hours of added tasks.
     *
     * @return the estimated hours of added tasks
     */
	@Transient
	public double getEstimatedHoursOfAddedTasks() {
		return CollectionUtils.sum(this.getTasks(), new DoubleFilter() {
			@Override
			public double filter(final Object o) {
				return ((Task) o).getAddedHours();
			}
		});
	}

	/**
     * Gets the iteration start estimated hours.
     *
     * @return the iteration start estimated hours
     */
	@Transient
	public double getIterationStartEstimatedHours() {
		return this.iterationStartEstimatedHours;
	}

	/**
     * Gets the task based added original hours.
     *
     * @return the task based added original hours
     */
	@Transient
	public double getTaskBasedAddedOriginalHours() {
		return CollectionUtils.sum(this.getTasks(), new DoubleFilter() {
			@Override
			public double filter(final Object o) {
				return ((Task) o).getAddedOriginalHours();
			}
		});
	}

	/**
     * Gets the task based overestimated original hours.
     *
     * @return the task based overestimated original hours
     */
	@Transient
	public double getTaskBasedOverestimatedOriginalHours() {
		return CollectionUtils.sum(this.getTasks(), new DoubleFilter() {
			@Override
			public double filter(final Object o) {
				return ((Task) o).getOverestimatedOriginalHours();
			}
		});
	}

	/**
     * Gets the task based underestimated original hours.
     *
     * @return the task based underestimated original hours
     */
	@Transient
	public double getTaskBasedUnderestimatedOriginalHours() {
		return CollectionUtils.sum(this.getTasks(), new DoubleFilter() {
			@Override
			public double filter(final Object o) {
				return ((Task) o).getUnderestimatedOriginalHours();
			}
		});
	}

	/**
     * Gets the task based completed remaining hours.
     *
     * @return the task based completed remaining hours
     */
	@Transient
	public double getTaskBasedCompletedRemainingHours() {
		if (this.tasks.size() == 0) {
			return 0.0;
		}

		double remainingHours = 0;
		final Iterator<Task> itr = this.tasks.iterator();
		while (itr.hasNext()) {
			final Task task = itr.next();
			remainingHours += task.getCompletedRemainingHours();
		}
		return remainingHours;
	}

	/**
     * Gets the overestimated hours.
     *
     * @return the overestimated hours
     */
	@Transient
	public double getOverestimatedHours() {
		return CollectionUtils.sum(this.getTasks(), new DoubleFilter() {
			@Override
			public double filter(final Object o) {
				return ((Task) o).getOverestimatedHours();
			}
		});
	}

	/**
     * Gets the underestimated hours.
     *
     * @return the underestimated hours
     */
	@Transient
	public double getUnderestimatedHours() {
		return CollectionUtils.sum(this.getTasks(), new DoubleFilter() {
			@Override
			public double filter(final Object o) {
				return ((Task) o).getUnderestimatedHours();
			}
		});
	}

	/**
     * Gets the total hours.
     *
     * @return the total hours
     */
	@Transient
	public double getTotalHours() {
		return this.getEstimatedHoursField();
	}

	/**
     * Gets the remaining hours.
     *
     * @return the remaining hours
     */
	@Transient
	@Deprecated
	public double getRemainingHours() {
		return this.isCompleted() ? 0.0 : this.getEstimatedHoursField();
	}

	/**
     * Gets the completed hours.
     *
     * @return the completed hours
     */
	@Transient
	public double getCompletedHours() {
		return this.isCompleted() ? this.getEstimatedHoursField() : 0.0;
	}

	/**
     * Gets the task based total hours.
     *
     * @return the task based total hours
     */
	@Transient
	@Deprecated
	public double getTaskBasedTotalHours() {
		return CollectionUtils.sum(this.getTasks(), new DoubleFilter() {
			@Override
			public double filter(final Object o) {
				return ((Task) o).getEstimatedHours();
			}
		});
	}

	/**
     * Gets the non task based original hours.
     *
     * @return the non task based original hours
     */
	@Transient
	@Deprecated
	public double getNonTaskBasedOriginalHours() {
		return this.isAdded() ? 0.0 : this.getEstimatedHoursField();
	}

	/**
     * Checks if is added.
     *
     * @return true, if is added
     */
	@Transient
	private boolean isAdded() {
		return StoryDisposition.ADDED == this.getDisposition();
	}

	/**
     * Gets the task based original hours.
     *
     * @return the task based original hours
     */
	@Transient
	@Deprecated
	public double getTaskBasedOriginalHours() {
		return this.isAdded() ? 0.0 : this.getTaskEstimatedOriginalHours();
	}

	/**
     * Gets the task estimated original hours.
     *
     * @return the task estimated original hours
     */
	@Transient
	@Deprecated
	public double getTaskEstimatedOriginalHours() {
		return this.getEstimatedOriginalHours();
	}

	/**
     * Gets the task estimated hours if story added.
     *
     * @return the task estimated hours if story added
     */
	@Transient
	@Deprecated
	public double getTaskEstimatedHoursIfStoryAdded() {
		if (this.isAdded()) {
			double result = this.getTaskBasedEstimatedOriginalHours();
			result += this.getSumOfTaskProperty(Task.ADDED_ORIGINAL_HOURS);
			return result;
		}
		return 0.0;
	}

	/**
     * Gets the task estimated hours if original story.
     *
     * @return the task estimated hours if original story
     */
	@Transient
	public double getTaskEstimatedHoursIfOriginalStory() {
		return this.isAdded() ? 0.0 : this.getTaskEstimatedOriginalHours();
	}

	/**
     * Gets the sum of task property.
     *
     * @param name
     *            the name
     * @return the sum of task property
     */
	private double getSumOfTaskProperty(final String name) {
		return CollectionUtils.sum(this.getTasks(), new DoublePropertyFilter(
				name));
	}

	/**
     * Gets the story estimated hours if story added.
     *
     * @return the story estimated hours if story added
     */
	@Transient
	public double getStoryEstimatedHoursIfStoryAdded() {
		return this.isAdded() ? this.getEstimatedHoursField() : 0.0;
	}

	/**
     * Gets the postponed story hours.
     *
     * @return the postponed story hours
     */
	@Transient
	public double getPostponedStoryHours() {
		return this.getPostponedHours() > 0.0 ? this.getEstimatedHoursField()
				: 0.0;
	}

	/**
     * Gets the status enum.
     *
     * @return the status enum
     */
	@Transient
	public StoryStatus getStatusEnum() {
		return StoryStatus.fromCode(this.status);
	}
}
