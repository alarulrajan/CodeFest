package com.technoetic.xplanner.forms.test;

import junit.framework.TestCase;

import org.apache.struts.action.ActionErrors;

import com.technoetic.xplanner.forms.ReorderStoriesForm;

/**
 * The Class TestReorderStoriesForm.
 */
public class TestReorderStoriesForm extends TestCase {
   
   /** The reorder stories form. */
   private ReorderStoriesForm reorderStoriesForm;

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      reorderStoriesForm = new ReorderStoriesForm();
   }

   /** Test get order no as int.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetOrderNoAsInt() throws Exception {
      reorderStoriesForm.setOrderNo(0, "2.3");
      assertEquals(2, reorderStoriesForm.getOrderNoAsInt(0));
   }

   /** Test validate.
     *
     * @throws Exception
     *             the exception
     */
   public void testValidate() throws Exception {
      reorderStoriesForm.setOrderNo(0, "x");
      ActionErrors actionErrors = reorderStoriesForm.validate(null, null);
      assertEquals(1, actionErrors.size());
   }
}