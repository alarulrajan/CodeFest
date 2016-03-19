package com.technoetic.xplanner.views;

/**
 * The Interface IterationPage.
 */
public interface IterationPage {
	
	/** The start time field. */
	// FIELDS
	String START_TIME_FIELD = "startDate";

	/** The start action. */
	// ACTIONS
	String START_ACTION = "iteration.status.editor.start";
	
	/** The close action. */
	String CLOSE_ACTION = "iteration.status.editor.close";

	/** The accuracy view. */
	// VIEWS
	String ACCURACY_VIEW = "iteration.link.accuracy";
	
	/** The statistics view. */
	String STATISTICS_VIEW = "iteration.link.statistics";
	
	/** The metrics view. */
	String METRICS_VIEW = "iteration.link.metrics";
}
