package com.technoetic.xplanner.tags;

/*
 * $Header$
 * $Revision: 70 $
 * $Date: 2004-09-27 21:26:11 +0300 (Пн, 27 сен 2004) $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */

import java.awt.Color;

import junit.framework.TestCase;

import com.technoetic.xplanner.XPlannerTestSupport;

/**
 * The Class TestProgressBarImageTag.
 */
public class TestProgressBarImageTag extends TestCase {
    
    /** The tag. */
    ProgressBarImageTag tag = new ProgressBarImageTag();
    
    /** The support. */
    private XPlannerTestSupport support;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        support = new XPlannerTestSupport();
        tag.setPageContext( support.pageContext );
    }

    /** Test get foreground color.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetForegroundColor() throws Exception {
        tag.setComplete(true);
        assertEquals(ProgressBarImageTag.COMPLETED_COLOR, tag.getForegroundColor());
        tag.setComplete(false);
        assertEquals(ProgressBarImageTag.UNCOMPLETED_COLOR, tag.getForegroundColor());
    }

    /** Test get background color.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetBackgroundColor() throws Exception {
        tag.setActual(10.0);
        tag.setEstimate(11.0);
        assertEquals(ProgressBarImageTag.UNWORKED_COLOR, tag.getBackgroundColor());
        tag.setActual(10.0);
        tag.setEstimate(9.0);
        assertEquals(ProgressBarImageTag.EXCEEDED_COLOR, tag.getBackgroundColor());        
    }

    /** Test get foreground color in print mode.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetForegroundColorInPrintMode() throws Exception {
        support.request.addParameter( PrintLinkTag.PRINT_PARAMETER_NAME, "" );
        tag.setComplete(true);
        assertEquals(Color.GRAY, tag.getForegroundColor());
        tag.setComplete(false);
        assertEquals(Color.DARK_GRAY, tag.getForegroundColor());
    }

     /** Test get background color in print mode.
         *
         * @throws Exception
         *             the exception
         */
     public void testGetBackgroundColorInPrintMode() throws Exception {
        support.request.addParameter( PrintLinkTag.PRINT_PARAMETER_NAME, "" );
        tag.setActual(10.0);
        tag.setEstimate(11.0);
        assertEquals(Color.LIGHT_GRAY, tag.getBackgroundColor());
        tag.setActual(10.0);
        tag.setEstimate(9.0);
        assertEquals(Color.BLACK, tag.getBackgroundColor());
    }


    /** Test get bar value.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetBarValue() throws Exception {
        tag.setActual(10.0);
        tag.setEstimate(11.0);
        assertEquals(10.0, tag.getBarValue(), 0);
        tag.setActual(10.0);
        tag.setEstimate(9.0);
        assertEquals(9.0, tag.getBarValue(), 0);
    }

    /** Test get max value.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetMaxValue() throws Exception {
        tag.setActual(10.0);
        tag.setEstimate(11.0);
        assertEquals(11.0, tag.getMaxValue(), 0);
        tag.setActual(10.0);
        tag.setEstimate(9.0);
        assertEquals(10.0, tag.getMaxValue(), 0);
    }
}