package com.technoetic.xplanner.mail;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import net.sf.xplanner.domain.Person;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateOperations;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.db.hibernate.HibernateOperationsWrapper;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.domain.repository.RepositoryException;

// DEBT: Should not have ANY sql or hql in any domain class. Instead it should be injected with one of the repository
public class EmailMessageImpl implements EmailMessage {
	protected Logger log = Logger.getLogger(this.getClass());
	private final Message msg;
	private StringWriter bodyWriter;
	private final ArrayList attachments = new ArrayList();
	HibernateOperationsWrapper hibernateOperationsWrapper;
	private ObjectRepository objectRepository;

	/* package scope */
	EmailMessageImpl() throws MessagingException {
		this.hibernateOperationsWrapper = new HibernateOperationsWrapper(
				ThreadSession.get());
		final Properties transportProperties = new Properties();
		final XPlannerProperties xplannerProperties = new XPlannerProperties();
		final Object hostName = xplannerProperties
				.getProperty("xplanner.mail.smtp.host");
		if (hostName != null) {
			transportProperties.put("mail.smtp.host", hostName);
		}
		final String userName = xplannerProperties
				.getProperty("xplanner.mail.smtp.user");
		SmtpAuthenticator auth = null;
		if (StringUtils.isNotBlank(userName)) {
			final String password = xplannerProperties
					.getProperty("xplanner.mail.smtp.password");
			auth = new SmtpAuthenticator(userName, password);
			transportProperties.put("mail.smtp.auth", "true");
			transportProperties.put("mail.smtp.starttls.enable", "true");
		}
		final Object portNbr = xplannerProperties
				.getProperty("xplanner.mail.smtp.port");
		if (portNbr != null) {
			transportProperties.put("mail.smtp.port", portNbr);
			transportProperties.put("mail.smtp.socketFactory.port", portNbr);
		}
		final Session session = Session.getDefaultInstance(transportProperties,
				auth);
		session.setDebug(this.log.isDebugEnabled());
		this.msg = new MimeMessage(session);
		this.msg.setSentDate(new Date());
	}

	public void setHibernateOperations(
			final HibernateOperations hibernateOperations) {
		this.hibernateOperationsWrapper = new HibernateOperationsWrapper(
				hibernateOperations);
	}

	@Override
	public void setFrom(final String from) throws AddressException,
			MessagingException {
		this.msg.setFrom(new InternetAddress(from));
	}

	@Override
	public void setRecipient(final int personId) throws MessagingException {
		try {
			final Person person = this.getPerson(personId);
			if (StringUtils.isEmpty(person.getEmail())) {
				throw new MessagingException("no email address for user: uid="
						+ person.getUserId() + ",id=" + person.getId());
			}
			this.setRecipients(person.getEmail());
		} catch (final MessagingException e) {
			throw e;
		} catch (final Exception e) {
			throw new MessagingException("error setting recipient", e);
		}
	}

	@Override
	public void setObjectRepository(final ObjectRepository objectRepository) {
		this.objectRepository = objectRepository;
	}

	private Person getPerson(final int personId) throws RepositoryException {
		return (Person) this.objectRepository.load(personId);
	}

	@Override
	public void setRecipients(final String recipients) throws AddressException,
			MessagingException {
		if (StringUtils.isNotEmpty(recipients)) {
			this.setRecipients(Message.RecipientType.TO, recipients.split(","));
		}
	}

	@Override
	public void setRecipients(final String[] recipients)
			throws AddressException, MessagingException {
		final InternetAddress[] addresses = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addresses[i] = new InternetAddress(recipients[i]);
		}
		this.msg.setRecipients(Message.RecipientType.TO, addresses);
	}

	@Override
	public void setCcRecipients(final String recipients)
			throws MessagingException, AddressException {
		if (StringUtils.isNotEmpty(recipients)) {
			this.setRecipients(Message.RecipientType.CC, recipients.split(","));
		}
	}

	private void setRecipients(final Message.RecipientType recipientType,
			final String[] recipients) throws AddressException,
			MessagingException {
		if (recipients.length > 0) {
			final InternetAddress[] addresses = new InternetAddress[recipients.length];
			for (int i = 0; i < recipients.length; i++) {
				addresses[i] = new InternetAddress(recipients[i]);
			}
			this.msg.setRecipients(recipientType, addresses);
		}
	}

	@Override
	public void setBody(final String body) throws MessagingException {
		this.bodyWriter = new StringWriter();
		this.bodyWriter.write(body);
	}

	@Override
	public PrintWriter getBodyWriter() {
		this.bodyWriter = new StringWriter();
		return new PrintWriter(this.bodyWriter);
	}

	@Override
	public void setSubject(final String subject) throws MessagingException {
		this.msg.setSubject(subject);
	}

	@Override
	public void setSentDate(final Date sentDate) throws MessagingException {
		this.msg.setSentDate(sentDate);
	}

	@Override
	public void addAttachment(final String filename) throws MessagingException {
		final File file = new File(filename);
		this.addAttachment(file.getName(), file);
	}

	@Override
	public void addAttachment(final String filename, final File file)
			throws MessagingException {
		final MimeBodyPart part = new MimeBodyPart();
		final FileDataSource fds = new FileDataSource(file);
		part.setDataHandler(new DataHandler(fds));
		part.setFileName(filename);
		this.attachments.add(part);
	}

	@Override
	public void send() throws MessagingException {
		final MimeMultipart parts = new MimeMultipart();
		if (this.bodyWriter == null) {
			this.setBody("");
		}
		final MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setContent(this.bodyWriter.toString(), "text/html");
		parts.addBodyPart(bodyPart);
		final Iterator iter = this.attachments.iterator();
		while (iter.hasNext()) {
			parts.addBodyPart((MimeBodyPart) iter.next());
		}
		this.msg.setContent(parts);

		Transport.send(this.msg);
	}
}
