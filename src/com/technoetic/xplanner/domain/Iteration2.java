package com.technoetic.xplanner.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.util.CollectionUtils;
import com.technoetic.xplanner.util.CollectionUtils.DoublePropertyFilter;

public class Iteration2 extends DomainObject implements Nameable,
		NoteAttachable, Describable {
	private int projectId;
	private String name;
	private String description;
	private Short statusShort;
	private Date startDate;
	private Date endDate;
	private List<UserStory> userStories = new ArrayList<UserStory>();
	private double daysWorked;
	private double estimatedHours;
	public static final int STORY_ID_INDEX = 0;
	public static final int ORDER_NO_INDEX = 1;

	// --------------------- Interface Describable ---------------------

	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	public StoryDisposition determineContinuedStoryDisposition() {
		StoryDisposition disposition;
		if (this.isCurrent() && this.isActive()) {
			disposition = StoryDisposition.ADDED;
		} else {
			disposition = StoryDisposition.CARRIED_OVER;
		}
		return disposition;
	}

	public boolean isCurrent() {
		return this.startDate.getTime() <= System.currentTimeMillis()
				&& this.endDate.getTime() > System.currentTimeMillis();
	}

	public int getProjectId() {
		return this.projectId;
	}

	public void setProjectId(final int projectId) {
		this.projectId = projectId;
	}

	// TODO: Once we get a full object model with bi-directional relationship
	// this code must move to the story

	public StoryDisposition determineNewStoryDisposition() {
		StoryDisposition disposition;
		if (this.isActive()) {
			disposition = StoryDisposition.ADDED;
		} else {
			disposition = StoryDisposition.PLANNED;
		}
		return disposition;
	}

	public boolean isActive() {
		return IterationStatus.ACTIVE == this.getStatus();
	}

	public double getActualHours() {
		return this.getCachedActualHours();
	}

	public double getCachedActualHours() {
		return this
				.getSumOfStoryProperty(UserStory.CACHED_TASK_BASED_ACTUAL_HOURS);
	}

	public double getTaskActualHours() {
		return this.getSumOfStoryProperty(UserStory.TASK_BASED_ACTUAL_HOURS);
	}

	public double getEstimatedHoursOfAddedTasks() {
		return this.getSumOfStoryProperty(UserStory.TASK_BASED_ADDED_HOURS);
	}

	public double getTaskAddedHours() {
		return this
				.getSumOfStoryProperty(UserStory.TASK_ESTIMATED_HOURS_IF_STORY_ADDED);
	}

	public double getAdjustedEstimatedHours() {
		return this
				.getSumOfStoryProperty(UserStory.TASK_BASED_ADJUSTED_ESTIMATED_HOURS);
	}

	public double getTaskActualCompletedHours() {
		return this.getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_HOURS);
	}

	public double getStoryCompletedHours() {
		return this.getSumOfStoryProperty(UserStory.COMPLETED_HOURS);
	}

	public double getTaskCompletedHours() {
		return this
				.getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_ORIGINAL_HOURS);
	}

	public double getDaysWorked() {
		return this.daysWorked;
	}

	public void setDaysWorked(final double daysWorked) {
		this.daysWorked = daysWorked;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	public double getEstimatedHours() {
		if (this.estimatedHours != 0) {
			return this.estimatedHours;
		}

		this.estimatedHours = this.getTaskCurrentEstimatedHours();
		return this.estimatedHours;
	}

	public double getTaskCurrentEstimatedHours() {
		return this.getSumOfStoryProperty(UserStory.ESTIMATED_HOURS);
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public double getAddedOriginalHours() {
		return this
				.getSumOfStoryProperty(UserStory.TASK_BASED_ADDED_ORIGINAL_HOURS);
	}

	/**
	 * @return sum(task.originalEstimate) where task.isComplete=true
	 */
	public double getCompletedOriginalHours() {
		return this
				.getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_ORIGINAL_HOURS);
	}

	public double getTaskEstimatedOriginalHours() {
		return this
				.getSumOfStoryProperty(UserStory.TASK_BASED_ESTIMATED_ORIGINAL_HOURS);
	}

	public double getOverestimatedOriginalHours() {
		return this
				.getSumOfStoryProperty(UserStory.TASK_BASED_OVERESTIMATED_ORIGINAL_HOURS);
	}

	public double getUnderestimatedOriginalHours() {
		return this
				.getSumOfStoryProperty(UserStory.TASK_BASED_UNDERESTIMATED_ORIGINAL_HOURS);
	}

	public double getTaskOverestimatedHours() {
		return this
				.getSumOfStoryProperty(UserStory.TASK_BASED_OVERESTIMATED_HOURS);
	}

	public double getPostponedHours() {
		return this.getSumOfStoryProperty(UserStory.TASK_BASED_POSTPONED_HOURS);
	}

	public double getStoryRemainingHours() {
		return this.getSumOfStoryProperty(UserStory.REMAINING_HOURS)
				- this.getStoryPostponedHours();
	}

	public double getRemainingHours() {
		return this.getTaskRemainingHours();
	}

	public double getTaskRemainingHours() {
		return this.getSumOfStoryProperty(UserStory.TASK_BASED_REMAINING_HOURS);
	}

	public double getTaskCompletedRemainingHours() {
		return this
				.getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_REMAINING_HOURS)
				- this.getTaskPostponedHours();
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	public IterationStatus getStatus() {
		return IterationStatus.fromInt(this.statusShort);
	}

	public void setStatus(final IterationStatus status) {
		this.statusShort = new Integer(status.code).shortValue();
	}

	public String getStatusKey() {
		return this.getStatus() != null ? this.getStatus().getKey() : null;
	}

	public double getTaskUnderestimatedOriginalHours() {
		return this
				.getSumOfStoryProperty(UserStory.TASK_BASED_UNDERESTIMATED_ORIGINAL_HOURS);
	}

	public double getTaskUnderestimatedHours() {
		return this
				.getSumOfStoryProperty(UserStory.TASK_BASED_UNDERESTIMATED_HOURS);
	}

	public List<UserStory> getUserStories() {
		return this.userStories;
	}

	public void setUserStories(final List<UserStory> userStories) {
		this.userStories = userStories;
	}

	public boolean isFuture() {
		return this.startDate.getTime() > System.currentTimeMillis();
	}

	public void setStatusKey(final String status) {
		this.setStatus(IterationStatus.fromKey(status));
	}

	public void start() {
		this.setStatus(IterationStatus.ACTIVE);
		this.startStories();
	}

	private void startStories() {
		for (final Iterator it = this.getUserStories().iterator(); it.hasNext();) {
			final UserStory story = (UserStory) it.next();
			story.start();
		}
	}

	public void close() {
		this.setStatus(IterationStatus.INACTIVE);
	}

	private double getSumOfStoryProperty(final String name) {
		return CollectionUtils.sum(this.getUserStories(),
				new DoublePropertyFilter(name));
	}

	@Override
	public String toString() {
		return "Iteration{" + "id='" + this.getId() + ", " + "projectId='"
				+ this.projectId + ", " + "name='" + this.name + "'" + "}";
	}

	public double getTaskPostponedHours() {
		return this.getSumOfStoryProperty(UserStory.TASK_BASED_POSTPONED_HOURS);
	}

	public double getStoryPostponedHours() {
		return this.getSumOfStoryProperty(UserStory.POSTPONED_STORY_HOURS);
	}

	public double getTaskTotalHours() {
		return this.getTaskOriginalHours() + this.getTaskAddedHours()
				- this.getTaskPostponedHours();
	}

	public double getStoryTotalHours() {
		return this.getStoryOriginalHours() + this.getStoryAddedHours()
				- this.getStoryPostponedHours();
	}

	public double getStoryAddedHours() {
		return this
				.getSumOfStoryProperty(UserStory.STORY_ESTIMATED_HOURS_IF_STORY_ADDED);
	}

	public double getStoryEstimatedHours() {
		return this.getSumOfStoryProperty(UserStory.STORY_ESTIMATED_HOURS);
	}

	public double getStoryOriginalHours() {
		return this
				.getSumOfStoryProperty(UserStory.STORY_ESTIMATED_ORIGINAL_HOURS);
	}

	public double getTaskOriginalHours() {
		return this
				.getSumOfStoryProperty(UserStory.TASK_ESTIMATED_HOURS_IF_ORIGINAL_STORY);
	}

	public String getNewTaskDispositionName(final UserStory story) {
		final TaskDisposition disposition = this.getNewTaskDisposition(story);
		return disposition.getName();
	}

	public TaskDisposition getNewTaskDisposition(final UserStory story) {
		TaskDisposition disposition;
		// if(story.getDisposition().equals(StoryDisposition.ADDED){
		// disposition = TaskDisposition.DISCOVERED;
		// }
		if (story.isStarted()) {
			disposition = TaskDisposition.DISCOVERED;
		} else {
			if (this.isActive()) {
				disposition = TaskDisposition.DISCOVERED;
			} else {
				disposition = TaskDisposition.PLANNED;
			}
		}
		return disposition;
	}

	public void modifyStoryOrder(final int[][] storyIdAndNewOrder) {
		final Map storiesById = this.mapStoriesById(this.getUserStories());
		final List orderChanges = this.getOrderChanges(storyIdAndNewOrder,
				storiesById);
		this.reorderStories(orderChanges);
	}

	private Map mapStoriesById(final Collection stories) {
		final Map storiesById = new TreeMap();
		for (final Iterator iterator = stories.iterator(); iterator.hasNext();) {
			final UserStory userStory = (UserStory) iterator.next();
			storiesById.put(new Integer(userStory.getId()), userStory);
		}
		return storiesById;
	}

	private void reorderStories(final List storiesByOrder) {
		for (int index = 0, newOrderNo = 1; index < storiesByOrder.size(); index++, newOrderNo++) {
			final UserStory orderNoChange = (UserStory) storiesByOrder
					.get(index);
			orderNoChange.setOrderNo(newOrderNo);
		}
	}

	private List getOrderChanges(final int[][] storyIdAndNewOrder,
			final Map storiesById) {
		final List storiesByOrder = new ArrayList();
		for (int index = 0; index < storyIdAndNewOrder.length; index++) {
			final Integer storyId = new Integer(
					storyIdAndNewOrder[index][Iteration2.STORY_ID_INDEX]);
			final int newOrderNo = storyIdAndNewOrder[index][Iteration2.ORDER_NO_INDEX];
			final UserStory userStory = (UserStory) storiesById.get(storyId);
			userStory.setOrderNo(newOrderNo);
			storiesByOrder.add(userStory);
		}
		Collections.sort(storiesByOrder, new StoryOrderNoChangeComparator());
		return storiesByOrder;
	}

	public Short getStatusShort() {
		return this.statusShort;
	}

	public void setStatusShort(final Short status) {
		this.statusShort = status;
	}
}
