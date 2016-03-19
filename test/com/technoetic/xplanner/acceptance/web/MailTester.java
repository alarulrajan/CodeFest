/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.acceptance.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.technoetic.xplanner.XPlannerProperties;

/**
 * The Class MailTester.
 */
public class MailTester {
   
   /** The current day offset. */
   private int currentDayOffset = 0;
   
   /** The tester. */
   private XPlannerWebTester tester;

   /** Gets the current day offset.
     *
     * @return the current day offset
     */
   public int getCurrentDayOffset() {
      return currentDayOffset;
   }

   /** The Class Email.
     */
   public static class Email {
      
      /** The subject. */
      public String subject;
      
      /** The recipients. */
      public String[] recipients;
      
      /** The from. */
      public String from;
      
      /** The body elements. */
      public List bodyElements;

      /** Instantiates a new email.
         *
         * @param from
         *            the from
         * @param recipients
         *            the recipients
         * @param subject
         *            the subject
         * @param bodyElements
         *            the body elements
         */
      public Email(String from, String[] recipients, String subject, List bodyElements){
         this.subject = subject;
         this.recipients = recipients;
         this.from = from;
         this.bodyElements = bodyElements;
      }

      /** Instantiates a new email.
         *
         * @param from
         *            the from
         * @param subject
         *            the subject
         * @param bodyElements
         *            the body elements
         */
      public Email(String from, String subject, List bodyElements){
         this(from, new String[0], subject, bodyElements);
      }

      /** Checks if is equal.
         *
         * @param message
         *            the message
         * @return true, if is equal
         */
      public boolean isEqual(SmtpMessage message) {
         if (!isSubjectEqual(message)) return false;
         if (!isFromAddressEqual(message)) return false;
         if (!isBodyContainingElements(message)) return false;
         return isRecipientsEqual(message);

      }

      /** Checks if is recipients equal.
         *
         * @param message
         *            the message
         * @return true, if is recipients equal
         */
      private boolean isRecipientsEqual(SmtpMessage message) {
         List messageRecipients = new ArrayList();
         messageRecipients.addAll(Arrays.asList(message.getHeaderValues("To")));
         messageRecipients.addAll(Arrays.asList(message.getHeaderValues("Cc")));
         messageRecipients.addAll(Arrays.asList(message.getHeaderValues("Bcc")));
         return messageRecipients.containsAll(Arrays.asList(recipients));
      }

      /** Checks if is body containing elements.
         *
         * @param message
         *            the message
         * @return true, if is body containing elements
         */
      private boolean isBodyContainingElements(SmtpMessage message) {
         String trimmedBody = StringUtils.deleteWhitespace(message.getBody());
         for (Iterator iterator = bodyElements.iterator(); iterator.hasNext();) {
            String element = (String) iterator.next();
            if (!StringUtils.contains(trimmedBody,
                                      StringUtils.deleteWhitespace(element))) {
               return false;
            }
         }
         return true;
      }

      /** Checks if is from address equal.
         *
         * @param message
         *            the message
         * @return true, if is from address equal
         */
      protected boolean isFromAddressEqual(SmtpMessage message) {
         return StringUtils.equals(from, message.getHeaderValue("From"));
      }

      /** Checks if is subject equal.
         *
         * @param message
         *            the message
         * @return true, if is subject equal
         */
      private boolean isSubjectEqual(SmtpMessage message) {
         return StringUtils.equals(subject, message.getHeaderValue("Subject"));
      }

      /* (non-Javadoc)
       * @see java.lang.Object#toString()
       */
      public String toString() {
         String result = "Email(from=" + from + ", " + "to={";
         for (int i=0; i<recipients.length; i++){
            if (i>0) result+=",";
            result+=recipients[i];
         }
         result+="}"+", subject=" + subject + ", body includes {\n";
         for (int i=0; i<bodyElements.size(); i++){
            if (i>0) result+=",\n";
            result+=bodyElements.get(i);
         }
         result+="\n})";
         return result;
      }
   }

   /** The smtp server. */
   private SimpleSmtpServer smtpServer;

   /** Instantiates a new mail tester.
     *
     * @param tester
     *            the tester
     */
   public MailTester(XPlannerWebTester tester) {
      this.tester = tester;
   }

   /** Assert number of email received by.
     *
     * @param expectedNumberOfEmailReceived
     *            the expected number of email received
     * @param receiver
     *            the receiver
     */
   public void assertNumberOfEmailReceivedBy(int expectedNumberOfEmailReceived, String receiver) {
      int cnt = 0;
      for (Iterator iterator = smtpServer.getReceivedEmail(); iterator.hasNext();) {
        SmtpMessage message = (SmtpMessage) iterator.next();
         if (Arrays.asList(message.getHeaderValues("To")).contains(receiver)) {
            cnt++;
         }
      }
      if (expectedNumberOfEmailReceived != cnt) {
         String message = "number of email sent to "+ receiver +" expected " + expectedNumberOfEmailReceived + " was " +
                          cnt +"\n messages received=" + getAllEmailsReceived();
         Assert.fail(message);
      }
   }

