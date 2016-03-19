package com.technoetic.xplanner.importer.spreadsheet;

/**
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 28, 2003
 * Time: 5:23:51 PM
 * To change this template use Options | File Templates.
 */

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import junitx.framework.ListAssert;

import com.technoetic.xplanner.importer.BaseTestCase;
import com.technoetic.xplanner.importer.SpreadsheetStory;
import com.technoetic.xplanner.importer.SpreadsheetStoryFactory;
import com.technoetic.xplanner.util.MainBeanFactory;

/**
 * The Class TestSpreadsheetStoryReader.
 */
public class TestSpreadsheetStoryReader extends BaseTestCase
{
   
   /** The reader. */
   SpreadsheetStoryReader reader;
   
   /** The header configuration. */
   private SpreadsheetHeaderConfiguration headerConfiguration = new SpreadsheetHeaderConfiguration("Feature/Story Title",
                                                                                                   "Iteration End Date",
                                                                                                   "Priority  (1 thru n)",
                                                                                                   "Status",
                                                                                                   "Work Unit Estimate");

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.importer.BaseTestCase#setUp()
    */
   protected void setUp() throws Exception
   {
      super.setUp();
      MainBeanFactory.initBeanProperties(this);
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.importer.BaseTestCase#tearDown()
    */
   protected void tearDown() throws Exception
   {
      super.tearDown();
   }

   /** Test spreadsheet reader.
     *
     * @throws Exception
     *             the exception
     */
   public void testSpreadsheetReader() throws Exception
   {
      InputStream stream = TestSpreadsheetStoryReader.class.getResourceAsStream("/data/Cookbook.xls");
      assertNotNull(stream);
      List stories = reader.readStories(headerConfiguration,stream);

      SpreadsheetStoryFactory factorySpreadsheet = new SpreadsheetStoryFactory();
      ArrayList expectedStories = new ArrayList();
      Calendar cal = getBaseDate();
      for (int i = 1; i <= 7; i++)
      {
         expectedStories.add(factorySpreadsheet.newInstance(cal.getTime(),
                                                            "Story " + i,
                                                            (i % 2 == 1) ? "C" : "",
                                                            i + 10, 4));
         if (i % 2 == 0) cal.add(Calendar.DAY_OF_MONTH, 14);
      }
      ListAssert.assertEquals(expectedStories, stories);
      validateExpectedDates(expectedStories, stories);
   }

   /** Validate expected dates.
     *
     * @param expectedStories
     *            the expected stories
     * @param stories
     *            the stories
     */
   private void validateExpectedDates(ArrayList expectedStories, List stories)
   {
      for (Iterator iterator = expectedStories.iterator(), actualStoriesIt = stories.iterator(); iterator.hasNext();)
      {
         SpreadsheetStory expectedStory = (SpreadsheetStory) iterator.next();
         SpreadsheetStory actualStory = (SpreadsheetStory) actualStoriesIt.next();
         assertEquals("Expected story date not equal on story " + expectedStory.getTitle(),
                      expectedStory.getEndDate(),
                      actualStory.getEndDate());
      }

   }

   /** Gets the base date.
     *
     * @return the base date
     */
   private Calendar getBaseDate()
   {
      Calendar cal = Calendar.getInstance();
      cal.set(2004, 6, 13, 0, 0, 0);
      cal.set(Calendar.MILLISECOND, 0);
      return cal;
   }

   /** Gets the reader.
     *
     * @return the reader
     */
   public SpreadsheetStoryReader getReader()
   {
      return reader;
   }

   /** Sets the reader.
     *
     * @param reader
     *            the new reader
     */
   public void setReader(SpreadsheetStoryReader reader)
   {
      this.reader = reader;
   }

}