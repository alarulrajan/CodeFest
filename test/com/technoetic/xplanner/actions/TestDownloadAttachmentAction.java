package com.technoetic.xplanner.actions;

import java.util.ArrayList;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Note;

import com.technoetic.mocks.hibernate.MockSession;

/**
 * The Class TestDownloadAttachmentAction.
 */
public class TestDownloadAttachmentAction extends TestCase {
    
    /** The action. */
    private DownloadAttachmentAction action;
    
    /** The test note. */
    private Note testNote;

    /** The mock session. */
    private MockSession mockSession;

    /** The file content. */
    private String fileContent;
    
    /** The file. */
    private byte[] file;
    
    /** The content type. */
    private String contentType;
    
    /** The file size. */
    private int fileSize;
    
    /** The filename. */
    private String filename;

    /** Instantiates a new test download attachment action.
     *
     * @param s
     *            the s
     */
    public TestDownloadAttachmentAction(String s) {
        super(s);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        action = new DownloadAttachmentAction();
        testNote = new Note();
        fileContent = "XXXXXX";
        file = fileContent.getBytes();
        contentType = "text/plain";
        fileSize = 6;
        filename = "testFile.txt";

        mockSession = new MockSession();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /** Test locate note.
     *
     * @throws Exception
     *             the exception
     */
    public void testLocateNote() throws Exception {
        int testId = 5;
        ArrayList notes = new ArrayList();
        notes.add(testNote);
        mockSession.findReturn = notes;
        String expectedQuery = "from object in class net.sf.xplanner.domain.Note"
                + " where id=" + testId;

        Note actualNote = action.locateNote(mockSession, testId);

        assertEquals("Session did not receive the correct query.", expectedQuery, mockSession.findQuery);
        assertEquals("Did not receive the correct note.", testNote, actualNote);
    }

    /**
     * The <code>testExecute</code> method is waiting to be implemented
     * when signoff is obtained for importing the org.mockobjects libaries
     *
     * @exception Exception if an error occurs
     */
    public void testExecute() throws Exception {
    }
}
