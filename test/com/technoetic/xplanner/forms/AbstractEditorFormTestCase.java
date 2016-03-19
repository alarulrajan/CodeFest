/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 30, 2005
 * Time: 3:57:02 PM
 */
package com.technoetic.xplanner.forms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import junit.framework.TestCase;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.technoetic.xplanner.XPlannerTestSupport;

/**
 * The Class AbstractEditorFormTestCase.
 */
public class AbstractEditorFormTestCase extends TestCase {
   
   /** The support. */
   protected XPlannerTestSupport support;
   
   /** The form. */
   protected AbstractEditorForm form;
   
   /** The Constant LOCALE. */
   public static final Locale LOCALE = new Locale("da", "nl");
   
   /** The Constant DATE_TIME_FORMAT. */
   public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
   
   /** The Constant DATE_FORMAT. */
   public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      support = new XPlannerTestSupport();
      support.resources.setMessage("format.datetime", DATE_TIME_FORMAT.toPattern());
      support.resources.setMessage("format.date", DATE_FORMAT.toPattern());
      support.request.setLocale(LOCALE);
      form.setServlet(support.actionServlet);
   }

   /** Assert one error.
     *
     * @param expectedErrorKey
     *            the expected error key
     * @param errors
     *            the errors
     */
   public void assertOneError(String expectedErrorKey, ActionErrors errors) {
      assertErrorsEqual(new String[] {expectedErrorKey}, errors);
   }

   /** Assert errors equal.
     *
     * @param expectedErrorKeys
     *            the expected error keys
     * @param errors
     *            the errors
     */
   public void assertErrorsEqual(String[] expectedErrorKeys, ActionErrors errors) {
      assertCollectionsEqual(Arrays.asList(expectedErrorKeys), toKeysList(errors));
   }

   /** Assert collections equal.
     *
     * @param expectedList
     *            the expected list
     * @param actualList
     *            the actual list
     */
   private void assertCollectionsEqual(List expectedList, List actualList) {
      List surplusItems = new ArrayList();
      List missingItems = new ArrayList();
      List matchingExpectedItems = new ArrayList();
      List matchingActualItems = new ArrayList();

      compareLists(expectedList, actualList, missingItems, matchingExpectedItems);
      compareLists(actualList, expectedList, surplusItems, matchingActualItems);

      List missingMatching = new ArrayList();
      compareLists(matchingExpectedItems, matchingActualItems, missingMatching, new ArrayList());
      assertEquals(0, missingMatching.size());

      if (missingItems.size() > 0 || surplusItems.size() > 0) {
         String errMsg = "Collections not equal\n";
         errMsg += dumpCollection("missing", missingItems);
         errMsg += dumpCollection("surplus", surplusItems);
         errMsg += dumpCollection("matching", matchingExpectedItems);
         fail(errMsg);
      }
   }

   /** Compare lists.
     *
     * @param expectedList
     *            the expected list
     * @param actualList
     *            the actual list
     * @param missingItems
     *            the missing items
     * @param matchingItems
     *            the matching items
     */
   private void compareLists(List expectedList, List actualList, List missingItems, List matchingItems) {
      List actuals = new ArrayList(actualList);
      for (Iterator expectedIterator = expectedList.iterator();expectedIterator.hasNext();) {
         Object key = expectedIterator.next();
         boolean found = false;
         Iterator actualIterator = actuals.iterator();
         while (actualIterator.hasNext() && !found) {
            found = actualIterator.next().equals(key);
            actualIterator.remove();
         }
         if (!found)
            missingItems.add(key);
         else
            matchingItems.add(key);
      }
   }

   /** To keys list.
     *
     * @param errors
     *            the errors
     * @return the list
     */
   private List toKeysList(ActionErrors errors) {
      List actualList = new ArrayList();
      Iterator iterator = errors.get();
      while (iterator.hasNext()) {
         ActionError error = (ActionError) iterator.next();
         actualList.add(error.getKey());
      }
      return actualList;
   }

   /** Dump collection.
     *
     * @param message
     *            the message
     * @param items
     *            the items
     * @return the string
     */
   private String dumpCollection(String message, Collection items) {
      String msg = items.size() + " " +message;
      Iterator iterator = items.iterator();
      boolean first = true;
      while (iterator.hasNext()) {
         if (first) {
            first = false;
            msg += ":\n";
         } else {
            msg += ",\n";
         }
         msg += "  " + iterator.next();
      }
      return msg+"\n";
   }
}