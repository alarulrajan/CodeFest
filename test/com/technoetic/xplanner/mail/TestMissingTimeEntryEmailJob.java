
package com.technoetic.xplanner.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.easymock.MockControl;

import com.technoetic.xplanner.AbstractUnitTestCase;

/**
 * The Class TestMissingTimeEntryEmailJob.
 */
public class TestMissingTimeEntryEmailJob extends AbstractUnitTestCase {
   
   /** The job. */
   private MissingTimeEntryEmailJob job;
   
   /** The story. */
   private UserStory story;
   
   /** The task. */
   private Task task;
   
   /** The project. */
   private Project project;
   
   /** The person. */
   private Person person;
   
   /** The time entry. */
   private TimeEntry timeEntry;

   /** The mock missing time entry notifier control. */
   MockControl mockMissingTimeEntryNotifierControl;
   
   /** The mock missing time entry notifier. */
   MissingTimeEntryNotifier mockMissingTimeEntryNotifier;

   /** The test date. */
   public Date testDate;


   /** Xset up.
     *
     * @throws Exception
     *             the exception
     */
   protected void xsetUp() throws Exception {
      super.setUp();

      project = new Project();
      project.setId(100);
      project.setName("Project name");
      story = new UserStory();
      story.setEstimatedHoursField(3.0);
      story.setName("Story1");
      task = new Task();
      task.setEstimatedHours(4.0);
      task.setName("Task1_1");
      task.setAcceptorId(1);
      person = new Person();
      person.setId(1);
      person.setName("acceptor");
      timeEntry = new TimeEntry();
      timeEntry.setId(1);
      timeEntry.setReportDate(new Date(1));
      List listTimeEntries = new ArrayList();
      listTimeEntries.add(timeEntry);
      task.setTimeEntries(listTimeEntries);
      task.setUserStory(story);
      mockMissingTimeEntryNotifierControl = MockControl.createControl(MissingTimeEntryNotifier.class);
      mockMissingTimeEntryNotifier = (MissingTimeEntryNotifier) mockMissingTimeEntryNotifierControl.getMock();
      job = new MissingTimeEntryEmailJob();
      job.setMissingTimeEntryNotifier(mockMissingTimeEntryNotifier);
      testDate = new Date();
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.AbstractUnitTestCase#tearDown()
    */
   public void tearDown() throws Exception {
      super.tearDown();
   }

/**
 * Test execute job.
 *
 * @throws Exception
 *             the exception
 */
// DEBT: Reenable this test
   public void testExecuteJob() throws Exception {
   }
//      List queryReturnValue = new ArrayList();
//      queryReturnValue.add(new Object[]{task, story, project});
//      mockMissingTimeEntryNotifierControl.expectAndReturn(
//            mockMissingTimeEntryNotifier.getTaskAcceptorsEmailNotification(testDate),
//            queryReturnValue);
//      mockMissingTimeEntryNotifierControl.expectAndReturn(
//            mockMissingTimeEntryNotifier.isProjectToBeNotified(job.projectsToBeNotified, project), true);
//      mockMissingTimeEntryNotifier.compileEmail(job.notificationEmails,
//                                                task.getAcceptorId(),
//                                                null,
//                                                task,
//                                                story);
//      mockMissingTimeEntryNotifier.sendNotifications(job.notificationEmails,
//                                                     MissingTimeEntryEmailJob.EMAIL_BODY_HEADER_FOR_ACCEPTORS,
//                                                     MissingTimeEntryEmailJob.SUBJECT_FOR_ACCEPTORS);
//
//      queryReturnValue = new ArrayList();
//      queryReturnValue.add(new Object[]{task, story, person, person});
//      mockMissingTimeEntryNotifierControl.expectAndReturn(
//            mockMissingTimeEntryNotifier.getProjectLeadsEmailNotification(testDate),
//            queryReturnValue);
//      mockMissingTimeEntryNotifier.compileEmail(job.notificationEmails,
//                                                person.getId(),
//                                                person,
//                                                task,
//                                                story);
//      mockMissingTimeEntryNotifier.sendNotifications(job.notificationEmails,
//                                                     MissingTimeEntryEmailJob.EMAIL_BODY_HEADER_FOR_PROJECT_LEADERS,
//                                                     MissingTimeEntryEmailJob.SUBJECT_FOR_PROJECT_LEADS);
//
//      mockMissingTimeEntryNotifierControl.replay();
//      job.executeInternal(null);
//      mockMissingTimeEntryNotifierControl.verify();
//   }

}