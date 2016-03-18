package com.technoetic.xplanner.mail;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.technoetic.xplanner.util.TimeGenerator;

/**
 * User: Tomasz Siwiec Date: Nov 02, 2004 Time: 9:52:55 PM
 */
public class MissingTimeEntryEmailJob {
	private final Logger log = Logger.getLogger(MissingTimeEntryEmailJob.class);
	public static final String NAME = "emailnotificationJob";
	public static final String GROUP = "xplanner";

	private MissingTimeEntryNotifier missingTimeEntryNotifier;
	private TimeGenerator timeGenerator;

	public MissingTimeEntryNotifier getMissingTimeEntryNotifier() {
		return this.missingTimeEntryNotifier;
	}

	public void setMissingTimeEntryNotifier(
			final MissingTimeEntryNotifier missingTimeEntryNotifier) {
		this.missingTimeEntryNotifier = missingTimeEntryNotifier;
	}

	public TimeGenerator getTimeGenerator() {
		return this.timeGenerator == null ? new TimeGenerator()
				: this.timeGenerator;
	}

	public void setTimeGenerator(final TimeGenerator timeGenerator) {
		this.timeGenerator = timeGenerator;
	}

	protected void executeInternal(final JobExecutionContext context)
			throws JobExecutionException {
		this.missingTimeEntryNotifier.execute();
	}

}
