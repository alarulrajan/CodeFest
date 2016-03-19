package com.technoetic.xplanner.domain.virtual;

import java.math.BigDecimal;

import junit.framework.TestCase;

/**
 * The Class TestTimesheetEntry.
 */
public class TestTimesheetEntry extends TestCase {
    
    /** The timesheet entry. */
    private TimesheetEntry timesheetEntry = null;
    
    /** The Constant PROJECT_ID. */
    public static final int PROJECT_ID = 7;
    
    /** The Constant PROJECT_NAME. */
    public static final String PROJECT_NAME = "Test Project";
    
    /** The Constant ITERATION_ID. */
    public static final int ITERATION_ID = 18;
    
    /** The Constant ITERATION_NAME. */
    public static final String ITERATION_NAME = "Test Iteration";
    
    /** The Constant STORY_ID. */
    public static final int STORY_ID = 24;
    
    /** The Constant STORY_NAME. */
    public static final String STORY_NAME = "Test Story";
    
    /** The Constant PERSON_NAME. */
    public static final String PERSON_NAME = "Test Person";
    
    /** The Constant DURATION. */
    public static final BigDecimal DURATION = new BigDecimal(7.5);

    /** Instantiates a new test timesheet entry.
     *
     * @param name
     *            the name
     */
    public TestTimesheetEntry(String name) {
        super(name);
    }

    /** Gets the test timesheet entry.
     *
     * @return the test timesheet entry
     */
    public static TimesheetEntry getTestTimesheetEntry() {
        TimesheetEntry te = new TimesheetEntry();
        te.setPersonName(PERSON_NAME);
        te.setProjectId(PROJECT_ID);
        te.setProjectName(PROJECT_NAME);
        te.setIterationId(ITERATION_ID);
        te.setIterationName(ITERATION_NAME);
        te.setStoryId(STORY_ID);
        te.setStoryName(STORY_NAME);
        te.setTotalDuration(DURATION);
        return te;
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.timesheetEntry = getTestTimesheetEntry();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        timesheetEntry = null;
        super.tearDown();
    }

    /** Test properties.
     *
     * @throws Exception
     *             the exception
     */
    public void testProperties() throws Exception {
        assertEquals("Invalid Project ID", PROJECT_ID,
                this.timesheetEntry.getProjectId());

        assertEquals("Invalid Person Name", PERSON_NAME,
                this.timesheetEntry.getPersonName());

        assertEquals("Invalid Project Name", PROJECT_NAME,
                this.timesheetEntry.getProjectName());

        assertEquals("Invalid Iteration ID", ITERATION_ID,
                this.timesheetEntry.getIterationId());

        assertEquals("Invalid Iteration Name", ITERATION_NAME,
                this.timesheetEntry.getIterationName());

        assertEquals("Invalid Story ID", STORY_ID,
                this.timesheetEntry.getStoryId());

        assertEquals("Invalid Story Name", STORY_NAME,
                this.timesheetEntry.getStoryName());

        assertEquals("Invalid Duration",
                DURATION.setScale(1, BigDecimal.ROUND_HALF_EVEN),
                this.timesheetEntry.getTotalDuration());
    }
}
