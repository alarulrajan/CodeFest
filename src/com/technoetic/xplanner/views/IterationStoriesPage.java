/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.views;

/**
 * The Interface IterationStoriesPage.
 */
public interface IterationStoriesPage extends IterationPage {
    
    /** The stories selected column. */
    String STORIES_SELECTED_COLUMN = "stories.tableheading.selected";
    
    /** The stories order column. */
    String STORIES_ORDER_COLUMN = "stories.tableheading.order";
    
    /** The stories priority column. */
    String STORIES_PRIORITY_COLUMN = "stories.tableheading.priority";
    
    /** The stories progress column. */
    String STORIES_PROGRESS_COLUMN = "stories.tableheading.progress";
    
    /** The stories estimated original hours column. */
    String STORIES_ESTIMATED_ORIGINAL_HOURS_COLUMN = "stories.tableheading.estimated_original_hours";
    
    /** The stories estimated hours column. */
    String STORIES_ESTIMATED_HOURS_COLUMN = "stories.tableheading.estimated_hours";
    
    /** The stories actual hours column. */
    String STORIES_ACTUAL_HOURS_COLUMN = "stories.tableheading.actual_hours";
    
    /** The stories remaining hours column. */
    String STORIES_REMAINING_HOURS_COLUMN = "stories.tableheading.remaining_hours";
    
    /** The stories iteration start estimate hours column. */
    String STORIES_ITERATION_START_ESTIMATE_HOURS_COLUMN = "stories.tableheading.iteration_start_estimate_hours";
    
    /** The import stories link. */
    String IMPORT_STORIES_LINK = "iteration.link.import_stories";
    
    /** The accuracylink. */
    String ACCURACYLINK = "iteration.link.accuracy";
}