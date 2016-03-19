package com.technoetic.xplanner.file;

import java.sql.Blob;

import com.technoetic.xplanner.domain.Identifiable;

/**
 * The Class File2.
 */
public class File2 implements Identifiable {
    
    /** The id. */
    private int id;
    
    /** The name. */
    private String name;
    
    /** The content type. */
    private String contentType;
    
    /** The data. */
    private Blob data;
    
    /** The file size. */
    private long fileSize;
    
    /** The directory. */
    private Directory2 directory;

    /**
     * Gets the content type.
     *
     * @return the content type
     */
    public String getContentType() {
        return this.contentType;
    }

    /**
     * Sets the content type.
     *
     * @param contentType
     *            the new content type
     */
    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public Blob getData() {
        return this.data;
    }

    /**
     * Sets the data.
     *
     * @param data
     *            the new data
     */
    public void setData(final Blob data) {
        this.data = data;
    }

    /**
     * Gets the file size.
     *
     * @return the file size
     */
    public long getFileSize() {
        return this.fileSize;
    }

    /**
     * Sets the file size.
     *
     * @param fileSize
     *            the new file size
     */
    public void setFileSize(final long fileSize) {
        this.fileSize = fileSize;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.Identifiable#getId()
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the directory.
     *
     * @return the directory
     */
    public Directory2 getDirectory() {
        return this.directory;
    }

    /**
     * Sets the directory.
     *
     * @param directory
     *            the new directory
     */
    protected void setDirectory(final Directory2 directory) {
        this.directory = directory;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "File{" + "id=" + this.id + ", name='" + this.name + '\''
                + ", directory=" + this.directory + ", contentType='"
                + this.contentType + '\'' + ", fileSize=" + this.fileSize + '}';
    }
}
