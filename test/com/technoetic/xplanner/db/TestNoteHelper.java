package com.technoetic.xplanner.db;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.Hibernate;
import org.hibernate.type.NullableType;

import com.technoetic.xplanner.AbstractUnitTestCase;

/**
 * User: Mateusz Prokopowicz Date: Dec 16, 2004 Time: 4:15:39 PM.
 */
public class TestNoteHelper extends AbstractUnitTestCase {
    
    /** The Constant PROJECT_ID. */
    public static final int PROJECT_ID = 1;
    
    /** The Constant ITERATION_ID. */
    public static final int ITERATION_ID = 22;
    
    /** The Constant STORY_ID. */
    public static final int STORY_ID = 333;
    
    /** The Constant NOTE1_ID. */
    public static final int NOTE1_ID = 4444;
    
    /** The Constant NOTE2_ID. */
    public static final int NOTE2_ID = 4444;

    /** The mock project. */
    Project mockProject = new Project();
    
    /** The mock iteration. */
    Iteration mockIteration = new Iteration();
    
    /** The mock story. */
    UserStory mockStory = new UserStory();
    
    /** The note1. */
    Note note1 = new Note();
    
    /** The note2. */
    Note note2 = new Note();

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.AbstractUnitTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setUpThreadSession(false);
        mockProject.setId(PROJECT_ID);
        mockIteration.setId(ITERATION_ID);
        mockStory.setId(STORY_ID);
        note1.setId(NOTE1_ID);
        note1.setAttachedToId(ITERATION_ID);
        note1.setFile(null);
        note2.setId(NOTE2_ID);
        note2.setAttachedToId(ITERATION_ID);
        note2.setFile(null);
    }

    /**
     * Test delete notes for.
     *
     * @throws Exception
     *             the exception
     */
    public void testDeleteNotesFor() throws Exception {
        Integer[] ids = new Integer[4];
        Arrays.fill(ids, new Integer(PROJECT_ID));
        NullableType[] types = new NullableType[4];
        Arrays.fill(types, Hibernate.INTEGER);
        List<Integer[]> notesAttachedToList = new ArrayList<Integer[]>();
        notesAttachedToList.add(new Integer[] { new Integer(PROJECT_ID),
                new Integer(ITERATION_ID), new Integer(STORY_ID), null });
        expect(hibernateTemplate.iterate(eq(NoteHelper.SELECT_ATTACHED_TO_ID), aryEq(ids), aryEq(types))).andReturn(notesAttachedToList.iterator());

        expect(
                hibernateTemplate.find(NoteHelper.SELECT_NOTE_ATTACHED_TO,
                        new Integer(PROJECT_ID), Hibernate.INTEGER)).andReturn(
                new ArrayList<Note>());

        List<Note> noteList = new ArrayList<Note>();
        noteList.add(note1);
        noteList.add(note2);
        expect(
                hibernateTemplate.find(NoteHelper.SELECT_NOTE_ATTACHED_TO,
                        new Integer(ITERATION_ID), Hibernate.INTEGER))
                .andReturn(noteList);

        expect(
                hibernateTemplate.bulkUpdate(NoteHelper.NOTE_DELETE_QUERY, new Integer(
                        NOTE1_ID))).andReturn(1);

        expect(
                hibernateTemplate.bulkUpdate(NoteHelper.NOTE_DELETE_QUERY, new Integer(NOTE2_ID))).andReturn(1);

        expect(
                hibernateTemplate.find(NoteHelper.SELECT_NOTE_ATTACHED_TO,
                        new Integer(STORY_ID), Hibernate.INTEGER)).andReturn(
                new ArrayList<Note>());

        replay();
        NoteHelper.deleteNotesFor(mockProject, hibernateTemplate);
        verify();
    }
}
