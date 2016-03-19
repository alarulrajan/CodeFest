/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 11, 2004
 * Time: 12:06:32 AM
 */
package com.technoetic.xplanner.importer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import junitx.framework.CallLog;

import com.technoetic.xplanner.importer.spreadsheet.Spreadsheet;
import com.technoetic.xplanner.importer.util.IOStreamFactory;

//ChangeSoon investigate having a default spring test configuration made of fake objects and have the tests override the beans they need to have control over

/**
 * The Class MockSpreadsheet.
 */
public class MockSpreadsheet extends Spreadsheet
{
   
   /** The stories return. */
   public List storiesReturn = Collections.EMPTY_LIST;
   
   /** The call log. */
   public CallLog callLog = new CallLog();

   /** Instantiates a new mock spreadsheet.
     */
   public MockSpreadsheet()
   {
      super(new IOStreamFactory(), new SpreadsheetStoryFactory());
   }

   /** Instantiates a new mock spreadsheet.
     *
     * @param factory
     *            the factory
     */
   public MockSpreadsheet(IOStreamFactory factory)
   {
      super(factory, new SpreadsheetStoryFactory());
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.importer.spreadsheet.Spreadsheet#open(java.lang.String)
    */
   public void open(String path)
   {
      callLog.addActualCallToCurrentMethod(path);
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.importer.spreadsheet.Spreadsheet#addStory(com.technoetic.xplanner.importer.SpreadsheetStory)
    */
   public void addStory(SpreadsheetStory spreadsheetStory)
   {
      callLog.addActualCallToCurrentMethod(spreadsheetStory);
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.importer.spreadsheet.Spreadsheet#setStories(java.util.List)
    */
   public void setStories(List stories)
   {
      callLog.addActualCallToCurrentMethod(stories);
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.importer.spreadsheet.Spreadsheet#getStories()
    */
   public List getStories()
   {
      return storiesReturn;
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.importer.spreadsheet.Spreadsheet#save()
    */
   public void save() throws IOException
   {
      callLog.addActualCallToCurrentMethod();
   }

   /** Verify.
     */
   public void verify()
   {
      callLog.verify();
   }

}