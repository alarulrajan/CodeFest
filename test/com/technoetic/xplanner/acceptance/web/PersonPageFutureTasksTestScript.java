package com.technoetic.xplanner.acceptance.web;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Task;

import com.technoetic.xplanner.testing.DateHelper;

/**
 * The Class PersonPageFutureTasksTestScript.
 */
public class PersonPageFutureTasksTestScript extends AbstractPageTestScript {
   
   /** The person. */
   private Person person;
   
   /** The future task person is acceptor for. */
   Task futureTaskPersonIsAcceptorFor;
   
   /** The future task with no acceptor. */
   Task futureTaskWithNoAcceptor;

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      project = newProject();
      iteration = newIteration(project);
      iteration.setStartDate(DateHelper.getDateDaysFromToday(7));
      iteration.setEndDate(DateHelper.getDateDaysFromToday(21));
      story = newUserStory(iteration);
      futureTaskPersonIsAcceptorFor = newTask(story);
      futureTaskWithNoAcceptor= newTask(story);
      person = newPerson();
      futureTaskPersonIsAcceptorFor.setAcceptorId(person.getId());
      commitCloseAndOpenSession();
      tester.login(person.getUserId(),"test");
      tester.clickLinkWithKey("navigation.me");
   }

   /** Test future tasks.
     *
     * @throws Exception
     *             the exception
     */
   public void testFutureTasks() throws Exception {
      tester.assertKeyNotPresent("person.label.future_tasks.none");
      tester.assertTextPresent(futureTaskPersonIsAcceptorFor.getName());
      tester.assertTextNotPresent(futureTaskWithNoAcceptor.getName());
   }
}
