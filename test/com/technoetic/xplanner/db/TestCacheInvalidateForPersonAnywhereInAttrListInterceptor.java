/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.db;
/**
 * User: mprokopowicz
 * Date: Apr 2, 2006
 * Time: 11:12:22 AM
 */

import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Person;

/**
 * The Class TestCacheInvalidateForPersonAnywhereInAttrListInterceptor.
 */
public class TestCacheInvalidateForPersonAnywhereInAttrListInterceptor extends TestCase {
   
   /** The cache invalidate for person anywhere in attr list interceptor. */
   private CacheInvalidateForPersonAnywhereInAttrListInterceptor cacheInvalidateForPersonAnywhereInAttrListInterceptor;
   
   /** The person id. */
   private int personId = 1;
   
   /** The person. */
   private Person person = new Person("userId");
   
   /** The args. */
   private Object[] args = new Object[]{"arg0", person, "arg2"};

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      person.setId(personId);
      cacheInvalidateForPersonAnywhereInAttrListInterceptor =
            new CacheInvalidateForPersonAnywhereInAttrListInterceptor(new HashMap());
   }

   /** Test get method cache key.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetMethodCacheKey() throws Exception {
      List methodCacheKey = cacheInvalidateForPersonAnywhereInAttrListInterceptor.getMethodCacheKey(args);
      assertEquals(1, methodCacheKey.size());
      assertEquals(new Integer(personId), methodCacheKey.get(0));
   }
}