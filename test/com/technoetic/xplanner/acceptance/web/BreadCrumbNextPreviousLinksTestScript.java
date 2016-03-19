package com.technoetic.xplanner.acceptance.web;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;
import net.sf.xplanner.tags.NavigationBarTag;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.views.IterationPage;

/**
 * The Class BreadCrumbNextPreviousLinksTestScript.
 */
public class BreadCrumbNextPreviousLinksTestScript extends AbstractPageTestScript {

   /** The middle project. */
   private Project middleProject;
   
   /** The middle story. */
   private UserStory middleStory;
   
   /** The middle iteration. */
   private Iteration middleIteration;


   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
    */
   @Override
protected void setUp() throws Exception {
//      new Timer().run(new Callable() { public Object run() throws Exception { mySetUp(); } });
//   }
//   private void mySetUp() throws Exception {
      super.setUp();
      middleProject = addProjectStructure();
      String nameSeed = middleProject.getName();
      middleProject.setName("2" + nameSeed); //middle
      newProject("1" + nameSeed, "");         //first
      newProject("3" + nameSeed, "");         //last
      commitSession();
      tester.login();
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#tearDown()
    */
   @Override
protected void tearDown() throws Exception {
//      new Timer().run(new Callable() { public Object run() throws Exception { myTearDown(); } });
//   }
//   private void myTearDown() throws Exception {
      super.tearDown();
   }

   /** Test context traverse links.
     *
     * @throws Exception
     *             the exception
     */
   public void testContextTraverseLinks() throws Exception {
      Task task = (Task) middleStory.getTasks().toArray()[1];

      tester.gotoProjectsPage();
      tester.clickLinkWithText(Integer.toString(middleProject.getId()));
      tester.clickLinkWithKey("history.link");
      assertTraversingMaintainTab("p_asc", "p_desc", "history");

      tester.clickLinkWithKey(NavigationBarTag.PROJECT_NAVIGATION_LINK_KEY);
      tester.clickLinkWithText(middleIteration.getName());
      tester.clickLinkWithKey("history.link");
      assertTraversingMaintainTab("i_asc", "i_desc", "history");

      tester.clickLinkWithKey(NavigationBarTag.ITERATION_NAVIGATION_LINK_KEY);
      tester.clickLinkWithText(middleStory.getName());
      tester.clickLinkWithKey("history.link");
      assertTraversingMaintainTab("u_asc", "u_desc", "history");

      tester.clickLinkWithKey(NavigationBarTag.STORY_NAVIGATION_LINK_KEY);
      tester.clickLinkWithText(task.getName());
      tester.clickLinkWithKey("history.link");
      assertTraversingMaintainTab("u_asc", "u_desc", "history");

      assertTraversalOnView(middleProject,
                            middleIteration,
                            IterationPage.METRICS_VIEW,
                            "iteration.metrics.total_hours");
      assertTraversalOnView(middleProject,
                            middleIteration,
                            IterationPage.STATISTICS_VIEW,
                            "iteration.statistics.progress.label");
      assertTraversalOnView(middleProject,
                            middleIteration,
                            IterationPage.ACCURACY_VIEW,
                            "iteration.label.current_status");
   }

   /** Assert traversal on view.
     *
     * @param project
     *            the project
     * @param iteration
     *            the iteration
     * @param view
     *            the view
     * @param expectedKey
     *            the expected key
     */
   private void assertTraversalOnView(Project project, Iteration iteration, String view, String expectedKey) {
      iterationTester.goToView(project, iteration, view);
      assertTraversingMaintainTab("i_asc", "i_desc", expectedKey);
   }

   /** Assert traversing maintain tab.
     *
     * @param asc
     *            the asc
     * @param desc
     *            the desc
     * @param expectedKey
     *            the expected key
     */
   private void assertTraversingMaintainTab(String asc, String desc, String expectedKey) {
      tester.assertKeyPresent(expectedKey);
      tester.clickLink(asc);
      tester.assertKeyPresent(expectedKey);
      tester.clickLink(desc);
      tester.clickLink(desc);
      tester.assertKeyPresent(expectedKey);
      tester.clickLink(asc);
   }

   /** Adds the project structure.
     *
     * @return the project
     * @throws Exception
     *             the exception
     */
   private Project addProjectStructure() throws Exception {
      Project project = newProject();
      for (int i = 0; i < 3; i++) {
         Iteration iteration = newIteration(project);
         if (i == 1) {
            middleIteration = iteration;
            createStoriesForMiddleIteration(iteration);
         } else {
            newUserStory(iteration);
         }
      }
      return project;
   }

   /** Creates the stories for middle iteration.
     *
     * @param iteration
     *            the iteration
     * @throws HibernateException
     *             the hibernate exception
     * @throws RepositoryException
     *             the repository exception
     */
   private void createStoriesForMiddleIteration(Iteration iteration) throws HibernateException, RepositoryException {
      int[] ordersOppositeToPriorities = new int[]{2, 0, 1};
      for (int j = 0; j < 3; j++) {
         UserStory story = newUserStory(iteration);
         story.setPriority(ordersOppositeToPriorities[2 - j]);
         story.setOrderNo(ordersOppositeToPriorities[j]);
         if (j == 2) {
            middleStory = story;
            for (int k = 0; k < 3; k++) {
               newTask(story);
            }
         }
      }
   }
}
