package junitx.framework;

/**
 * The Class MultiLineStringMemberEqualAssert.
 */
public class MultiLineStringMemberEqualAssert extends ValueMemberEqualAssert {
    
    /* (non-Javadoc)
     * @see junitx.framework.ValueMemberEqualAssert#assertValueEquals(java.lang.String, java.lang.Object, java.lang.Object)
     */
    public boolean assertValueEquals(String propertyName, Object expectedValue, Object actualValue) {
        if (expectedValue instanceof String && actualValue instanceof String) {
            String expected = stripNewlines((String) expectedValue);
            String actual = stripNewlines((String) actualValue);
            Assert.assertEquals(propertyName, expected, actual);
            return true;
        }
        return false;
    }
    
    /** Strip newlines.
     *
     * @param value
     *            the value
     * @return the string
     */
    private String stripNewlines(String value) {
        return value.replaceAll("[\r\n]", "");
    }
}