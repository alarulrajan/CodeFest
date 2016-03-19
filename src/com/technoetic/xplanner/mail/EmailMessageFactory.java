/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.mail;

import com.technoetic.xplanner.domain.repository.ObjectRepository;

/**
 * A factory for creating EmailMessage objects.
 */
public class EmailMessageFactory {
	
	/** The person repository. */
	private final ObjectRepository personRepository;
	
	/** The email message class. */
	private Class emailMessageClass;
	
	/** The email message. */
	public EmailMessage emailMessage;

	/**
     * Instantiates a new email message factory.
     *
     * @param objectRepository
     *            the object repository
     */
	public EmailMessageFactory(final ObjectRepository objectRepository) {
		this.personRepository = objectRepository;
	}

	/**
     * Creates a new EmailMessage object.
     *
     * @return the email message
     * @throws Exception
     *             the exception
     */
	public EmailMessage createMessage() throws Exception {
		if (this.emailMessageClass == null) {
			this.emailMessageClass = EmailMessageImpl.class;
		}

		if (this.emailMessage == null) {
			final EmailMessage emailMessage = (EmailMessage) this.emailMessageClass
					.newInstance();
			emailMessage.setObjectRepository(this.personRepository);
			return emailMessage;
		} else {
			return this.emailMessage;
		}

	}

	/**
     * Sets the email message class.
     *
     * @param emailMessageClass
     *            the new email message class
     */
	public void setEmailMessageClass(final Class emailMessageClass) {
		this.emailMessageClass = emailMessageClass;
	}

	/**
     * Sets the email message.
     *
     * @param newEmailMessage
     *            the new email message
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
	public void setEmailMessage(final EmailMessage newEmailMessage)
			throws java.lang.InstantiationException,
			java.lang.IllegalAccessException {
		this.emailMessage = newEmailMessage;
	}
}
