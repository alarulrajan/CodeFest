package com.technoetic.xplanner.tags.displaytag;

import net.sf.xplanner.domain.Task;

import com.technoetic.xplanner.domain.TaskStatus;

public class TaskOrdering implements Comparable {
	public TaskStatus status;
	public int orderNo;
	public String storyName;
	public String taskName;

	public TaskOrdering(final Task task) {
		this.status = task.getStatus();
		this.orderNo = task.getUserStory().getOrderNo();
		this.storyName = task.getUserStory().getName();
		this.taskName = task.getName();
	}

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
