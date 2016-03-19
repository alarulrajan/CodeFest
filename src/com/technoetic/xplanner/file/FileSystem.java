package com.technoetic.xplanner.file;

import java.io.InputStream;

import net.sf.xplanner.domain.Directory;
import net.sf.xplanner.domain.File;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

/**
 * The Interface FileSystem.
 */
public interface FileSystem {
    
    /**
     * Gets the root directory.
     *
     * @return the root directory
     * @throws HibernateException
     *             the hibernate exception
     */
    Directory getRootDirectory() throws HibernateException;

    /**
     * Gets the directory.
     *
     * @param session
     *            the session
     * @param directoryId
     *            the directory id
     * @return the directory
     * @throws HibernateException
     *             the hibernate exception
     */
    Directory getDirectory(Session session, int directoryId)
            throws HibernateException;

    /**
     * Gets the directory.
     *
     * @param path
     *            the path
     * @return the directory
     * @throws HibernateException
     *             the hibernate exception
     */
    Directory getDirectory(String path) throws HibernateException;

    /**
     * Creates the file.
     *
     * @param session
     *            the session
     * @param directoryId
     *            the directory id
     * @param name
     *            the name
     * @param contentType
     *            the content type
     * @param size
     *            the size
     * @param data
     *            the data
     * @return the file
     * @throws HibernateException
     *             the hibernate exception
     */
    File createFile(Session session, int directoryId, String name,
            String contentType, long size, InputStream data)
            throws HibernateException;

    /**
     * Creates the file.
     *
     * @param session
     *            the session
     * @param directory
     *            the directory
     * @param filename
     *            the filename
     * @param contentType
     *            the content type
     * @param fileSize
     *            the file size
     * @param input
     *            the input
     * @return the file
     */
    File createFile(Session session, Directory directory, String filename,
            String contentType, long fileSize, InputStream input);

    /**
     * Gets the file.
     *
     * @param session
     *            the session
     * @param fileId
     *            the file id
     * @return the file
     * @throws HibernateException
     *             the hibernate exception
     */
    File getFile(Session session, int fileId) throws HibernateException;

    /**
     * Delete file.
     *
     * @param session
     *            the session
     * @param fileId
     *            the file id
     * @throws HibernateException
     *             the hibernate exception
     */
    void deleteFile(Session session, int fileId) throws HibernateException;

    /**
     * Creates the directory.
     *
     * @param session
     *            the session
     * @param parentDirectoryId
     *            the parent directory id
     * @param name
     *            the name
     * @return the directory
     * @throws HibernateException
     *             the hibernate exception
     */
    Directory createDirectory(Session session, int parentDirectoryId,
            String name) throws HibernateException;

    /**
     * Creates the directory.
     *
     * @param session
     *            the session
     * @param parent
     *            the parent
     * @param name
     *            the name
     * @return the directory
     * @throws HibernateException
     *             the hibernate exception
     */
    Directory createDirectory(Session session, Directory parent, String name)
            throws HibernateException;

    /**
     * Delete directory.
     *
     * @param session
     *            the session
     * @param directoryId
     *            the directory id
     * @throws HibernateException
     *             the hibernate exception
     */
    void deleteDirectory(Session session, int directoryId)
            throws HibernateException;

}
