package com.technoetic.xplanner.domain.virtual;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

/**
 * The Class TestTimesheet.
 */
public class TestTimesheet extends TestCase {
    
    /** The timesheet. */
    private Timesheet timesheet = null;
    
    /** The Constant START. */
    private static final Date START = getWeekStartDate();
    
    /** The Constant END. */
    private static final Date END = getWeekEndDate();

    /** Instantiates a new test timesheet.
     *
     * @param name
     *            the name
     */
    public TestTimesheet(String name) {
        super(name);
    }

    /** Gets the test timesheet.
     *
     * @return the test timesheet
     */
    public static Timesheet getTestTimesheet() {
        Timesheet ts = new Timesheet(START, END);
        ts.addEntry(TestTimesheetEntry.getTestTimesheetEntry());
        ts.addDailyEntry(TestDailyTimesheetEntry.getTestDailyTimesheetEntry());
        return ts;
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.timesheet = getTestTimesheet();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        timesheet = null;
        super.tearDown();
    }

    /** Test properties.
     *
     * @throws Exception
     *             the exception
     */
    public void testProperties() throws Exception {
        assertEquals("Invalid Person Name", TestTimesheetEntry.PERSON_NAME,
                this.timesheet.getPersonName());
        assertEquals("Invalid Total Duration", TestTimesheetEntry.DURATION,
                this.timesheet.getTotal());
        assertEquals("Invalid Number of Entries", 1,
                timesheet.getEntries().size());
        int numDays = elapsedDays(START, END);
        assertEquals("Invalid Number of Daily Entries", numDays,
                timesheet.getDailyEntries().size());
        assertEquals("Invalid Project Data", 1,
                timesheet.getProjectData().values().size());
        assertEquals("Invalid Iteration Data", 1,
                timesheet.getIterationData().values().size());
        assertEquals("Invalid Story Data", 1,
                timesheet.getIterationData().values().size());

    }

//    private int elapsedDays(Date start, Date end) {
//        return (int)((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24)) + 1;
//    }

    /**
 * This was originally broken (and still may be). The elapsed days can't be
 * calculated based on elapsed ms since issues like daylight saving time will
 * mess up the calculations.
 *
 * @param start
 *            the start
 * @param end
 *            the end
 * @return the int
 */
    private int elapsedDays(Date start, Date end) {
        int elapsed = 0;
        GregorianCalendar g1 = new GregorianCalendar();
        g1.setTime(start);
        GregorianCalendar g2 = new GregorianCalendar();
        g2.setTime(end);

        GregorianCalendar gc1, gc2;

        if (g2.after(g1)) {
            gc2 = (GregorianCalendar)g2.clone();
            gc1 = (GregorianCalendar)g1.clone();
        } else {
            gc2 = (GregorianCalendar)g1.clone();
            gc1 = (GregorianCalendar)g2.clone();
        }

        gc1.clear(Calendar.MILLISECOND);
        gc1.clear(Calendar.SECOND);
        gc1.clear(Calendar.MINUTE);
        gc1.clear(Calendar.HOUR_OF_DAY);

        gc2.clear(Calendar.MILLISECOND);
        gc2.clear(Calendar.SECOND);
        gc2.clear(Calendar.MINUTE);
        gc2.clear(Calendar.HOUR_OF_DAY);

        while (gc1.before(gc2)) {
            gc1.add(Calendar.DATE, 1);
            elapsed++;
        }
        return elapsed+1; // always include today
    }

    /** Gets the week end date.
     *
     * @return the week end date
     */
    private static Date getWeekEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        int weekday = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, 7 - weekday);
        return cal.getTime();
    }

    /** Gets the week start date.
     *
     * @return the week start date
     */
    private static Date getWeekStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        int weekday = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, -weekday + 1);
        return cal.getTime();
    }

}
