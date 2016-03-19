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

/**
 * The Class ClassUtil.
 */
public class ClassUtil {
	
	/**
     * Gets the field value.
     *
     * @param object
     *            the object
     * @param fieldName
     *            the field name
     * @return the field value
     * @throws Exception
     *             the exception
     */
	public static Object getFieldValue(final Object object,
			final String fieldName) throws Exception {
		final Field field = ClassUtil.getField(object, fieldName);
		field.setAccessible(true);
		return field.get(object);
	}

	/**
     * Sets the field value.
     *
     * @param object
     *            the object
     * @param fieldName
     *            the field name
     * @param value
     *            the value
     * @throws Exception
     *             the exception
     */
	public static void setFieldValue(final Object object,
			final String fieldName, final Object value) throws Exception {
		final Field field = ClassUtil.getField(object, fieldName);
		field.setAccessible(true);
		field.set(object, value);
	}

	/**
     * Gets the field.
     *
     * @param object
     *            the object
     * @param fieldName
     *            the field name
     * @return the field
     * @throws NoSuchFieldException
     *             the no such field exception
     */
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

	/**
     * Gets the all field names.
     *
     * @param object
     *            the object
     * @return the all field names
     * @throws Exception
     *             the exception
     */
	public static List getAllFieldNames(final Object object) throws Exception {
		final Map allFields = ClassUtil.getAllFieldByNames(object.getClass());
		return new ArrayList(allFields.keySet());
	}

	/**
     * Gets the all fields.
     *
     * @param object
     *            the object
     * @return the all fields
     * @throws Exception
     *             the exception
     */
	public static List getAllFields(final Object object) throws Exception {
		final Map allFields = ClassUtil.getAllFieldByNames(object.getClass());
		return new ArrayList(allFields.values());
	}

	/**
     * Gets the all field by names.
     *
     * @param theClass
     *            the the class
     * @return the all field by names
     */
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

	/**
     * Gets the class field by names.
     *
     * @param theClass
     *            the the class
     * @return the class field by names
     */
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