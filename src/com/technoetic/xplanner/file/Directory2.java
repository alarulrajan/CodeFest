package com.technoetic.xplanner.file;

import java.util.Set;

import com.technoetic.xplanner.domain.Identifiable;

/**
 * The Class Directory2.
 */
public class Directory2 implements Identifiable {
	
	/** The id. */
	private int id;
	
	/** The name. */
	private String name;
	
	/** The subdirectories. */
	private Set subdirectories;
	
	/** The files. */
	private Set files;
	
	/** The parent. */
	private Directory2 parent;

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
	protected void setId(final int id) {
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
     * Gets the parent.
     *
     * @return the parent
     */
	public Directory2 getParent() {
		return this.parent;
	}

	/**
     * Sets the parent.
     *
     * @param parent
     *            the new parent
     */
	public void setParent(final Directory2 parent) {
		this.parent = parent;
	}

	/**
     * Gets the subdirectories.
     *
     * @return the subdirectories
     */
	public Set getSubdirectories() {
		return this.subdirectories;
	}

	/**
     * Sets the subdirectories.
     *
     * @param subdirectories
     *            the new subdirectories
     */
	public void setSubdirectories(final Set subdirectories) {
		this.subdirectories = subdirectories;
	}

	/**
     * Gets the files.
     *
     * @return the files
     */
	public Set getFiles() {
		return this.files;
	}

	/**
     * Sets the files.
     *
     * @param files
     *            the new files
     */
	public void setFiles(final Set files) {
		this.files = files;
	}
}
