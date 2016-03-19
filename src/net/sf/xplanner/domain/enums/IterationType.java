package net.sf.xplanner.domain.enums;

import java.util.Arrays;
import java.util.List;

/**
 * The Enum IterationType.
 */
public enum IterationType {
	
	/** The regular. */
	REGULAR(0), 
 /** The backlog. */
 BACKLOG(1);
	
	/** The all values. */
	private static List<IterationType> ALL_VALUES = Arrays.asList(IterationType
			.values());
	
	/** The id. */
	private int id;

	/**
     * Instantiates a new iteration type.
     *
     * @param id
     *            the id
     */
	private IterationType(final int id) {
		this.id = id;
	}

	/**
     * Gets the code.
     *
     * @return the code
     */
	public int getCode() {
		return this.id;
	}

	/**
     * Gets the message code.
     *
     * @return the message code
     */
	public String getMessageCode() {
		return "iteration.type." + this.toString().toLowerCase();
	}

	/**
     * Gets the all values.
     *
     * @return the all values
     */
	public static List<IterationType> getAllValues() {
		return IterationType.ALL_VALUES;
	}
}
