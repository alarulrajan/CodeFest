/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.forms.ReorderStoriesForm;

/**
 * Created by IntelliJ IDEA.
 * User: SG0897500
 * Date: Mar 7, 2006
 * Time: 3:11:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestIteration extends AbstractUnitTestCase {

   /** The expected story list. */
   private Collection expectedStoryList = null;
   
   /** The iteration. */
   private Iteration iteration = null;


   /* (non-Javadoc)
    * @see com.technoetic.xplanner.AbstractUnitTestCase#setUp()
    */
   @Override
protected void setUp() throws Exception {
      super.setUp();
      iteration = new Iteration();
    }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.AbstractUnitTestCase#tearDown()
    */
   @Override
protected void tearDown() throws Exception {
      super.tearDown();
   }

   /** Test modify story order with no duplicates.
     */
   public void testModifyStoryOrderWithNoDuplicates() {

      assertStoryData(new int[]{1,2,3}, new int[]{1,2,3}, new int[]{3, 1, 2}, new int[]{3, 1, 2});
   }

   /** Test modify story order with gap in from order.
     */
   public void testModifyStoryOrderWithGapInFromOrder() {

      assertStoryData(new int[]{1,2,3}, new int[]{1,2,3}, new int[]{3, 1, 2}, new int[]{4, 1, 2});
   }

   /** Test modify story order with gap in from order2.
     */
   public void testModifyStoryOrderWithGapInFromOrder2() {

      assertStoryData(new int[]{1,2,3}, new int[]{1,2,3}, new int[]{1, 2, 3}, new int[]{2, 4, 6});
   }

   /** Test modify story order with gap in from order3.
     */
   public void testModifyStoryOrderWithGapInFromOrder3() {

      assertStoryData(new int[]{1,2,3}, new int[]{1,2,3}, new int[]{3, 1, 2}, new int[]{6, 2, 4});
   }

   /** Test modify story order with duplicate form orders.
     */
   public void testModifyStoryOrderWithDuplicateFormOrders() {

      assertStoryData(new int[]{1,2,3}, new int[]{1,2,3}, new int[]{3, 2, 1}, new int[]{3, 2, 2});
   }

   /** Test modify story order with duplicate form orders2.
     */
   public void testModifyStoryOrderWithDuplicateFormOrders2() {

      assertStoryData(new int[]{1,2,3}, new int[]{1,2,3}, new int[]{1, 3, 2}, new int[]{1, 2, 2});
   }

   /** Test modify story order with duplicate form orders3.
     */
   public void testModifyStoryOrderWithDuplicateFormOrders3() {

      assertStoryData(new int[]{1,2,3,4}, new int[]{1,2,3,4}, new int[]{1, 4, 3, 2}, new int[]{1, 2, 2, 2});
   }

   /** Test modify story order with duplicate form orders4.
     */
   public void testModifyStoryOrderWithDuplicateFormOrders4() {

      assertStoryData(new int[]{1,2,3,4,5}, new int[]{1,2,3,4,5}, new int[]{1, 4, 3, 2, 5}, new int[]{1, 2, 2, 2, 3});
   }

   /** Test modify story order with gaps and duplicates.
     */
   public void testModifyStoryOrderWithGapsAndDuplicates() {

      assertStoryData(new int[]{1,2,3,4,5}, new int[]{1,2,3,4,5}, new int[]{2,1,5,4,3}, new int[]{1,1,6,5,3});
   }

   /** Test modify story order for existing stories continued or moved
     * stories from past iteration.
     */
   public void testModifyStoryOrderForExistingStoriesContinuedOrMovedStoriesFromPastIteration() {

      assertStoryData(new int[]{1,2,3,4,5}, new int[]{0,1,2,3,4}, new int[]{1,2,3,4,5}, new int[]{0,1,2,3,4});
   }

   /** Test modify story order for existing stories continued or moved
     * stories from future iteration.
     */
   public void testModifyStoryOrderForExistingStoriesContinuedOrMovedStoriesFromFutureIteration() {

      assertStoryData(new int[]{1,2,3,4,5}, new int[]{1,2,3,4,Integer.MAX_VALUE}, new int[]{1,2,3,4,5}, new int[]{1,2,3,4,Integer.MAX_VALUE});
   }

   /** Assert story data.
     *
     * @param storyIds
     *            the story ids
     * @param storyOrders
     *            the story orders
     * @param storyExpectedOrder
     *            the story expected order
     * @param formOrder
     *            the form order
     */
   private void assertStoryData(int[] storyIds, int[] storyOrders, int[] storyExpectedOrder, int[] formOrder)
   {
      List<UserStory> storyListToBeOrdered = new ArrayList<UserStory>(storyIds.length);
      expectedStoryList = new ArrayList<String>(storyIds.length);
      ReorderStoriesForm mockForm = new ReorderStoriesForm();
      for (int i = 0; i < storyIds.length; i++) {
         UserStory storyToBeOrdered = new UserStory();
         storyToBeOrdered.setId(storyIds[i]);
         storyToBeOrdered.setOrderNo(storyOrders[i]);
         storyListToBeOrdered.add(storyToBeOrdered);
         UserStory storyExpected = new UserStory();
         storyExpected.setId(storyIds[i]);
         storyExpected.setOrderNo(storyExpectedOrder[i]);
         expectedStoryList.add(storyExpected);
         mockForm.setOrderNo(i, Integer.toString(formOrder[i]));
         mockForm.setStoryId(i, Integer.toString(storyIds[i]));
         iteration.getUserStories().add(storyToBeOrdered);
      }
      iteration.modifyStoryOrder(buildStoryIdNewOrderArray(storyIds, formOrder));
      Collections.sort(storyListToBeOrdered, new StoryOrderNoComparator());
      verifyResultsOrder(storyListToBeOrdered);
      verifyNoDuplicates(storyListToBeOrdered);
   }

   /** Builds the story id new order array.
     *
     * @param storyIds
     *            the story ids
     * @param formOrder
     *            the form order
     * @return the int[][]
     */
   private int[][] buildStoryIdNewOrderArray(int[] storyIds, int[] formOrder) {
      int[][] storyIdAndNewOrder = new int[storyIds.length][2];
      for (int index = 0; index < storyIdAndNewOrder.length; index++) {
         storyIdAndNewOrder[index][0] = storyIds[index];
         storyIdAndNewOrder[index][1] = formOrder[index];
      }
      return storyIdAndNewOrder;
   }


   /** Verify results order.
     *
     * @param reorderedStoryResult
     *            the reordered story result
     */
   private void verifyResultsOrder(Collection reorderedStoryResult) {

      for (Iterator expectedIterator = expectedStoryList.iterator(); expectedIterator.hasNext();) {
         UserStory expectedStory = (UserStory) expectedIterator.next();
         for (Iterator resultIterator = reorderedStoryResult.iterator(); resultIterator.hasNext();) {
            UserStory resultStory = (UserStory)resultIterator.next();
            if(expectedStory.getId() == resultStory.getId()){
               assertEquals("The order does not match", expectedStory.getOrderNo(), resultStory.getOrderNo());
            }
         }
      }
   }

   /** Verify no duplicates.
     *
     * @param reorderedStoryResult
     *            the reordered story result
     */
   private void verifyNoDuplicates(Collection reorderedStoryResult) {
      Map storiesByOrder = new HashMap();
      for (Iterator iterator = reorderedStoryResult.iterator(); iterator.hasNext();) {
         UserStory userStory = (UserStory) iterator.next();
         UserStory userStoryWithSameOrder = (UserStory) storiesByOrder.get(new Integer(userStory.getOrderNo()));
         if (userStoryWithSameOrder != null) {
            fail("User story ");
         }
         storiesByOrder.put(new Integer(userStory.getOrderNo()), userStory);
      }
   }

   /** The Class StoryOrderNoComparator.
     */
   class StoryOrderNoComparator implements Comparator {

      /* (non-Javadoc)
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(Object o1, Object o2) {
         return new Integer(((UserStory)o1).getOrderNo()).compareTo(new Integer(((UserStory)o2).getOrderNo()));
      }
   }
}

