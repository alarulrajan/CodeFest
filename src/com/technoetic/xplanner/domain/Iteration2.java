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

/**
 * The Class Iteration2.
 */
public class Iteration2 extends DomainObject implements Nameable,
        NoteAttachable, Describable {
    
    /** The project id. */
    private int projectId;
    
    /** The name. */
    private String name;
    
    /** The description. */
    private String description;
    
    /** The status short. */
    private Short statusShort;
    
    /** The start date. */
    private Date startDate;
    
    /** The end date. */
    private Date endDate;
    
    /** The user stories. */
    private List<UserStory> userStories = new ArrayList<UserStory>();
    
    /** The days worked. */
    private double daysWorked;
    
    /** The estimated hours. */
    private double estimatedHours;
    
    /** The Constant STORY_ID_INDEX. */
    public static final int STORY_ID_INDEX = 0;
    
    /** The Constant ORDER_NO_INDEX. */
    public static final int ORDER_NO_INDEX = 1;

    // --------------------- Interface Describable ---------------------

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.Describable#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.Nameable#getDescription()
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Determine continued story disposition.
     *
     * @return the story disposition
     */
    public StoryDisposition determineContinuedStoryDisposition() {
        StoryDisposition disposition;
        if (this.isCurrent() && this.isActive()) {
            disposition = StoryDisposition.ADDED;
        } else {
            disposition = StoryDisposition.CARRIED_OVER;
        }
        return disposition;
    }

    /**
     * Checks if is current.
     *
     * @return true, if is current
     */
    public boolean isCurrent() {
        return this.startDate.getTime() <= System.currentTimeMillis()
                && this.endDate.getTime() > System.currentTimeMillis();
    }

    /**
     * Gets the project id.
     *
     * @return the project id
     */
    public int getProjectId() {
        return this.projectId;
    }

    /**
     * Sets the project id.
     *
     * @param projectId
     *            the new project id
     */
    public void setProjectId(final int projectId) {
        this.projectId = projectId;
    }

    // ChangeSoon: Once we get a full object model with bi-directional relationship
    // this code must move to the story

    /**
     * Determine new story disposition.
     *
     * @return the story disposition
     */
    public StoryDisposition determineNewStoryDisposition() {
        StoryDisposition disposition;
        if (this.isActive()) {
            disposition = StoryDisposition.ADDED;
        } else {
            disposition = StoryDisposition.PLANNED;
        }
        return disposition;
    }

    /**
     * Checks if is active.
     *
     * @return true, if is active
     */
    public boolean isActive() {
        return IterationStatus.ACTIVE == this.getStatus();
    }

    /**
     * Gets the actual hours.
     *
     * @return the actual hours
     */
    public double getActualHours() {
        return this.getCachedActualHours();
    }

    /**
     * Gets the cached actual hours.
     *
     * @return the cached actual hours
     */
    public double getCachedActualHours() {
        return this
                .getSumOfStoryProperty(UserStory.CACHED_TASK_BASED_ACTUAL_HOURS);
    }

    /**
     * Gets the task actual hours.
     *
     * @return the task actual hours
     */
    public double getTaskActualHours() {
        return this.getSumOfStoryProperty(UserStory.TASK_BASED_ACTUAL_HOURS);
    }

    /**
     * Gets the estimated hours of added tasks.
     *
     * @return the estimated hours of added tasks
     */
    public double getEstimatedHoursOfAddedTasks() {
        return this.getSumOfStoryProperty(UserStory.TASK_BASED_ADDED_HOURS);
    }

    /**
     * Gets the task added hours.
     *
     * @return the task added hours
     */
    public double getTaskAddedHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_ESTIMATED_HOURS_IF_STORY_ADDED);
    }

    /**
     * Gets the adjusted estimated hours.
     *
     * @return the adjusted estimated hours
     */
    public double getAdjustedEstimatedHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_ADJUSTED_ESTIMATED_HOURS);
    }

    /**
     * Gets the task actual completed hours.
     *
     * @return the task actual completed hours
     */
    public double getTaskActualCompletedHours() {
        return this.getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_HOURS);
    }

    /**
     * Gets the story completed hours.
     *
     * @return the story completed hours
     */
    public double getStoryCompletedHours() {
        return this.getSumOfStoryProperty(UserStory.COMPLETED_HOURS);
    }

    /**
     * Gets the task completed hours.
     *
     * @return the task completed hours
     */
    public double getTaskCompletedHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_ORIGINAL_HOURS);
    }

    /**
     * Gets the days worked.
     *
     * @return the days worked
     */
    public double getDaysWorked() {
        return this.daysWorked;
    }

    /**
     * Sets the days worked.
     *
     * @param daysWorked
     *            the new days worked
     */
    public void setDaysWorked(final double daysWorked) {
        this.daysWorked = daysWorked;
    }

    /**
     * Gets the end date.
     *
     * @return the end date
     */
    public Date getEndDate() {
        return this.endDate;
    }

    /**
     * Sets the end date.
     *
     * @param endDate
     *            the new end date
     */
    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the estimated hours.
     *
     * @return the estimated hours
     */
    public double getEstimatedHours() {
        if (this.estimatedHours != 0) {
            return this.estimatedHours;
        }

        this.estimatedHours = this.getTaskCurrentEstimatedHours();
        return this.estimatedHours;
    }

    /**
     * Gets the task current estimated hours.
     *
     * @return the task current estimated hours
     */
    public double getTaskCurrentEstimatedHours() {
        return this.getSumOfStoryProperty(UserStory.ESTIMATED_HOURS);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.Nameable#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the added original hours.
     *
     * @return the added original hours
     */
    public double getAddedOriginalHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_ADDED_ORIGINAL_HOURS);
    }

    /**
     * Gets the completed original hours.
     *
     * @return sum(task.originalEstimate) where task.isComplete=true
     */
    public double getCompletedOriginalHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_ORIGINAL_HOURS);
    }

    /**
     * Gets the task estimated original hours.
     *
     * @return the task estimated original hours
     */
    public double getTaskEstimatedOriginalHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_ESTIMATED_ORIGINAL_HOURS);
    }

    /**
     * Gets the overestimated original hours.
     *
     * @return the overestimated original hours
     */
    public double getOverestimatedOriginalHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_OVERESTIMATED_ORIGINAL_HOURS);
    }

    /**
     * Gets the underestimated original hours.
     *
     * @return the underestimated original hours
     */
    public double getUnderestimatedOriginalHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_UNDERESTIMATED_ORIGINAL_HOURS);
    }

    /**
     * Gets the task overestimated hours.
     *
     * @return the task overestimated hours
     */
    public double getTaskOverestimatedHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_OVERESTIMATED_HOURS);
    }

    /**
     * Gets the postponed hours.
     *
     * @return the postponed hours
     */
    public double getPostponedHours() {
        return this.getSumOfStoryProperty(UserStory.TASK_BASED_POSTPONED_HOURS);
    }

    /**
     * Gets the story remaining hours.
     *
     * @return the story remaining hours
     */
    public double getStoryRemainingHours() {
        return this.getSumOfStoryProperty(UserStory.REMAINING_HOURS)
                - this.getStoryPostponedHours();
    }

    /**
     * Gets the remaining hours.
     *
     * @return the remaining hours
     */
    public double getRemainingHours() {
        return this.getTaskRemainingHours();
    }

    /**
     * Gets the task remaining hours.
     *
     * @return the task remaining hours
     */
    public double getTaskRemainingHours() {
        return this.getSumOfStoryProperty(UserStory.TASK_BASED_REMAINING_HOURS);
    }

    /**
     * Gets the task completed remaining hours.
     *
     * @return the task completed remaining hours
     */
    public double getTaskCompletedRemainingHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_REMAINING_HOURS)
                - this.getTaskPostponedHours();
    }

    /**
     * Gets the start date.
     *
     * @return the start date
     */
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * Sets the start date.
     *
     * @param startDate
     *            the new start date
     */
    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public IterationStatus getStatus() {
        return IterationStatus.fromInt(this.statusShort);
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
    public void setStatus(final IterationStatus status) {
        this.statusShort = new Integer(status.code).shortValue();
    }

    /**
     * Gets the status key.
     *
     * @return the status key
     */
    public String getStatusKey() {
        return this.getStatus() != null ? this.getStatus().getKey() : null;
    }

    /**
     * Gets the task underestimated original hours.
     *
     * @return the task underestimated original hours
     */
    public double getTaskUnderestimatedOriginalHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_UNDERESTIMATED_ORIGINAL_HOURS);
    }

    /**
     * Gets the task underestimated hours.
     *
     * @return the task underestimated hours
     */
    public double getTaskUnderestimatedHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_UNDERESTIMATED_HOURS);
    }

    /**
     * Gets the user stories.
     *
     * @return the user stories
     */
    public List<UserStory> getUserStories() {
        return this.userStories;
    }

    /**
     * Sets the user stories.
     *
     * @param userStories
     *            the new user stories
     */
    public void setUserStories(final List<UserStory> userStories) {
        this.userStories = userStories;
    }

    /**
     * Checks if is future.
     *
     * @return true, if is future
     */
    public boolean isFuture() {
        return this.startDate.getTime() > System.currentTimeMillis();
    }

    /**
     * Sets the status key.
     *
     * @param status
     *            the new status key
     */
    public void setStatusKey(final String status) {
        this.setStatus(IterationStatus.fromKey(status));
    }

    /**
     * Start.
     */
    public void start() {
        this.setStatus(IterationStatus.ACTIVE);
        this.startStories();
    }

    /**
     * Start stories.
     */
    private void startStories() {
        for (final Iterator it = this.getUserStories().iterator(); it.hasNext();) {
            final UserStory story = (UserStory) it.next();
            story.start();
        }
    }

    /**
     * Close.
     */
    public void close() {
        this.setStatus(IterationStatus.INACTIVE);
    }

    /**
     * Gets the sum of story property.
     *
     * @param name
     *            the name
     * @return the sum of story property
     */
    private double getSumOfStoryProperty(final String name) {
        return CollectionUtils.sum(this.getUserStories(),
                new DoublePropertyFilter(name));
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.domain.DomainObject#toString()
     */
    @Override
    public String toString() {
        return "Iteration{" + "id='" + this.getId() + ", " + "projectId='"
                + this.projectId + ", " + "name='" + this.name + "'" + "}";
    }

    /**
     * Gets the task postponed hours.
     *
     * @return the task postponed hours
     */
    public double getTaskPostponedHours() {
        return this.getSumOfStoryProperty(UserStory.TASK_BASED_POSTPONED_HOURS);
    }

    /**
     * Gets the story postponed hours.
     *
     * @return the story postponed hours
     */
    public double getStoryPostponedHours() {
        return this.getSumOfStoryProperty(UserStory.POSTPONED_STORY_HOURS);
    }

    /**
     * Gets the task total hours.
     *
     * @return the task total hours
     */
    public double getTaskTotalHours() {
        return this.getTaskOriginalHours() + this.getTaskAddedHours()
                - this.getTaskPostponedHours();
    }

    /**
     * Gets the story total hours.
     *
     * @return the story total hours
     */
    public double getStoryTotalHours() {
        return this.getStoryOriginalHours() + this.getStoryAddedHours()
                - this.getStoryPostponedHours();
    }

    /**
     * Gets the story added hours.
     *
     * @return the story added hours
     */
    public double getStoryAddedHours() {
        return this
                .getSumOfStoryProperty(UserStory.STORY_ESTIMATED_HOURS_IF_STORY_ADDED);
    }

    /**
     * Gets the story estimated hours.
     *
     * @return the story estimated hours
     */
    public double getStoryEstimatedHours() {
        return this.getSumOfStoryProperty(UserStory.STORY_ESTIMATED_HOURS);
    }

    /**
     * Gets the story original hours.
     *
     * @return the story original hours
     */
    public double getStoryOriginalHours() {
        return this
                .getSumOfStoryProperty(UserStory.STORY_ESTIMATED_ORIGINAL_HOURS);
    }

    /**
     * Gets the task original hours.
     *
     * @return the task original hours
     */
    public double getTaskOriginalHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_ESTIMATED_HOURS_IF_ORIGINAL_STORY);
    }

    /**
     * Gets the new task disposition name.
     *
     * @param story
     *            the story
     * @return the new task disposition name
     */
    public String getNewTaskDispositionName(final UserStory story) {
        final TaskDisposition disposition = this.getNewTaskDisposition(story);
        return disposition.getName();
    }

    /**
     * Gets the new task disposition.
     *
     * @param story
     *            the story
     * @return the new task disposition
     */
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

    /**
     * Modify story order.
     *
     * @param storyIdAndNewOrder
     *            the story id and new order
     */
    public void modifyStoryOrder(final int[][] storyIdAndNewOrder) {
        final Map storiesById = this.mapStoriesById(this.getUserStories());
        final List orderChanges = this.getOrderChanges(storyIdAndNewOrder,
                storiesById);
        this.reorderStories(orderChanges);
    }

    /**
     * Map stories by id.
     *
     * @param stories
     *            the stories
     * @return the map
     */
    private Map mapStoriesById(final Collection stories) {
        final Map storiesById = new TreeMap();
        for (final Iterator iterator = stories.iterator(); iterator.hasNext();) {
            final UserStory userStory = (UserStory) iterator.next();
            storiesById.put(new Integer(userStory.getId()), userStory);
        }
        return storiesById;
    }

    /**
     * Reorder stories.
     *
     * @param storiesByOrder
     *            the stories by order
     */
    private void reorderStories(final List storiesByOrder) {
        for (int index = 0, newOrderNo = 1; index < storiesByOrder.size(); index++, newOrderNo++) {
            final UserStory orderNoChange = (UserStory) storiesByOrder
                    .get(index);
            orderNoChange.setOrderNo(newOrderNo);
        }
    }

    /**
     * Gets the order changes.
     *
     * @param storyIdAndNewOrder
     *            the story id and new order
     * @param storiesById
     *            the stories by id
     * @return the order changes
     */
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

    /**
     * Gets the status short.
     *
     * @return the status short
     */
    public Short getStatusShort() {
        return this.statusShort;
    }

    /**
     * Sets the status short.
     *
     * @param status
     *            the new status short
     */
    public void setStatusShort(final Short status) {
        this.statusShort = status;
    }
}
