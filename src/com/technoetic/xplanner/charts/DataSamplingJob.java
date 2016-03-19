package com.technoetic.xplanner.charts;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * The Class DataSamplingJob.
 */
public class DataSamplingJob extends QuartzJobBean {
    
    /** The data sampling command. */
    private DataSamplingCommand dataSamplingCommand;
    
    /** The Constant GROUP. */
    public static final String GROUP = "xplanner";
    
    /** The Constant NAME. */
    public static final String NAME = "datasamplingJob";

    /** The log. */
    private final Logger LOG = Logger.getLogger(DataSamplingJob.class);

    /**
     * Gets the data sampling command.
     *
     * @return the data sampling command
     */
    public DataSamplingCommand getDataSamplingCommand() {
        return this.dataSamplingCommand;
    }

    /**
     * Sets the data sampling command.
     *
     * @param dataSamplingSupport
     *            the new data sampling command
     */
    public void setDataSamplingCommand(
            final DataSamplingCommand dataSamplingSupport) {
        this.dataSamplingCommand = dataSamplingSupport;
    }

    /* (non-Javadoc)
     * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
     */
    @Override
    protected void executeInternal(final JobExecutionContext context)
            throws JobExecutionException {
        this.LOG.info("generating data samples...");
        this.dataSamplingCommand.execute();
        this.LOG.info("generating data samples...Done");
    }

}
