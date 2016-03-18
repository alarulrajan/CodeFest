package com.technoetic.xplanner.file;

import java.io.InputStream;
import java.util.List;

import net.sf.xplanner.domain.Directory;
import net.sf.xplanner.domain.File;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

// Repository for files and directories
public class FileSystemImpl implements FileSystem {
	@Override
	public Directory getRootDirectory() throws HibernateException {
		final Session session = ThreadSession.get();
		final List dirs = session
				.createQuery(
						"from dir in " + Directory.class
								+ " where dir.parent is null").setMaxResults(1)
				.list();
		if (dirs.iterator().hasNext()) {
			return (Directory) dirs.iterator().next();
		} else {
			final Directory root = new Directory();
			root.setName("");
			session.save(root);
			session.flush();
			session.refresh(root);
			return root;
		}
	}

	@Override
	public Directory getDirectory(final Session session, final int directoryId)
			throws HibernateException {
		return (Directory) session.load(Directory.class, new Integer(
				directoryId));
	}

	@Override
	public Directory getDirectory(final String path) throws HibernateException {
		final String[] pathElements = path.split("/");
		Directory dir = this.getRootDirectory();
		final Session session = ThreadSession.get();
		for (int i = 1; i < pathElements.length; i++) {
			final List subdirectory = session
					.createQuery(
							"from dir in "
									+ Directory.class
									+ " where dir.parent = :parent and dir.name = :name")
					.setParameter("parent", dir,
							Hibernate.entity(Directory.class))
					.setParameter("name", pathElements[i]).setMaxResults(1)
					.list();
			if (subdirectory.size() > 0) {
				dir = (Directory) subdirectory.get(0);
			} else {
				dir = this.createDirectory(session, dir, pathElements[i]);
			}
		}
		return dir;
	}

	@Override
	public File createFile(final Session session, final Directory directory,
			final String name, final String contentType, final long size,
			final InputStream data) throws HibernateException {
		final File file = new File();
		file.setName(name);
		file.setContentType(contentType);
		file.setFileSize(size);
		if (data != null) {

			file.setData(Hibernate.createBlob(data, (int) size, session));
		}
		ThreadSession.get().save(file);
		directory.getFiles().add(file);
		return file;
	}

	@Override
	public File createFile(final Session session, final int directoryId,
			final String name, final String contentType, final long size,
			final InputStream data) throws HibernateException {
		final Directory directory = (Directory) session.load(Directory.class,
				new Integer(directoryId));
		return this.createFile(session, directory, name, contentType, size,
				data);
	}

	@Override
	public File getFile(final Session session, final int fileId)
			throws HibernateException {
		return (File) session.load(File.class, new Integer(fileId));
	}

	@Override
	public void deleteFile(final Session session, final int fileId)
			throws HibernateException {
		session.delete("from file in " + File.class + " where id = ?",
				new Integer(fileId), Hibernate.INTEGER);
	}

	@Override
	public Directory createDirectory(final Session session,
			final int parentDirectoryId, final String name)
			throws HibernateException {
		final Directory parent = this.getDirectory(session, parentDirectoryId);
		return this.createDirectory(session, parent, name);
	}

	@Override
	public Directory createDirectory(final Session session,
			final Directory parent, final String name)
			throws HibernateException {
		final Directory subdirectory = new Directory();
		subdirectory.setName(name);
		session.save(subdirectory);
		session.flush();
		session.refresh(subdirectory);
		parent.getSubdirectories().add(subdirectory);
		return subdirectory;
	}

	@Override
	public void deleteDirectory(final Session session, final int directoryId)
			throws HibernateException {
		session.delete("from dir in " + Directory.class + " where id = ?",
				new Integer(directoryId), Hibernate.INTEGER);
	}
}
