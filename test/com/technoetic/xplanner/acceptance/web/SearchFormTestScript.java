package com.technoetic.xplanner.acceptance.web;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

/**
 * The Class SearchFormTestScript.
 */
public class SearchFormTestScript extends AbstractPageTestScript {
   
   /** The project to search. */
   private String projectToSearch = testProjectName + "-findMe";
   
   /** The iteration to find. */
   private String iterationToFind = testIterationName + "yoohoo";
   
   /** The story to find. */
   private String storyToFind = storyName + "peekaboo";
   
   /** The task to find. */
   private String taskToFind = testTaskName + "heythere";
   
   /** The project description to find. */
   private String projectDescriptionToFind = testProjectDescription + "proojaaactooor";
   
   /** The iteration description to find. */
   private String iterationDescriptionToFind = testIterationDescription + "widget";
   
   /** The story description to find. */
   private String storyDescriptionToFind = testStoryDescription + "gadget";
   
   /** The task description to find. */
   private String taskDescriptionToFind = testTaskDescription + "thingamajig";
   
   /** The forbidden project name. */
   private String forbiddenProjectName = projectToSearch + "-duplicated";

   /** The Constant SUBJECT_SUFFIX. */
   public static final String SUBJECT_SUFFIX = " Note Subject";
   
   /** The Constant PROJECT_NOTE_SUBJECT. */
   public static final String PROJECT_NOTE_SUBJECT = "Project" + SUBJECT_SUFFIX;
   
   /** The Constant ITERATION_NOTE_SUBJECT. */
   public static final String ITERATION_NOTE_SUBJECT = "Iteration" + SUBJECT_SUFFIX;
   
   /** The Constant STORY_NOTE_SUBJECT. */
   public static final String STORY_NOTE_SUBJECT = "Story" + SUBJECT_SUFFIX;
   
   /** The Constant TASK_NOTE_SUBJECT. */
   public static final String TASK_NOTE_SUBJECT = "Task" + SUBJECT_SUFFIX;

   /** The Constant NOTE_BODY_SUFFIX. */
   public static final String NOTE_BODY_SUFFIX = " Note Body";
   
   /** The Constant PROJECT_NOTE_BODY. */
   public static final String PROJECT_NOTE_BODY = "Project" + NOTE_BODY_SUFFIX;
   
   /** The Constant ITERATION_NOTE_BODY. */
   public static final String ITERATION_NOTE_BODY = "Iteration" + NOTE_BODY_SUFFIX;
   
   /** The Constant STORY_NOTE_BODY. */
   public static final String STORY_NOTE_BODY = "Story" + NOTE_BODY_SUFFIX;
   
   /** The Constant TASK_NOTE_BODY. */
   public static final String TASK_NOTE_BODY = "Task" + NOTE_BODY_SUFFIX;

   /** The Constant GLOBAL_SCOPE_ENABLE_PROPERTY. */
   public static final String GLOBAL_SCOPE_ENABLE_PROPERTY = "search.content.globalScopeEnable";
   
   /** The task id. */
   private String taskId;
   
   /** The story id. */
   private String storyId;
   
