/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.db;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * User: mprokopowicz Date: Mar 31, 2006 Time: 12:21:35 PM.
 */


public class TestMethodCacheInvalidateInterceptor extends TestCase {
   
   /** The Constant RETURN_VALUE. */
   static final Integer RETURN_VALUE = new Integer(1);
   
   /** The Constant TEST_METHOD_NAME. */
   static final String TEST_METHOD_NAME = "testMethodName";
   
   /** The method cache invalidate interceptor. */
   private MethodCacheInvalidateInterceptor methodCacheInvalidateInterceptor;
   
   /** The cache map. */
   private Map cacheMap;
   
   /** The attrs. */
   private Object[] attrs;

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      attrs = new Object[]{"arg1"};
      super.setUp();
      cacheMap = new HashMap();
      methodCacheInvalidateInterceptor = new MethodCacheInvalidateInterceptor(cacheMap);
      methodCacheInvalidateInterceptor.setMethodsToInvalidate(Arrays.asList(new Object[]{TEST_METHOD_NAME}));
   }

   /** Test invalidate_method with arguments.
     *
     * @throws Exception
     *             the exception
     */
   public void testInvalidate_methodWithArguments() throws Exception {
      List argList = Arrays.asList(new Object[]{"arg1"});
      Map methodCache = new HashMap();
      methodCache.put(argList, RETURN_VALUE);
      cacheMap.put(TEST_METHOD_NAME, methodCache);
      methodCacheInvalidateInterceptor.invalidate(argList);
      assertTrue(methodCache.isEmpty());
   }

   /** Test invalidate_method with no arguments.
     *
     * @throws Exception
     *             the exception
     */
   public void testInvalidate_methodWithNoArguments() throws Exception {
      List callerArgList = Arrays.asList(attrs);
      List cachedMethodArgList = Collections.EMPTY_LIST;
      Map methodCache = new HashMap();
      methodCache.put(cachedMethodArgList, RETURN_VALUE);
      cacheMap.put(TEST_METHOD_NAME, methodCache);
      methodCacheInvalidateInterceptor.invalidate(callerArgList);
      assertTrue(methodCache.isEmpty());
   }

   /** Test get method cache key.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetMethodCacheKey() throws Exception {
      List methodCacheKey = methodCacheInvalidateInterceptor.getMethodCacheKey(attrs);
      assertEquals(1, methodCacheKey.size());
      assertEquals("arg1", methodCacheKey.get(0));
   }
}