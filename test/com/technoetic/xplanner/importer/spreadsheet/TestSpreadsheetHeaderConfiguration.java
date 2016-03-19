package com.technoetic.xplanner.importer.spreadsheet;

/**
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 28, 2003
 * Time: 5:23:51 PM
 * To change this template use Options | File Templates.
 */

import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.technoetic.xplanner.importer.BaseTestCase;
import com.technoetic.xplanner.importer.MissingColumnHeaderSpreadsheetImporterException;

/**
 * The Class TestSpreadsheetHeaderConfiguration.
 */
public class TestSpreadsheetHeaderConfiguration extends BaseTestCase
{
   
   /** The header config. */
   private SpreadsheetHeaderConfiguration headerConfig;
   
   /** The worksheet. */
   private HSSFSheet worksheet;

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.importer.BaseTestCase#setUp()
    */
   protected void setUp() throws Exception
   {
      super.setUp();
      InputStream stream = TestSpreadsheetHeaderReader.class.getResourceAsStream("/data/Cookbook.xls");
      assertNotNull(stream);
      headerConfig = new SpreadsheetHeaderConfiguration("Feature/Story Title",
                                                        "Iteration End Date",
                                                        "Priority  (1 thru n)",
                                                        "Status",
                                                        "Work Unit Estimate");
      POIFSFileSystem fs = new POIFSFileSystem(stream);
      HSSFWorkbook wb = new HSSFWorkbook(fs);
      worksheet = wb.getSheet("Features");
      headerConfig.setWorksheet(worksheet);
   }

   /** Test get story title column index.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetStoryTitleColumnIndex() throws Exception
   {
      long index = headerConfig.getStoryTitleColumnIndex();
      assertEquals(4, index);
   }

   /** Test get story estimate column index.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetStoryEstimateColumnIndex() throws Exception
   {
      long index = headerConfig.getStoryEstimateColumnIndex();
      assertEquals(5, index);
   }

   /** Test get story end date column index.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetStoryEndDateColumnIndex() throws Exception
   {
      long index = headerConfig.getStoryEndDateColumnIndex();
      assertEquals(7, index);
   }

   /** Test get story priority column index.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetStoryPriorityColumnIndex() throws Exception
   {
      long index = headerConfig.getStoryPriorityColumnIndex();
      assertEquals(0, index);
   }

   /** Test get story title column index failed_ no such column.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetStoryTitleColumnIndexFailed_NoSuchColumn() throws Exception
   {
      SpreadsheetHeaderConfiguration headerConfiguration = new SpreadsheetHeaderConfiguration();
      headerConfiguration.setWorksheet(worksheet);
      headerConfiguration.setTitleHeader("Wrong name");
      try
      {
         headerConfiguration.getStoryTitleColumnIndex();
         fail("MissingColumnHeaderSpreadsheetImporterException has not been thrown");
      }
      catch (MissingColumnHeaderSpreadsheetImporterException e)
      {
      }
   }

}