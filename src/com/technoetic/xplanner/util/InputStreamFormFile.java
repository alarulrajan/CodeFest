package com.technoetic.xplanner.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.struts.upload.FormFile;

public class InputStreamFormFile implements FormFile {
	private byte[] fileData;
	private String fileName;
	private int fileSize;
	private String contentType;

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

	@Override
	public String getFileName() {
		return this.fileName;
	}

	@Override
	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	@Override
	public int getFileSize() {
		return this.fileSize;
	}

	@Override
	public void setFileSize(final int fileSize) {
		this.fileSize = fileSize;
	}

	@Override
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	@Override
	public byte[] getFileData() throws FileNotFoundException, IOException {
		return this.fileData;
	}

	@Override
	public InputStream getInputStream() throws FileNotFoundException,
			IOException {
		return new ByteArrayInputStream(this.fileData);
	}

	@Override
	public void destroy() {
		this.fileName = null;
		this.fileSize = -1;
		this.contentType = null;
		this.fileData = null;
	}

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

	@Override
	public int hashCode() {
		return super.hashCode();
	}

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
