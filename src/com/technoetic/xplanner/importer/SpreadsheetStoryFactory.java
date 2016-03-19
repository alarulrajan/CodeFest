package com.technoetic.xplanner.importer;

import java.util.Date;

/*
 * $Header$
 * $Revision: 540 $
 * $Date: 2005-06-07 07:03:50 -0500 (Tue, 07 Jun 2005) $
 *
 * Copyright (c) 1999-2002 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */

/**
 * A factory for creating SpreadsheetStory objects.
 */
public class SpreadsheetStoryFactory {
    
    /**
     * New instance.
     *
     * @param title
     *            the title
     * @param status
     *            the status
     * @param estimate
     *            the estimate
     * @return the spreadsheet story
     */
    public SpreadsheetStory newInstance(final String title,
            final String status, final double estimate) {
        return new SpreadsheetStory(title, status, estimate);
    }

    /**
     * New instance.
     *
     * @param storyEndDate
     *            the story end date
     * @param title
     *            the title
     * @param status
     *            the status
     * @param estimate
     *            the estimate
     * @param priority
     *            the priority
     * @return the spreadsheet story
     */
    public SpreadsheetStory newInstance(final Date storyEndDate,
            final String title, final String status, final double estimate,
            final int priority) {
        return new SpreadsheetStory(storyEndDate, title, status, estimate,
                priority);
    }

    /**
     * New instance.
     *
     * @return the spreadsheet story
     */
    public SpreadsheetStory newInstance() {
        return new SpreadsheetStory("", "", 0);
    }

}
