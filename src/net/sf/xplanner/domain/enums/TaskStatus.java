package net.sf.xplanner.domain.enums;

import org.apache.commons.lang.StringUtils;

public enum TaskStatus {
	NON_STARTED(' ', "notStarted"), STARTED('S', "started"), COMPLETED('C',
			"completed");

	private char status;
	private String statusName;

	private TaskStatus(final char status, final String statusName) {
		this.status = status;
		this.statusName = statusName;
	}

	public static TaskStatus fromCode(final char code) {
		final TaskStatus[] values = TaskStatus.values();
		for (final TaskStatus taskStatus : values) {
			if (taskStatus.status == code) {
				return taskStatus;
			}
		}
		return TaskStatus.NON_STARTED;
	}

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
