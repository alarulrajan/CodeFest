/*
 * Created by IntelliJ IDEA.
 * User: Jacq
* Date: Aug 28, 2004
 * Time: 11:47:00 PM
 */
package com.technoetic.xplanner.acceptance.web;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.xml.sax.SAXException;


/**
 * The Interface XPlannerWebTester.
 */
public interface XPlannerWebTester extends WebTester {
    
    /** The default password. */
    String DEFAULT_PASSWORD = "password";
    
    /** The project page. */
    String PROJECT_PAGE = "project.jsp";
    
    /** The iteration page. */
    String ITERATION_PAGE = "iteration.jsp";
    
    /** The story page. */
    String STORY_PAGE = "userstory.jsp";
    
    /** The task page. */
    String TASK_PAGE = "task.jsp";
    
    /** The feature page. */
    String FEATURE_PAGE = "feature.jsp";
    
    /** The main table id. */
    String MAIN_TABLE_ID = "objecttable";


    /** Adds the feature.
     *
     * @param name
     *            the name
     * @param description
     *            the description
     * @return the string
     */
    String addFeature(String name, String description);

    /** Adds the note.
     *
     * @param subject
     *            the subject
     * @param body
     *            the body
     * @param authorName
     *            the author name
     */
    void addNote(String subject, String body, String authorName);

    /** Adds the note.
     *
     * @param subject
     *            the subject
     * @param body
     *            the body
     * @param authorName
     *            the author name
     * @param filename
     *            the filename
     */
    void addNote(String subject, String body, String authorName, String filename);

   /** Adds the project.
     *
     * @param projectName
     *            the project name
     * @param description
     *            the description
     * @return the string
     */
   String addProject(String projectName, String description);

    /** Adds the task.
     *
     * @param name
     *            the name
     * @param acceptorName
     *            the acceptor name
     * @param description
     *            the description
     * @param estimatedHours
     *            the estimated hours
     * @return the string
     */
    String addTask(String name, String acceptorName, String description, String estimatedHours);
    
    /** Adds the task.
     *
     * @param name
     *            the name
     * @param acceptorName
     *            the acceptor name
     * @param description
     *            the description
     * @param estimatedHours
     *            the estimated hours
     * @param disposition
     *            the disposition
     * @return the string
     */
    String addTask(String name, String acceptorName, String description, String estimatedHours, String disposition);

    /** Sets the time entry.
     *
     * @param index
     *            the index
     * @param durationInHours
     *            the duration in hours
     * @param firstPersonInitials
     *            the first person initials
     */
    void setTimeEntry(int index, int durationInHours, String firstPersonInitials);
    
    /** Sets the time entry.
     *
     * @param index
     *            the index
     * @param startHourOffset
     *            the start hour offset
     * @param endHourOffset
     *            the end hour offset
     * @param firstPersonInitials
     *            the first person initials
     */
    void setTimeEntry(int index, int startHourOffset, int endHourOffset, String firstPersonInitials);
    
    /** Sets the time entry.
     *
     * @param index
     *            the index
     * @param startTime
     *            the start time
     * @param endTime
     *            the end time
     * @param firstPersonInitials
     *            the first person initials
     * @param secondPersonInitials
     *            the second person initials
     */
    void setTimeEntry(int index,
                      int startTime, int endTime, String firstPersonInitials, String secondPersonInitials);

    /** Adds the user story.
     *
     * @param storyName
     *            the story name
     * @param storyDescription
     *            the story description
     * @param estimatedHours
     *            the estimated hours
     * @param orderNo
     *            the order no
     * @return the string
     */
    String addUserStory(String storyName, String storyDescription, String estimatedHours, String orderNo);

    /** Assert link present with key.
     *
     * @param key
     *            the key
     */
    void assertLinkPresentWithKey(String key);

    /** Assert link not present with key.
     *
     * @param key
     *            the key
     */
    void assertLinkNotPresentWithKey(String key);

    /** Assert on feature page.
     */
    void assertOnFeaturePage();

    /** Assert on move continue story page.
     *
     * @param name
     *            the name
     */
    void assertOnMoveContinueStoryPage(String name);

    /** Assert on move continue task page.
     *
     * @param name
     *            the name
     */
    void assertOnMoveContinueTaskPage(String name);

    /** Assert on page.
     *
     * @param page
     *            the page
     */
    void assertOnPage(String page);

   /** Assert on project page.
     */
   void assertOnProjectPage();

    /** Assert on story page.
     */
    void assertOnStoryPage();

    /** Assert on story page.
     *
     * @param storyName
     *            the story name
     */
    void assertOnStoryPage(String storyName);

