/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.mail;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.db.TaskQueryHelper;
import com.technoetic.xplanner.util.TimeGenerator;

/**
 * User: Mateusz Prokopowicz Date: Apr 19, 2005 Time: 1:34:22 PM
 */
public class MissingTimeEntryNotifier implements
		com.technoetic.xplanner.Command {
	private static Logger log = Logger
			.getLogger(MissingTimeEntryNotifier.class);

	public static final String EMAIL_TASK_HEADER = "iteration.metrics.tableheading.task";
	public static final String EMAIL_STORY_HEADER = "iteration.metrics.tableheading.story";
	public static final String SUBJECT_FOR_ACCEPTORS = "job.emailnotifier.subjectForAcceptors";
	public static final String SUBJECT_FOR_PROJECT_LEADS = "job.emailnotifier.subjectForProjectLeeds";
	public static final String EMAIL_BODY_HEADER_FOR_ACCEPTORS = "job.emailnotifier.bodyHeaderForAcceptors";
	public static final String EMAIL_BODY_HEADER_FOR_PROJECT_LEADERS = "job.emailnotifier.bodyheaderForProjectLeeds";
	public static final String EMAIL_BODY_FOOTER = "job.emailnotifier.bodyFooter";

	private final TaskQueryHelper taskQueryHelper;
	private final EmailNotificationSupport emailNotificationSupport;
	private TimeGenerator timeGenerator;

	public void setTimeGenerator(final TimeGenerator timeGenerator) {
		this.timeGenerator = timeGenerator;
	}

	public MissingTimeEntryNotifier(final TaskQueryHelper taskQueryHelper,
			final EmailNotificationSupport emailNotificationSupport) {
		this.taskQueryHelper = taskQueryHelper;
		this.emailNotificationSupport = emailNotificationSupport;
	}

	public void sendMissingTimeEntryReportToLeads(final Date endDate) {
		Collection col;
		final HashMap notificationEmails = new HashMap();
		col = this.taskQueryHelper.getProjectLeedsEmailNotification(endDate);
		for (final Iterator iterator = col.iterator(); iterator.hasNext();) {
			final Object[] objects = (Object[]) iterator.next();
			final Task task = (Task) objects[0];
			final UserStory story = (UserStory) objects[1];
			final Person receiver = (Person) objects[2];
			final Person acceptor = (Person) objects[3];

			this.emailNotificationSupport.compileEmail(notificationEmails,
					receiver.getId(), acceptor, task, story);
		}
		this.emailNotificationSupport.sendNotifications(notificationEmails,
				MissingTimeEntryNotifier.EMAIL_BODY_HEADER_FOR_PROJECT_LEADERS,
				MissingTimeEntryNotifier.SUBJECT_FOR_PROJECT_LEADS);
		MissingTimeEntryNotifier.log
				.debug("Notifications have been sent to project leads.");
	}

	public void sendMissingTimeEntryReminderToAcceptors(final Date endDate) {
		final Map projectsToBeNotified = new HashMap();
		final Map notificationEmails = new HashMap();
		final Collection col = this.taskQueryHelper
				.getTaskAcceptorsEmailNotification(endDate);
		for (final Iterator it = col.iterator(); it.hasNext();) {
			final Object[] objects = (Object[]) it.next();
			final Task task = (Task) objects[0];
			final UserStory story = (UserStory) objects[1];
			final Project project = (Project) objects[2];
			if (this.emailNotificationSupport.isProjectToBeNotified(
					projectsToBeNotified, project)) {
				this.emailNotificationSupport.compileEmail(notificationEmails,
						task.getAcceptorId(), null, task, story);
			}
		}
		this.emailNotificationSupport.sendNotifications(notificationEmails,
				MissingTimeEntryNotifier.EMAIL_BODY_HEADER_FOR_ACCEPTORS,
				MissingTimeEntryNotifier.SUBJECT_FOR_ACCEPTORS);
		MissingTimeEntryNotifier.log
				.debug("Notifications have been sent to acceptors.");
	}

	@Override
	public void execute() {
		// fixme the notifier should be configured with a time window to check
		// for idle started tasks. Right now we hard code the previous day which
		// may not be the right day if the job schedule is changed and if team
		// is distributed
		final Date yesterdayMidnight = TimeGenerator.shiftDate(
				this.timeGenerator.getTodaysMidnight(), Calendar.DATE, -1);
		MissingTimeEntryNotifier.log
				.debug("Send notification for tasks with no time entry after "
						+ yesterdayMidnight);
		this.sendMissingTimeEntryReminderToAcceptors(yesterdayMidnight);
		this.sendMissingTimeEntryReportToLeads(yesterdayMidnight);
	}
}
