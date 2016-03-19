package net.sf.xplanner.domain;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * XplannerPlus, agile planning software.
 *
 * @author Maksym_Chyrkov. Copyright (C) 2009 Maksym Chyrkov This program is
 *         free software: you can redistribute it and/or modify it under the
 *         terms of the GNU General Public License as published by the Free
 *         Software Foundation, either version 3 of the License, or (at your
 *         option) any later version.
 * 
 *         This program is distributed in the hope that it will be useful, but
 *         WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with this program. If not, see <http://www.gnu.org/licenses/>
 */

@Entity
@Table(name = "xfile")
public class File extends DomainObject implements java.io.Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7604190182077162703L;
	
	/** The name. */
	private String name;
	
	/** The content type. */
	private String contentType;
	
	/** The data. */
	private Blob data;
	
	/** The file size. */
	private long fileSize;
	
	/** The directory. */
	private Directory directory;

	/**
     * Instantiates a new file.
     */
	public File() {
	}

	/**
     * Gets the name.
     *
     * @return the name
     */
	@Column(name = "name")
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
     * Gets the content type.
     *
     * @return the content type
     */
	@Column(name = "content_type")
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
	@Lob
	@Column(name = "data", length = 1000000000)
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
	@Column(name = "file_size")
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

	/**
     * Gets the directory.
     *
     * @return the directory
     */
	@ManyToOne
	@JoinColumn(name = "dir_id")
	public Directory getDirectory() {
		return this.directory;
	}

	/**
     * Sets the directory.
     *
     * @param directory
     *            the new directory
     */
	public void setDirectory(final Directory directory) {
		this.directory = directory;
	}

}
