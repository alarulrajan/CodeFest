/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.charts;

import net.sf.xplanner.domain.Iteration;

import com.technoetic.xplanner.util.TimeGenerator;

/**
 * User: Mateusz Prokopowicz Date: Apr 19, 2005 Time: 5:29:45 PM.
 */
public interface DataSampler {
    
    /**
     * Generate opening data samples.
     *
     * @param iteration
     *            the iteration
     */
    void generateOpeningDataSamples(Iteration iteration);

    /**
     * Generate closing data samples.
     *
     * @param iteration
     *            the iteration
     */
    void generateClosingDataSamples(Iteration iteration);

    /**
     * Sets the time generator.
     *
     * @param timeGenerator
     *            the new time generator
     */
    void setTimeGenerator(TimeGenerator timeGenerator);

    /**
     * Generate data samples.
     *
     * @param iteration
     *            the iteration
     */
    void generateDataSamples(Iteration iteration);
}
