package com.technoetic.xplanner.util;

import org.apache.log4j.Logger;

/**
 * The Class LogUtil.
 */
public class LogUtil {
    
    /**
     * Gets the caller method.
     *
     * @return the caller method
     */
    public static String getCallerMethod() {
        String methodName = "";
        final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace != null) {
            methodName = stackTrace[2].getClassName() + "."
                    + stackTrace[2].getMethodName();
        }

        return methodName;
    }

    /**
     * Gets the caller class.
     *
     * @return the caller class
     */
    public static String getCallerClass() {
        String className = "";
        final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace != null) {
            className = stackTrace[2].getClassName();
        }

        return className;
    }

    /**
     * Gets the logger.
     *
     * @param c
     *            the c
     * @return the logger
     */
    public static Logger getLogger(final Class c) {
        return Logger.getLogger(c.getName());
    }

    /**
     * Gets the logger.
     *
     * @return the logger
     */
    public static Logger getLogger() {
        return Logger.getLogger(LogUtil.getCallerClass());
    }
}
