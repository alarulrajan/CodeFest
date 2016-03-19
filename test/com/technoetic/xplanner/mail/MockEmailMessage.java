package com.technoetic.xplanner.mail;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.orm.hibernate3.HibernateOperations;

import com.technoetic.xplanner.domain.repository.MetaRepository;
import com.technoetic.xplanner.domain.repository.ObjectRepository;

/**
 * The Class MockEmailMessage.
 */
public class MockEmailMessage implements EmailMessage {
    
    /** The set from called. */
    public boolean setFromCalled;
    
    /** The set from address exception. */
    public AddressException setFromAddressException;
    
    /** The set from messaging exception. */
    public MessagingException setFromMessagingException;
    
    /** The set from from. */
    public String setFromFrom;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.mail.EmailMessage#setFrom(java.lang.String)
     */
    public void setFrom(String from) throws AddressException, MessagingException {
        setFromCalled = true;
        setFromFrom = from;
        if (setFromAddressException != null) {
            throw setFromAddressException;
        }
        if (setFromMessagingException != null) {
            throw setFromMessagingException;
        }
    }

    /** The set recipients called. */
    public boolean setRecipientsCalled;
    
    /** The set recipients address exception. */
    public AddressException setRecipientsAddressException;
    
    /** The set recipients messaging exception. */
    public MessagingException setRecipientsMessagingException;
    
    /** The set recipients recipients. */
    public String setRecipientsRecipients;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.mail.EmailMessage#setRecipients(java.lang.String)
     */
    public void setRecipients(String recipients) throws AddressException, MessagingException {
        setRecipientsCalled = true;
        setRecipientsRecipients = recipients;
        if (setRecipientsAddressException != null) {
            throw setRecipientsAddressException;
        }
        if (setRecipientsMessagingException != null) {
            throw setRecipientsMessagingException;
        }
    }

    /** The set recipients2 called. */
    public boolean setRecipients2Called;
    
    /** The set recipients2 address exception. */
    public AddressException setRecipients2AddressException;
    
    /** The set recipients2 messaging exception. */
    public MessagingException setRecipients2MessagingException;
    
    /** The set recipients2 recipients. */
    public String[] setRecipients2Recipients;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.mail.EmailMessage#setRecipients(java.lang.String[])
     */
    public void setRecipients(String[] recipients) throws AddressException, MessagingException {
        setRecipients2Called = true;
        setRecipients2Recipients = recipients;
        if (setRecipientsAddressException != null) {
            throw setRecipients2AddressException;
        }
        if (setRecipientsMessagingException != null) {
            throw setRecipients2MessagingException;
        }
    }

    /** The set body called. */
    public boolean setBodyCalled;
    
    /** The set body messaging exception. */
    public MessagingException setBodyMessagingException;
    
    /** The set body body. */
    public String setBodyBody;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.mail.EmailMessage#setBody(java.lang.String)
     */
    public void setBody(String body) throws MessagingException {
        setBodyCalled = true;
        setBodyBody = body;
        if (setBodyMessagingException != null) {
            throw setBodyMessagingException;
        }
    }

    /** The get body writer called. */
    public boolean getBodyWriterCalled;
    
    /** The get body writer return. */
    public java.io.PrintWriter getBodyWriterReturn;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.mail.EmailMessage#getBodyWriter()
     */
    public java.io.PrintWriter getBodyWriter() {
        getBodyWriterCalled = true;
        return getBodyWriterReturn;
    }

    /** The set subject called. */
    public boolean setSubjectCalled;
    
    /** The set subject messaging exception. */
    public MessagingException setSubjectMessagingException;
    
    /** The set subject subject. */
    public String setSubjectSubject;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.mail.EmailMessage#setSubject(java.lang.String)
     */
    public void setSubject(String subject) throws MessagingException {
        setSubjectCalled = true;
        setSubjectSubject = subject;
        if (setSubjectMessagingException != null) {
            throw setSubjectMessagingException;
        }
    }

    /** The set sent date called. */
    public boolean setSentDateCalled;
    
    /** The set sent date messaging exception. */
    public MessagingException setSentDateMessagingException;
    
    /** The set sent date sent date. */
    public java.util.Date setSentDateSentDate;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.mail.EmailMessage#setSentDate(java.util.Date)
     */
    public void setSentDate(java.util.Date sentDate) throws MessagingException {
        setSentDateCalled = true;
        setSentDateSentDate = sentDate;
        if (setSentDateMessagingException != null) {
            throw setSentDateMessagingException;
        }
    }

    /** The add attachment called. */
    public boolean addAttachmentCalled;
    
    /** The add attachment messaging exception. */
    public MessagingException addAttachmentMessagingException;
    
    /** The add attachment filename. */
    public String addAttachmentFilename;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.mail.EmailMessage#addAttachment(java.lang.String)
     */
    public void addAttachment(String filename) throws MessagingException {
        addAttachmentCalled = true;
        addAttachmentFilename = filename;
        if (addAttachmentMessagingException != null) {
            throw addAttachmentMessagingException;
        }
    }

    /** The add attachment file. */
    public File addAttachmentFile;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.mail.EmailMessage#addAttachment(java.lang.String, java.io.File)
     */
    public void addAttachment(String filename, File file) throws MessagingException {
        addAttachmentCalled = true;
        addAttachmentFilename = filename;
        addAttachmentFile = file;
        if (addAttachmentMessagingException != null) {
            throw addAttachmentMessagingException;
        }
    }

    /** The send called. */
    public boolean sendCalled;
    
    /** The send messaging exception. */
    public MessagingException sendMessagingException;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.mail.EmailMessage#send()
     */
    public void send() throws MessagingException {
        sendCalled = true;
        if (sendMessagingException != null) {
            throw sendMessagingException;
        }
    }

    /** The set cc recipients called. */
    public boolean setCcRecipientsCalled;
    
    /** The set cc recipients recipients. */
    public String setCcRecipientsRecipients;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.mail.EmailMessage#setCcRecipients(java.lang.String)
     */
    public void setCcRecipients(String recipients) throws MessagingException, AddressException {
        setCcRecipientsCalled = true;
        setCcRecipientsRecipients = recipients;
    }

    /** The set recipient called. */
    public boolean setRecipientCalled;
    
    /** The set recipient person id. */
    public int setRecipientPersonId;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.mail.EmailMessage#setRecipient(int)
     */
    public void setRecipient(int personId) throws MessagingException {
        setRecipientCalled = true;
        setRecipientPersonId = personId;
    }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.mail.EmailMessage#setObjectRepository(com.technoetic.xplanner.domain.repository.ObjectRepository)
    */
   public void setObjectRepository(ObjectRepository objectRepository) {
      
   }

   /** Sets the object repository.
     *
     * @param repository
     *            the new object repository
     */
   public void setObjectRepository(MetaRepository repository) {

   }

   /** Sets the hibernate operations.
     *
     * @param hibernateOperations
     *            the new hibernate operations
     */
   public void setHibernateOperations(HibernateOperations hibernateOperations)
   {
   }
}
