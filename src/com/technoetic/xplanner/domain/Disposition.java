/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.domain;

/**
 * The Interface Disposition.
 */
public interface Disposition {
	
	/** The planned text. */
	String PLANNED_TEXT = "Planned";
	
	/** The carried over text. */
	String CARRIED_OVER_TEXT = "Carried Over";
	
	/** The discovered text. */
	String DISCOVERED_TEXT = "Discovered";
	
	/** The added text. */
	String ADDED_TEXT = "Added";
	
	/** The overhead text. */
	String OVERHEAD_TEXT = "Overhead";

	/** The planned name. */
	String PLANNED_NAME = "planned";
	
	/** The carried over name. */
	String CARRIED_OVER_NAME = "carriedOver";
	
	/** The discovered name. */
	String DISCOVERED_NAME = "discovered";
	
	/** The added name. */
	String ADDED_NAME = "added";
	
	/** The overhead name. */
	String OVERHEAD_NAME = "overhead";

	/** The planned key. */
	String PLANNED_KEY = "disposition.planned";
	
	/** The carried over key. */
	String CARRIED_OVER_KEY = "disposition.carriedOver";
	
	/** The discovered key. */
	String DISCOVERED_KEY = "disposition.discovered";
	
	/** The added key. */
	String ADDED_KEY = "disposition.added";
	
	/** The overhead key. */
	String OVERHEAD_KEY = "disposition.overhead";

	/** The planned abbreviation key. */
	String PLANNED_ABBREVIATION_KEY = "disposition.planned.abbreviation";
	
	/** The carried over abbreviation key. */
	String CARRIED_OVER_ABBREVIATION_KEY = "disposition.carriedOver.abbreviation";
	
	/** The discovered abbreviation key. */
	String DISCOVERED_ABBREVIATION_KEY = "disposition.discovered.abbreviation";
	
	/** The added abbreviation key. */
	String ADDED_ABBREVIATION_KEY = "disposition.added.abbreviation";
	
	/** The overhead abbreviation key. */
	String OVERHEAD_ABBREVIATION_KEY = "disposition.overhead.abbreviation";
}
