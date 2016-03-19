package com.technoetic.xplanner.forms;

import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.technoetic.xplanner.XPlannerTestSupport;

/**
 * The Class TestTaskEditorForm.
 */
public class TestTaskEditorForm extends TestCase {
    
    /** Instantiates a new test task editor form.
     *
     * @param name
     *            the name
     */
    public TestTaskEditorForm(String name) {
        super(name);
    }

    /** The support. */
    private XPlannerTestSupport support;
    
    /** The form. */
    private TaskEditorForm form;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
       super.setUp();
       support = new XPlannerTestSupport();
       support.resources.setMessage("format.date", "yyyy-MM-dd");
       form = new TaskEditorForm();
       form.setName("name");
    }

    /** Test reset.
     */
    public void testReset() {
        form.reset(support.mapping, support.request);

        assertNull("variable not reset", form.getAction());
        assertNull("variable not reset", form.getName());
        assertNull("variable not reset", form.getDescription());
        assertNull("variable not reset", form.getType());
        assertNull("variable not reset", form.getDispositionName());
        assertEquals("variable not reset", 0, form.getUserStoryId());
        assertEquals("variable not reset", 0, form.getAcceptorId());
        assertEquals("variable not reset", 0.0, form.getEstimatedHours(), 0);
        assertEquals("variable not reset", 0.0, form.getActualHours(), 0);
        assertTrue("variable not reset", !form.isCompleted());
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
        assertEquals("wrong key", "task.editor.missing_name", error.getKey());
    }


    /** Test validate negative estimate.
     */
    public void testValidateNegativeEstimate() {
        form.setEstimatedHours(-10.0);
        form.setAction("Update");

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "task.editor.negative_estimated_hours", error.getKey());
    }

    /** Test default created date.
     */
    public void testDefaultCreatedDate() {
        assertNotNull(form.getCreatedDate());
    }
}