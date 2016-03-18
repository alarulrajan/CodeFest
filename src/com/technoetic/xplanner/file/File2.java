package com.technoetic.xplanner.file;

import java.sql.Blob;

import com.technoetic.xplanner.domain.Identifiable;

public class File2 implements Identifiable {
	private int id;
	private String name;
	private String contentType;
	private Blob data;
	private long fileSize;
	private Directory2 directory;

	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	public Blob getData() {
		return this.data;
	}

	public void setData(final Blob data) {
		this.data = data;
	}

	public long getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(final long fileSize) {
		this.fileSize = fileSize;
	}

	@Override
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Directory2 getDirectory() {
		return this.directory;
	}

	protected void setDirectory(final Directory2 directory) {
		this.directory = directory;
	}

	@Override
	public String toString() {
		return "File{" + "id=" + this.id + ", name='" + this.name + '\''
				+ ", directory=" + this.directory + ", contentType='"
				+ this.contentType + '\'' + ", fileSize=" + this.fileSize + '}';
	}
}
