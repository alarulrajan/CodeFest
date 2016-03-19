package junitx.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Jacques Date: Oct 2, 2003 Time: 12:59:21 AM.
 */
public class CallLog
{
   
   /** The actual calls. */
   List<String> actualCalls = new ArrayList<String>();
   
   /** The expected calls. */
   List<String> expectedCalls = new ArrayList<String>();

   /** Adds the actual call to.
     *
     * @param method
     *            the method
     */
   public void addActualCallTo(String method)
   {
      actualCalls.add(method);
   }

   /** Adds the actual call to current method.
     */
   public void addActualCallToCurrentMethod()
   {
      addActualCallTo(getCallerMethod());
   }

   /** Adds the actual call to current method.
     *
     * @param arg
     *            the arg
     */
   public void addActualCallToCurrentMethod(Object arg)
   {
      addActualCallTo(getCallerMethod() + "(" + arg + ")");
   }

   /** Adds the expected call to.
     *
     * @param methodName
     *            the method name
     */
   public void addExpectedCallTo(String methodName)
   {
      expectedCalls.add(methodName);
   }

   /** Adds the expected call to.
     *
     * @param methodName
     *            the method name
     * @param arg
     *            the arg
     */
   public void addExpectedCallTo(String methodName,
                                 Object arg)
   {
      expectedCalls.add(methodName + "(" + arg + ")");
   }

   /** Gets the caller method.
     *
     * @return the caller method
     */
   public static String getCallerMethod()
   {
      String methodName = "";
      StackTraceElement[] stackTrace = new Throwable().getStackTrace();
      if (stackTrace != null)
      {
         methodName = stackTrace[2].getMethodName();
      }
      return methodName;
   }

   /** Verify.
     *
     * @param expectedMethodCalls
     *            the expected method calls
     */
   public void verify(String[] expectedMethodCalls)
   {
      //ChangeSoon change ArrayAssert to print elements on both side if the element counts are different
      ArrayAssert.assertEquals("calls", expectedMethodCalls, actualCalls.toArray(new String[0]));
   }

   /** Verify no calls.
     */
   public void verifyNoCalls()
   {
      verify(new String[0]);
   }

   /** Verify.
     */
   public void verify()
   {
      verify((String[]) expectedCalls.toArray(new String[expectedCalls.size()]));
   }

   /** Reset.
     */
   public void reset()
   {
      expectedCalls.clear();
      actualCalls.clear();
   }
}
