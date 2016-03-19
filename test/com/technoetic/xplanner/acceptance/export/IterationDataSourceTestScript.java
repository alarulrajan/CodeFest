package com.technoetic.xplanner.acceptance.export;

import net.sf.jasperreports.engine.JRException;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript;
import com.technoetic.xplanner.export.IterationDataSource;

/**
 * The Class IterationDataSourceTestScript.
 */
public class IterationDataSourceTestScript extends AbstractDatabaseTestScript {
    
    /** The source. */
    IterationDataSource source;
    
    /** The iteration. */
    protected Iteration iteration;
    
    /** The story. */
    protected UserStory story;
    
    /** The task. */
    protected Task task;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
     */
    @Override
	public void setUp() throws Exception {
        super.setUp();

        Project project = newProject();
        iteration = newIteration(project);
        story = newUserStory(iteration);
    }

    /** Test next_ one story no task.
     *
     * @throws Exception
     *             the exception
     */
    public void testNext_OneStoryNoTask() throws Exception {
        source = new IterationDataSource(iteration, getSession());
        assertFieldValues(story, null);
        assertFalse("next", source.next());
    }

    /** Test next_ one story with task.
     *
     * @throws Exception
     *             the exception
     */
    public void testNext_OneStoryWithTask() throws Exception {
        task = newTask(story);
        source = new IterationDataSource(iteration, getSession());
        assertFieldValues(story, task);
        assertFalse("next", source.next());
    }

    /** Test next_ two stories with different priority.
     *
     * @throws Exception
     *             the exception
     */
    public void testNext_TwoStoriesWithDifferentPriority () throws Exception {
        story.setName("aaaa"); //first in alphabetical order
        story.setPriority(4); //second in priority order
        task = newTask(story);
        task.setName("aaa task");
        UserStory anotherStory = newUserStory(iteration);
        anotherStory.setName("zzzz"); //second in alphabetical order
        anotherStory.setPriority(1); //first in priority order
        Task anotherTask = newTask(anotherStory);
        anotherTask.setName("zzz task");
        source = new IterationDataSource(iteration, getSession());
        assertFieldValues(anotherStory, anotherTask);
        assertFieldValues(story, task);
        assertFalse("next", source.next());
    }

    /** Assert field values.
     *
     * @param story
     *            the story
     * @param task
     *            the task
     * @throws JRException
     *             the JR exception
     */
    private void assertFieldValues(UserStory story, Task task) throws JRException {
        assertTrue("no next", source.next());
        assertEquals("story name", story.getName(),source.getFieldValue("StoryName"));
        if (task != null)
            assertEquals("task name", task.getName(),source.getFieldValue("TaskName"));
    }
}