package com.technoetic.xplanner.testing;


import java.text.MessageFormat;

import junitx.framework.CallLog;

import com.technoetic.xplanner.util.Callable;


/**
 *
 * How to use the Timer class to time the setup of a test
 *
 public void setUp() throws Exception {
    new Timer().run("setUp", new Callable() {
       public Object run() throws Exception {
          mySetUp();
          return null;
       }
    });
 }

 private void mySetUp() throws Exception {
 //original setup
 }

 }
 */
public class Timer {
   
   /** Run.
     *
     * @param c
     *            the c
     * @throws Exception
     *             the exception
     */
   public void run (Callable c) throws Exception {
      run(CallLog.getCallerMethod(), c);
   }

   /** Run.
     *
     * @param message
     *            the message
     * @param c
     *            the c
     * @throws Exception
     *             the exception
     */
   public void run(String message, Callable c) throws Exception {
      long start = System.currentTimeMillis();
      c.run();
      long duration = System.currentTimeMillis() - start;
      long ms = duration % 1000;
      duration /= 1000;
      long s = duration % 60;
      duration /= 60;
      long m = duration % 60;
      duration /= 60;
      long h = duration % 24;
      logTime(message, h, m, s, ms);
   }

   /** Log time.
     *
     * @param message
     *            the message
     * @param h
     *            the h
     * @param m
     *            the m
     * @param s
     *            the s
     * @param ms
     *            the ms
     */
   static private void logTime(String message, long h, long m, long s, long ms) {
      System.out.println(MessageFormat.format("{0}={1,number,00}:{2,number,00}:{3,number,00}.{4,number,000}",
                                              new Object[] {message,
                                                            new Long(h),
                                                            new Long(m),
                                                            new Long(s),
                                                            new Long(ms)}));
   }

   /** The main method.
     *
     * @param args
     *            the arguments
     */
   public static void main(String[] args) {
      logTime("test", 1,2,3,4);
   }
}