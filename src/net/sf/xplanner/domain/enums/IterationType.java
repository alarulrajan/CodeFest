package net.sf.xplanner.domain.enums;

import java.util.Arrays;
import java.util.List;

public enum IterationType {
	REGULAR(0), BACKLOG(1);
	private static List<IterationType> ALL_VALUES = Arrays.asList(IterationType
			.values());
	private int id;

	private IterationType(final int id) {
		this.id = id;
	}

	public int getCode() {
		return this.id;
	}

	public String getMessageCode() {
		return "iteration.type." + this.toString().toLowerCase();
	}

	public static List<IterationType> getAllValues() {
		return IterationType.ALL_VALUES;
	}
}
