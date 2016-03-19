package com.technoetic.xplanner.acceptance.web;

import java.io.ByteArrayInputStream;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;

import org.xml.sax.SAXException;

import com.technoetic.xplanner.security.SecurityHelper;

/**
 * The Class PeoplePageTestScript.
 */
public class PeoplePageTestScript extends AbstractPageTestScript
{
    
    /** The Constant IMPORT_PEOPLE_SUCCESSFULLY. */
    private static final String IMPORT_PEOPLE_SUCCESSFULLY =
          "importtestperson1,Import test person 1,tester1@sabre.com,T1,123456\n" +
          "importtestperson2,Import test person 2,tester2@sabre.com,T2,12345";
    
    /** The Constant IMPORT_PEOPLE_PARTIALLY_FAILED. */
    private static final String IMPORT_PEOPLE_PARTIALLY_FAILED =
          "importtestperson1,Import test person 1,tester1@sabre.com,TS1,123456\n" +
          "importtestperson2,Import test person 2,12345";
    
    /** The name. */
    private String name = generateUniqueName();
    
    /** The case_sens_name. */
    private String case_sens_name = null;
    
    /** The first project name. */
    private final String firstProjectName = "FirstProject";
    
    /** The second project name. */
    private final String secondProjectName = "SecondProject";
    
    /** The user a. */
    private String userA;
    
    /** The user b. */
    private String userB;

