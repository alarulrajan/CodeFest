/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Aug 29, 2004
 * Time: 12:00:26 AM
 */
package com.technoetic.xplanner.acceptance.web;

import java.io.PrintStream;
import java.util.Date;

import net.sourceforge.jwebunit.api.ITestingEngine;
import net.sourceforge.jwebunit.util.TestContext;

import com.meterware.httpunit.TableCell;
import com.meterware.httpunit.WebLink;

/**
 * The Interface WebTester.
 */
public interface WebTester {
   
   /** Assert button not present.
     *
     * @param buttonId
     *            the button id
     */
   void assertButtonNotPresent(String buttonId);
   
   /** Assert button present.
     *
     * @param buttonId
     *            the button id
     */
   void assertButtonPresent(String buttonId);
   
   /** Assert checkbox not selected.
     *
     * @param checkBoxName
     *            the check box name
     */
   void assertCheckboxNotSelected(String checkBoxName);
   
   /** Assert checkbox selected.
     *
     * @param checkBoxName
     *            the check box name
     */
   void assertCheckboxSelected(String checkBoxName);
   
   /** Assert cookie present.
     *
     * @param cookieName
     *            the cookie name
     */
   void assertCookiePresent(String cookieName);
   
   /** Assert cookie value equals.
     *
     * @param cookieName
     *            the cookie name
     * @param expectedValue
     *            the expected value
     */
   void assertCookieValueEquals(String cookieName, String expectedValue);
   
   /** Assert element not present.
     *
     * @param anID
     *            the an id
     */
   void assertElementNotPresent(String anID);
   
   /** Assert element present.
     *
     * @param anID
     *            the an id
     */
   void assertElementPresent(String anID);
   
   /** Assert form element empty.
     *
     * @param formElementName
     *            the form element name
     */
   void assertFormElementEmpty(String formElementName);
   
   /** Assert form element equals.
     *
     * @param formElementName
     *            the form element name
     * @param expectedValue
     *            the expected value
     */
   void assertFormElementEquals(String formElementName, String expectedValue);
   
   /** Assert form element not present.
     *
     * @param formElementName
     *            the form element name
     */
   void assertFormElementNotPresent(String formElementName);
   
   /** Assert form element not present with label.
     *
     * @param formElementLabel
     *            the form element label
     */
   void assertFormElementNotPresentWithLabel(String formElementLabel);
   
   /** Assert form element present.
     *
     * @param formElementName
     *            the form element name
     */
   void assertFormElementPresent(String formElementName);
   
   /** Assert form element present with label.
     *
     * @param formElementLabel
     *            the form element label
     */
   void assertFormElementPresentWithLabel(String formElementLabel);
   
   /** Assert form not present.
     */
   void assertFormNotPresent();
   
   /** Assert form not present.
     *
     * @param nameOrID
     *            the name or id
     */
   void assertFormNotPresent(String nameOrID);
   
   /** Assert form present.
     */
   void assertFormPresent();
   
   /** Assert form present.
     *
     * @param nameOrID
     *            the name or id
     */
   void assertFormPresent(String nameOrID);
   
   /** Assert frame present.
     *
     * @param frameName
     *            the frame name
     */
   void assertFramePresent(String frameName);
   
   /** Assert key in table.
     *
     * @param tableSummaryOrId
     *            the table summary or id
     * @param key
     *            the key
     */
   void assertKeyInTable(String tableSummaryOrId, String key);
   
   /** Assert key not in table.
     *
     * @param tableSummaryOrId
     *            the table summary or id
     * @param key
     *            the key
     */
   void assertKeyNotInTable(String tableSummaryOrId, String key);
   
   /** Assert key not present.
     *
     * @param key
     *            the key
     */
   void assertKeyNotPresent(String key);
   
   /** Assert key present.
     *
     * @param key
     *            the key
     */
   void assertKeyPresent(String key);
   
   /** Assert key present.
     *
     * @param key
     *            the key
     * @param arg
     *            the arg
     */
   void assertKeyPresent(String key, Object arg);
   
   /** Assert keys in table.
     *
     * @param tableSummaryOrId
     *            the table summary or id
     * @param keys
     *            the keys
     */
   void assertKeysInTable(String tableSummaryOrId, String[] keys);
   
   /** Assert link not present.
     *
     * @param linkId
     *            the link id
     */
   void assertLinkNotPresent(String linkId);
   
   /** Assert link not present with image.
     *
     * @param imageFileName
     *            the image file name
     */
   void assertLinkNotPresentWithImage(String imageFileName);
   
