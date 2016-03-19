package com.technoetic.xplanner.importer;

import junit.framework.TestCase;

import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import com.technoetic.xplanner.util.MainBeanFactory;

/*
 * $Header$
 * $Revision: 659 $
 * $Date: 2005-08-18 16:05:57 +0300 (Чт, 18 авг 2005) $
 *
 * Copyright (c) 1999-2002 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */

/**
 * The Class BaseTestCase.
 */
public abstract class BaseTestCase extends TestCase
{
   
   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception
   {
//      pushNewFactory();
      super.setUp();
   }

   /* (non-Javadoc)
    * @see junit.framework.TestCase#tearDown()
    */
   protected void tearDown() throws Exception
   {
      super.tearDown();
//      popFactory();
   }

   /** Push new factory.
     */
   public static void pushNewFactory()
   {
      MainBeanFactory.factory = new DefaultListableBeanFactory(MainBeanFactory.factory);
   }

   /** Pop factory.
     */
   public static void popFactory()
   {
      MainBeanFactory.factory = ((HierarchicalBeanFactory) MainBeanFactory.factory).getParentBeanFactory();
   }
}