   /** Instantiates a new people page test script.
     *
     * @param test
     *            the test
     */
   public PeoplePageTestScript(String test)
   {
      super(test);
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
    */
   public void setUp() throws Exception
   {
      super.setUp();
      userA = generateUniqueName("peoplePage");
      userB = generateUniqueName("peoplePage");
      tester.login();
      tester.clickLinkWithKey("navigation.people");

   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#tearDown()
    */
   protected void tearDown() throws Exception
   {
      if (name != null)
      {
         tester.deleteObjects(Person.class, "name", name);
      }
      if (case_sens_name != null)
      {
         tester.deleteObjects(Person.class, "name", case_sens_name);
      }
      tester.getSession().delete("from person in class " + Person.class + " where name like 'Import test person%'");
      tester.deleteObjects(Person.class, "name", userA);
      tester.deleteObjects(Person.class, "name", userB);
      tester.deleteObjects(Project.class, "name", firstProjectName);
      tester.deleteObjects(Project.class, "name", secondProjectName);
      super.tearDown();
   }

   /** Test unique user id.
     *
     * @throws Exception
     *             the exception
     */
   public void testUniqueUserId() throws Exception
   {
      name = "A. Persona1 " + System.currentTimeMillis();
      String userId = "userId " + System.currentTimeMillis();
      personTester.addPerson(name, userId, developerInitials, "email@fds", "000");
      personTester.gotoPeoplePage();
      tester.assertTextPresent(name);
      personTester.addPersonWithError(name, userId, developerInitials, "email", "000");
      tester.assertKeyPresent("person.editor.userid_exist");
   }

   /** Test case sensitive unique user id.
     *
     * @throws Exception
     *             the exception
     */
   public void testCaseSensitiveUniqueUserId() throws Exception
   {
      personTester.assertOnPeoplePage();
      String userId = "CASE_SENSITIVE" + developerUserId;
      personTester.addPerson(name, userId, developerInitials, "email", "000");
      personTester.assertOnPeoplePage();
      case_sens_name = "case_sens_" + generateUniqueName();
      personTester.addPersonWithError(case_sens_name,
                                      userId.toLowerCase(),
                                      developerInitials,
                                      "email",
                                      "000");
      //ChangeSoon refactor according to case sensitive func. in login module
      assertTrue(SecurityHelper.isAuthenticationCaseSensitive());
      //{
      tester.assertKeyPresent("person.editor.userid_exist");
      //}
      //else
      //{
      //    tester.assertOnPeoplePage();
      //    tester.clickLinkWithText(case_sens_name);
      //    tester.clickLinkWithKey("person.link.edit");
      //    tester.assertTextPresent("case_sensitive" + developerUserId);
      //}
   }

   /** Test content and links.
     */
   public void testContentAndLinks()
   {
      personTester.assertOnPeoplePage();
      tester.clickLinkWithKey("navigation.top");
   }

   /** Test manipulating people.
     *
     * @throws Exception
     *             the exception
     */
   public void testManipulatingPeople() throws Exception
   {
      tester.gotoProjectsPage();
      setUpTestProject();
      name = "A. Persona " + System.currentTimeMillis();
      String userid = userA;
      String initials = "ap";
      String email = userA + "@nowhere.com";
      String phone = "2-323-555-5555";

      personTester.addPerson(name, userid, initials, email, phone);

      personTester.assertOnPeoplePage();
      tester.assertTextPresent(name);
      tester.assertTextPresent(userid);
      tester.assertTextPresent(initials);
      tester.assertTextPresent(email);
      tester.assertTextPresent(phone);

      String newEmail = userA + "-1@somewhere.com";

      tester.clickEditLinkInRowWithText(name);
      tester.assertFormElementEquals("name", name);
      tester.assertFormElementEquals("userIdentifier", userid);
      tester.assertFormElementEquals("initials", initials);
      tester.assertFormElementEquals("email", email);
      tester.assertFormElementEquals("phone", phone);
      tester.setFormElement("email", newEmail);

      tester.submit();

      personTester.assertOnPeoplePage();
      tester.assertTextNotPresent(email);
      tester.assertTextPresent(newEmail);
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#traverseLinkWithKeyAndReturn(java.lang.String)
    */
   protected void traverseLinkWithKeyAndReturn(String key) throws Exception
   {
      tester.clickLinkWithKey(key);
      tester.gotoPage("view", "projects", 0);
   }

   /** Test import people_ success.
     *
     * @throws Exception
     *             the exception
     */
   public void testImportPeople_Success() throws Exception
   {
      tester.clickLinkWithKey("people.link.import_people");
      sendImportFile("import.csv", IMPORT_PEOPLE_SUCCESSFULLY);
      assertImportStatus("importtestperson1", "Success");
      assertImportStatus("importtestperson2", "Success");
   }

   /** Test import people_ partially failed.
     *
     * @throws Exception
     *             the exception
     */
   public void testImportPeople_PartiallyFailed() throws Exception
   {
      tester.clickLinkWithKey("people.link.import_people");
      sendImportFile("/import.csv", IMPORT_PEOPLE_PARTIALLY_FAILED);
      assertImportStatus("importtestperson1", "Success");
      assertImportStatus("importtestperson2", "Entry is not formated properly");
   }

   /** Test import people_ users already exists.
     *
     * @throws Exception
     *             the exception
     */
   public void testImportPeople_UsersAlreadyExists() throws Exception
   {
      tester.clickLinkWithKey("people.link.import_people");
      sendImportFile("import.csv", IMPORT_PEOPLE_SUCCESSFULLY);
      sendImportFile("import.csv", IMPORT_PEOPLE_SUCCESSFULLY);
      assertImportStatus("importtestperson1", "User already exists");
      assertImportStatus("importtestperson2", "User already exists");
   }


   /** Assert import status.
     *
     * @param personName
     *            the person name
     * @param status
     *            the status
     */
   private void assertImportStatus(String personName, String status)
   {
      tester.assertCellTextForRowWithTextAndColumnKeyEquals("objecttable",
                                                            personName,
                                                            "people.import.tableheading.status",
                                                            status);
   }

    /** Test import people_ failed_ no import file.
     *
     * @throws Exception
     *             the exception
     */
    public void testImportPeople_Failed_NoImportFile() throws Exception
    {
        tester.clickLinkWithKey("people.link.import_people");
        tester.submit("");
        tester.assertKeyPresent("import.status.no_import_file");

   }

   /** Send import file.
     *
     * @param importFileName
     *            the import file name
     * @param fileContents
     *            the file contents
     */
   private void sendImportFile(String importFileName, String fileContents)
   {
      tester.uploadFile("formFile", importFileName, new ByteArrayInputStream(fileContents.getBytes()));
      tester.submit();
   }

   /** Test downgrade people.
     *
     * @throws Exception
     *             the exception
     */
   public void testDowngradePeople() throws Exception
   {
      personTester.addPerson(userA, userA, userA, "userA email", "userA phone", true);
      personTester.addPerson(userB, userB, userB, "userB email", "userB phone", false);
      tester.gotoProjectsPage();
      tester.addProject(firstProjectName, "First Project Description");
      tester.addProject(secondProjectName, "Second Project Description");

      loginAs(userA);

      editProfile(userB);
      setRole(firstProjectName, "person.editor.role.admin");
      tester.submit();

      loginAs(userB);
      tester.assertTextPresent(firstProjectName);
      tester.assertTextNotPresent(secondProjectName);

      loginAs(userA);

      editProfile(userB);
      setRole(firstProjectName, "person.editor.role.none");
      setRole(secondProjectName, "person.editor.role.viewer");
      tester.submit();

      loginAs(userB);
      tester.assertTextNotPresent(firstProjectName);
      tester.assertTextPresent(secondProjectName);

   }

   /** Login as.
     *
     * @param userid
     *            the userid
     * @throws Exception
     *             the exception
     */
   private void loginAs(String userid) throws Exception
   {
      tester.logout();
      tester.login(userid, XPlannerWebTester.DEFAULT_PASSWORD);
   }

   /** Edits the profile.
     *
     * @param userid
     *            the userid
     */
   private void editProfile(String userid)
   {
      tester.clickLinkWithKey("navigation.people");
      tester.clickImageLinkInTableForRowWithText(EDIT_IMAGE, "objecttable", userid);
   }

   /** Sets the role.
     *
     * @param project
     *            the project
     * @param role
     *            the role
     * @throws SAXException
     *             the SAX exception
     */
   private void setRole(String project, String role) throws SAXException
   {
      selectRoleForProject(project, tester.getMessage(role));
   }

   /** Verify content and change role for the first project.
     *
     * @param userid
     *            the userid
     * @param roleOnProject
     *            the role on project
     * @throws SAXException
     *             the SAX exception
     */
   private void verifyContentAndChangeRoleForTheFirstProject(String userid, String roleOnProject) throws SAXException
   {
      tester.clickImageLinkInTableForRowWithText(EDIT_IMAGE, "objecttable", userid);
      personTester.assertOnPersonPage(userid);
      tester.assertKeyPresent("person.editor.roles");
      tester.assertTextNotPresent(firstProjectName);
      tester.assertTextPresent(secondProjectName);
      tester.assertTextNotPresent(tester.getMessage("person.editor.is_sysadmin"));
      selectRoleForProject(secondProjectName, tester.getMessage(roleOnProject));
      tester.submit();
   }

   /** Select role for project.
     *
     * @param projectName
     *            the project name
     * @param role
     *            the role
     * @throws SAXException
     *             the SAX exception
     */
   private void selectRoleForProject(String projectName, String role) throws SAXException
   {
      int[] rows = tester.getRowNumbersWithText(ROLES_TABLE, projectName);
      if (rows != null)
      {
         int rowNbr = rows[0] - 1;
         tester.selectOption("projectRole[" + rowNbr + "]", role);
      }
   }
}
