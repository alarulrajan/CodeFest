
package junitx.framework;

import java.util.Calendar;

/**
 * To be used when equality of a Calendar depends only on its time and not its
 * zone and other attributes of Calendar that doesn't affect its time.
 */
public class TimeOnlyCalendarMemberEqualAssert extends ValueMemberEqualAssert {
    
    /* (non-Javadoc)
     * @see junitx.framework.ValueMemberEqualAssert#assertValueEquals(java.lang.String, java.lang.Object, java.lang.Object)
     */
    public boolean assertValueEquals(String propertyName, Object expected, Object actual) {
        if (expected instanceof Calendar && actual instanceof Calendar) {
            Assert.assertEquals(propertyName, getMillis(expected), getMillis(actual));
            return true;
        }
        return false;
    }

    /** Gets the millis.
     *
     * @param expected
     *            the expected
     * @return the millis
     */
    private long getMillis(Object expected) {
        return ((Calendar)expected).getTimeInMillis();
    }
}