   /** Assert link not present with text.
     *
     * @param linkText
     *            the link text
     */
   void assertLinkNotPresentWithText(String linkText);
   
   /** Assert link not present with text.
     *
     * @param linkText
     *            the link text
     * @param index
     *            the index
     */
   void assertLinkNotPresentWithText(String linkText, int index);
   
   /** Assert link present.
     *
     * @param linkId
     *            the link id
     */
   void assertLinkPresent(String linkId);
   
   /** Assert link present with image.
     *
     * @param imageFileName
     *            the image file name
     */
   void assertLinkPresentWithImage(String imageFileName);
   
   /** Assert link present with text.
     *
     * @param linkText
     *            the link text
     */
   void assertLinkPresentWithText(String linkText);
   
   /** Assert link present with text.
     *
     * @param linkText
     *            the link text
     * @param index
     *            the index
     */
   void assertLinkPresentWithText(String linkText, int index);
   
   /** Assert option equals.
     *
     * @param selectName
     *            the select name
     * @param option
     *            the option
     */
   void assertOptionEquals(String selectName, String option);
   
   /** Assert option values equal.
     *
     * @param selectName
     *            the select name
     * @param expectedValues
     *            the expected values
     */
   void assertOptionValuesEqual(String selectName, String[] expectedValues);
   
   /** Assert option values not equal.
     *
     * @param selectName
     *            the select name
     * @param optionValues
     *            the option values
     */
   void assertOptionValuesNotEqual(String selectName, String[] optionValues);
   
   /** Assert options equal.
     *
     * @param selectName
     *            the select name
     * @param expectedOptions
     *            the expected options
     */
   void assertOptionsEqual(String selectName, String[] expectedOptions);
   
   /** Assert options not equal.
     *
     * @param selectName
     *            the select name
     * @param expectedOptions
     *            the expected options
     */
   void assertOptionsNotEqual(String selectName, String[] expectedOptions);
   
   /** Assert radio option not present.
     *
     * @param name
     *            the name
     * @param radioOption
     *            the radio option
     */
   void assertRadioOptionNotPresent(String name, String radioOption);
   
   /** Assert radio option not selected.
     *
     * @param name
     *            the name
     * @param radioOption
     *            the radio option
     */
   void assertRadioOptionNotSelected(String name, String radioOption);
   
   /** Assert radio option present.
     *
     * @param name
     *            the name
     * @param radioOption
     *            the radio option
     */
   void assertRadioOptionPresent(String name, String radioOption);
   
   /** Assert radio option selected.
     *
     * @param name
     *            the name
     * @param radioOption
     *            the radio option
     */
   void assertRadioOptionSelected(String name, String radioOption);
   
   /** Assert submit button not present.
     *
     * @param buttonName
     *            the button name
     */
   void assertSubmitButtonNotPresent(String buttonName);
   
   /** Assert submit button present.
     *
     * @param buttonName
     *            the button name
     */
   void assertSubmitButtonPresent(String buttonName);
   
   /** Assert submit button value.
     *
     * @param buttonName
     *            the button name
     * @param expectedValue
     *            the expected value
     */
   void assertSubmitButtonValue(String buttonName, String expectedValue);

/**
 * Assert table equals.
 *
 * @param tableSummaryOrId
 *            the table summary or id
 * @param expectedCellValues
 *            the expected cell values
 */
//   void assertTableEquals(String tableSummaryOrId, ExpectedTable expectedTable);
   void assertTableEquals(String tableSummaryOrId, String[][] expectedCellValues);
   
   /** Assert table not present.
     *
     * @param tableSummaryOrId
     *            the table summary or id
     */
   void assertTableNotPresent(String tableSummaryOrId);
   
   /** Assert table present.
     *
     * @param tableSummaryOrId
     *            the table summary or id
     */
   void assertTablePresent(String tableSummaryOrId);

/**
 * Assert table rows equal.
 *
 * @param tableSummaryOrId
 *            the table summary or id
 * @param startRow
 *            the start row
 * @param expectedCellValues
 *            the expected cell values
 */
//   void assertTableRowsEqual(String tableSummaryOrId, int startRow, ExpectedTable expectedTable);
   void assertTableRowsEqual(String tableSummaryOrId, int startRow, String[][] expectedCellValues);
   
   /** Assert text in element.
     *
     * @param elementID
     *            the element id
     * @param text
     *            the text
     */
   void assertTextInElement(String elementID, String text);
   
