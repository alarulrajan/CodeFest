package com.technoetic.xplanner.domain;

import java.util.Date;

import junit.framework.TestCase;
import net.sf.xplanner.domain.TimeEntry;

/**
 * The Class TestTimeEntry.
 */
public class TestTimeEntry extends TestCase {
    
    /** The time entry. */
    private TimeEntry timeEntry;

    /** Instantiates a new test time entry.
     *
     * @param s
     *            the s
     */
    public TestTimeEntry(String s) {
        super(s);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        timeEntry = new TimeEntry();
    }

    /** Test edited time entry.
     */
    public void testEditedTimeEntry() {
        timeEntry.setStartTime(new Date(0));
        timeEntry.setEndTime(new Date(3600000));
        timeEntry.setDuration(0.5);

        double duration = timeEntry.getDuration();

        assertEquals("wrong duration", 1, duration, 0);
    }
}
