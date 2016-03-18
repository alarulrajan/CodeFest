package com.technoetic.xplanner.db;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.File;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.type.NullableType;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.technoetic.xplanner.domain.NoteAttachable;

/**
 * User: Mateusz Prokopowicz Date: Dec 16, 2004 Time: 2:10:17 PM
 */
public class NoteHelper {
	// FIXME: Externalize all these query in the Note.xml mapping file
	// DEBT: Turn this into a NoteRepository that is spring loaded. No more
	// static entry point. A session is passed in at construction time.

	// FIXME: Remove all duplicated code
	static final String SELECT_NOTES_FILE_IS_ATTACHED_TO = "select note from Note note where note.file.id= ?";

	static final String FILE_DELETE_QUERY = "from file in " + File.class
			+ " where file.id= ?";

	static final String NOTE_DELETE_QUERY = "from note in " + Note.class
			+ " where note.id = ?";

	static final String SELECT_NOTE_ATTACHED_TO = "select note from Note note where note.attachedToId = ?";

	static final String SELECT_ATTACHED_TO_ID = "SELECT t.id, s.id, i.id, p.id FROM "
			+ Project.class.getName()
			+ " as p left join p.iterations as i left join i.userStories as s left join s.tasks as t"
			+ " WHERE  p.id = ? or i.id = ? or s.id = ? or t.id = ? order by i.id, s.id, t.id";

	static final String NOTE_DELETION_QUERY = "from note in " + Note.class
			+ " where note.attachedToId = ?";

	static final Class[] DELETE_ORDER = { Task.class, UserStory.class,
			Iteration.class, Project.class };

	public static int deleteNote(final Note note,
			final HibernateTemplate session) throws HibernateException {
		if (note.getFile() != null) {
			final List noteList = session.find(
					NoteHelper.SELECT_NOTES_FILE_IS_ATTACHED_TO, new Integer(
							note.getFile().getId()), Hibernate.INTEGER);
			if (noteList != null && noteList.size() == 1) {
				session.bulkUpdate(NoteHelper.FILE_DELETE_QUERY, new Integer(
						((Note) noteList.get(0)).getFile().getId()));
			}
		}
		session.delete(note);
		return 1;
	}

	public static int deleteNote(final Note note, final Session session)
			throws HibernateException {
		if (note.getFile() != null) {
			final List noteList = session.find(
					NoteHelper.SELECT_NOTES_FILE_IS_ATTACHED_TO, new Integer(
							note.getFile().getId()), Hibernate.INTEGER);
			if (noteList != null && noteList.size() == 1) {
				session.delete(NoteHelper.FILE_DELETE_QUERY, new Integer(
						((Note) noteList.get(0)).getFile().getId()),
						Hibernate.INTEGER);
			}
		}
		return session.delete(NoteHelper.NOTE_DELETE_QUERY,
				new Integer(note.getId()), Hibernate.INTEGER);
	}

	public static int deleteNotesFor(final NoteAttachable obj,
			final HibernateTemplate session) throws HibernateException {
		return NoteHelper.deleteNotesFor(obj.getClass(), obj.getId(), session);
	}

	public static int deleteNotesFor(final NoteAttachable obj,
			final Session session) throws HibernateException {
		return NoteHelper.deleteNotesFor(obj.getClass(), obj.getId(), session);
	}

	public static int deleteNotesFor(final Class clazz, final int objectId,
			final HibernateTemplate hibernateTemplate)
			throws HibernateException {
		final Integer[] ids = new Integer[4];
		Arrays.fill(ids, new Integer(objectId));
		final NullableType[] types = new NullableType[4];
		Arrays.fill(types, Hibernate.INTEGER);
		final Iterator objIt = hibernateTemplate.iterate(
				NoteHelper.SELECT_ATTACHED_TO_ID, ids);
		Object[] oldRow = new Object[4];
		Arrays.fill(oldRow, new Integer(-1));
		final int retVal = 0;
		while (objIt.hasNext()) {
			final Object[] row = (Object[]) objIt.next();
			for (int i = 0; i <= Arrays.asList(NoteHelper.DELETE_ORDER)
					.indexOf(clazz); i++) {
				if (row[i] != null && !row[i].equals(oldRow[i])) {
					final List noteList = hibernateTemplate.find(
							NoteHelper.SELECT_NOTE_ATTACHED_TO, row[i]);
					for (final Iterator noteIt = noteList.iterator(); noteIt
							.hasNext();) {
						NoteHelper.deleteNote((Note) noteIt.next(),
								hibernateTemplate);
					}
				}
			}
			oldRow = row;
		}
		return retVal;
	}

	public static int deleteNotesFor(final Class clazz, final int objectId,
			final Session hibernateTemplate) throws HibernateException {
		final Integer[] ids = new Integer[4];
		Arrays.fill(ids, new Integer(objectId));
		final NullableType[] types = new NullableType[4];
		Arrays.fill(types, Hibernate.INTEGER);
		final Iterator objIt = hibernateTemplate.iterate(
				NoteHelper.SELECT_ATTACHED_TO_ID, ids, types);
		Object[] oldRow = new Object[4];
		Arrays.fill(oldRow, new Integer(-1));
		final int retVal = 0;
		while (objIt.hasNext()) {
			final Object[] row = (Object[]) objIt.next();
			for (int i = 0; i <= Arrays.asList(NoteHelper.DELETE_ORDER)
					.indexOf(clazz); i++) {
				if (row[i] != null && !row[i].equals(oldRow[i])) {
					final List noteList = hibernateTemplate.find(
							NoteHelper.SELECT_NOTE_ATTACHED_TO, row[i],
							Hibernate.INTEGER);
					for (final Iterator noteIt = noteList.iterator(); noteIt
							.hasNext();) {
						NoteHelper.deleteNote((Note) noteIt.next(),
								hibernateTemplate);
					}
				}
			}
			oldRow = row;
		}
		return retVal;
	}
}