   /** Assert text in table.
     *
     * @param tableSummaryOrId
     *            the table summary or id
     * @param text
     *            the text
     */
   void assertTextInTable(String tableSummaryOrId, String text);
   
   /** Assert text in table.
     *
     * @param tableSummaryOrId
     *            the table summary or id
     * @param text
     *            the text
     */
   void assertTextInTable(String tableSummaryOrId, String[] text);
   
   /** Assert text not in element.
     *
     * @param elementID
     *            the element id
     * @param text
     *            the text
     */
   void assertTextNotInElement(String elementID, String text);
   
   /** Assert text not in table.
     *
     * @param tableSummaryOrId
     *            the table summary or id
     * @param text
     *            the text
     */
   void assertTextNotInTable(String tableSummaryOrId, String text);
   
   /** Assert text not in table.
     *
     * @param tableSummaryOrId
     *            the table summary or id
     * @param text
     *            the text
     */
   void assertTextNotInTable(String tableSummaryOrId, String[] text);
   
   /** Assert text not present.
     *
     * @param text
     *            the text
     */
   void assertTextNotPresent(String text);
   
   /** Assert text present.
     *
     * @param text
     *            the text
     */
   void assertTextPresent(String text);
   
   /** Assert text present with key.
     *
     * @param key
     *            the key
     * @deprecated use {@link #assertKeyPresent(String)} instead
     */
   void assertTextPresentWithKey(String key);
   
   /** Assert text not present with key.
     *
     * @param key
     *            the key
     */
   void assertTextNotPresentWithKey(String key);
   
   /** Assert title equals.
     *
     * @param title
     *            the title
     */
   void assertTitleEquals(String title);
   
   /** Assert title equals key.
     *
     * @param titleKey
     *            the title key
     */
   void assertTitleEqualsKey(String titleKey);
   
   /** Assert window present.
     *
     * @param windowName
     *            the window name
     */
   void assertWindowPresent(String windowName);
   
   /** Begin at.
     *
     * @param relativeURL
     *            the relative url
     */
   void beginAt(String relativeURL);
   
   /** Check checkbox.
     *
     * @param checkBoxName
     *            the check box name
     */
   void checkCheckbox(String checkBoxName);
   
   /** Check checkbox.
     *
     * @param checkBoxName
     *            the check box name
     * @param value
     *            the value
     */
   void checkCheckbox(String checkBoxName, String value);
   
   /** Click button.
     *
     * @param buttonId
     *            the button id
     */
   void clickButton(String buttonId);
   
   /** Click link.
     *
     * @param linkId
     *            the link id
     */
   void clickLink(String linkId);
   
   /** Click link.
     *
     * @param link
     *            the link
     */
   void clickLink(WebLink link);
   
   /** Click link with image.
     *
     * @param imageFileName
     *            the image file name
     */
   void clickLinkWithImage(String imageFileName);
   
   /** Click link with text.
     *
     * @param linkText
     *            the link text
     */
   void clickLinkWithText(String linkText);
   
   /** Click link with text.
     *
     * @param linkText
     *            the link text
     * @param index
     *            the index
     */
   void clickLinkWithText(String linkText, int index);
   
   /** Click link with text after text.
     *
     * @param linkText
     *            the link text
     * @param labelText
     *            the label text
     */
   void clickLinkWithTextAfterText(String linkText, String labelText);
   
   /** Dump cookies.
     */
   void dumpCookies();
   
   /** Dump cookies.
     *
     * @param stream
     *            the stream
     */
   void dumpCookies(PrintStream stream);
   
   /** Dump response.
     */
   void dumpResponse();
   
   /** Dump response.
     *
     * @param stream
     *            the stream
     */
   void dumpResponse(PrintStream stream);
   
   /** Dump table.
     *
     * @param tableNameOrId
     *            the table name or id
     * @param stream
     *            the stream
     */
   void dumpTable(String tableNameOrId, PrintStream stream);
   
   /** Dump table.
     *
     * @param tableNameOrId
     *            the table name or id
     * @param table
     *            the table
     */
   void dumpTable(String tableNameOrId, String[][] table);
   
   /** Dump table.
     *
     * @param tableNameOrId
     *            the table name or id
     * @param table
     *            the table
     * @param stream
     *            the stream
     */
   void dumpTable(String tableNameOrId, String[][] table, PrintStream stream);
   
   /** Find link with text.
     *
     * @param text
     *            the text
     * @return the web link
     */
   WebLink findLinkWithText(String text);
   
