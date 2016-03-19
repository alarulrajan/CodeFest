/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.acceptance.security;

import com.technoetic.xplanner.acceptance.web.XPlannerWebTester;
import com.technoetic.xplanner.acceptance.web.XPlannerWebTesterImpl;

/**
 * The Class ServerCacheManager.
 */
public class ServerCacheManager {
   
   /** The invalidate cache. */
   private boolean invalidateCache;

   /** Invalidate server cache if needed.
     */
   public void invalidateServerCacheIfNeeded() {
      if (invalidateCache) {
         XPlannerWebTester tester = new XPlannerWebTesterImpl();
         tester.login();
         tester.gotoRelativeUrl("/do/invalidateCache");
      }
   }

   /** Request server cache invalidation.
     */
   public void requestServerCacheInvalidation() {
      invalidateCache = true;
   }


}
