package com.technoetic.xplanner.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

/**
 * The Class TestIteration.
 */
public class TestIteration extends TestCase {
    
    /** The iteration. */
    Iteration iteration = new Iteration();

    /** Test get actual hours with no stories.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetActualHoursWithNoStories() throws Exception {
        assertEquals(0.0d, iteration.getCachedActualHours(),0.0);
    }

    /** Test get estimated hours with no stories.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetEstimatedHoursWithNoStories() throws Exception {
        assertEquals(0.0d, iteration.getEstimatedHours(),0.0);
    }

    /** Test get adjusted estimated hours with no stories.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetAdjustedEstimatedHoursWithNoStories() throws Exception {
        assertEquals(0.0d, iteration.getAdjustedEstimatedHours(),0.0);
    }

    /** Test get remaining hours.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetRemainingHours() throws Exception {
        assertEquals(0.0d, iteration.getTaskRemainingHours(),0.0);
    }

    /** Test get estimated original hours.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetEstimatedOriginalHours() throws Exception {
      assertEquals(0.0d, iteration.getTaskEstimatedOriginalHours(),0.0);
        List<UserStory> stories = new ArrayList<UserStory>();
        UserStory story = new UserStory();
        //start iteration
        story.setEstimatedOriginalHours(new Double(10.0));
        stories.add(story);
        List<Task> tasks = new ArrayList();
        Task task = new Task();
        task.setUserStory(story);
        task.setEstimatedOriginalHours(3);
        tasks.add(task);
        story.setTasks(tasks);
        iteration.setUserStories(stories);
      assertEquals(3.0d, iteration.getTaskEstimatedOriginalHours(),0.0);
        task.setDisposition(TaskDisposition.ADDED);
      assertEquals(0.0d, iteration.getTaskEstimatedOriginalHours(),0.0);
    }

    /** Test get overestimated hours.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetOverestimatedHours() throws Exception {
        Iteration iteration = setUpTestIteration();
        assertEquals(7.0d, iteration.getTaskOverestimatedHours(),0.0);
    }

    /** Test get overestimated original hours.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetOverestimatedOriginalHours() throws Exception {
        Iteration iteration = setUpTestIteration();
        assertEquals(4.0d, iteration.getOverestimatedOriginalHours(),0.0);
    }

    /** Test get underestimated hours.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetUnderestimatedHours() throws Exception {
        Iteration iteration = setUpTestIteration();
        assertEquals(1.0d, iteration.getTaskUnderestimatedOriginalHours(),0.0);
    }

   /** Test get underestimated original hours.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetUnderestimatedOriginalHours() throws Exception {
        Iteration iteration = setUpTestIteration();
        assertEquals(1.0d, iteration.getUnderestimatedOriginalHours(),0.0);
    }

   /** Test get added hours.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetAddedHours() throws Exception {
        Iteration iteration = setUpTestIteration();
        assertEquals(5.0d, iteration.getEstimatedHoursOfAddedTasks(),0.0);
    }

   /** Test get added or discovered original hours.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetAddedOrDiscoveredOriginalHours() throws Exception {
        Iteration iteration = setUpTestIteration();
        assertEquals(5.0d, iteration.getAddedOriginalHours(),0.0);
    }

   /** Test get postponed hours.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetPostponedHours() throws Exception {
        Iteration iteration = setUpTestIteration();
        assertEquals(2.0d, iteration.getPostponedHours(),0.0);
    }

   /** Test is future.
     *
     * @throws Exception
     *             the exception
     */
   public void testIsFuture() throws Exception
    {
        Iteration iteration = new Iteration();
        iteration.setStartDate(new Date(System.currentTimeMillis() - 7200000));
        assertFalse(iteration.isFuture());
        iteration.setStartDate(new Date(System.currentTimeMillis() + 7200000));
        assertTrue(iteration.isFuture());
    }

   /** Test is active.
     *
     * @throws Exception
     *             the exception
     */
   public void testIsActive() throws Exception
    {
        Iteration iteration = new Iteration();
        iteration.setIterationStatus(IterationStatus.INACTIVE);
        assertFalse(iteration.isActive());
    }

