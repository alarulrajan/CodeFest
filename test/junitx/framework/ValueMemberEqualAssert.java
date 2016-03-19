
package junitx.framework;


/**
 * The Class ValueMemberEqualAssert.
 */
public abstract class ValueMemberEqualAssert implements MemberEqualAssert {
    
    /* (non-Javadoc)
     * @see junitx.framework.MemberEqualAssert#assertEquals(java.lang.String, java.lang.Object, java.lang.Object, junitx.framework.MemberAccessStrategy)
     */
    public boolean assertEquals(String memberName,
                                Object expectedObject,
                                Object actualObject,
                                MemberAccessStrategy accessStrategy) {
        Object expectedValue = accessStrategy.getValidMemberValue(expectedObject, memberName);
        Object actualValue = accessStrategy.getValidMemberValue(actualObject, memberName);
        if (expectedValue == null || actualValue == null) {
            Assert.assertEquals(memberName, expectedValue, actualValue);
            return true;
        }
        return assertValueEquals(memberName, expectedValue, actualValue);
    }

    /** Assert value equals.
     *
     * @param memberName
     *            the member name
     * @param expectedValue
     *            the expected value
     * @param actualValue
     *            the actual value
     * @return true, if successful
     */
    public abstract boolean assertValueEquals(String memberName, Object expectedValue, Object actualValue);

}