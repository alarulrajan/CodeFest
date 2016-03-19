/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 31, 2005
 * Time: 1:42:57 AM
 */
package com.technoetic.xplanner.testing;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * The Class DateHelper.
 */
public class DateHelper {
   
   /** The Constant RESOURCE_BUNDLE_NAME. */
   public static final String RESOURCE_BUNDLE_NAME = "ResourceBundle";

   /** The messages. */
   static ResourceBundle messages = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);

   /** Gets the date string days from today.
     *
     * @param daysFromToday
     *            the days from today
     * @return the date string days from today
     */
   public static String getDateStringDaysFromToday(int daysFromToday)
   {
      String dateFormatString = getMessage("format.date");
      DateFormat format = new SimpleDateFormat(dateFormatString);
      return format.format(getDateDaysFromToday(daysFromToday));
   }

   /** Gets the date days from today.
     *
     * @param daysFromToday
     *            the days from today
     * @return the date days from today
     */
   public static Date getDateDaysFromToday(int daysFromToday) {
      return getDateDaysFromDate(new Date(), daysFromToday);
   }

   /** Gets the date days from date.
     *
     * @param today
     *            the today
     * @param daysFromDate
     *            the days from date
     * @return the date days from date
     */
   public static Date getDateDaysFromDate(Date today, int daysFromDate) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(today);
      calendar.add(Calendar.DATE, daysFromDate);
      return calendar.getTime();
   }

   /** Gets the date time string hours from now.
     *
     * @param hoursFromNow
     *            the hours from now
     * @return the date time string hours from now
     */
   public static String getDateTimeStringHoursFromNow(int hoursFromNow)
   {
      String dateFormatString = getMessage("format.datetime");
      DateFormat format = new SimpleDateFormat(dateFormatString);
      return format.format(getDateHoursFromNow(hoursFromNow));
   }

   /** Gets the date hours from now.
     *
     * @param hoursFromNow
     *            the hours from now
     * @return the date hours from now
     */
   private static Date getDateHoursFromNow(int hoursFromNow) {
      Date now = new Date();
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(now);
      calendar.add(Calendar.HOUR, hoursFromNow);
      return calendar.getTime();
   }

   /** Gets the message.
     *
     * @param key
     *            the key
     * @return the message
     */
   public static String getMessage(String key) {
      String message;
      try {
         message = messages.getString(key);
      } catch (Exception e) {
          throw new RuntimeException("No message found for key [" + key + "]." );
//FIXME:                  "\nError: " + ExceptionUtility.stackTraceToString(e));
      }
      return message;
   }

   /** Creates the date.
     *
     * @param year
     *            the year
     * @param month
     *            the month
     * @param day
     *            the day
     * @return the date
     */
   public static Date createDate(int year, int month, int day)
   {
      Calendar cal = Calendar.getInstance();
      cal.set(year, month, day, 0, 0, 0);
      cal.set(Calendar.MILLISECOND, 0);
      return cal.getTime();
   }
}