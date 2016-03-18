/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.mail;

import com.technoetic.xplanner.domain.repository.ObjectRepository;

public class EmailMessageFactory {
	private final ObjectRepository personRepository;
	private Class emailMessageClass;
	public EmailMessage emailMessage;

	public EmailMessageFactory(final ObjectRepository objectRepository) {
		this.personRepository = objectRepository;
	}

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

	public void setEmailMessageClass(final Class emailMessageClass) {
		this.emailMessageClass = emailMessageClass;
	}

	public void setEmailMessage(final EmailMessage newEmailMessage)
			throws java.lang.InstantiationException,
			java.lang.IllegalAccessException {
		this.emailMessage = newEmailMessage;
	}
}
