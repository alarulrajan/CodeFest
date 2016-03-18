package com.technoetic.xplanner.charts;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class DataSamplingJob extends QuartzJobBean {
	private DataSamplingCommand dataSamplingCommand;
	public static final String GROUP = "xplanner";
	public static final String NAME = "datasamplingJob";

	private final Logger LOG = Logger.getLogger(DataSamplingJob.class);

	public DataSamplingCommand getDataSamplingCommand() {
		return this.dataSamplingCommand;
	}

	public void setDataSamplingCommand(
			final DataSamplingCommand dataSamplingSupport) {
		this.dataSamplingCommand = dataSamplingSupport;
	}

	@Override
	protected void executeInternal(final JobExecutionContext context)
			throws JobExecutionException {
		this.LOG.info("generating data samples...");
		this.dataSamplingCommand.execute();
		this.LOG.info("generating data samples...Done");
	}

}
