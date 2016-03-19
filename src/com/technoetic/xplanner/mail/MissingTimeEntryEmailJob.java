package com.technoetic.xplanner.mail;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.technoetic.xplanner.util.TimeGenerator;

/**
 * User: Tomasz Siwiec Date: Nov 02, 2004 Time: 9:52:55 PM.
 */
public class MissingTimeEntryEmailJob {
	
	/** The log. */
	private final Logger log = Logger.getLogger(MissingTimeEntryEmailJob.class);
	
	/** The Constant NAME. */
	public static final String NAME = "emailnotificationJob";
	
	/** The Constant GROUP. */
	public static final String GROUP = "xplanner";

	/** The missing time entry notifier. */
	private MissingTimeEntryNotifier missingTimeEntryNotifier;
	
	/** The time generator. */
	private TimeGenerator timeGenerator;

	/**
     * Gets the missing time entry notifier.
     *
     * @return the missing time entry notifier
     */
	public MissingTimeEntryNotifier getMissingTimeEntryNotifier() {
		return this.missingTimeEntryNotifier;
	}

	/**
     * Sets the missing time entry notifier.
     *
     * @param missingTimeEntryNotifier
     *            the new missing time entry notifier
     */
	public void setMissingTimeEntryNotifier(
			final MissingTimeEntryNotifier missingTimeEntryNotifier) {
		this.missingTimeEntryNotifier = missingTimeEntryNotifier;
	}

	/**
     * Gets the time generator.
     *
     * @return the time generator
     */
	public TimeGenerator getTimeGenerator() {
		return this.timeGenerator == null ? new TimeGenerator()
				: this.timeGenerator;
	}

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
     * Execute internal.
     *
     * @param context
     *            the context
     * @throws JobExecutionException
     *             the job execution exception
     */
	protected void executeInternal(final JobExecutionContext context)
			throws JobExecutionException {
		this.missingTimeEntryNotifier.execute();
	}

}
