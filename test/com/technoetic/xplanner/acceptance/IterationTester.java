/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Jan 15, 2006
 * Time: 10:40:14 PM
 */
package com.technoetic.xplanner.acceptance;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;

import com.technoetic.xplanner.acceptance.web.XPlannerWebTester;
import com.technoetic.xplanner.views.IterationPage;

/**
 * The Class IterationTester.
 */
public class IterationTester {
   
   /** The tester. */
   XPlannerWebTester tester;

   /** Adds the iteration.
     *
     * @param iterationName
     *            the iteration name
     * @param startDateString
     *            the start date string
     * @param endDateString
     *            the end date string
     * @param description
     *            the description
     * @return the string
     */
   public String addIteration(String iterationName,
                              String startDateString,
                              String endDateString,
                              String description) {
      tester.assertOnProjectPage();
      tester.clickLinkWithKey("project.link.create_iteration");
      tester.assertLinkPresentWithKey("form.description.help");
      tester.assertKeyPresent("iteration.editor.create");
      tester.assertKeyPresent("iteration.editor.name");
      tester.assertKeyPresent("iteration.editor.set_date");
      tester.assertKeyPresent("iteration.editor.start_date");
      tester.assertKeyPresent("iteration.editor.end_date");
      tester.assertKeyPresent("iteration.editor.description");

      tester.setFormElement("name", iterationName);
      tester.setFormElement("startDateString", startDateString);
      tester.setFormElement("endDateString", endDateString);
      tester.setFormElement("description", description);
      tester.submit();
      tester.assertOnProjectPage();
      return tester.getIdFromLinkWithText(iterationName);
   }


   /** Start.
     *
     * @param iteration
     *            the iteration
     */
   public void start(Iteration iteration) {
      goToDefaultView(iteration);
      startCurrentIteration();
   }

   /** Close.
     *
     * @param iteration
     *            the iteration
     */
   public void close(Iteration iteration) {
      goToDefaultView(iteration);
      closeCurrentIteration();
   }

   /** Start current iteration.
     */
   public void startCurrentIteration() {
      assertOnIterationPage();
      tester.clickLinkWithKey(IterationPage.START_ACTION);
      confirmStartCurrentIteration();
      assertCurrentIterationStarted();
   }

   /** Confirm start current iteration.
     */
   private void confirmStartCurrentIteration() {
//      if (tester.getDialog().getElement("start") != null) {
//         if(tester.getDialog().getElement("closeIterations") != null) {
//            tester.uncheckCheckbox("closeIterations");
//         }
//         tester.submit("start");
//      }
   }

   /** Assert on start iteration prompt page and start.
     */
   public void assertOnStartIterationPromptPageAndStart() {
      assertOnStartIterationPromptPage();
      confirmStartCurrentIteration();
   }

   /** Assert on start iteration prompt page.
     */
   public void assertOnStartIterationPromptPage() {
      tester.assertKeyPresent("iteration.status.editor.message_4");
   }

   /** Close current iteration.
     */
   public void closeCurrentIteration() {
      assertOnIterationPage();
      tester.clickLinkWithKey(IterationPage.CLOSE_ACTION);
   }

   /** Go to default view.
     *
     * @param iteration
     *            the iteration
     */
   public void goToDefaultView(Iteration iteration) {goToDefaultView(iteration.getId());}
   
   /** Go to default view.
     *
     * @param iterationId
     *            the iteration id
     */
   public void goToDefaultView(int iterationId) {tester.gotoPage("view", "iteration", iterationId);}
   
   /** Go to view.
     *
     * @param project
     *            the project
     * @param iteration
     *            the iteration
     * @param view
     *            the view
     */
   public void goToView(Project project, Iteration iteration, String view) {
      tester.gotoProjectsPage();
      tester.clickLinkWithText(Integer.toString(project.getId()));
      tester.clickLinkWithText(iteration.getName());
      tester.clickLinkWithKey(view);
   }

   /** Assert on iteration page.
     */
   public void assertOnIterationPage() {
      tester.assertKeyPresent("iteration.prefix");
      tester.assertLinkPresentWithKey("navigation.top");
      tester.assertLinkPresentWithKey("navigation.project");
      tester.assertLinkPresentWithKey("action.edit.iteration");
//      assertLinkPresentWithKey("iteration.link.edit");
      tester.assertLinkPresentWithKey("iteration.link.metrics");
      tester.assertLinkPresentWithKey("iteration.link.create_story");
      tester.assertKeyPresent("notes.label.notes");
   }

   /** Assert current iteration started.
     */
   public void assertCurrentIterationStarted() {
      tester.assertKeyNotPresent(IterationPage.START_ACTION);
      tester.assertKeyPresent(IterationPage.CLOSE_ACTION);
   }

   /** Assert current iteration closed.
     */
   public void assertCurrentIterationClosed() {
      tester.assertKeyPresent(IterationPage.START_ACTION);
      tester.assertKeyNotPresent(IterationPage.CLOSE_ACTION);
   }

   /** Instantiates a new iteration tester.
     *
     * @param tester
     *            the tester
     */
   public IterationTester(XPlannerWebTester tester) {
      this.tester = tester;
   }


}