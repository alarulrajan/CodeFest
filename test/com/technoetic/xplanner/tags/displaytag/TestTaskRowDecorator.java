package com.technoetic.xplanner.tags.displaytag;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Mar 1, 2005
 * Time: 9:04:26 PM
 */

import java.util.ArrayList;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;

/**
 * The Class TestTaskRowDecorator.
 */
public class TestTaskRowDecorator extends TestCase
{
    
    /** The task row decorator. */
    TaskRowDecorator taskRowDecorator;

    /** Test get css classes.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetCssClasses() throws Exception
    {
        taskRowDecorator = new TaskRowDecorator();
        Task task = new Task();
        TimeEntry t1 = new TimeEntry();
        t1.setDuration(1);
        ArrayList timeEntries = new ArrayList();
        timeEntries.add(t1);
        task.setTimeEntries(timeEntries);

        Row row = new Row(task, 0);
        assertEquals("task_started", taskRowDecorator.getCssClasses(row));

        task.setCompleted(true);
        assertEquals("task_completed", taskRowDecorator.getCssClasses(row));

        task.setTimeEntries(null);
        task.setCompleted(false);
        task.setAcceptorId(1);
        assertEquals("task_unestimated", taskRowDecorator.getCssClasses(row));

        task.setAcceptorId(0);
        assertEquals("task_unassigned", taskRowDecorator.getCssClasses(row));

        task.setAcceptorId(1);
        task.setEstimatedHours(1.1);
        assertEquals("", taskRowDecorator.getCssClasses(row));
    }
}