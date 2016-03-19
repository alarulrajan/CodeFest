/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.charts;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.Iteration;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.technoetic.xplanner.Command;
import com.technoetic.xplanner.util.TimeGenerator;

/**
 * The Class DataSamplingCommand.
 */
public class DataSamplingCommand extends HibernateDaoSupport implements Command {
    
    /** The Constant LOG. */
    public static final Logger LOG = Logger
            .getLogger(DataSamplingCommand.class);
    
    /** The Constant ITERATION_TO_SAMPLE_QUERY. */
    public static final String ITERATION_TO_SAMPLE_QUERY = "com.technoetic.xplanner.domain.IterationToSample";
    
    /** The data sampler. */
    private DataSampler dataSampler;
    
    /** The time generator. */
    private TimeGenerator timeGenerator;

    /**
     * Sets the time generator.
     *
     * @param timeGenerator
     *            the new time generator
     */
    public void setTimeGenerator(final TimeGenerator timeGenerator) {
        this.timeGenerator = timeGenerator;
    }

    /**
     * Sets the data sampler.
     *
     * @param dataSampler
     *            the new data sampler
     */
    public void setDataSampler(final DataSampler dataSampler) {
        this.dataSampler = dataSampler;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.Command#execute()
     */
    @Override
    public void execute() {
        final Date samplingDate = this.timeGenerator.getTodaysMidnight();
        final List iterations = this.getIterationList(samplingDate);
        for (int i = 0; i < iterations.size(); i++) {
            try {
                final Iteration iteration = (Iteration) iterations.get(i);
                DataSamplingCommand.LOG.debug("Generate datasamples at "
                        + samplingDate.toString());
                DataSamplingCommand.LOG.debug(" for iteration: projectId ["
                        + iteration.getProject().getId() + "], id ["
                        + iteration.getId() + "], name [" + iteration.getName()
                        + "]");

                this.dataSampler.generateDataSamples(iteration);

            } catch (final Exception e) {
                DataSamplingCommand.LOG.error("Error saving datasamples "
                        + e.getMessage());
                DataSamplingCommand.LOG.debug("Stack trace: ", e);
            }
        }
    }

    /**
     * Gets the iteration list.
     *
     * @param samplingDate
     *            the sampling date
     * @return the iteration list
     */
    public List getIterationList(final Date samplingDate) {
        final Date prevSamplingDate = TimeGenerator.shiftDate(samplingDate,
                Calendar.DAY_OF_MONTH, -1);
        final List iterationToSampleList = this.getHibernateTemplate()
                .findByNamedQueryAndNamedParam(
                        DataSamplingCommand.ITERATION_TO_SAMPLE_QUERY,
                        new String[] { "prevSamplingDate", "samplingDate" },
                        new Object[] { prevSamplingDate, samplingDate });
        for (final Iterator iterator = iterationToSampleList.iterator(); iterator
                .hasNext();) {
            final Iteration iteration = (Iteration) iterator.next();
            DataSamplingCommand.LOG.debug("Iteration " + iteration.getName()
                    + " contains " + iteration.getUserStories().size()
                    + " stories.");
        }
        DataSamplingCommand.LOG.debug("Iterations to sample on " + samplingDate
                + " size: " + iterationToSampleList.size());
        return iterationToSampleList;

    }
}
