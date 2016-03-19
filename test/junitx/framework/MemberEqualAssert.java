package junitx.framework;

/**
 * The Interface MemberEqualAssert.
 */
public interface MemberEqualAssert {
    
    /** Assert equals.
     *
     * @param propertyName
     *            the property name
     * @param expectedObject
     *            the expected object
     * @param actualObject
     *            the actual object
     * @param accessStrategy
     *            the access strategy
     * @return true, if successful
     */
    boolean assertEquals(String propertyName, Object expectedObject, Object actualObject, MemberAccessStrategy accessStrategy);
}