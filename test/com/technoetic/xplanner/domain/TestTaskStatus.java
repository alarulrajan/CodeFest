package com.technoetic.xplanner.domain;


import junit.framework.TestCase;

/**
 * The Class TestTaskStatus.
 */
public class TestTaskStatus extends TestCase
{

    /** Test compare to.
     *
     * @throws Exception
     *             the exception
     */
    public void testCompareTo() throws Exception
    {
        assertTrue(TaskStatus.STARTED.compareTo(TaskStatus.NON_STARTED) < 0);
        assertTrue(TaskStatus.STARTED.compareTo(TaskStatus.COMPLETED) < 0);
        assertTrue(TaskStatus.NON_STARTED.compareTo(TaskStatus.COMPLETED) < 0);
    }
}