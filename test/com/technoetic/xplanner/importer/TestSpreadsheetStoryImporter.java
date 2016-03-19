package com.technoetic.xplanner.importer;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Mar 31, 2005
 * Time: 10:29:36 PM
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.importer.spreadsheet.SpreadsheetHeaderConfiguration;
import com.technoetic.xplanner.importer.spreadsheet.SpreadsheetStoryWriter;
import com.technoetic.xplanner.testing.DateHelper;

/**
 * The Class TestSpreadsheetStoryImporter.
 */
public class TestSpreadsheetStoryImporter extends TestCase
{
   
   /** The iteration. */
   Iteration iteration;
   
   /** The Constant TEST_YEAR. */
   public static final int TEST_YEAR = 2005;
   
   /** The Constant TEST_MONTH. */
   public static final int TEST_MONTH = 4;
   
   /** The Constant TEST_DAY_OF_MONTH. */
   public static final int TEST_DAY_OF_MONTH = 20;
   
   /** The Constant TEST_DATE. */
   public static final Date TEST_DATE = DateHelper.createDate(TEST_YEAR, TEST_MONTH, TEST_DAY_OF_MONTH);
   
   /** The header configuration. */
   private final SpreadsheetHeaderConfiguration headerConfiguration = new SpreadsheetHeaderConfiguration(
      SpreadsheetStoryWriter.TITLE_HEADER,
      SpreadsheetStoryWriter.END_DATE_HEADER,
      SpreadsheetStoryWriter.PRIORITY_HEADER,
      SpreadsheetStoryWriter.STATUS_HEADER,
      SpreadsheetStoryWriter.ESTIMATE_HEADER);

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   @Override
protected void setUp() throws Exception
   {
      super.setUp();
      iteration = new Iteration();
      iteration.setStartDate(DateHelper.getDateDaysFromDate(TEST_DATE, -1));
      iteration.setEndDate(TEST_DATE);
   }

   /** Test import stories_ single story.
     *
     * @throws Exception
     *             the exception
     */
   public void testImportStories_SingleStory() throws Exception
   {
      assertImportStoriesImportRightNumberOfStories("only story", 1);
   }

   /** Test import stories_ multiple stories.
     *
     * @throws Exception
     *             the exception
     */
   public void testImportStories_MultipleStories() throws Exception
   {
      assertImportStoriesImportRightNumberOfStories("generic story", 13);
   }

   /** Test import stories_ bad input stream.
     *
     * @throws Exception
     *             the exception
     */
   public void testImportStories_BadInputStream() throws Exception
   {
      SpreadsheetStoryImporter spreadsheetStoryImporter = new SpreadsheetStoryImporter(null);
      try
      {
         File tempFile = File.createTempFile("test", "");
         tempFile.createNewFile();
         tempFile.deleteOnExit();
         spreadsheetStoryImporter.importStories(iteration, headerConfiguration, new FileInputStream(tempFile), false);
         fail("Should get exception due to non-.xsl file");
      }
      catch (SpreadsheetImporterException e)
      {
      }
   }

   /** Assert import stories import right number of stories.
     *
     * @param title
     *            the title
     * @param expectedNumberOfStories
     *            the expected number of stories
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
   private void assertImportStoriesImportRightNumberOfStories(String title, int expectedNumberOfStories)
      throws IOException
   {
      final List storiesToReturn = createTestSpreadsheetStories(title, expectedNumberOfStories);

      SpreadsheetStoryImporter storyImporter = createFakeImporter(storiesToReturn);

      assertStoriesCreated(expectedNumberOfStories, storyImporter, storiesToReturn, title);
   }

   /** Creates the fake importer.
     *
     * @param storiesToReturn
     *            the stories to return
     * @return the spreadsheet story importer
     */
   private SpreadsheetStoryImporter createFakeImporter(final List storiesToReturn)
   {
      return new SpreadsheetStoryImporter(null)
      {
         @Override
        protected List readStoriesFromSpreadsheet(SpreadsheetHeaderConfiguration headerConfiguration,
                                                   InputStream inputStream)
         {
            return storiesToReturn;
         }
      };
   }

   /** Assert stories created.
     *
     * @param expectedNumberOfStories
     *            the expected number of stories
     * @param storyImporter
     *            the story importer
     * @param storiesToReturn
     *            the stories to return
     * @param title
     *            the title
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
   private void assertStoriesCreated(int expectedNumberOfStories,
                                     SpreadsheetStoryImporter storyImporter,
                                     List storiesToReturn, String title)
      throws IOException
   {
      List returnedStories = storyImporter.importStories(iteration, headerConfiguration, null, false);
      assertEquals(storiesToReturn.size(), returnedStories.size());
      Collection userStories = iteration.getUserStories();
      assertEquals(expectedNumberOfStories, userStories.size());

      UserStory userStory = (UserStory) userStories.iterator().next();
      assertEquals(title, userStory.getName());
   }

   /** Creates the test spreadsheet stories.
     *
     * @param title
     *            the title
     * @param numStories
     *            the num stories
     * @return the list
     */
   private List createTestSpreadsheetStories(String title, int numStories)
   {
      final List storiesToReturn = new LinkedList();
      for (int i = 0; i < numStories; ++i)
      {
         SpreadsheetStory spreadsheetStory = new SpreadsheetStory(TEST_DATE, title, "", 0, TEST_MONTH);
         storiesToReturn.add(spreadsheetStory);
      }
      return storiesToReturn;
   }


}