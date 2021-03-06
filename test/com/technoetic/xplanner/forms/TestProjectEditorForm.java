package com.technoetic.xplanner.forms;

import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.technoetic.xplanner.XPlannerTestSupport;

/**
 * The Class TestProjectEditorForm.
 */
public class TestProjectEditorForm extends TestCase {
    
    /** Instantiates a new test project editor form.
     *
     * @param name
     *            the name
     */
    public TestProjectEditorForm(String name) {
        super(name);
    }

    /** The support. */
    private XPlannerTestSupport support;
    
    /** The form. */
    private ProjectEditorForm form;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        support = new XPlannerTestSupport();
        form = new ProjectEditorForm();
        form.setName("name");
    }

    /** Test reset.
     */
    public void testReset() {
        form.reset(support.mapping, support.request);

        assertNull("variable not reset", form.getAction());
        assertNull("variable not reset", form.getName());
        assertNull("variable not reset", form.getDescription());
    }

    /** Test validate form ok.
     */
    public void testValidateFormOk() {
        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 0, errors.size());
    }

    /** Test validate missing name not submitted.
     */
    public void testValidateMissingNameNotSubmitted() {
        form.setName(null);

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 0, errors.size());
    }

    /** Test validate missing name.
     */
    public void testValidateMissingName() {
        form.setName(null);
        form.setAction("Update");

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "project.editor.missing_name", error.getKey());
    }
}