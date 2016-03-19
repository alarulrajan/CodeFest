package com.technoetic.xplanner.mail;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.technoetic.xplanner.domain.repository.ObjectRepository;

/**
 * The Interface EmailMessage.
 */
public interface EmailMessage {
	
	/**
     * Sets the from.
     *
     * @param from
     *            the new from
     * @throws AddressException
     *             the address exception
     * @throws MessagingException
     *             the messaging exception
     */
	void setFrom(String from) throws AddressException, MessagingException;

	/**
     * Sets the recipients.
     *
     * @param recipients
     *            the new recipients
     * @throws AddressException
     *             the address exception
     * @throws MessagingException
     *             the messaging exception
     */
	void setRecipients(String recipients) throws AddressException,
			MessagingException;

	/**
     * Sets the recipients.
     *
     * @param recipients
     *            the new recipients
     * @throws AddressException
     *             the address exception
     * @throws MessagingException
     *             the messaging exception
     */
	void setRecipients(String[] recipients) throws AddressException,
			MessagingException;

	/**
     * Sets the body.
     *
     * @param body
     *            the new body
     * @throws MessagingException
     *             the messaging exception
     */
	void setBody(String body) throws MessagingException;

	/**
     * Gets the body writer.
     *
     * @return the body writer
     */
	PrintWriter getBodyWriter();

	/**
     * Sets the subject.
     *
     * @param subject
     *            the new subject
     * @throws MessagingException
     *             the messaging exception
     */
	void setSubject(String subject) throws MessagingException;

	/**
     * Sets the sent date.
     *
     * @param sentDate
     *            the new sent date
     * @throws MessagingException
     *             the messaging exception
     */
	void setSentDate(Date sentDate) throws MessagingException;

	/**
     * Adds the attachment.
     *
     * @param filename
     *            the filename
     * @throws MessagingException
     *             the messaging exception
     */
	void addAttachment(String filename) throws MessagingException;

	/**
     * Adds the attachment.
     *
     * @param filename
     *            the filename
     * @param file
     *            the file
     * @throws MessagingException
     *             the messaging exception
     */
	void addAttachment(String filename, File file) throws MessagingException;

	/**
     * Send.
     *
     * @throws MessagingException
     *             the messaging exception
     */
	void send() throws MessagingException;

	/**
     * Sets the cc recipients.
     *
     * @param recipients
     *            the new cc recipients
     * @throws MessagingException
     *             the messaging exception
     * @throws AddressException
     *             the address exception
     */
	void setCcRecipients(String recipients) throws MessagingException,
			AddressException;

	/**
     * Sets the recipient.
     *
     * @param personId
     *            the new recipient
     * @throws MessagingException
     *             the messaging exception
     */
	void setRecipient(int personId) throws MessagingException;

	/**
     * Sets the object repository.
     *
     * @param objectRepository
     *            the new object repository
     */
	void setObjectRepository(ObjectRepository objectRepository);
}
