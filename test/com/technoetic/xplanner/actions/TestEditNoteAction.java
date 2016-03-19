package com.technoetic.xplanner.actions;

import static org.easymock.EasyMock.*;

import java.io.ByteArrayInputStream;
import java.util.Date;

import net.sf.xplanner.domain.Directory;
import net.sf.xplanner.domain.File;
import net.sf.xplanner.domain.Note;

import com.technoetic.mocks.sql.MockBlob;
import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.file.FileSystem;
import com.technoetic.xplanner.forms.NoteEditorForm;
import com.technoetic.xplanner.util.InputStreamFormFile;

/**
 * The Class TestEditNoteAction.
 */
public class TestEditNoteAction extends AbstractUnitTestCase {
    
    /** The action. */
    private EditNoteAction action;
    
    /** The test note. */
    private Note testNote;
    
    /** The note form. */
    private NoteEditorForm noteForm;

    /** The file content. */
    private String fileContent;
    
    /** The blob. */
    private MockBlob blob;
    
    /** The test form file. */
    private InputStreamFormFile testFormFile;
    
    /** The support. */
    private XPlannerTestSupport support;
    
    /** The mock file system. */
    private FileSystem mockFileSystem;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.AbstractUnitTestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        support = new XPlannerTestSupport();
        testNote = new Note();
        noteForm = new NoteEditorForm();
        fileContent = "XXXXXX";
        blob = new MockBlob();
        blob.getBinaryStreamReturn = new ByteArrayInputStream(fileContent.getBytes());
        testFormFile = new InputStreamFormFile(new ByteArrayInputStream(fileContent.getBytes()));
        testFormFile.setContentType("text/plain");
        testFormFile.setFileSize(6);
        testFormFile.setFileName("testFile.txt");
        testNote.setSubmissionTime(new Date());
        mockFileSystem = createLocalMock(FileSystem.class);
        action = new EditNoteAction();
        action.setFileSystem(mockFileSystem);
    }

    /** Test populate.
     *
     * @throws Exception
     *             the exception
     */
    public void testPopulate() throws Exception {
        Note actualNote = new Note();
        actualNote.setSubmissionTime(testNote.getSubmissionTime());
        noteForm.setFormFile(testFormFile);
        Directory mockDirectory = createLocalMock(Directory.class);
        expect(mockFileSystem.getDirectory("/attachments/project/0")).andReturn(mockDirectory);
        
        File mockFile = new File();
        mockFile.setName("FOO");
        testNote.setFile(mockFile);
        
        expect(mockFileSystem.createFile(mockSession,same(mockDirectory), eq("testFile.txt"), eq("text/plain"),
                eq(6l), isA(ByteArrayInputStream.class))).andReturn(mockFile);
        
        replay();

        action.populateObject(support.request, actualNote, noteForm);

        verify();
        assertEquals("Note not equal after populate", testNote, actualNote);
    }
}
