package com.technoetic.xplanner.forms;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.struts.action.ActionErrors;

/**
 * The Class TestTimeEditorForm.
 */
public class TestTimeEditorForm extends AbstractEditorFormTestCase {
   
   /** The time editor form. */
   private TimeEditorForm timeEditorForm;
   
   /** The Constant SECONDS. */
   public static final long SECONDS = 1000;
   
   /** The Constant MINUTES. */
   public static final long MINUTES = 60 * SECONDS;
   
   /** The Constant HOURS. */
   public static final long HOURS = 60 * MINUTES;
   
   /** The Constant DAYS. */
   public static final long DAYS = 24 * HOURS;

   /** The Constant START_TIME. */
   public static final long START_TIME = new GregorianCalendar(2002, 2, 2, 0, 0).getTimeInMillis();
   
   /** The Constant END_TIME. */
   public static final long END_TIME = START_TIME + 1 * DAYS;
   
   /** The Constant PERSON1. */
   public static final String PERSON1 = "100";
   
   /** The Constant PERSON2. */
   public static final String PERSON2 = "111";

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.forms.AbstractEditorFormTestCase#setUp()
    */
   protected void setUp() throws Exception {
      form = timeEditorForm = new TimeEditorForm();
      super.setUp();
      timeEditorForm.setRowcount(1);
      timeEditorForm.setEntryId(0, "1");
      timeEditorForm.setDeleted(0, null);
      timeEditorForm.setStartTime(0, formatDateTime(START_TIME));
      timeEditorForm.setEndTime(0, formatDateTime(END_TIME));
      timeEditorForm.setReportDate(0, formatDate(START_TIME));
      timeEditorForm.setPerson1Id(0, PERSON1);
      timeEditorForm.setPerson2Id(0, "200");
      timeEditorForm.setDescription(0, "the description");
   }

   /** Format date time.
     *
     * @param date
     *            the date
     * @return the string
     */
   public String formatDateTime(long date) {
      return DATE_TIME_FORMAT.format(new Date(date));
   }

   /** Format date.
     *
     * @param date
     *            the date
     * @return the string
     */
   public String formatDate(long date) {
      return DATE_FORMAT.format(new Date(date));
   }

   /** Test reset.
     */
   public void testReset() {
      timeEditorForm.setDuration(0, "5");

      timeEditorForm.reset(support.mapping, support.request);

      assertEquals(0, timeEditorForm.getRowcount());
      assertNull(timeEditorForm.getEntryId(0));
      assertNull(timeEditorForm.getDeleted(0));
      assertNull(timeEditorForm.getStartTime(0));
      assertNull(timeEditorForm.getEndTime(0));
      assertNull(timeEditorForm.getPerson1Id(0));
      assertNull(timeEditorForm.getPerson2Id(0));
      assertNull(timeEditorForm.getDuration(0));
   }

   /** Test description.
     */
   public void testDescription() {
      assertEquals("Wrong description", "the description", timeEditorForm.getDescription(0));
   }

   /** Test validate form ok.
     */
   public void testValidateFormOk() {
      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertNoErrors(errors);
   }

   /** Test parseable duration.
     */
   public void testParseableDuration() {
      timeEditorForm.setStartTime(0, null);
      timeEditorForm.setEndTime(0, null);
      timeEditorForm.setDuration(0, "2,1");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertNoErrors(errors);
   }

