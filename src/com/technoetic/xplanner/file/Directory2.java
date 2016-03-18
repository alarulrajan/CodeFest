package com.technoetic.xplanner.file;

import java.util.Set;

import com.technoetic.xplanner.domain.Identifiable;

public class Directory2 implements Identifiable {
	private int id;
	private String name;
	private Set subdirectories;
	private Set files;
	private Directory2 parent;

	@Override
	public int getId() {
		return this.id;
	}

	protected void setId(final int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Directory2 getParent() {
		return this.parent;
	}

	public void setParent(final Directory2 parent) {
		this.parent = parent;
	}

	public Set getSubdirectories() {
		return this.subdirectories;
	}

	public void setSubdirectories(final Set subdirectories) {
		this.subdirectories = subdirectories;
	}

	public Set getFiles() {
		return this.files;
	}

	public void setFiles(final Set files) {
		this.files = files;
	}
}