   /** The iteration id. */
   private String iterationId;

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
    */
   public void setUp() throws Exception {
//      new Timer().run(new Callable() { public void run() throws Exception { mySetUp(); } });
//   }
//
//   private void mySetUp() throws Exception {
      super.setUp();
      String startDate = tester.dateStringForNDaysAway(0);
      String endDate = tester.dateStringForNDaysAway(14);
      tester.login();
      setUpTestPerson();
      setUpTestProject(projectToSearch, projectDescriptionToFind);
      //projectId = tester.addProject(projectToSearch, projectDescriptionToFind);
      setUpTestRole("editor");
      tester.gotoProjectsPage();
      tester.gotoProjectsPage();
      tester.clickLinkWithText(projectToSearch);
      tester.addNote(PROJECT_NOTE_SUBJECT, PROJECT_NOTE_BODY, "SearchFormTestScript");
      iterationId = iterationTester.addIteration(iterationToFind, startDate, endDate, iterationDescriptionToFind);
      tester.clickLinkWithText(iterationToFind);
      tester.addNote(ITERATION_NOTE_SUBJECT, ITERATION_NOTE_BODY, "SearchFormTestScript");

      storyId = tester.addUserStory(storyToFind, storyDescriptionToFind, "8.0", "1");
      tester.clickLinkWithText(storyToFind);
      tester.addNote(STORY_NOTE_SUBJECT, STORY_NOTE_BODY, "SearchFormTestScript");
      taskId = tester.addTask(taskToFind, developerName, taskDescriptionToFind, testTaskEstimatedHours);
      tester.clickLinkWithText(taskToFind);
      tester.addNote(TASK_NOTE_SUBJECT, TASK_NOTE_BODY, "SearchFormTestScript");
      tester.gotoProjectsPage();
      tester.editProperty(GLOBAL_SCOPE_ENABLE_PROPERTY, "true");
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#tearDown()
    */
   public void tearDown() throws Exception {
//         new Timer().run(new Callable() { public void run() throws Exception { myTearDown(); } });
//      }
//
//      private void myTearDown() throws Exception {
      tester.editProperty(GLOBAL_SCOPE_ENABLE_PROPERTY, "true");
      tester.clickLinkWithText("logout");
      super.deleteLocalNote(taskId);
      tester.deleteObjects(Task.class, "name", taskToFind);
      super.deleteLocalNote(storyId);
      tester.deleteObjects(UserStory.class, "name", storyToFind);
      super.deleteLocalNote(iterationId);
      tester.deleteObjects(Iteration.class, "name", iterationToFind);
      super.deleteLocalNote(Integer.toString(project.getId()));
      tester.deleteObjects(Project.class, "name", projectToSearch);
      tester.deleteObjects(Project.class, "name", forbiddenProjectName);
      super.tearDown();
      tearDownTestPerson();
   }

   /** Test search form.
     *
     * @throws Exception
     *             the exception
     */
   public void testSearchForm() throws Exception {
      searchForTextInTitle();
      searchForTextInDescription();
   }

   /** Test search without permissions.
     *
     * @throws Exception
     *             the exception
     */
   public void testSearchWithoutPermissions() throws Exception {
      createUserWithoutPermission();

      tester.login(noPermissionUserName, XPlannerWebTester.DEFAULT_PASSWORD);
      searchFor("proojaaactooor");
      tester.assertKeyPresent("empty.search.results");
   }

   /** Test search with editor permissions.
     *
     * @throws Exception
     *             the exception
     */
   public void testSearchWithEditorPermissions() throws Exception {
      createUserWithEditorRoleForProject(projectToSearch);

      tester.login(editorRoleUserName, XPlannerWebTester.DEFAULT_PASSWORD);
      searchFor("proojaaactooor");
      searchForTextInTitle();
      searchForTextInDescription();
   }

   /** Test search with editor permissions_ one project should not be found.
     *
     * @throws Exception
     *             the exception
     */
   public void testSearchWithEditorPermissions_OneProjectShouldNotBeFound() throws Exception {
      tester.gotoProjectsPage();
      tester.addProject(forbiddenProjectName, projectDescriptionToFind);
      createUserWithEditorRoleForProject(projectToSearch);

      tester.login(editorRoleUserName, XPlannerWebTester.DEFAULT_PASSWORD);
      searchFor("proojaaactooor");
      searchForTextInTitle();
      searchForTextInDescription();
   }

   /** Test search with editor permissions_ restricted scope.
     *
     * @throws Exception
     *             the exception
     */
   public void testSearchWithEditorPermissions_RestrictedScope() throws Exception {
      tester.editProperty(GLOBAL_SCOPE_ENABLE_PROPERTY, "false");
      tester.gotoProjectsPage();
      tester.addProject(forbiddenProjectName, projectDescriptionToFind);
      personTester.gotoPeoplePage();
      personTester.addPersonWithRole(editorRoleUserName,
                               editorRoleUserName,
                               developerInitials,
                               userEmail,
                               userPhone,
                               projectToSearch, tester.getMessage("person.editor.role.editor"));
      personTester.assignRoleToPersonOnProject(editorRoleUserName,
                                         forbiddenProjectName,
                                         tester.getMessage("person.editor.role.editor"));
      tester.logout();
      tester.login(editorRoleUserName, XPlannerWebTester.DEFAULT_PASSWORD);
      assertNoContentSearchForm();
      tester.gotoProjectsPage();
      tester.assertTextPresent(forbiddenProjectName);
      tester.clickLinkWithText(projectToSearch);
      searchForTextInTitle();
   }

   /** Assert no content search form.
     */
   private void assertNoContentSearchForm() {
      tester.assertFormNotPresent("search");
   }

   /** Search for text in description.
     */
   private void searchForTextInDescription() {
      searchFor("proojaaactooor");
      tester.assertLinkPresentWithText(projectToSearch);
      tester.clickLinkWithText(projectToSearch);
      tester.assertOnProjectPage();

      searchFor("widget");
      tester.assertLinkPresentWithText(iterationToFind);
      tester.clickLinkWithText(iterationToFind);
      iterationTester.assertOnIterationPage();

      searchFor("gadget");
      tester.assertLinkPresentWithText(storyToFind);
      tester.clickLinkWithText(storyToFind);
      tester.assertOnStoryPage(storyToFind);

      searchFor("thingamajig");
      tester.assertLinkPresentWithText(taskToFind);
      tester.clickLinkWithText(taskToFind);
      tester.assertOnTaskPage();
   }

   /** Search for text in title.
     */
   private void searchForTextInTitle() {
      searchFor("findMe");
      tester.assertLinkPresentWithText(projectToSearch);
      tester.assertLinkNotPresentWithText(forbiddenProjectName);
      tester.clickLinkWithText(projectToSearch);
      tester.assertOnProjectPage();

      searchFor("yoohoo");
      tester.assertLinkPresentWithText(iterationToFind);
      tester.clickLinkWithText(iterationToFind);
      iterationTester.assertOnIterationPage();

      searchFor("peekaboo");
      tester.assertLinkPresentWithText(storyToFind);
      tester.clickLinkWithText(storyToFind);
      tester.assertOnStoryPage(storyToFind);

      searchFor("heythere");
      tester.assertLinkPresentWithText(taskToFind);
      tester.clickLinkWithText(taskToFind);
      tester.assertOnTaskPage();
   }

   /** Search for.
     *
     * @param textToFind
     *            the text to find
     */
   private void searchFor(String textToFind) {
      tester.assertFormPresent("search");
      tester.setWorkingForm("search");
      tester.setFormElement("searchedContent", textToFind);
      tester.submit();
      tester.assertKeyPresent("search.results");
   }

   /** Test note links.
     *
     * @throws Exception
     *             the exception
     */
   public void testNoteLinks() throws Exception {
      searchForNote("Project");
      searchForNote("Iteration");
      searchForNote("Story");
      searchForNote("Task");
   }

   /** Test search orphan note.
     *
     * @throws Exception
     *             the exception
     */
   public void testSearchOrphanNote() throws Exception {
      tester.deleteObjects(UserStory.class, "name", storyToFind);
      searchFor("Story" + SUBJECT_SUFFIX);
      tester.assertLinkNotPresentWithText("Story" + SUBJECT_SUFFIX);
   }

   /** Search for note.
     *
     * @param prefix
     *            the prefix
     */
   private void searchForNote(String prefix) {
      searchFor(prefix + SUBJECT_SUFFIX);
      tester.assertLinkPresentWithText(prefix + SUBJECT_SUFFIX);
      tester.clickLinkWithText(prefix + SUBJECT_SUFFIX);
      tester.assertTextPresent(prefix + SUBJECT_SUFFIX);
      tester.assertTextPresent(prefix + NOTE_BODY_SUFFIX);
   }
}
