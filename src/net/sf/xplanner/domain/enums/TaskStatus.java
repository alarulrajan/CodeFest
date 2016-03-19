package net.sf.xplanner.domain.enums;

import org.apache.commons.lang.StringUtils;

/**
 * The Enum TaskStatus.
 */
public enum TaskStatus {
    
    /** The non started. */
    NON_STARTED(' ', "notStarted"), 
 /** The started. */
 STARTED('S', "started"), 
 /** The completed. */
 COMPLETED('C',
            "completed");

    /** The status. */
    private char status;
    
    /** The status name. */
    private String statusName;

    /**
     * Instantiates a new task status.
     *
     * @param status
     *            the status
     * @param statusName
     *            the status name
     */
    private TaskStatus(final char status, final String statusName) {
        this.status = status;
        this.statusName = statusName;
    }

    /**
     * From code.
     *
     * @param code
     *            the code
     * @return the task status
     */
    public static TaskStatus fromCode(final char code) {
        final TaskStatus[] values = TaskStatus.values();
        for (final TaskStatus taskStatus : values) {
            if (taskStatus.status == code) {
                return taskStatus;
            }
        }
        return TaskStatus.NON_STARTED;
    }

    /**
     * From name.
     *
     * @param name
     *            the name
     * @return the task status
     */
    public static TaskStatus fromName(final String name) {
        final TaskStatus[] values = TaskStatus.values();
        for (final TaskStatus taskStatus : values) {
            if (StringUtils.equalsIgnoreCase(taskStatus.statusName, name)) {
                return taskStatus;
            }
        }
        return TaskStatus.NON_STARTED;
    }
}
