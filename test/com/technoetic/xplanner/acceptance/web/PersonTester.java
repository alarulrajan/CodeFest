package com.technoetic.xplanner.acceptance.web;

import org.xml.sax.SAXException;

/**
 * The Class PersonTester.
 */
public class PersonTester {
   
   /** The tester. */
   private XPlannerWebTester tester;


   /** Instantiates a new person tester.
     *
     * @param tester
     *            the tester
     */
   public PersonTester(XPlannerWebTester tester) {
      this.tester = tester;
   }

   /** Adds the person.
     *
     * @param name
     *            the name
     * @param userId
     *            the user id
     * @param initials
     *            the initials
     * @param email
     *            the email
     * @param phone
     *            the phone
     * @throws Exception
     *             the exception
     */
   public void addPerson(String name,
                         String userId,
                         String initials,
                         String email,
                         String phone) throws Exception {
      addPerson(name, userId, initials, email, phone, true);
   }

   /** Adds the person.
     *
     * @param name
     *            the name
     * @param userId
     *            the user id
     * @param initials
     *            the initials
     * @param email
     *            the email
     * @param phone
     *            the phone
     * @param createSystemAdminIfPossible
     *            the create system admin if possible
     * @throws Exception
     *             the exception
     */
   public void addPerson(String name,
                         String userId,
                         String initials,
                         String email,
                         String phone,
                         boolean createSystemAdminIfPossible)
         throws Exception {
      gotoPeoplePage();
      tester.clickLinkWithKey("people.link.add_person");
      tester.assertTextPresent("Create Profile:");
      populatePersonForm(name, userId, initials, email, phone, createSystemAdminIfPossible);
      tester.submit();
      tester.assertKeyNotPresent("errors.header");
   }

   /** Adds the person with error.
     *
     * @param name
     *            the name
     * @param userId
     *            the user id
     * @param initials
     *            the initials
     * @param email
     *            the email
     * @param phone
     *            the phone
     * @throws Exception
     *             the exception
     */
   public void addPersonWithError(String name,
                                  String userId,
                                  String initials,
                                  String email,
                                  String phone) throws Exception {
      addPersonWithError(name, userId, initials, email, phone, true);
   }

   /** Adds the person with error.
     *
     * @param name
     *            the name
     * @param userId
     *            the user id
     * @param initials
     *            the initials
     * @param email
     *            the email
     * @param phone
     *            the phone
     * @param createSystemAdminIfPossible
     *            the create system admin if possible
     * @throws Exception
     *             the exception
     */
   public void addPersonWithError(String name,
                                  String userId,
                                  String initials,
                                  String email,
                                  String phone,
                                  boolean createSystemAdminIfPossible) throws Exception {
      gotoPeoplePage();
      tester.clickLinkWithKey("people.link.add_person");
      tester.assertTextPresent("Create Profile:");
      populatePersonForm(name, userId, initials, email, phone, createSystemAdminIfPossible);
      tester.submit();
   }

   /** Adds the person with role.
     *
     * @param name
     *            the name
     * @param userId
     *            the user id
     * @param initials
     *            the initials
     * @param email
     *            the email
     * @param phone
     *            the phone
     * @param projectName
     *            the project name
     * @param roleName
     *            the role name
     * @throws Exception
     *             the exception
     */
   public void addPersonWithRole(String name,
                                 String userId,
                                 String initials,
                                 String email,
                                 String phone,
                                 String projectName, String roleName)
         throws Exception {
      gotoPeoplePage();
      tester.clickLinkWithKey("people.link.add_person");
      tester.assertTextPresent("Create Profile:");
      populatePersonForm(name, userId, initials, email, phone, false);
      tester.assignRoleOnProject(projectName, roleName);
      tester.submit();
      tester.assertKeyNotPresent("errors.header");
   }

// --------------------- Interface WebTester ---------------------

   /**
 * Assert on people page.
 */
public void assertOnPeoplePage() {
      tester.assertKeyPresent("people.title");
      tester.assertKeyPresent("people.tableheading.name");
      tester.assertKeyPresent("people.tableheading.initials");
      tester.assertKeyPresent("people.tableheading.phone");
      tester.assertKeyPresent("people.tableheading.email");
      tester.assertKeyPresent("people.tableheading.actions");
   }

   /** Assert on person page.
     *
     * @param userIdentifier
     *            the user identifier
     */
   public void assertOnPersonPage(String userIdentifier) {
      tester.assertKeyPresent("person.editor.edit_prefix");
      tester.assertFormElementEquals("userIdentifier", userIdentifier);
   }

   /** Assign role to person on project.
     *
     * @param developerName
     *            the developer name
     * @param projectName
     *            the project name
     * @param roleName
     *            the role name
     * @throws SAXException
     *             the SAX exception
     */
   public void assignRoleToPersonOnProject(String developerName, String projectName, String roleName) throws
                                                                                                      SAXException {
      gotoPeoplePage();
      tester.clickLinkWithText(developerName);
      tester.clickLinkWithKey("action.edit.person");
      tester.assignRoleOnProject(projectName, roleName);
      tester.submit();
   }

   /** Goto people page.
     */
   public void gotoPeoplePage() {
      tester.gotoProjectsPage();
      tester.clickLinkWithKey("projects.link.people");
      assertOnPeoplePage();
   }

   /** Populate person form.
     *
     * @param name
     *            the name
     * @param userId
     *            the user id
     * @param initials
     *            the initials
     * @param email
     *            the email
     * @param phone
     *            the phone
     * @param createSystemAdminIfPossible
     *            the create system admin if possible
     */
   public void populatePersonForm(String name,
                                  String userId,
                                  String initials,
                                  String email,
                                  String phone,
                                  boolean createSystemAdminIfPossible) {
      tester.setFormElement("name", name);
      tester.setFormElement("userIdentifier", userId);
      tester.setFormElement("initials", initials);
      tester.setFormElement("email", email);
      tester.setFormElement("phone", phone);
      tester.setFormElement("newPassword", XPlannerWebTesterImpl.DEFAULT_PASSWORD);
      tester.setFormElement("newPasswordConfirm", XPlannerWebTesterImpl.DEFAULT_PASSWORD);
//      if (createSystemAdminIfPossible && tester.getDialog().getForm().hasParameterNamed("systemAdmin")) {
//         tester.setFormElement("systemAdmin", "true");
//      }
   }
}