    /** Assert on task page.
     */
    void assertOnTaskPage();

    /** Assert on top page.
     */
    void assertOnTopPage();

    /** Assert option listed.
     *
     * @param selectName
     *            the select name
     * @param option
     *            the option
     */
    void assertOptionListed(String selectName, String option);

    /** Assert option not listed.
     *
     * @param selectName
     *            the select name
     * @param option
     *            the option
     */
    void assertOptionNotListed(String selectName, String option);

    /** Click continue link in row with text.
     *
     * @param name
     *            the name
     */
    void clickContinueLinkInRowWithText(String name);

    /** Click enter time link in row with text.
     *
     * @param name
     *            the name
     */
    void clickEnterTimeLinkInRowWithText(String name);

    /** Click delete link for row with text.
     *
     * @param text
     *            the text
     */
    void clickDeleteLinkForRowWithText(String text);

    /** Click edit link in row with text.
     *
     * @param text
     *            the text
     */
    void clickEditLinkInRowWithText(String text);

    /** Click edit time image.
     */
    void clickEditTimeImage();

    /** Click image link in note with subject.
     *
     * @param imageName
     *            the image name
     * @param subject
     *            the subject
     */
    void clickImageLinkInNoteWithSubject(String imageName, String subject);

    /** Click image link in table for row with text.
     *
     * @param imageName
     *            the image name
     * @param tableId
     *            the table id
     * @param text
     *            the text
     */
    void clickImageLinkInTableForRowWithText(String imageName, String tableId, String text);

    /** Click link with key.
     *
     * @param key
     *            the key
     */
    void clickLinkWithKey(String key);

    /** Date string for n days away.
     *
     * @param daysFromToday
     *            the days from today
     * @return the string
     */
    String dateStringForNDaysAway(int daysFromToday);

    /** Date time string for n hours away.
     *
     * @param hoursFromNow
     *            the hours from now
     * @return the string
     */
    String dateTimeStringForNHoursAway(int hoursFromNow);

    /** Delete note with subject.
     *
     * @param subject
     *            the subject
     */
    void deleteNoteWithSubject(String subject);

    /** Delete objects.
     *
     * @param clazz
     *            the clazz
     * @param attribute
     *            the attribute
     * @param value
     *            the value
     */
    void deleteObjects(Class clazz, String attribute, String value);

    /** Delete project.
     *
     * @param name
     *            the name
     * @throws Exception
     *             the exception
     */
    void deleteProject(String name) throws Exception;

    /** Edits the note with subject.
     *
     * @param subject
     *            the subject
     */
    void editNoteWithSubject(String subject);

    /** Gets the current page object id.
     *
     * @return the current page object id
     */
    String getCurrentPageObjectId();

    /** Gets the id from link with text.
     *
     * @param linkText
     *            the link text
     * @return the id from link with text
     */
    String getIdFromLinkWithText(String linkText);

    /** Gets the row numbers with text.
     *
     * @param tableId
     *            the table id
     * @param text
     *            the text
     * @return the row numbers with text
     * @throws SAXException
     *             the SAX exception
     */
    int[] getRowNumbersWithText(String tableId, String text) throws SAXException;

    /** Gets the session.
     *
     * @return the session
     * @throws HibernateException
     *             the hibernate exception
     */
    Session getSession() throws HibernateException;

    /** Gets the x planner login id.
     *
     * @return the x planner login id
     */
    String getXPlannerLoginId();

    /** Gets the x planner login password.
     *
     * @return the x planner login password
     */
    String getXPlannerLoginPassword();

    /** Goto page.
     *
     * @param operation
     *            the operation
     * @param objectType
     *            the object type
     * @param oid
     *            the oid
     */
    void gotoPage(String operation, String objectType, int oid) ;

   /** Goto projects page.
     */
   void gotoProjectsPage();

    /** Goto relative url.
     *
     * @param relativeUrl
     *            the relative url
     */
    void gotoRelativeUrl(String relativeUrl);

    /** Login.
     */
    void login();

    /** Login.
     *
     * @param user
     *            the user
     * @param password
     *            the password
     */
    void login(String user, String password);

    /** Logout.
     *
     * @throws Exception
     *             the exception
     */
    void logout() throws Exception;

   /** Change locale.
     *
     * @param language
     *            the language
     * @throws Exception
     *             the exception
     */
   void changeLocale(String language) throws Exception;

    /** Release session.
     *
     * @throws HibernateException
     *             the hibernate exception
     * @throws SQLException
     *             the SQL exception
     */
    void releaseSession() throws HibernateException, SQLException;

    /** Sets the form element with label.
     *
     * @param formElementLabel
     *            the form element label
     * @param value
     *            the value
     */
    void setFormElementWithLabel(String formElementLabel, String value);

