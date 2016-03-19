package com.technoetic.xplanner.acceptance.web;

import java.util.List;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.Hibernate;


/**
 * The Class SearchByIdTestScript.
 */
public class SearchByIdTestScript extends AbstractPageTestScript
{
   
   /** The project. */
   private Project project;
   
   /** The iteration. */
   private Iteration iteration;
   
   /** The project note. */
   private Note projectNote;
   
   /** The iteration note. */
   private Note iterationNote;
   
   /** The story. */
   private UserStory story;

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
    */
   protected void setUp() throws Exception
   {
      super.setUp();
      project = newProject();
      iteration = newIteration(project);
      story = newUserStory(iteration);
      List personList = tester.getSession().find("from person in class " +
                                    Person.class +
                                    " " +
                                    "where person.userId = ?",
                                    tester.getXPlannerLoginId(),
                                    Hibernate.STRING);
      Person loggedPerson = (Person)personList.iterator().next();
      projectNote = createNoteFor(project.getId(), loggedPerson.getId());
      iterationNote = createNoteFor(iteration.getId(), loggedPerson.getId());
      commitCloseAndOpenSession();
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#tearDown()
    */
   protected void tearDown() throws Exception
   {
      super.tearDown();
   }

   /** Test search by id_ sysadmin finds all types of object.
     *
     * @throws Exception
     *             the exception
     */
   public void testSearchById_SysadminFindsAllTypesOfObject() throws Exception
   {
      tester.login();
      tester.gotoRelativeUrl("/do/view/projects");

      searchBy(project.getId());
      tester.assertTextPresent(project.getName());

      searchBy(iteration.getId());
      tester.assertTextPresent(project.getName());
      tester.assertTextPresent(iteration.getName());

      searchBy(story.getId());
      tester.assertTextPresent(project.getName());
      tester.assertTextPresent(iteration.getName());
      tester.assertTextPresent(story.getName());

      searchBy(projectNote.getId());
      tester.assertTextPresent(project.getName());
      assertNoteStuff(projectNote);

      searchBy(iterationNote.getId());
      tester.assertTextPresent(iteration.getName());
      assertNoteStuff(iterationNote);
   }

   /** Test search by id_ sysadmin doest not find object and gets aid not
     * found error.
     *
     * @throws Exception
     *             the exception
     */
   public void testSearchById_SysadminDoestNotFindObjectAndGetsAIDNotFoundError() throws Exception
   {
      tester.login();
      tester.gotoRelativeUrl("/do/view/projects");
      searchBy(-1);
      tester.assertKeyPresent("idsearch.error.idNotFound", new Integer(-1));
   }

   /** Test search by id_ editor doest not find object and gets aid not
     * found error.
     *
     * @throws Exception
     *             the exception
     */
   public void testSearchById_EditorDoestNotFindObjectAndGetsAIDNotFoundError() throws Exception
   {
      tester.login();
      Project nonSearchableProject = setUpProject(false);
      createUserWithoutPermission();

      tester.login(noPermissionUserName, XPlannerWebTester.DEFAULT_PASSWORD);
      tester.gotoRelativeUrl("/do/view/projects");

      searchBy(-1);
      tester.assertKeyPresent("idsearch.error.idNotFound", new Integer(-1));
   }

   /** Test search by id_ editor finds object but has no right to view it
     * and gets a unauthrorized action error.
     *
     * @throws Exception
     *             the exception
     */
   public void testSearchById_EditorFindsObjectButHasNoRightToViewItAndGetsAUnauthrorizedActionError() throws Exception
   {
      tester.login();
      Project nonSearchableProject = setUpProject(false);
      createUserWithoutPermission();
      tester.login(noPermissionUserName, XPlannerWebTester.DEFAULT_PASSWORD);
      tester.gotoRelativeUrl("/do/view/projects");
      searchBy(nonSearchableProject.getId());
      tester.assertKeyPresent("security.notauthorized");

   }

   /** Test search by id_ editor finds object and has right to view it.
     *
     * @throws Exception
     *             the exception
     */
   public void testSearchById_EditorFindsObjectAndHasRightToViewIt() throws Exception
   {
      tester.login();
      Project nonSearchableProject = setUpProject(false);
      createUserWithEditorRoleForProject(project.getName());

      tester.login(editorRoleUserName, XPlannerWebTester.DEFAULT_PASSWORD);
      tester.gotoRelativeUrl("/do/view/projects");

      tester.assertTextPresent(project.getName());
      tester.assertTextNotPresent(nonSearchableProject.getName());

      searchBy(project.getId());
      tester.assertTextPresent(project.getName());
   }

   /** Search by.
     *
     * @param searchedId
     *            the searched id
     */
   private void searchBy(int searchedId)
   {
      tester.assertFormPresent("idSearchForm");
      tester.setWorkingForm("idSearchForm");
      tester.setFormElement("searchedId", searchedId + "");
      tester.submit();
   }

   /** Assert note stuff.
     *
     * @param note
     *            the note
     */
   private void assertNoteStuff(Note note)
   {
      tester.assertTextPresent(note.getSubject());
      tester.assertTextPresent(note.getBody());
   }
}
