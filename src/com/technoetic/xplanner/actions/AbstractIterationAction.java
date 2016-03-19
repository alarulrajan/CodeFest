/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 23, 2005
 * Time: 12:05:30 PM
 */
package com.technoetic.xplanner.actions;

import com.technoetic.xplanner.charts.DataSampler;
import com.technoetic.xplanner.domain.Identifiable;

/**
 * The Class AbstractIterationAction.
 *
 * @param <T>
 *            the generic type
 */
public abstract class AbstractIterationAction<T extends Identifiable> extends
        AbstractAction<T> {
    
    /** The data sampler. */
    protected DataSampler dataSampler;

    /**
     * Sets the data sampler.
     *
     * @param dataSampler
     *            the new data sampler
     */
    public void setDataSampler(final DataSampler dataSampler) {
        this.dataSampler = dataSampler;
    }

    /**
     * Gets the data sampler.
     *
     * @return the data sampler
     */
    public DataSampler getDataSampler() {
        return this.dataSampler;
    }
}