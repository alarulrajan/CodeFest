package com.technoetic.xplanner.tags.displaytag;

/**
 * User: Mateusz Prokopowicz
 * Date: Jan 20, 2005
 * Time: 11:31:35 AM
 */

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;

import junit.framework.TestCase;

import org.displaytag.properties.MediaTypeEnum;
import org.displaytag.tags.TableTag;
import org.easymock.MockControl;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.tags.WritableTag;

/**
 * The Class TestActionButtonsColumnTag.
 */
public class TestActionButtonsColumnTag extends TestCase {
    
    /** The action buttons column tag. */
    ActionButtonsColumnTag actionButtonsColumnTag;
    
    /** The table tag. */
    TableTag tableTag;
    
    /** The support. */
    protected XPlannerTestSupport support;
    
    /** The writable parent table control. */
    private MockControl writableParentTableControl;
    
    /** The writable parent table tag. */
    private WritableTag writableParentTableTag;
    
    /** The mock action buttons tag. */
    private MockActionButtonsTag mockActionButtonsTag;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();
        support = new XPlannerTestSupport();
        mockActionButtonsTag = new MockActionButtonsTag();
        support.pageContext.setAttribute(TableTag.PAGE_ATTRIBUTE_MEDIA, MediaTypeEnum.HTML);
        actionButtonsColumnTag = new ActionButtonsColumnTag();
        actionButtonsColumnTag.setActionButtonsTag(mockActionButtonsTag);
        tableTag = new TableTag() {
            public boolean isEmpty() {
                return false;
            }
            protected boolean isIncludedRow()
            {
                return true;
            }
        };

        actionButtonsColumnTag.setParent(tableTag);
        actionButtonsColumnTag.setPageContext(support.pageContext);
        actionButtonsColumnTag.setName("project");
        actionButtonsColumnTag.setId("action");
        writableParentTableControl = MockControl.createControl(WritableTag.class);
        writableParentTableTag = (WritableTag) writableParentTableControl.getMock();
        actionButtonsColumnTag.setParent(writableParentTableTag);
    }

    /** Test get action for project_ user has permission.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetActionForProject_UserHasPermission() throws Exception {
        writableParentTableControl.reset();
        writableParentTableControl.expectAndReturn(writableParentTableTag.isWritable(), true);
        writableParentTableControl.expectAndReturn(writableParentTableTag.getParent(), tableTag);
        writableParentTableControl.replay();
        mockActionButtonsTag.doStartTag_returnVal= BodyTag.EVAL_BODY_BUFFERED;
        int status = actionButtonsColumnTag.doStartTag();
        writableParentTableControl.verify();
        assertEquals("wrong doStartTag status", BodyTag.EVAL_BODY_BUFFERED, status);
        assertTrue("actionButtonsTag.doStartTag() has not been called", mockActionButtonsTag.wasDoStartTagCalled);
    }

    /** Test get action for project_ user has no permission.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetActionForProject_UserHasNoPermission() throws Exception {
        writableParentTableControl.reset();
        writableParentTableControl.expectAndReturn(writableParentTableTag.isWritable(), false);
        writableParentTableControl.replay();
        int status = actionButtonsColumnTag.doStartTag();
        writableParentTableControl.verify();
        assertEquals("wrong doStartTag status", BodyTag.SKIP_BODY, status);
        assertFalse("actionButtonsTag.doStartTag() has been called", mockActionButtonsTag.wasDoStartTagCalled);
    }

   /** The Class MockActionButtonsTag.
     */
   private class MockActionButtonsTag extends ActionButtonsTag
   {
      
      /** The do start tag_return val. */
      int doStartTag_returnVal;
      
      /** The was do start tag called. */
      boolean wasDoStartTagCalled = false;
      
      /* (non-Javadoc)
       * @see com.technoetic.xplanner.tags.displaytag.ActionButtonsTag#doStartTag()
       */
      public int doStartTag() throws JspException
      {
         wasDoStartTagCalled = true;
         return doStartTag_returnVal;
      }
   }
}