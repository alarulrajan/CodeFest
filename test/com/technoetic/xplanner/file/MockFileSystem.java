package com.technoetic.xplanner.file;

import java.io.InputStream;

import net.sf.xplanner.domain.Directory;
import net.sf.xplanner.domain.File;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

/**
 * The Class MockFileSystem.
 */
public class MockFileSystem implements FileSystem {
    
    /* (non-Javadoc)
     * @see com.technoetic.xplanner.file.FileSystem#createDirectory(org.hibernate.classic.Session, net.sf.xplanner.domain.Directory, java.lang.String)
     */
    public Directory createDirectory(Session session, Directory parent, String name) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.file.FileSystem#createDirectory(org.hibernate.classic.Session, int, java.lang.String)
     */
    public Directory createDirectory(Session session, int parentDirectoryId, String name) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /** The create file return. */
    public File createFileReturn;
    
    /* (non-Javadoc)
     * @see com.technoetic.xplanner.file.FileSystem#createFile(org.hibernate.classic.Session, net.sf.xplanner.domain.Directory, java.lang.String, java.lang.String, long, java.io.InputStream)
     */
    public File createFile(Session session, Directory directory, String name, String contentType,
            long size, InputStream data) throws HibernateException {
        return createFileReturn;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.file.FileSystem#createFile(org.hibernate.classic.Session, int, java.lang.String, java.lang.String, long, java.io.InputStream)
     */
    public File createFile(Session session, int directoryId, String name, String contentType,
            long size, InputStream data) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.file.FileSystem#deleteDirectory(org.hibernate.classic.Session, int)
     */
    public void deleteDirectory(Session session, int directoryId) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.file.FileSystem#deleteFile(org.hibernate.classic.Session, int)
     */
    public void deleteFile(Session session, int fileId) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.file.FileSystem#getDirectory(org.hibernate.classic.Session, int)
     */
    public Directory getDirectory(Session session, int directoryId) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /** The get directory2 return. */
    public Directory getDirectory2Return;
    
    /* (non-Javadoc)
     * @see com.technoetic.xplanner.file.FileSystem#getDirectory(java.lang.String)
     */
    public Directory getDirectory(String path) throws HibernateException {
        return getDirectory2Return;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.file.FileSystem#getFile(org.hibernate.classic.Session, int)
     */
    public File getFile(Session session, int fileId) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.file.FileSystem#getRootDirectory()
     */
    public Directory getRootDirectory() throws HibernateException {
        throw new UnsupportedOperationException();
    }
}
