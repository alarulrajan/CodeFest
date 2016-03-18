/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 20, 2006
 * Time: 6:05:05 AM
 */
package com.technoetic.xplanner.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassUtil {
	public static Object getFieldValue(final Object object,
			final String fieldName) throws Exception {
		final Field field = ClassUtil.getField(object, fieldName);
		field.setAccessible(true);
		return field.get(object);
	}

	public static void setFieldValue(final Object object,
			final String fieldName, final Object value) throws Exception {
		final Field field = ClassUtil.getField(object, fieldName);
		field.setAccessible(true);
		field.set(object, value);
	}

	private static Field getField(final Object object, final String fieldName)
			throws NoSuchFieldException {
		final Map allFields = ClassUtil.getAllFieldByNames(object.getClass());
		final Field field = (Field) allFields.get(fieldName);
		if (field == null) {
			throw new NoSuchFieldException(fieldName + " is not a field of "
					+ object.getClass());
		}
		return field;
	}

	public static List getAllFieldNames(final Object object) throws Exception {
		final Map allFields = ClassUtil.getAllFieldByNames(object.getClass());
		return new ArrayList(allFields.keySet());
	}

	public static List getAllFields(final Object object) throws Exception {
		final Map allFields = ClassUtil.getAllFieldByNames(object.getClass());
		return new ArrayList(allFields.values());
	}

	public static Map getAllFieldByNames(final Class theClass) {
		Map fields;
		final Class superclass = theClass.getSuperclass();
		if (superclass != Object.class) {
			fields = ClassUtil.getAllFieldByNames(superclass);
		} else {
			fields = new HashMap();
		}
		fields.putAll(ClassUtil.getClassFieldByNames(theClass));
		return fields;
	}

	private static Map getClassFieldByNames(final Class theClass) {
		final Field[] fields = theClass.getDeclaredFields();
		final Map fieldNames = new HashMap(fields.length);
		for (int i = 0; i < fields.length; i++) {
			final Field field = fields[i];
			field.setAccessible(true);
			fieldNames.put(field.getName(), field);
		}
		return fieldNames;
	}

}