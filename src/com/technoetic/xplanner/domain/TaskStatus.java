package com.technoetic.xplanner.domain;

import java.util.Arrays;
import java.util.List;

@Deprecated
public class TaskStatus implements Comparable {
	public static final TaskStatus STARTED = new TaskStatus("S");
	public static final TaskStatus NON_STARTED = new TaskStatus("");
	public static final TaskStatus COMPLETED = new TaskStatus("C");
	public static final List statusOrdering = Arrays.asList(new TaskStatus[] {
			TaskStatus.STARTED, TaskStatus.NON_STARTED, TaskStatus.COMPLETED });

	private final String status;

	private TaskStatus(final String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}

	@Override
	public int compareTo(final Object o) {
		return TaskStatus.statusOrdering.indexOf(this)
				- TaskStatus.statusOrdering.indexOf(o);
	}
}
