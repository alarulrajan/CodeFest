/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Apr 2, 2006
 * Time: 1:34:01 PM
 */
package com.technoetic.xplanner.util;

import junit.framework.TestCase;
import junitx.framework.ArrayAssert;

/**
 * The Class TestClassUtil.
 */
public class TestClassUtil extends TestCase {

   /** The Class Base.
     */
   public static class Base {
      
      /** The i. */
      private int i;

      /** Sets the i.
         *
         * @param i
         *            the new i
         */
      public void setI(int i) { this.i = i; }
   }

   /** The Class Derived.
     */
   public static class Derived extends Base {
      
      /** The s. */
      private String s;

      /** Sets the s.
         *
         * @param s
         *            the new s
         */
      public void setS(String s) { this.s = s; }
   }

   /** The Class DerivedOverriding.
     */
   public static class DerivedOverriding extends Base {
      
      /** The i. */
      private int i;
      
      /** The s. */
      private String s;

      /* (non-Javadoc)
       * @see com.technoetic.xplanner.util.TestClassUtil.Base#setI(int)
       */
      public void setI(int i) { this.i = i; }
      
      /** Sets the s.
         *
         * @param s
         *            the new s
         */
      public void setS(String s) { this.s = s; }
   }

   /** Test get all field names without inheritance.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetAllFieldNamesWithoutInheritance() throws Exception {
      assertMembersEquals(new String[]{"i"}, new Base());
   }

   /** Test get all field names with inherited fields.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetAllFieldNamesWithInheritedFields() throws Exception {
      assertMembersEquals(new String[]{"s", "i"},new Derived());
   }

   /** Test get all field names with inherited and overridden fields.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetAllFieldNamesWithInheritedAndOverriddenFields() throws Exception {
      assertMembersEquals(new String[]{"s", "i"}, new DerivedOverriding());
   }

   /** Assert members equals.
     *
     * @param expectedMembers
     *            the expected members
     * @param object
     *            the object
     * @throws Exception
     *             the exception
     */
   private void assertMembersEquals(String[] expectedMembers, Base object) throws Exception {
     ArrayAssert.assertEquivalenceArrays(expectedMembers, ClassUtil.getAllFieldNames(object).toArray());
   }

   /** Test get field value without inheritance.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetFieldValueWithoutInheritance() throws Exception {
      Base object = new Base();
      object.setI(1);
     assertEquals(new Integer(1), ClassUtil.getFieldValue(object, "i"));
   }

   /** Test get field value with inheritance.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetFieldValueWithInheritance() throws Exception {
      Derived object = new Derived();
      object.setI(1);
      object.setS("test");
      assertEquals(new Integer(1), ClassUtil.getFieldValue(object, "i"));
      assertEquals("test", ClassUtil.getFieldValue(object, "s"));
   }

   /** Test get field value with inheritance and overridden fields.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetFieldValueWithInheritanceAndOverriddenFields() throws Exception {
      DerivedOverriding object = new DerivedOverriding();
      object.setI(1);
      object.setS("test");
      assertEquals(new Integer(1), ClassUtil.getFieldValue(object, "i"));
      assertEquals("test", ClassUtil.getFieldValue(object, "s"));
   }

   /** Test get field value throw exception if member does not exist.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetFieldValueThrowExceptionIfMemberDoesNotExist() throws Exception {
      Base object = new Base();
      try {
         ClassUtil.getFieldValue(object, "unkown");
         fail("Did not throw " + NoSuchFieldException.class);
      } catch (NoSuchFieldException e) {
      }
   }



}