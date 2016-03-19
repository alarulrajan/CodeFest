package com.technoetic.xplanner.tags.displaytag;

import net.sf.xplanner.domain.Task;

import com.technoetic.xplanner.domain.TaskStatus;

/**
 * The Class TaskOrdering.
 */
public class TaskOrdering implements Comparable {
	
	/** The status. */
	public TaskStatus status;
	
	/** The order no. */
	public int orderNo;
	
	/** The story name. */
	public String storyName;
	
	/** The task name. */
	public String taskName;

	/**
     * Instantiates a new task ordering.
     *
     * @param task
     *            the task
     */
	public TaskOrdering(final Task task) {
		this.status = task.getStatus();
		this.orderNo = task.getUserStory().getOrderNo();
		this.storyName = task.getUserStory().getName();
		this.taskName = task.getName();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final Object o) {
		final TaskOrdering order = (TaskOrdering) o;
		if (!this.status.equals(order.status)) {
			return this.status.compareTo(order.status);
		}
		if (this.orderNo != order.orderNo) {
			return this.orderNo - order.orderNo;
		}
		if (!this.storyName.equals(order.storyName)) {
			return this.storyName.compareTo(order.storyName);
		}
		if (!this.taskName.equals(order.taskName)) {
			return this.taskName.compareTo(order.taskName);
		}
		return 0;
	}
}
