package junit.extensions;

import java.lang.reflect.Field;

/**
 * The Class FieldAccessor.
 */
public final class FieldAccessor {
    
    /** Gets the.
     *
     * @param object
     *            the object
     * @param fieldName
     *            the field name
     * @return the object
     */
    public static Object get(Object object, String fieldName) {
        if (object == null)
            throw new RuntimeException("Object passed to FieldAccessor.get is null");

        Class objectClass = object.getClass();
        Exception exception = null;
        while (objectClass != null) {
            try {
                Field field = objectClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(object);
            } catch (Exception ex) {
                exception = ex;
                objectClass = objectClass.getSuperclass();
            }
        }
        exception.printStackTrace();
        throw new RuntimeException("Failed field access: get");
    }

    /** Gets the static.
     *
     * @param classObject
     *            the class object
     * @param fieldName
     *            the field name
     * @return the static
     */
    public static Object getStatic(Class classObject, String fieldName) {
        Exception exception = null;
        while (classObject != null) {
            try {
                Field field = classObject.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(classObject);
            } catch (Exception ex) {
                exception = ex;
                classObject = classObject.getSuperclass();
            }
        }
        exception.printStackTrace();
        throw new RuntimeException("Failed static field access: getStatic");
    }

    /** Sets the.
     *
     * @param object
     *            the object
     * @param fieldName
     *            the field name
     * @param value
     *            the value
     */
    public static void set(Object object, String fieldName, Object value) {
        if (object == null)
            throw new RuntimeException("Object passed to FieldAccessor.set is null");

        Class objectClass = object.getClass();
        Exception exception = null;
        while (objectClass != null) {
            try {
                Field field = objectClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(object, value);
                return;
            } catch (Exception ex) {
                exception = ex;
                objectClass = objectClass.getSuperclass();
            }
        }
        throw new RuntimeException("Failed field access: set", exception);
    }

    /** Sets the static.
     *
     * @param objectClass
     *            the object class
     * @param fieldName
     *            the field name
     * @param value
     *            the value
     */
    public static void setStatic(Class objectClass, String fieldName, Object value) {
        if (objectClass == null)
            throw new RuntimeException("Object passed to FieldAccessor.setStatic is null");

        Exception exception = null;
        while (objectClass != null) {
            try {
                Field field = objectClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(objectClass, value);
                return;
            } catch (Exception ex) {
                exception = ex;
                objectClass = objectClass.getSuperclass();
            }
        }
        exception.printStackTrace();
        throw new RuntimeException("Failed field access: setStatic");
    }
}
