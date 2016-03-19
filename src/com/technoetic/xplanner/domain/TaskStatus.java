package com.technoetic.xplanner.domain;

import java.util.Arrays;
import java.util.List;

/**
 * The Class TaskStatus.
 */
@Deprecated
public class TaskStatus implements Comparable {
    
    /** The Constant STARTED. */
    public static final TaskStatus STARTED = new TaskStatus("S");
    
    /** The Constant NON_STARTED. */
    public static final TaskStatus NON_STARTED = new TaskStatus("");
    
    /** The Constant COMPLETED. */
    public static final TaskStatus COMPLETED = new TaskStatus("C");
    
    /** The Constant statusOrdering. */
    public static final List statusOrdering = Arrays.asList(new TaskStatus[] {
            TaskStatus.STARTED, TaskStatus.NON_STARTED, TaskStatus.COMPLETED });

    /** The status. */
    private final String status;

    /**
     * Instantiates a new task status.
     *
     * @param status
     *            the status
     */
    private TaskStatus(final String status) {
        this.status = status;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.status;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Object o) {
        return TaskStatus.statusOrdering.indexOf(this)
                - TaskStatus.statusOrdering.indexOf(o);
    }
}
