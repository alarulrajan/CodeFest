package com.technoetic.xplanner.forms;

import junit.framework.TestCase;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.actions.EditObjectAction;

/**
 * The Class TestImportPeopleForm.
 */
public class TestImportPeopleForm extends TestCase {
    
    /** Instantiates a new test import people form.
     *
     * @param name
     *            the name
     */
    public TestImportPeopleForm(String name) {
        super(name);
    }

    /** The support. */
    private XPlannerTestSupport support;
    
    /** The form. */
    private ImportPeopleForm form;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        support = new XPlannerTestSupport();
        form = new ImportPeopleForm();
        form.setServlet(support.actionServlet);
    }

    /** Test reset.
     */
    public void testReset() {
        form.reset(support.mapping, support.request);
        assertNull(form.getFormFile());
        assertNull(form.getAction());
        assertEquals(0, form.getResults().size());
    }

    /** Test validate form ok.
     */
    public void testValidateFormOk() {
        ActionErrors errors = form.validate(support.mapping, support.request);
        assertEquals("wrong # of expected errors", 0, errors.size());
    }

    /** Test validate form fail.
     */
    public void testValidateFormFail() {
        form.setAction(EditObjectAction.UPDATE_ACTION);
        ActionErrors errors = form.validate(support.mapping, support.request);
        assertEquals("wrong # of expected errors", 1, errors.size());
        assertEquals("wrong error message",
                     ImportForm.NO_IMPORT_FILE_KEY,
                     ((ActionError) errors.get().next()).getKey());
    }
}