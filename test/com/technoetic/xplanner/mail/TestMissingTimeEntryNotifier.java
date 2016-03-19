package com.technoetic.xplanner.mail;

/**
 * User: Tomasz Siwiec
 * Date: Nov 02, 2004
 * Time: 10:20:08 PM
 */

import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.db.TaskQueryHelper;

/**
 * The Class TestMissingTimeEntryNotifier.
 */
public class TestMissingTimeEntryNotifier extends AbstractUnitTestCase {
   
   /** The missing time entry notifier. */
   private MissingTimeEntryNotifier missingTimeEntryNotifier;
   
   /** The project. */
   private Project project;
   
   /** The story. */
   UserStory story;
   
   /** The task. */
   private Task task;
   
   /** The receiver. */
   private Person receiver;
   
   /** The receiver id. */
   int receiverId = 1;

   /** The mock task query helper. */
   TaskQueryHelper mockTaskQueryHelper;
   
   /** The mock email notification support. */
   EmailNotificationSupport mockEmailNotificationSupport;
   
   /** The end date. */
   private Date endDate;
   
   /** The task with no time entry list. */
   private List taskWithNoTimeEntryList;
   
   /** The projects to be notified. */
   private Map projectsToBeNotified;
   
   /** The notification email map. */
   private Map notificationEmailMap;

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.AbstractUnitTestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      project = new Project();
      story = new UserStory();
      task = new Task();
      project.setName("testProject");
      story.setName("testStory");
      task.setName("testTask");
      task.setId(2);
      receiver = new Person();
      receiver.setId(1);
      endDate = new Date();
      mockEmailNotificationSupport = createLocalMock(EmailNotificationSupport.class);
      mockTaskQueryHelper = createLocalMock(TaskQueryHelper.class);
      missingTimeEntryNotifier = new MissingTimeEntryNotifier(mockTaskQueryHelper, mockEmailNotificationSupport);
      taskWithNoTimeEntryList = new ArrayList();
      projectsToBeNotified = new HashMap();
      notificationEmailMap = new HashMap();
      taskWithNoTimeEntryList.add(new Object[]{task, story, project});
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.AbstractUnitTestCase#tearDown()
    */
   public void tearDown() throws Exception {
      super.tearDown();
   }

   /** Test send missing time entry report to leads.
     *
     * @throws Exception
     *             the exception
     */
   public void testSendMissingTimeEntryReportToLeads() throws Exception {
      Person acceptor = new Person();
      acceptor.setId(2);
      List taskWithNoTimeEntryList = new ArrayList();
      taskWithNoTimeEntryList.add(new Object[]{task, story, receiver, acceptor});
      expect(mockTaskQueryHelper.getProjectLeedsEmailNotification(endDate)).andReturn(
                                                 taskWithNoTimeEntryList);
      Map notificationEmailMap = new HashMap();
      mockEmailNotificationSupport.compileEmail(notificationEmailMap, receiver.getId(), acceptor, task, story);
      mockEmailNotificationSupport.sendNotifications(notificationEmailMap,
                                                     MissingTimeEntryNotifier.EMAIL_BODY_HEADER_FOR_PROJECT_LEADERS,
                                                     MissingTimeEntryNotifier.SUBJECT_FOR_PROJECT_LEADS);
      replay();
      missingTimeEntryNotifier.sendMissingTimeEntryReportToLeads(endDate);
      verify();
   }

   /** Test send missing time entry reminder to acceptors_ project to be
     * notified set.
     *
     * @throws Exception
     *             the exception
     */
   public void testSendMissingTimeEntryReminderToAcceptors_ProjectToBeNotifiedSet() throws Exception {
      expect(mockTaskQueryHelper.getTaskAcceptorsEmailNotification(endDate)).andReturn(
                                                 taskWithNoTimeEntryList);
      Map projectsToBeNotified = new HashMap();
      expect(mockEmailNotificationSupport.isProjectToBeNotified(
            projectsToBeNotified,
            project)).andReturn(true);
      Map notificationEmailMap = new HashMap();
      mockEmailNotificationSupport.compileEmail(notificationEmailMap, task.getAcceptorId(), null, task, story);
      mockEmailNotificationSupport.sendNotifications(notificationEmailMap,
                                                     MissingTimeEntryNotifier.EMAIL_BODY_HEADER_FOR_ACCEPTORS,
                                                     MissingTimeEntryNotifier.SUBJECT_FOR_ACCEPTORS);
      replay();
      missingTimeEntryNotifier.sendMissingTimeEntryReminderToAcceptors(endDate);
      verify();
   }

   /** Test send missing time entry reminder to acceptors_ project to be
     * notified not set.
     *
     * @throws Exception
     *             the exception
     */
   public void testSendMissingTimeEntryReminderToAcceptors_ProjectToBeNotifiedNotSet() throws Exception {
      expect(mockTaskQueryHelper.getTaskAcceptorsEmailNotification(endDate)).andReturn(taskWithNoTimeEntryList);
      expect(mockEmailNotificationSupport.isProjectToBeNotified(
            projectsToBeNotified,
            project)).andReturn(false);
      mockEmailNotificationSupport.sendNotifications(notificationEmailMap,
                                                     MissingTimeEntryNotifier.EMAIL_BODY_HEADER_FOR_ACCEPTORS,
                                                     MissingTimeEntryNotifier.SUBJECT_FOR_ACCEPTORS);
      replay();
      missingTimeEntryNotifier.sendMissingTimeEntryReminderToAcceptors(endDate);
      verify();
   }
}
