/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.db;
/**
 * User: mprokopowicz
 * Date: Mar 31, 2006
 * Time: 11:48:35 AM
 */

import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;

import com.technoetic.xplanner.AbstractUnitTestCase;

/**
 * The Class TestMethodCacheInterceptor.
 */
public class TestMethodCacheInterceptor extends AbstractUnitTestCase {
   
   /** The method cache interceptor. */
   private MethodCacheInterceptor methodCacheInterceptor;
   
   /** The cache map. */
   private Map cacheMap;
   
   /** The mock method invocation. */
   private MethodInvocation mockMethodInvocation;
   
   /** The Constant RETURN_VALUE. */
   static final Integer RETURN_VALUE = new Integer(1);
   
   /** The Constant TEST_METHOD_NAME. */
   static final String TEST_METHOD_NAME = "testMethodName";


   /* (non-Javadoc)
    * @see com.technoetic.xplanner.AbstractUnitTestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      cacheMap = new HashMap();
      methodCacheInterceptor = new MethodCacheInterceptor(cacheMap);
      mockMethodInvocation = createLocalMock(MethodInvocation.class);
   }

   /** Test cache_no data in cache.
     *
     * @throws Throwable
     *             the throwable
     */
   public void testCache_noDataInCache() throws Throwable {
      final List argumentList = Arrays.asList(new Object[]{"arg1"});
      expect(mockMethodInvocation.proceed()).andReturn(RETURN_VALUE);
      replay();

      methodCacheInterceptor.cache(TEST_METHOD_NAME, argumentList, mockMethodInvocation);

      verify();
      assertTrue(cacheMap.containsKey(TEST_METHOD_NAME));
      final Map methodCache = (Map) cacheMap.get(TEST_METHOD_NAME);
      Object cachedValue = methodCache.get(argumentList);
      assertEquals(cachedValue, RETURN_VALUE);
   }

   /** Test cache_arguments dont match.
     *
     * @throws Throwable
     *             the throwable
     */
   public void testCache_argumentsDontMatch() throws Throwable {
      final List arg1List = Arrays.asList(new Object[]{"arg1"});
      final List arg2List = Arrays.asList(new Object[]{"arg2"});
      Map methodCache = new HashMap();
      cacheMap.put(TEST_METHOD_NAME, methodCache);
      methodCache.put(arg1List, RETURN_VALUE);
      expect(mockMethodInvocation.proceed()).andReturn(RETURN_VALUE);
      replay();

      methodCacheInterceptor.cache(TEST_METHOD_NAME, arg2List, mockMethodInvocation);

      verify();
      assertTrue(cacheMap.containsKey(TEST_METHOD_NAME));
      final Map actualMethodCache = (Map) cacheMap.get(TEST_METHOD_NAME);
      Object cachedValue = actualMethodCache.get(arg2List);
      assertEquals(cachedValue, RETURN_VALUE);
      assertEquals(2, methodCache.keySet().size());
   }

   /** Test cache_get cached value.
     *
     * @throws Throwable
     *             the throwable
     */
   public void testCache_getCachedValue() throws Throwable {
      final List argList = Arrays.asList(new Object[]{"arg1"});
      final List argList2 = new ArrayList(Arrays.asList(new Object[]{"arg1"}));
      Map methodCache = new HashMap();
      methodCache.put(argList, RETURN_VALUE);
      cacheMap.put(TEST_METHOD_NAME, methodCache);
      replay();

      methodCacheInterceptor.cache(TEST_METHOD_NAME, argList2, mockMethodInvocation);

      verify();
      assertTrue(cacheMap.containsKey(TEST_METHOD_NAME));
      final Map actualMethodCache = (Map) cacheMap.get(TEST_METHOD_NAME);
      Object cachedValue = actualMethodCache.get(argList);
      assertEquals(cachedValue, RETURN_VALUE);
   }
}