   /** Gets the dialog.
     *
     * @return the dialog
     */
   ITestingEngine getDialog();
   
   /** Gets the message.
     *
     * @param key
     *            the key
     * @return the message
     */
   String getMessage(String key);
   
   /** Gets the test context.
     *
     * @return the test context
     */
   TestContext getTestContext();
   
   /** Goto frame.
     *
     * @param frameName
     *            the frame name
     */
   void gotoFrame(String frameName);
   
   /** Goto page.
     *
     * @param url
     *            the url
     */
   void gotoPage(String url);
   
   /** Goto root window.
     */
   void gotoRootWindow();
   
   /** Goto window.
     *
     * @param windowName
     *            the window name
     */
   void gotoWindow(String windowName);
   
   /** Reset.
     */
   void reset();
   
   /** Select option.
     *
     * @param selectName
     *            the select name
     * @param option
     *            the option
     */
   void selectOption(String selectName, String option);
   
   /** Sets the form element.
     *
     * @param formElementName
     *            the form element name
     * @param value
     *            the value
     */
   void setFormElement(String formElementName, String value);
   
   /** Sets the working form.
     *
     * @param nameOrId
     *            the new working form
     */
   void setWorkingForm(String nameOrId);
   
   /** Submit.
     */
   void submit();
   
   /** Submit.
     *
     * @param buttonName
     *            the button name
     */
   void submit(String buttonName);
   
   /** Uncheck checkbox.
     *
     * @param checkBoxName
     *            the check box name
     */
   void uncheckCheckbox(String checkBoxName);
   
   /** Uncheck checkbox.
     *
     * @param checkBoxName
     *            the check box name
     * @param value
     *            the value
     */
   void uncheckCheckbox(String checkBoxName, String value);

   /** Gets the cell.
     *
     * @param tableId
     *            the table id
     * @param columnName
     *            the column name
     * @param rowIndex
     *            the row index
     * @return the cell
     */
   TableCell getCell(String tableId, String columnName, int rowIndex);

   /** Assert link present with image.
     *
     * @param imageName
     *            the image name
     * @param pathElement
     *            the path element
     */
   void assertLinkPresentWithImage(String imageName, String pathElement);

   /** Click link with image.
     *
     * @param imageName
     *            the image name
     * @param pathElement
     *            the path element
     */
   void clickLinkWithImage(String imageName, String pathElement);

   /** Assert cell text for row with text and column key occurs.
     *
     * @param tableId
     *            the table id
     * @param rowName
     *            the row name
     * @param columnName
     *            the column name
     * @param text
     *            the text
     * @param nbr
     *            the nbr
     */
   void assertCellTextForRowWithTextAndColumnKeyOccurs(String tableId,
                                                       String rowName,
                                                       String columnName,
                                                       String text, int nbr);

   /** Assert cell number for row with key.
     *
     * @param tableId
     *            the table id
     * @param rowName
     *            the row name
     * @param key
     *            the key
     * @param val
     *            the val
     * @param nbr
     *            the nbr
     */
   void assertCellNumberForRowWithKey(String tableId,
                                             String rowName,
                                             String key,
                                             Number val, int nbr);

   /** Assert cell number for row with text and column key occurs.
     *
     * @param tableId
     *            the table id
     * @param rowName
     *            the row name
     * @param columnName
     *            the column name
     * @param val
     *            the val
     * @param nbr
     *            the nbr
     */
   void assertCellNumberForRowWithTextAndColumnKeyOccurs(String tableId,
                                                         String rowName,
                                                         String columnName,
                                                         Number val, int nbr);

   /** Checks if is key present.
     *
     * @param key
     *            the key
     * @return true, if is key present
     */
   boolean isKeyPresent(String key);
   
   /** Checks if is text present.
     *
     * @param text
     *            the text
     * @return true, if is text present
     */
   boolean isTextPresent(String text);
   
   /** Checks if is link present with text.
     *
     * @param linkText
     *            the link text
     * @return true, if is link present with text
     */
   boolean isLinkPresentWithText(String linkText);
   
   /** Checks if is link present with key.
     *
     * @param key
     *            the key
     * @return true, if is link present with key
     */
   boolean isLinkPresentWithKey(String key);

   /** Gets the current date.
     *
     * @return the current date
     */
   Date getCurrentDate();

   /** Gets the current day index.
     *
     * @return the current day index
     */
   int getCurrentDayIndex();
}