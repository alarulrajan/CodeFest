package com.technoetic.xplanner.util;

public class ToStringUtils {
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

	public static String safeToString(final Object o) {
		return o == null ? "null" : o.toString();
	}
}
