package com.technoetic.xplanner.importer.spreadsheet;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: May 19, 2005
 * Time: 8:38:12 PM
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.importer.SpreadsheetStory;
import com.technoetic.xplanner.importer.SpreadsheetStoryFactory;
import com.technoetic.xplanner.importer.SpreadsheetStoryImporter;
import com.technoetic.xplanner.testing.DateHelper;

/**
 * The Class TestSpreadsheetStoryImporterRealSpreadsheet.
 */
public class TestSpreadsheetStoryImporterRealSpreadsheet extends TestCase
{
   
   /** The Constant TEST_DATE. */
   public static final Date TEST_DATE = DateHelper.createDate(2005, 4, 20);

   /** The header configuration. */
   private final SpreadsheetHeaderConfiguration headerConfiguration = new SpreadsheetHeaderConfiguration(
      SpreadsheetStoryWriter.TITLE_HEADER,
      SpreadsheetStoryWriter.END_DATE_HEADER,
      SpreadsheetStoryWriter.PRIORITY_HEADER,
      SpreadsheetStoryWriter.STATUS_HEADER,
      SpreadsheetStoryWriter.ESTIMATE_HEADER);

   /** Test header only spreadsheet.
     *
     * @throws Exception
     *             the exception
     */
   public void testHeaderOnlySpreadsheet() throws Exception
   {
      ArrayList stories = new ArrayList();
      int expectedNumberOfStories = stories.size();
      File tempFile = createTestSpreadsheet(stories);
      assertStoriesImported(expectedNumberOfStories, tempFile, null);
   }

   /** Test spreadsheet with one story.
     *
     * @throws Exception
     *             the exception
     */
   public void testSpreadsheetWithOneStory() throws Exception
   {
      SpreadsheetStoryFactory spreadsheetStoryFactory = new SpreadsheetStoryFactory();
      ArrayList stories = createStories(spreadsheetStoryFactory, 1);
      int expectedNumberOfStories = stories.size();
      File tempFile = createTestSpreadsheet(stories);
      assertStoriesImported(expectedNumberOfStories, tempFile, spreadsheetStoryFactory);
   }

   /** Test spreadsheet with multiple stories.
     *
     * @throws Exception
     *             the exception
     */
   public void testSpreadsheetWithMultipleStories() throws Exception
   {
      SpreadsheetStoryFactory spreadsheetStoryFactory = new SpreadsheetStoryFactory();
      ArrayList stories = createStories(spreadsheetStoryFactory, 13);
      int expectedNumberOfStories = stories.size();
      File tempFile = createTestSpreadsheet(stories);
      List list = assertStoriesImported(expectedNumberOfStories, tempFile, spreadsheetStoryFactory);
      assertStoryAttributes(stories, list);
   }

   /** Assert story attributes.
     *
     * @param stories
     *            the stories
     * @param list
     *            the list
     */
   private void assertStoryAttributes(ArrayList stories, List list)
   {
      for (Iterator iterator = stories.iterator(), it = list.iterator(); iterator.hasNext();)
      {
         SpreadsheetStory expectedStory = (SpreadsheetStory) iterator.next();
         UserStory story = (UserStory) it.next();
         assertEquals(expectedStory.getTitle(), story.getName());
         assertTrue("Estimates are not equal: " + expectedStory.getEstimate() + ", " + story.getEstimatedHours(),
                    expectedStory.getEstimate() == story.getEstimatedHours());
         assertEquals(expectedStory.getPriority(), story.getPriority());
         assertFalse(0 == story.getIteration().getId());
      }
   }

   /** Creates the stories.
     *
     * @param spreadsheetStoryFactory
     *            the spreadsheet story factory
     * @param numStories
     *            the num stories
     * @return the array list
     */
   private ArrayList createStories(SpreadsheetStoryFactory spreadsheetStoryFactory, int numStories)
   {
      ArrayList stories = new ArrayList();

      for (int i = 1; i <= numStories; ++i)
      {
         SpreadsheetStory spreadsheetStory = spreadsheetStoryFactory.newInstance(TEST_DATE,
                                                                                 "Some story",
                                                                                 "",
                                                                                 2, 4);
         stories.add(spreadsheetStory);
      }
      return stories;
   }

   /** Assert stories imported.
     *
     * @param expectedNumberOfStories
     *            the expected number of stories
     * @param tempFile
     *            the temp file
     * @param spreadsheetStoryFactory
     *            the spreadsheet story factory
     * @return the list
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
   private List assertStoriesImported(int expectedNumberOfStories,
                                      File tempFile,
                                      SpreadsheetStoryFactory spreadsheetStoryFactory)
      throws IOException
   {
      SpreadsheetStoryImporter storyImporter = new SpreadsheetStoryImporter(spreadsheetStoryFactory);
      InputStream in = new FileInputStream(tempFile);
      Iteration iteration = new Iteration();
      iteration.setStartDate(DateHelper.createDate(2005, 4, 8));
      iteration.setEndDate(TEST_DATE);
      iteration.setId(45);
      List list = storyImporter.importStories(iteration, headerConfiguration, in, false);

      assertEquals(expectedNumberOfStories, list.size());
      return list;
   }

   /** Creates the test spreadsheet.
     *
     * @param stories
     *            the stories
     * @return the file
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
   private File createTestSpreadsheet(ArrayList stories)
      throws IOException
   {
      File tempFile = File.createTempFile("test ", "with " + stories.size() + " stories.xls");
//      tempFile.deleteOnExit();
      OutputStream out = new FileOutputStream(tempFile);
      SpreadsheetStoryWriter writer = new SpreadsheetStoryWriter(out);
      writer.writeStories(stories);
      return tempFile;
   }
}