    /** Verify notes link.
     */
    void verifyNotesLink();

    /** Assert cell text for row with text and column key equals.
     *
     * @param tableId
     *            the table id
     * @param rowName
     *            the row name
     * @param key
     *            the key
     * @param text
     *            the text
     */
    void assertCellTextForRowWithTextAndColumnKeyEquals(String tableId, String rowName, String key, String text);

    /** Assert cell text for row with text and column name equals.
     *
     * @param tableId
     *            the table id
     * @param rowName
     *            the row name
     * @param columnName
     *            the column name
     * @param text
     *            the text
     */
    void assertCellTextForRowWithTextAndColumnNameEquals(String tableId,
                                                         String rowName,
                                                         String columnName,
                                                         String text);


    /** Assert cell number for row with text and column name equals.
     *
     * @param tableId
     *            the table id
     * @param rowName
     *            the row name
     * @param columnName
     *            the column name
     * @param num
     *            the num
     */
    void assertCellNumberForRowWithTextAndColumnNameEquals(String tableId,
                                                           String rowName,
                                                           String columnName,
                                                           Number num);
    
    /** Assert cell number for row with text and column key equals.
     *
     * @param tableId
     *            the table id
     * @param rowName
     *            the row name
     * @param key
     *            the key
     * @param number
     *            the number
     */
    void assertCellNumberForRowWithTextAndColumnKeyEquals(String tableId, String rowName, String key, Number number);

    /** Assert cell text for row index and column key contains.
     *
     * @param tableId
     *            the table id
     * @param rowIndex
     *            the row index
     * @param columnName
     *            the column name
     * @param expectedCellText
     *            the expected cell text
     */
    void assertCellTextForRowIndexAndColumnKeyContains(String tableId, int rowIndex, String columnName,
                                                       String expectedCellText);

    /** Assert cell text for row index and column key equals.
     *
     * @param tableId
     *            the table id
     * @param rowIndex
     *            the row index
     * @param columnKey
     *            the column key
     * @param expectedCellText
     *            the expected cell text
     */
    void assertCellTextForRowIndexAndColumnKeyEquals(String tableId, int rowIndex, String columnKey,
                                                     String expectedCellText);

    /** Assert actual hours column present.
     */
    void assertActualHoursColumnPresent();

   /** Sets the up smtp server.
     *
     * @throws Exception
     *             the exception
     */
   //DEBT: Should be extracted in its own helper class
    void setUpSmtpServer() throws Exception;
    
    /** Tear down smtp server.
     */
    void tearDownSmtpServer();

    /** Tear down.
     *
     * @throws Exception
     *             the exception
     */
    void tearDown() throws Exception;

    /** Assert email notification message.
     *
     * @param subject
     *            the subject
     * @param recipients
     *            the recipients
     * @param from
     *            the from
     * @param bodyElements
     *            the body elements
     * @throws InterruptedException
     *             the interrupted exception
     */
    void assertEmailNotificationMessage(String subject, String[] recipients, String from, List bodyElements)
        throws InterruptedException;

    /** Assert no email message.
     *
     * @param subject
     *            the subject
     * @param recipients
     *            the recipients
     * @param from
     *            the from
     * @param bodyElements
     *            the body elements
     * @throws InterruptedException
     *             the interrupted exception
     */
    void assertNoEmailMessage(String subject,
                              String[] recipients,
                              String from,
                              List bodyElements)
        throws InterruptedException;

    /** Upload file.
     *
     * @param fieldName
     *            the field name
     * @param fileName
     *            the file name
     * @param fileContents
     *            the file contents
     */
    void uploadFile(String fieldName, String fileName, InputStream fileContents);

    /** Assign role on project.
     *
     * @param projectName
     *            the project name
     * @param roleName
     *            the role name
     * @throws SAXException
     *             the SAX exception
     */
    void assignRoleOnProject(String projectName, String roleName) throws SAXException;

   /** Complete current task.
     */
   void completeCurrentTask();

   /** Edits the property.
     *
     * @param propertyName
     *            the property name
     * @param propertyValue
     *            the property value
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
   void editProperty(String propertyName, String propertyValue) throws UnsupportedEncodingException;

   /** Move current day.
     *
     * @param days
     *            the days
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
   void moveCurrentDay(int days)
         throws UnsupportedEncodingException;

   /** Execute task.
     *
     * @param taskExecutorUrl
     *            the task executor url
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
   void executeTask(String taskExecutorUrl)
         throws UnsupportedEncodingException;

   /** Reset time.
     *
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
   void resetTime() throws UnsupportedEncodingException;
}