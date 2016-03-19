/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.filters;

import junit.framework.TestCase;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.format.PrintfFormat;

/**
 * The Class TestActivityLogFilterHelper.
 */
public class TestActivityLogFilterHelper extends TestCase {
   
   /** The support. */
   private XPlannerTestSupport support;
   
   /** The activity log filter helper. */
   private ActivityLogFilterHelper activityLogFilterHelper;
   
   /** The action. */
   private String action;
   
   /** The action link. */
   private String actionLink;
   
   /** The ip address. */
   private String ipAddress;
   
   /** The query string. */
   private String queryString;

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      action = "edit/project";
      actionLink = "/do/" + action;
      ipAddress = "10.10.100.100";
      queryString = "projectId=0";
      activityLogFilterHelper = new ActivityLogFilterHelper();
      support = new XPlannerTestSupport();
      support.request.setRequestURI(actionLink);
      support.request.setRemoteAddr(ipAddress);
      support.request.setQueryString(queryString);
   }

   /** Test activity log filter helper.
     *
     * @throws Exception
     *             the exception
     */
   public void testActivityLogFilterHelper() throws Exception {
      activityLogFilterHelper.doHelperSetUp(support.request);

      String actualLogRecord = activityLogFilterHelper.getStartLogRecord();
      String expectedLogRecord = getExpectedStartLog();
      assertEquals("Wrong filter START log message", expectedLogRecord, actualLogRecord);

      actualLogRecord = activityLogFilterHelper.getEndLogRecord();
      expectedLogRecord = getExpectedEndLog();
      assertEquals("Wrong filter END log message", expectedLogRecord, actualLogRecord);
   }

   /** Gets the expected start log.
     *
     * @return the expected start log
     */
   private String getExpectedStartLog(){
      PrintfFormat formater = new PrintfFormat(ActivityLogFilterHelper.LOG_LINE_PATTERN);
      Object[] elements = {activityLogFilterHelper.getFormatedStartDate(),
                           ActivityLogFilterHelper.NO_USER_MSG,
                           ipAddress,
                           action,
                           ActivityLogFilterHelper.ACTION_START,
                           "",
                           queryString};
      return formater.sprintf(elements);
   }

   /** Gets the expected end log.
     *
     * @return the expected end log
     */
   private String getExpectedEndLog() {
      PrintfFormat formater = new PrintfFormat(ActivityLogFilterHelper.LOG_LINE_PATTERN);
      Object[] elements = {activityLogFilterHelper.getFormatedEndDate(),
                           ActivityLogFilterHelper.NO_USER_MSG,
                           ipAddress,
                           action, ActivityLogFilterHelper.ACTION_END,
                           activityLogFilterHelper.getActionPeriod(),
                           queryString};
      return formater.sprintf(elements);
   }

}