   /** Test validate bad start date.
     */
   public void testValidateBadStartDate() {
      timeEditorForm.setStartTime(0, "bogus");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.UNPARSABLE_TIME_ERROR_KEY, errors);
   }

   /** Test validate bad end date.
     */
   public void testValidateBadEndDate() {
      timeEditorForm.setEndTime(0, "bogus");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.UNPARSABLE_TIME_ERROR_KEY, errors);
   }

   /** Test validate missing end date.
     */
   public void testValidateMissingEndDate() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setEndTime(0, null);

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.MISSING_TIME_ERROR_KEY, errors);
   }

   /** Test validate negative interval.
     */
   public void testValidateNegativeInterval() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setEndTime(0, formatDateTime(START_TIME - 1 * DAYS));

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.NEGATIVE_INTERVAL_ERROR_KEY, errors);
   }

   /** Test validate overlapping interval.
     */
   public void testValidateOverlappingInterval() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setPerson1Id(1, PERSON1);
      timeEditorForm.setStartTime(1, formatDateTime(START_TIME + 1 * HOURS));
      timeEditorForm.setEndTime(1, formatDateTime(END_TIME - 3 * HOURS));
      timeEditorForm.setReportDate(1, formatDate(START_TIME));

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.OVERLAPPING_INTERVAL_ERROR_KEY, errors);
   }

   /** Test validate overlapping interval with different developers.
     */
   public void testValidateOverlappingIntervalWithDifferentDevelopers() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setPerson1Id(1, PERSON2);
      timeEditorForm.setStartTime(1, formatDateTime(START_TIME + 1 * HOURS));
      timeEditorForm.setReportDate(1, formatDate(START_TIME));

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertNoErrors(errors);
   }

   /** Test validate overlapping interval with duration.
     */
   public void testValidateOverlappingIntervalWithDuration() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setPerson1Id(1, PERSON1);
      timeEditorForm.setStartTime(1, formatDateTime(START_TIME - 1 * DAYS));
      timeEditorForm.setDuration(1, "48");
      timeEditorForm.setReportDate(1, formatDate(START_TIME));

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.OVERLAPPING_INTERVAL_ERROR_KEY, errors);
   }

   /** Test validate overlapping interval with two nonpaired entries.
     */
   public void testValidateOverlappingIntervalWithTwoNonpairedEntries() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setPerson2Id(0, "0");
      timeEditorForm.setPerson1Id(1, PERSON2);
      timeEditorForm.setStartTime(1, formatDateTime(START_TIME + 1 * DAYS));
      timeEditorForm.setReportDate(1, formatDate(START_TIME));

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertNoErrors(errors);
   }

   /** Assert no errors.
     *
     * @param errors
     *            the errors
     */
   private void assertNoErrors(ActionErrors errors) {assertEquals("wrong # of expected errors", 0, errors.size());}

   /** Test out of order time interval.
     */
   public void testOutOfOrderTimeInterval() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setPerson1Id(1, "1");
      timeEditorForm.setStartTime(1, formatDateTime(START_TIME + 1 * HOURS));
      timeEditorForm.setReportDate(1, formatDate(START_TIME));

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertNoErrors(errors);
   }

   /** Test validate missing person1.
     */
   public void testValidateMissingPerson1() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setPerson1Id(0, "");
      timeEditorForm.setPerson2Id(0, "");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.MISSING_PERSON_ERROR_KEY, errors);
   }

   /** Test validate missing person2.
     */
   public void testValidateMissingPerson2() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setStartTime(0, null);
      timeEditorForm.setEndTime(0, null);
      timeEditorForm.setDuration(0, "5");
      timeEditorForm.setPerson1Id(0, "");
      timeEditorForm.setPerson2Id(0, "");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.MISSING_PERSON_ERROR_KEY, errors);
   }

   /** Test validate same people.
     */
   public void testValidateSamePeople() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setPerson1Id(0, PERSON1);
      timeEditorForm.setPerson2Id(0, PERSON1);

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.SAME_PEOPLE_ERROR_KEY, errors);
   }

   /** Test validate only end time and duration.
     */
   public void testValidateOnlyEndTimeAndDuration() {
      timeEditorForm.setStartTime(0, null);
      timeEditorForm.setDuration(0, "3");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.MISSING_TIME_ERROR_KEY, errors);

   }

   /** Test validate interval and duration2.
     */
   public void testValidateIntervalAndDuration2() {
      timeEditorForm.setEndTime(0, null);
      timeEditorForm.setDuration(0, "3");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertNoErrors(errors);
      assertEquals(formatDateTime(START_TIME + 3 * HOURS), timeEditorForm.getEndTime(0));
   }

   /** Test validate interval and duration3.
     */
   public void testValidateIntervalAndDuration3() {
      timeEditorForm.setStartTime(0, null);
      timeEditorForm.setDuration(0, "3");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.MISSING_TIME_ERROR_KEY, errors);
   }

   /** Test validate interval and duration not last row.
     */
   public void testValidateIntervalAndDurationNotLastRow() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setDuration(0, "3");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertNoErrors(errors);
   }

   /** Test validate interval and duration at last row.
     */
   public void testValidateIntervalAndDurationAtLastRow() {
      timeEditorForm.setDuration(0, "3");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.BOTH_INTERVAL_AND_DURATION_ERROR_KEY, errors);
   }

   /** Test validate duration.
     */
   public void testValidateDuration() {
      timeEditorForm.setStartTime(0, null);
      timeEditorForm.setEndTime(0, null);
      timeEditorForm.setDuration(0, "3");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertNoErrors(errors);
   }

   /** Test validate missing report date.
     */
   public void testValidateMissingReportDate() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setReportDate(0, null);

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.MISSING_REPORT_DATE_ERROR_KEY, errors);
   }
}
