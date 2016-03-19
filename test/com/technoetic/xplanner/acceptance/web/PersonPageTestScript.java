package com.technoetic.xplanner.acceptance.web;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;

/**
 * The Class PersonPageTestScript.
 */
public class PersonPageTestScript extends AbstractPageTestScript {
   
   /** The project. */
   private Project project;

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
//      newProject(); // Need at least one project in order to get the edit person link to show up.
      setUpTestProject();
      Person person = newPerson();
      person.setUserId(guestUserId);
      person.setName(guestName);
      person.setInitials(guestInitials);
      person.setEmail("test@example.com");
      person.setPhone("555-1212");
      commitCloseAndOpenSession();
      tester.login();
      tester.clickLinkWithKey("navigation.people");
      tester.clickLinkWithText(guestName);
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#tearDown()
    */
   protected void tearDown() throws Exception {
      super.tearDown();
   }

   /** Test person page.
     */
   public void testPersonPage() {
      // Just check that the page shows up
      tester.assertTextNotPresent("error");
   }

   /** Test report export.
     *
     * @throws Exception
     *             the exception
     */
   public void testReportExport() throws Exception {
      checkExportUri("person", "jrpdf");
   }

   /** Test edit link.
     *
     * @throws Exception
     *             the exception
     */
   public void testEditLink() throws Exception {
      tester.assertLinkPresentWithKey("action.edit.person");
      tester.clickLinkWithKey("action.edit.person");
   }

   /** Test people link.
     *
     * @throws Exception
     *             the exception
     */
   public void testPeopleLink() throws Exception {
      tester.assertLinkPresentWithKey("projects.link.people");
      tester.clickLinkWithKey("projects.link.people");
   }

   /** Test timesheet link.
     *
     * @throws Exception
     *             the exception
     */
   public void testTimesheetLink() throws Exception {
      tester.assertLinkPresentWithKey("person.link.timesheet");
      tester.clickLinkWithKey("person.link.timesheet");
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#traverseLinkWithKeyAndReturn(java.lang.String)
    */
   protected void traverseLinkWithKeyAndReturn(String key) throws Exception {
      tester.clickLinkWithKey(key);
      tester.gotoProjectsPage();
   }

   /** Test edit my profile.
     *
     * @throws Exception
     *             the exception
     */
   public void testEditMyProfile() throws Exception {
      tester.clickLinkWithKey("logout");
      tester.login(guestUserId, "test");
      tester.assertOnTopPage();
      tester.clickLinkWithKey("navigation.me");
      tester.clickLinkWithKey("action.edit.person");
      personTester.assertOnPersonPage(guestUserId);
      tester.assertKeyNotPresent("security.notauthorized");
   }

   /** Test project roles.
     *
     * @throws Exception
     *             the exception
     */
   public void testProjectRoles() throws Exception {
      tester.gotoProjectsPage();
      setUpTestProject();

      tester.clickLinkWithKey("navigation.people");
      tester.clickEditLinkInRowWithText(guestName);
      tester.assertKeyPresent("person.editor.edit_prefix");
      tester.assertOptionEquals("projectRole[0]", "None");
      tester.assertFormElementNotPresentWithLabel("Editor");
   }

   /** Test only sys admin can add user.
     *
     * @throws Exception
     *             the exception
     */
   public void testOnlySysAdminCanAddUser() throws Exception {
      assertUserOfRoleCannotAddUser("admin");
      assertUserOfRoleCannotAddUser("editor");
      assertUserOfRoleCannotAddUser("viewer");
   }

   /** Test required fields.
     *
     * @throws Exception
     *             the exception
     */
   public void testRequiredFields() throws Exception {
      tester.clickLinkWithKey("action.edit.person");
      tester.setFormElement("userIdentifier", "");
      tester.setFormElement("email", "");
      tester.setFormElement("name", "");
      tester.setFormElement("initials", "");
      tester.submit();
      tester.assertKeyPresent("person.editor.missing_name");
      tester.assertKeyPresent("person.editor.missing_initials");
      tester.assertKeyPresent("person.editor.missing_user_id");
      tester.assertKeyPresent("person.editor.missing_email");
   }

   /** Assert user of role cannot add user.
     *
     * @param roleName
     *            the role name
     * @throws Exception
     *             the exception
     */
   private void assertUserOfRoleCannotAddUser(String roleName) throws Exception {
      project = setUpProject(NOT_HIDDEN);
      Person person = newPerson();
      commitSession();
      setUpRole(person, project, roleName);
      commitCloseAndOpenSession();

      tester.clickLinkWithKey("logout");

      String personUserId = person.getUserId();
      String personName = person.getName();

      tester.login(personUserId, "test");
      tester.assertTextPresent(project.getName());
      tester.clickLinkWithKey("navigation.people");
      tester.assertTextPresent(personName);
      tester.assertLinkNotPresentWithKey("people.link.add_person");
   }
}
