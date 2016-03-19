package com.technoetic.xplanner.forms;

import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.upload.DiskFile;
import org.apache.struts.upload.FormFile;

import com.technoetic.xplanner.XPlannerTestSupport;

/**
 * The Class TestNoteEditorForm.
 */
public class TestNoteEditorForm extends TestCase {
    
    /** Instantiates a new test note editor form.
     *
     * @param name
     *            the name
     */
    public TestNoteEditorForm(String name) {
        super(name);
    }

    /** The support. */
    private XPlannerTestSupport support;
    
    /** The form. */
    private NoteEditorForm form;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        support = new XPlannerTestSupport();
        form = new NoteEditorForm();
        form.setSubject("subject");
        form.setBody("body");
        FormFile testFile = new DiskFile("data/TestAttachment.txt");
        form.setFormFile(testFile);
        form.setAuthorId(100);
    }

    /** Test reset.
     */
    public void testReset() {
        form.reset(support.mapping, support.request);

        assertNull("variable not reset", form.getAction());
        assertNull("variable not reset", form.getSubject());
        assertNull("variable not reset", form.getBody());
        assertNull("variable not reset", form.getFormFile());
        assertNull("variable not reset", form.getAttachedToType());
        assertEquals("variable not reset", 0, form.getAttachedToId());
    }

    /** Test validate form ok.
     */
    public void testValidateFormOk() {
        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 0, errors.size());
    }

    /** Test validate missing subject not submitted.
     */
    public void testValidateMissingSubjectNotSubmitted() {
        form.setSubject(null);

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 0, errors.size());
    }

    /** Test validate missing subject.
     */
    public void testValidateMissingSubject() {
        form.setSubject(null);
        form.setAction("Update");

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "note.editor.missing_subject", error.getKey());
    }

    /** Test validate missing body.
     */
    public void testValidateMissingBody() {
        form.setBody(null);
        form.setAction("Update");

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "note.editor.missing_body", error.getKey());
    }

    /** Test validate missing author.
     */
    public void testValidateMissingAuthor() {
        form.setAuthorId(0);
        form.setAction("Update");

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "note.editor.missing_author", error.getKey());
    }

    /** Test validate missing file on update ok.
     */
    public void testValidateMissingFileOnUpdateOk() {
        form.setFormFile(null);
        form.setAction(com.technoetic.xplanner.actions.EditObjectAction.UPDATE_ACTION);

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 0, errors.size());
    }

    /** Test validate missing file on create ok.
     */
    public void testValidateMissingFileOnCreateOk() {
        form.setFormFile(null);
        form.setAction(com.technoetic.xplanner.actions.EditObjectAction.CREATE_ACTION);

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 0, errors.size());
    }
}
