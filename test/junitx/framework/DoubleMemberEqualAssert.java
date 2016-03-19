package junitx.framework;

/**
 * The Class DoubleMemberEqualAssert.
 */
public class DoubleMemberEqualAssert extends ValueMemberEqualAssert {
    
    /** The delta. */
    private double delta;

    /** Instantiates a new double member equal assert.
     */
    public DoubleMemberEqualAssert() { this(0.009); }
    
    /** Instantiates a new double member equal assert.
     *
     * @param delta
     *            the delta
     */
    public DoubleMemberEqualAssert(double delta) { this.delta = delta; }

    /* (non-Javadoc)
     * @see junitx.framework.ValueMemberEqualAssert#assertValueEquals(java.lang.String, java.lang.Object, java.lang.Object)
     */
    public boolean assertValueEquals(String propertyName, Object expectedValue, Object actualValue) {
        if (expectedValue instanceof Double) {
            Assert.assertEquals(propertyName,
                                ((Double) expectedValue).doubleValue(),
                                ((Double) actualValue).doubleValue(),
                                delta);
            return true;
        }
        return false;
    }
}