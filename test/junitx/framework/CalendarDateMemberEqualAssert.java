
package junitx.framework;

import java.util.Calendar;
import java.util.Date;

/**
 * The Class CalendarDateMemberEqualAssert.
 */
public class CalendarDateMemberEqualAssert extends ValueMemberEqualAssert {
    
    /* (non-Javadoc)
     * @see junitx.framework.ValueMemberEqualAssert#assertValueEquals(java.lang.String, java.lang.Object, java.lang.Object)
     */
    public boolean assertValueEquals(String propertyName, Object expected, Object actual) {
        if (expected instanceof Date && actual instanceof Calendar) {
            Assert.assertEquals(propertyName, getDate(expected), getDateFromCalendar(actual));
            return true;
        } else if (expected instanceof Calendar && actual instanceof Date) {
            Assert.assertEquals(propertyName, getDateFromCalendar(expected), getDate(actual));
            return true;
        }
        return false;
    }

    /** Gets the date.
     *
     * @param expected
     *            the expected
     * @return the date
     */
    private Date getDate(Object expected) {
        return new Date(((Date)expected).getTime());
    }

    /** Gets the date from calendar.
     *
     * @param actual
     *            the actual
     * @return the date from calendar
     */
    private Date getDateFromCalendar(Object actual) {
        return new Date(((Calendar) actual).getTimeInMillis());
    }


}