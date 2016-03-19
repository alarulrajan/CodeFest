package com.technoetic.xplanner.acceptance.web;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;

/**
 * The Class ChangePersonRoleTestScript.
 */
public class ChangePersonRoleTestScript extends AbstractPageTestScript {
   
   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      tester.login();
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#tearDown()
    */
   protected void tearDown() throws Exception {
       super.tearDown();
   }

   /** Test bug user not listed after added as editor.
     *
     * @throws Exception
     *             the exception
     */
   public void testBugUserNotListedAfterAddedAsEditor() throws Exception {
      setUpTestProject();
      setUpTestPerson();
      setUpPersonRole(project, developer, "editor");
      commitCloseAndOpenSession();
      setUpTestIterationAndStory();
      addTaskToStory();
      assertPersonCanTrackStory();
      assertPersonCanAcceptTask();
    }

   /** Test project admin can assign role to other people.
     *
     * @throws Exception
     *             the exception
     */
   public void testProjectAdminCanAssignRoleToOtherPeople() throws Exception
   {
      Project project1 = newProject();
      Project project2 = newProject();
      setUpTestPerson();
      commitCloseAndOpenSession();
      Person project1Admin = addPersonWithRoleOnProject("projectAdmin", project1, "admin");
      tester.logout();
      tester.login(project1Admin.getUserId(), "test");
      setUpPersonRole(project1, project1Admin, "editor");
      commitCloseAndOpenSession();
   }

   /** Adds the person with role on project.
     *
     * @param userId
     *            the user id
     * @param project
     *            the project
     * @param roleName
     *            the role name
     * @return the person
     * @throws Exception
     *             the exception
     */
   private Person addPersonWithRoleOnProject(String userId, Project project, String roleName) throws Exception {
      Person person = mom.newPerson(userId);
      setUpPersonRole(project, person,  roleName);
      commitCloseAndOpenSession();
      return person;
   }

   /** Assert person can accept task.
     */
   private void assertPersonCanAcceptTask() {
       tester.clickLinkWithText(testTaskName);
       tester.clickLinkWithKey("action.edit.task");
       tester.assertOptionListed("acceptorId", developer.getName());
   }

    /** Assert person can track story.
     *
     * @throws Exception
     *             the exception
     */
    private void assertPersonCanTrackStory() throws Exception {
        tester.gotoProjectsPage();
        tester.clickLinkWithText(testProjectName);
        tester.clickLinkWithText(testIterationName);
        tester.clickLinkWithText(storyName);
        tester.clickLinkWithKey("action.edit.story");
        tester.assertOptionListed("trackerId", developer.getName());
        tester.selectOption("trackerId", developer.getName());
        tester.submit();
    }

   /** Adds the task to story.
     */
   private void addTaskToStory() {
       tester.clickLinkWithKey("story.link.create_task");
       tester.assertKeyPresent("task.prefix");
       tester.assertLinkPresentWithKey("form.description.help");
       tester.setFormElement("name", testTaskName);
       tester.setFormElement("estimatedHours", testTaskEstimatedHours);
       tester.setFormElement("description", testTaskDescription);
       tester.submit();
   }

}