   /** Assert email has not been received.
     *
     * @param email
     *            the email
     * @throws InterruptedException
     *             the interrupted exception
     */
   public void assertEmailHasNotBeenReceived(Email email) throws InterruptedException {
      boolean isFound = hasEmailBeenReceived(email);
      Assert.assertFalse("A message " + email + " has been sent", isFound);
   }

   /** Assert email has not been received.
     *
     * @param recipient
     *            the recipient
     * @param email
     *            the email
     * @throws InterruptedException
     *             the interrupted exception
     */
   public void assertEmailHasNotBeenReceived(String recipient, Email email) throws InterruptedException {
      email.recipients = new String[]{recipient};
      assertEmailHasNotBeenReceived(email);
   }

   /** Assert email has been received.
     *
     * @param expectedEmail
     *            the expected email
     * @throws InterruptedException
     *             the interrupted exception
     */
   public void assertEmailHasBeenReceived(Email expectedEmail) throws InterruptedException {
      boolean isFound = hasEmailBeenReceived(expectedEmail);
      if (!isFound) {
         Assert.fail("A message " + expectedEmail + " not found in \n" + getAllEmailsReceived());
      }
   }

   /** Assert email has been received.
     *
     * @param recipient
     *            the recipient
     * @param email
     *            the email
     * @throws InterruptedException
     *             the interrupted exception
     */
   public void assertEmailHasBeenReceived(String recipient, Email email) throws InterruptedException {
      email.recipients = new String[] {recipient};
      assertEmailHasBeenReceived(email);
   }

   /** The Constant SECOND. */
   public static final int SECOND = 2;

   /** Checks for email been received.
     *
     * @param email
     *            the email
     * @return true, if successful
     * @throws InterruptedException
     *             the interrupted exception
     */
   private boolean hasEmailBeenReceived(Email email) throws InterruptedException {
      int timeout = 4 * SECOND;
      while (--timeout > 0) {
         Thread.sleep(500);
         if (hasEmailArrived(email)) return true;
      }
      return false;
   }

   /** Checks for email arrived.
     *
     * @param email
     *            the email
     * @return true, if successful
     */
   private boolean hasEmailArrived(Email email) {
      for (Iterator it = smtpServer.getReceivedEmail(); it.hasNext();) {
         SmtpMessage message = (SmtpMessage) it.next();
         if (email.isEqual(message)) return true;
      }
      return false;
   }

   /** Sets the up.
     *
     * @throws Exception
     *             the exception
     */
   public void setUp() throws Exception{
      currentDayOffset = 0;
      startSmtp();
   }

   /** Start smtp.
     *
     * @throws InterruptedException
     *             the interrupted exception
     */
   private void startSmtp() throws InterruptedException {
      int port = Integer.parseInt(new XPlannerProperties().getProperty("xplanner.mail.smtp.port"));
      smtpServer = new SimpleSmtpServer(port);
      Thread t = new Thread(smtpServer);
      tryStartNTimes(t, 10);
   }

   /** Try start n times.
     *
     * @param t
     *            the t
     * @param tries
     *            the tries
     * @throws InterruptedException
     *             the interrupted exception
     */
   private void tryStartNTimes(Thread t, int tries) throws InterruptedException {
      while (tries > 0) {
         try {
            t.start();
            break;
         } catch (Exception e) {
            Thread.sleep(100);
            tries--;
         }
      }
   }

   /** Tear down.
     */
   public void tearDown() {
      currentDayOffset = 0;
      stopSmtp();
   }

   /** Stop smtp.
     */
   private void stopSmtp() {
      if (smtpServer != null && !smtpServer.isStopped()) {
         smtpServer.stop();
      }
   }

   /** Reset smtp.
     *
     * @throws Exception
     *             the exception
     */
   public void resetSmtp() throws Exception {
      stopSmtp();
      startSmtp();
   }
   
   /** Gets the all emails received.
     *
     * @return the all emails received
     */
   public String getAllEmailsReceived() {
      String result = "{\n";
      Iterator email = smtpServer.getReceivedEmail();
      while (email.hasNext()) {
         SmtpMessage message = (SmtpMessage) email.next();
         if (email.hasNext()) {
            result += "  " + message + "\n###########################################################\n";
         }
      }
      result += "}";
      return result;
   }

   /** Move current day and send email.
     *
     * @param days
     *            the days
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
   public void moveCurrentDayAndSendEmail(int days) throws UnsupportedEncodingException {
      tester.moveCurrentDay(days);
      tester.executeTask("/do/edit/missingTimeEntryNotification");
   }
}