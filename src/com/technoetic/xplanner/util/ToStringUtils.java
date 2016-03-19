package com.technoetic.xplanner.util;

/**
 * The Class ToStringUtils.
 */
public class ToStringUtils {
    
    /**
     * Array to string.
     *
     * @param array
     *            the array
     * @return the string
     */
    public static String arrayToString(final Object[] array) {
        final StringBuffer str = new StringBuffer();
        if (array.length > 1) {
            str.append("{");
        }
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                str.append(",");
            }
            final Object o = array[i];
            str.append(ToStringUtils.safeToString(o));
        }
        if (array.length > 1) {
            str.append("}");
        }
        return str.toString();
    }

    /**
     * Safe to string.
     *
     * @param o
     *            the o
     * @return the string
     */
    public static String safeToString(final Object o) {
        return o == null ? "null" : o.toString();
    }
}
