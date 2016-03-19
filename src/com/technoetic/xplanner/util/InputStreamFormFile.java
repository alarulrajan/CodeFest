package com.technoetic.xplanner.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.struts.upload.FormFile;

/**
 * The Class InputStreamFormFile.
 */
public class InputStreamFormFile implements FormFile {
	
	/** The file data. */
	private byte[] fileData;
	
	/** The file name. */
	private String fileName;
	
	/** The file size. */
	private int fileSize;
	
	/** The content type. */
	private String contentType;

	/**
     * Instantiates a new input stream form file.
     *
     * @param inputStream
     *            the input stream
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
	public InputStreamFormFile(final InputStream inputStream)
			throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		final byte[] buffer = new byte[8192];
		int bytesRead = 0;
		while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
			baos.write(buffer, 0, bytesRead);
		}

		this.fileData = baos.toByteArray();
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.upload.FormFile#getFileName()
	 */
	@Override
	public String getFileName() {
		return this.fileName;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.upload.FormFile#setFileName(java.lang.String)
	 */
	@Override
	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.upload.FormFile#getFileSize()
	 */
	@Override
	public int getFileSize() {
		return this.fileSize;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.upload.FormFile#setFileSize(int)
	 */
	@Override
	public void setFileSize(final int fileSize) {
		this.fileSize = fileSize;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.upload.FormFile#getContentType()
	 */
	@Override
	public String getContentType() {
		return this.contentType;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.upload.FormFile#setContentType(java.lang.String)
	 */
	@Override
	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.upload.FormFile#getFileData()
	 */
	@Override
	public byte[] getFileData() throws FileNotFoundException, IOException {
		return this.fileData;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.upload.FormFile#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws FileNotFoundException,
			IOException {
		return new ByteArrayInputStream(this.fileData);
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.upload.FormFile#destroy()
	 */
	@Override
	public void destroy() {
		this.fileName = null;
		this.fileSize = -1;
		this.contentType = null;
		this.fileData = null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}

		final InputStreamFormFile that = (InputStreamFormFile) obj;
		return (this.fileData == null ? that.fileData == null : Arrays.equals(
				this.fileData, that.fileData))
				&& (this.fileName == null ? that.fileName == null
						: this.fileName.equals(that.fileName))
				&& this.fileSize == that.fileSize
				&& (this.contentType == null ? that.contentType == null
						: this.contentType.equals(that.contentType)) && true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("InputStreamFormFile - ");
		sb.append("fileName: " + this.fileName + "\n");
		sb.append("fileSize: " + this.fileSize + "\n");
		sb.append("contentType: " + this.contentType + "\n");
		sb.append("fileData: " + new String(this.fileData) + "\n");
		return sb.toString();
	}

}