   /** Test is current.
     *
     * @throws Exception
     *             the exception
     */
   public void testIsCurrent() throws Exception
    {
        Iteration iteration = new Iteration();
        iteration.setStartDate(new Date(System.currentTimeMillis() - 2000));
        iteration.setEndDate(new Date(System.currentTimeMillis() - 1000));
        assertFalse("Past iteration", iteration.isCurrent());
        iteration.setStartDate(new Date(System.currentTimeMillis() - 1000));
        iteration.setEndDate(new Date(System.currentTimeMillis() + 1000));
        assertTrue("Current iteration", iteration.isCurrent());
        iteration.setStartDate(new Date(System.currentTimeMillis() + 1000));
        iteration.setEndDate(new Date(System.currentTimeMillis() + 2000));
        assertFalse("Future iteration", iteration.isCurrent());
    }

   /** Test get completed original hours.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetCompletedOriginalHours() throws Exception {
      Iteration iteration = setUpTestIteration();
      assertEquals(9.0, iteration.getCompletedOriginalHours(), 0.0);
   }

   /** Test get completed hours.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetCompletedHours() throws Exception {
      Iteration iteration = setUpTestIteration();
      assertEquals(6.0, iteration.getTaskActualCompletedHours(), 0.0);
   }

   /** Sets the up test iteration.
     *
     * @return the iteration
     * @throws Exception
     *             the exception
     */
   private Iteration setUpTestIteration() throws Exception {
      Iteration iteration = new Iteration();
      iteration.setIterationStatus(IterationStatus.INACTIVE);

      List tasks1 = new ArrayList();
      List tasks2 = new ArrayList();
      ArrayList twoHourTimeEntry = getTimeEntriesForDurationInHours(2);

      UserStory story1 = new UserStory();
      story1.setDisposition(StoryDisposition.ADDED);
      story1.setEstimatedOriginalHours(new Double(10.0));

      UserStory story2 = new UserStory();
      story2.setDisposition(StoryDisposition.CARRIED_OVER);
      story2.setPostponedHours(2.0d);
      story2.setEstimatedOriginalHours(new Double(10.0));

      Task task1 = new Task();
      task1.setEstimatedHours(1.0);
      task1.setTimeEntries(twoHourTimeEntry);
      task1.setUserStory(story1);
      tasks1.add(task1);

      tasks2.add(createTask(4.0d, twoHourTimeEntry, TaskDisposition.PLANNED, story2));
      tasks2.add(createTask(4.0d, twoHourTimeEntry, TaskDisposition.DISCOVERED, story2));

      story1.setTasks(tasks1);

      story2.setTasks(tasks2);

      List userStories = new ArrayList();
      userStories.add(story1);
      userStories.add(story2);
      iteration.setUserStories(userStories);
      iteration.start();
      task1.setEstimatedHours(5.0);
      task1.setCompleted(true);
      task1.setDisposition(TaskDisposition.ADDED);
      return iteration;
   }

   /** Creates the task.
     *
     * @param estimatedHours
     *            the estimated hours
     * @param twoHourTimeEntry
     *            the two hour time entry
     * @param disposition
     *            the disposition
     * @return the task
     */
   private Task createTask(double estimatedHours, ArrayList twoHourTimeEntry, TaskDisposition disposition) {
      Task task2 = new Task();
      task2.setEstimatedHours(estimatedHours);
      task2.setTimeEntries(twoHourTimeEntry);
      task2.setCompleted(true);
      task2.setDisposition(disposition);
      return task2;
   }

   /** Creates the task.
     *
     * @param estimatedHours
     *            the estimated hours
     * @param twoHourTimeEntry
     *            the two hour time entry
     * @param disposition
     *            the disposition
     * @param story
     *            the story
     * @return the task
     */
   private Task createTask(double estimatedHours, ArrayList twoHourTimeEntry, TaskDisposition disposition, UserStory story) {
      Task task2 = createTask(estimatedHours, twoHourTimeEntry, disposition);
      task2.setUserStory(story);
      return task2;
   }

   /** Gets the time entries for duration in hours.
     *
     * @param duration
     *            the duration
     * @return the time entries for duration in hours
     */
   private ArrayList getTimeEntriesForDurationInHours(int duration) {
      long now = new Date().getTime();
      TimeEntry timeEntry = new TimeEntry();
      timeEntry.setStartTime(new Date(now - 7200000));
      timeEntry.setEndTime(new Date(now));
      timeEntry.setDuration(duration);
      ArrayList timeEntries = new ArrayList();
      timeEntries.add(timeEntry);
      return timeEntries;
   }
}