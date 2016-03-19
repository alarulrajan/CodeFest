package net.sf.xplanner.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.domain.NoteAttachable;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.StoryOrderNoChangeComparator;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.util.CollectionUtils;
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
@Table(name = "iteration")
public class Iteration extends NamedObject implements java.io.Serializable,
        NoteAttachable {
    
    /** The Constant STORY_ID_INDEX. */
    public static final int STORY_ID_INDEX = 0;
    
    /** The Constant ORDER_NO_INDEX. */
    public static final int ORDER_NO_INDEX = 1;
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7878797325126050044L;
    
    /** The project. */
    private Project project = new Project();
    
    /** The start date. */
    private Date startDate;
    
    /** The end date. */
    private Date endDate;
    
    /** The status. */
    private short status = IterationStatus.INACTIVE.toInt();
    
    /** The days worked. */
    private Double daysWorked;
    
    /** The user stories. */
    private List<UserStory> userStories = new ArrayList<UserStory>();
    
    /** The estimated hours. */
    private double estimatedHours;

    /**
     * Instantiates a new iteration.
     */
    public Iteration() {
    }

    /**
     * Gets the project.
     *
     * @return the project
     */
    @ManyToOne
    @JoinColumn(name = "project_id")
    public Project getProject() {
        return this.project;
    }

    /**
     * Sets the project.
     *
     * @param project
     *            the new project
     */
    public void setProject(final Project project) {
        this.project = project;
    }

    /**
     * Gets the start date.
     *
     * @return the start date
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", length = 10)
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
     * Gets the end date.
     *
     * @return the end date
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "end_date", length = 10)
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
     * Gets the status.
     *
     * @return the status
     */
    @Column(name = "status")
    public short getStatus() {
        return this.status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
    public void setStatus(final Short status) {
        this.status = status;
    }

    /**
     * Gets the days worked.
     *
     * @return the days worked
     */
    @Column(name = "days_worked", precision = 22, scale = 0)
    public Double getDaysWorked() {
        return this.daysWorked;
    }

    /**
     * Sets the days worked.
     *
     * @param daysWorked
     *            the new days worked
     */
    public void setDaysWorked(final Double daysWorked) {
        this.daysWorked = daysWorked;
    }

    /**
     * Gets the estimated hours.
     *
     * @return the estimated hours
     */
    @Transient
    public double getEstimatedHours() {
        if (this.estimatedHours != 0) {
            return this.estimatedHours;
        }

        this.estimatedHours = this.getTaskCurrentEstimatedHours();
        return this.estimatedHours;
    }

    /**
     * Gets the user stories.
     *
     * @return the user stories
     */
    @OneToMany(mappedBy = "iteration", cascade = CascadeType.REMOVE)
    public List<UserStory> getUserStories() {
        return this.userStories;
    }

    /**
     * Gets the adjusted estimated hours.
     *
     * @return the adjusted estimated hours
     */
    @Transient
    public double getAdjustedEstimatedHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_ADJUSTED_ESTIMATED_HOURS);
    }

    /**
     * Gets the remaining hours.
     *
     * @return the remaining hours
     */
    @Transient
    public double getRemainingHours() {
        return this.getTaskRemainingHours();
    }

    /**
     * Gets the task remaining hours.
     *
     * @return the task remaining hours
     */
    @Transient
    public double getTaskRemainingHours() {
        return this.getSumOfStoryProperty(UserStory.TASK_BASED_REMAINING_HOURS);
    }

    /**
     * Gets the sum of story property.
     *
     * @param name
     *            the name
     * @return the sum of story property
     */
    @Transient
    @Deprecated
    private double getSumOfStoryProperty(final String name) {
        return CollectionUtils.sum(this.getUserStories(),
                new DoublePropertyFilter(name));
    }

    /**
     * Checks if is active.
     *
     * @return true, if is active
     */
    @Transient
    public boolean isActive() {
        return IterationStatus.ACTIVE.toInt() == this.getStatus();
    }

    /**
     * Checks if is current.
     *
     * @return true, if is current
     */
    @Transient
    @Deprecated
    public boolean isCurrent() {
        return this.startDate.getTime() <= System.currentTimeMillis()
                && this.endDate.getTime() > System.currentTimeMillis();
    }

    /**
     * Checks if is future.
     *
     * @return true, if is future
     */
    @Transient
    @Deprecated
    public boolean isFuture() {
        return this.startDate.getTime() > System.currentTimeMillis();
    }

    /**
     * Modify story order.
     *
     * @param storyIdAndNewOrder
     *            the story id and new order
     */
    @Deprecated
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
                    storyIdAndNewOrder[index][Iteration.STORY_ID_INDEX]);
            final int newOrderNo = storyIdAndNewOrder[index][Iteration.ORDER_NO_INDEX];
            final UserStory userStory = (UserStory) storiesById.get(storyId);
            userStory.setOrderNo(newOrderNo);
            storiesByOrder.add(userStory);
        }
        Collections.sort(storiesByOrder, new StoryOrderNoChangeComparator());
        return storiesByOrder;
    }

    /**
     * Gets the postponed hours.
     *
     * @return the postponed hours
     */
    @Transient
    public double getPostponedHours() {
        return this.getSumOfStoryProperty(UserStory.TASK_BASED_POSTPONED_HOURS);
    }

    /**
     * Sets the iteration status.
     *
     * @param active
     *            the new iteration status
     */
    public void setIterationStatus(final IterationStatus active) {
        this.setStatus(active.toInt());
    }

    /**
     * Start.
     */
    public void start() {
        this.setIterationStatus(IterationStatus.ACTIVE);
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
     * Sets the user stories.
     *
     * @param stories
     *            the new user stories
     */
    public void setUserStories(final List<UserStory> stories) {
        this.userStories = stories;
    }

    /**
     * Sets the status key.
     *
     * @param inactiveKey
     *            the new status key
     */
    public void setStatusKey(final String inactiveKey) {
        // ChangeSoon 

    }

    /**
     * Gets the added original hours.
     *
     * @return the added original hours
     */
    @Transient
    @Deprecated
    public double getAddedOriginalHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_ADDED_ORIGINAL_HOURS);
    }

    /**
     * Gets the completed original hours.
     *
     * @return sum(task.originalEstimate) where task.isComplete=true
     */
    @Transient
    public double getCompletedOriginalHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_ORIGINAL_HOURS);
    }

    /**
     * Gets the task estimated original hours.
     *
     * @return the task estimated original hours
     */
    @Transient
    public double getTaskEstimatedOriginalHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_ESTIMATED_ORIGINAL_HOURS);
    }

    /**
     * Gets the overestimated original hours.
     *
     * @return the overestimated original hours
     */
    @Transient
    public double getOverestimatedOriginalHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_OVERESTIMATED_ORIGINAL_HOURS);
    }

    /**
     * Gets the underestimated original hours.
     *
     * @return the underestimated original hours
     */
    @Transient
    public double getUnderestimatedOriginalHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_UNDERESTIMATED_ORIGINAL_HOURS);
    }

    /**
     * Gets the task overestimated hours.
     *
     * @return the task overestimated hours
     */
    @Transient
    public double getTaskOverestimatedHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_OVERESTIMATED_HOURS);
    }

    /**
     * Gets the story remaining hours.
     *
     * @return the story remaining hours
     */
    @Transient
    public double getStoryRemainingHours() {
        return this.getSumOfStoryProperty(UserStory.REMAINING_HOURS)
                - this.getStoryPostponedHours();
    }

    /**
     * Gets the task completed remaining hours.
     *
     * @return the task completed remaining hours
     */
    @Transient
    public double getTaskCompletedRemainingHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_REMAINING_HOURS)
                - this.getTaskPostponedHours();
    }

    /**
     * Gets the task postponed hours.
     *
     * @return the task postponed hours
     */
    @Transient
    public double getTaskPostponedHours() {
        return this.getSumOfStoryProperty(UserStory.TASK_BASED_POSTPONED_HOURS);
    }

    /**
     * Gets the story postponed hours.
     *
     * @return the story postponed hours
     */
    @Transient
    public double getStoryPostponedHours() {
        return this.getSumOfStoryProperty(UserStory.POSTPONED_STORY_HOURS);
    }

    /**
     * Gets the task total hours.
     *
     * @return the task total hours
     */
    @Transient
    public double getTaskTotalHours() {
        return this.getTaskOriginalHours() + this.getTaskAddedHours()
                - this.getTaskPostponedHours();
    }

    /**
     * Gets the story total hours.
     *
     * @return the story total hours
     */
    @Transient
    public double getStoryTotalHours() {
        return this.getStoryOriginalHours() + this.getStoryAddedHours()
                - this.getStoryPostponedHours();
    }

    /**
     * Gets the story added hours.
     *
     * @return the story added hours
     */
    @Transient
    public double getStoryAddedHours() {
        return this
                .getSumOfStoryProperty(UserStory.STORY_ESTIMATED_HOURS_IF_STORY_ADDED);
    }

    /**
     * Gets the story estimated hours.
     *
     * @return the story estimated hours
     */
    @Transient
    public double getStoryEstimatedHours() {
        return this.getSumOfStoryProperty(UserStory.STORY_ESTIMATED_HOURS);
    }

    /**
     * Gets the story original hours.
     *
     * @return the story original hours
     */
    @Transient
    public double getStoryOriginalHours() {
        return this
                .getSumOfStoryProperty(UserStory.STORY_ESTIMATED_ORIGINAL_HOURS);
    }

    /**
     * Gets the task original hours.
     *
     * @return the task original hours
     */
    @Transient
    public double getTaskOriginalHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_ESTIMATED_HOURS_IF_ORIGINAL_STORY);
    }

    /**
     * Gets the task underestimated original hours.
     *
     * @return the task underestimated original hours
     */
    @Transient
    public double getTaskUnderestimatedOriginalHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_UNDERESTIMATED_ORIGINAL_HOURS);
    }

    /**
     * Gets the task underestimated hours.
     *
     * @return the task underestimated hours
     */
    @Transient
    public double getTaskUnderestimatedHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_UNDERESTIMATED_HOURS);
    }

    /**
     * Gets the estimated hours of added tasks.
     *
     * @return the estimated hours of added tasks
     */
    @Transient
    public double getEstimatedHoursOfAddedTasks() {
        return this.getSumOfStoryProperty(UserStory.TASK_BASED_ADDED_HOURS);
    }

    /**
     * Gets the task actual completed hours.
     *
     * @return the task actual completed hours
     */
    @Transient
    public double getTaskActualCompletedHours() {
        return this.getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_HOURS);
    }

    /**
     * Gets the story completed hours.
     *
     * @return the story completed hours
     */
    @Transient
    public double getStoryCompletedHours() {
        return this.getSumOfStoryProperty(UserStory.COMPLETED_HOURS);
    }

    /**
     * Gets the task completed hours.
     *
     * @return the task completed hours
     */
    @Transient
    public double getTaskCompletedHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_ORIGINAL_HOURS);
    }

    /**
     * Gets the task added hours.
     *
     * @return the task added hours
     */
    @Transient
    public double getTaskAddedHours() {
        return this
                .getSumOfStoryProperty(UserStory.TASK_ESTIMATED_HOURS_IF_STORY_ADDED);
    }

    /**
     * Gets the status key.
     *
     * @return the status key
     */
    @Transient
    public String getStatusKey() {
        return IterationStatus.fromInt(this.getStatus()).getKey();
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
     * Gets the cached actual hours.
     *
     * @return the cached actual hours
     */
    @Transient
    public double getCachedActualHours() {
        return this
                .getSumOfStoryProperty(UserStory.CACHED_TASK_BASED_ACTUAL_HOURS);
    }

    // ChangeSoon: Once we get a full object model with bi-directional relationship
    /**
     * Determine new story disposition.
     *
     * @return the story disposition
     */
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

    /**
     * Close.
     */
    public void close() {
        this.setIterationStatus(IterationStatus.INACTIVE);
    }

    /**
     * Gets the task current estimated hours.
     *
     * @return the task current estimated hours
     */
    @Transient
    public double getTaskCurrentEstimatedHours() {
        return this.getSumOfStoryProperty(UserStory.ESTIMATED_HOURS);
    }

    /**
     * Gets the task actual hours.
     *
     * @return the task actual hours
     */
    @Transient
    public double getTaskActualHours() {
        return this.getSumOfStoryProperty(UserStory.TASK_BASED_ACTUAL_HOURS);
    }

}
