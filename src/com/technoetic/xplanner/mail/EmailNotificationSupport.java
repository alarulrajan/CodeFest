/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.mail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.DomainSpecificPropertiesFactory;
import com.technoetic.xplanner.XPlannerProperties;

/**
 * User: mprokopowicz Date: Feb 3, 2006 Time: 5:36:56 PM
 */
public class EmailNotificationSupport {
	private final EmailFormatter emailFormatter;
	private final DomainSpecificPropertiesFactory propertiesFactory;
	private static final Logger log = Logger
			.getLogger(EmailNotificationSupport.class);
	private final EmailMessageFactory emailMessageFactory;
	static final String XPLANNER_MAIL_FROM_KEY = "xplanner.mail.from";

	public EmailNotificationSupport(final EmailFormatter emailFormatter,
			final EmailMessageFactory emailMessageFactory,
			final DomainSpecificPropertiesFactory propertiesFactory) {
		this.emailFormatter = emailFormatter;
		this.emailMessageFactory = emailMessageFactory;
		this.propertiesFactory = propertiesFactory;
	}

	public void sendNotifications(
			final Map<Integer, List<Object>> notificationEmails,
			final Map<String, Object> params) {
		final Set<Integer> keySet = notificationEmails.keySet();
		final Iterator<Integer> iterator = keySet.iterator();
		final ResourceBundle bundle = ResourceBundle
				.getBundle("EmailResourceBundle");
		final String subject = bundle.getString((String) params
				.get(EmailFormatterImpl.SUBJECT));
		while (iterator.hasNext()) {
			final Integer id = iterator.next();
			final List<Object> bodyEntryList = notificationEmails.get(id);
			try {
				final EmailMessage emailMessage = this.emailMessageFactory
						.createMessage();
				emailMessage.setRecipient(id.intValue());
				emailMessage.setSubject(subject);
				emailMessage
						.setFrom(this.propertiesFactory
								.getDefaultProperties()
								.getProperty(
										EmailNotificationSupport.XPLANNER_MAIL_FROM_KEY));
				final String formatedText = this.emailFormatter
						.formatEmailEntry(bodyEntryList, params);
				emailMessage.setBody(formatedText);
				emailMessage.send();
			} catch (final Exception e) {
				EmailNotificationSupport.log.error("Error sending email: ", e);
			}
		}
	}

	public void sendNotifications(
			final Map<Integer, List<Object>> notificationEmails,
			final String emailHeaderKey, final String subjectKey) {
		final Set<Integer> keySet = notificationEmails.keySet();
		final Iterator<Integer> iterator = keySet.iterator();
		final ResourceBundle bundle = ResourceBundle
				.getBundle("ResourceBundle");
		final String subject = bundle.getString(subjectKey);
		final String emailHeader = bundle.getString(emailHeaderKey);
		final String emailFooter = bundle
				.getString(MissingTimeEntryNotifier.EMAIL_BODY_FOOTER);
		final String emailTaskHeader = bundle
				.getString(MissingTimeEntryNotifier.EMAIL_TASK_HEADER);
		final String emailStoryHeader = bundle
				.getString(MissingTimeEntryNotifier.EMAIL_STORY_HEADER);
		while (iterator.hasNext()) {
			final Integer id = iterator.next();
			final List<Object> bodyEntryList = notificationEmails.get(id);
			try {
				final EmailMessage emailMessage = this.emailMessageFactory
						.createMessage();
				emailMessage.setRecipient(id.intValue());
				emailMessage.setSubject(subject);
				emailMessage
						.setFrom(this.propertiesFactory
								.getDefaultProperties()
								.getProperty(
										EmailNotificationSupport.XPLANNER_MAIL_FROM_KEY));
				final String formatedText = this.emailFormatter
						.formatEmailEntry(emailHeader, emailFooter,
								emailStoryHeader, emailTaskHeader,
								bodyEntryList);
				emailMessage.setBody(formatedText);
				emailMessage.send();
			} catch (final Exception e) {
				EmailNotificationSupport.log.error("Error sending email: ", e);
			}
		}
	}

	public void compileEmail(
			final Map<Integer, List<Object>> notificationEmails,
			final int receiverId, final Person acceptor, final Task task,
			final UserStory story) {
		List<Object> emailBodyList;
		if (notificationEmails.containsKey(new Integer(receiverId))) {
			emailBodyList = notificationEmails.get(new Integer(receiverId));
		} else {
			emailBodyList = new ArrayList<Object>();
			notificationEmails.put(new Integer(receiverId), emailBodyList);
		}
		final List<Object> entryList = new ArrayList<Object>();
		entryList.add(task);
		entryList.add(story);
		if (acceptor != null) {
			entryList.add(acceptor.getName());
		}
		emailBodyList.add(entryList);
	}

	public boolean isProjectToBeNotified(
			final Map<Integer, Boolean> projectsToBeNotified,
			final Project project) {
		Boolean isNotified = projectsToBeNotified.get(new Integer(project
				.getId()));
		if (isNotified == null) {
			isNotified = Boolean.valueOf(this.isProjectToBeNotified(project));
			projectsToBeNotified.put(new Integer(project.getId()), isNotified);
		}
		return isNotified.booleanValue();
	}

	public boolean isProjectToBeNotified(final Project project) {
		final Properties projectDynamicProperties = this.propertiesFactory
				.createPropertiesFor(project);
		final String stringValue = projectDynamicProperties.getProperty(
				XPlannerProperties.SEND_NOTIFICATION_KEY,
				Boolean.TRUE.toString());
		return Boolean.valueOf(stringValue).booleanValue();
	}
}
