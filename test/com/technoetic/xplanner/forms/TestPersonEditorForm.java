package com.technoetic.xplanner.forms;

import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.technoetic.xplanner.XPlannerTestSupport;

/**
 * The Class TestPersonEditorForm.
 */
public class TestPersonEditorForm extends TestCase {
    
    /** Instantiates a new test person editor form.
     *
     * @param name
     *            the name
     */
    public TestPersonEditorForm(String name) {
        super(name);
    }

    /** The support. */
    private XPlannerTestSupport support;
    
    /** The form. */
    private PersonEditorForm form;


    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        support = new XPlannerTestSupport();
        form = new PersonEditorForm();

        form.reset(support.mapping, support.request);
        form.setName("name");
        form.setInitials("nn");
        form.setUserIdentifier("uid");
        form.setEmail("test@email");
    }


    /** Test reset.
     */
    public void testReset() {
        form.reset(support.mapping, support.request);

        assertNull("variable not reset", form.getAction());
        assertNull("variable not reset", form.getName());
        assertNull("variable not reset", form.getEmail());
        assertNull("variable not reset", form.getPhone());
        assertNull("variable not reset", form.getInitials());
        assertEquals("variable not reset", 0, form.getPersonId());
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
        assertEquals("wrong key", "person.editor.missing_name", error.getKey());
    }

    /** Test validate missing email.
     */
    public void testValidateMissingEmail() {
        form.setEmail(null);
        form.setAction("Update");

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "person.editor.missing_email", error.getKey());
    }

    /** Test validate missing initials.
     */
    public void testValidateMissingInitials() {
        form.setInitials(null);
        form.setAction("Update");

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "person.editor.missing_initials", error.getKey());
    }

    /** Test validate missing user id.
     */
    public void testValidateMissingUserId() {
        form.setUserId(null);
        form.setAction("Update");

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "person.editor.missing_user_id", error.getKey());
    }

    /** Test validate password missmatch.
     */
    public void testValidatePasswordMissmatch() {
        form.setNewPassword("one");
        form.setNewPasswordConfirm("two");
        form.setAction("Update");
        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", PersonEditorForm.PASSWORD_MISMATCH_ERROR, error.getKey());
    }
}