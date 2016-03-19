package com.technoetic.xplanner.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletRequest;

/**
 * The Class RequestUtils.
 */
public class RequestUtils {
	
	/**
     * To string.
     *
     * @param request
     *            the request
     * @return the string
     */
	public static String toString(final ServletRequest request) {
		final Map parameterMap = request.getParameterMap();
		final ArrayList names = new ArrayList(parameterMap.keySet());
		Collections.sort(names);
		final StringBuffer str = new StringBuffer();
		str.append("{\n");
		for (int i = 0; i < names.size(); i++) {
			str.append("   ");
			final String name = (String) names.get(i);
			str.append(name);
			str.append("=");
			final Object o = parameterMap.get(name);
			str.append(ToStringUtils.arrayToString((Object[]) o));
			str.append("\n");
		}
		str.append("}\n");
		return str.toString();
	}

	/**
     * Checks if is parameter true.
     *
     * @param request
     *            the request
     * @param paramName
     *            the param name
     * @return true, if is parameter true
     */
	public static boolean isParameterTrue(final ServletRequest request,
			final String paramName) {
		return Boolean.valueOf(request.getParameter(paramName)).booleanValue();
	}

	/**
     * Checks if is attribute true.
     *
     * @param request
     *            the request
     * @param attrName
     *            the attr name
     * @return true, if is attribute true
     */
	public static boolean isAttributeTrue(final ServletRequest request,
			final String attrName) {
		final Object value = request.getAttribute(attrName);
		if (value == null || !(value instanceof Boolean)) {
			return false;
		}
		return ((Boolean) value).booleanValue();
	}
}
