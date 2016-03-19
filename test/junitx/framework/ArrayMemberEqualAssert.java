package junitx.framework;

/**
 * The Class ArrayMemberEqualAssert.
 */
public class ArrayMemberEqualAssert extends ValueMemberEqualAssert {
    
    /* (non-Javadoc)
     * @see junitx.framework.ValueMemberEqualAssert#assertValueEquals(java.lang.String, java.lang.Object, java.lang.Object)
     */
    public boolean assertValueEquals(String propertyName, Object expectedValue, Object actualValue) {
        if (expectedValue != null && expectedValue.getClass().isArray()) {
            if (expectedValue.getClass().getComponentType() == byte.class) {
                ArrayAssert.assertEquals(propertyName, (byte[]) expectedValue, (byte[]) actualValue);
            } else {
                ArrayAssert.assertEquals(propertyName, (Object[]) expectedValue, (Object[]) actualValue);
            }
            return true;
        }
        return false;
    }
}