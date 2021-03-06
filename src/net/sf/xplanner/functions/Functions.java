package net.sf.xplanner.functions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

/**
 * The Class Functions.
 */
public class Functions {

    /**
     * Filter.
     *
     * @param objects
     *            the objects
     * @param fieldName
     *            the field name
     * @param fieldValue
     *            the field value
     * @return the list
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     * @throws NoSuchMethodException
     *             the no such method exception
     */
    public static List<Object> filter(final List<Object> objects,
            final String fieldName, final Object fieldValue)
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        if (objects == null || objects.size() == 0
                || StringUtils.isBlank(fieldName) || fieldValue == null) {
            return objects;
        }
        int intValue = 0;
        final List<Object> result = new ArrayList<Object>(objects.size());

        if (fieldValue instanceof Long) {
            intValue = ((Long) fieldValue).intValue();
        }

        for (final Object object : objects) {
            final Object property = PropertyUtils
                    .getProperty(object, fieldName);
            if (property instanceof Enum) {
                if (fieldValue.equals(((Enum) property).name())) {
                    result.add(object);
                }
            } else if (fieldValue.equals(property)) {
                result.add(object);
            } else if (fieldValue instanceof Long && property.equals(intValue)) {
                result.add(object);
            }
        }
        return result;
    }
}
