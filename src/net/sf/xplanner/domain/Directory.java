package net.sf.xplanner.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "xdir")
public class Directory extends DomainObject implements java.io.Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1957144606982414797L;
    
    /** The name. */
    private String name;
    
    /** The files. */
    private List<File> files;
    
    /** The parent. */
    private Directory parent;
    
    /** The subdirectories. */
    private List<Directory> subdirectories;

    /**
     * Instantiates a new directory.
     */
    public Directory() {
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
     * Gets the files.
     *
     * @return the files
     */
    @OneToMany(mappedBy = "directory")
    public List<File> getFiles() {
        return this.files;
    }

    /**
     * Sets the files.
     *
     * @param files
     *            the new files
     */
    public void setFiles(final List<File> files) {
        this.files = files;
    }

    /**
     * Gets the parent.
     *
     * @return the parent
     */
    @ManyToOne
    public Directory getParent() {
        return this.parent;
    }

    /**
     * Sets the parent.
     *
     * @param parent
     *            the new parent
     */
    public void setParent(final Directory parent) {
        this.parent = parent;
    }

    /**
     * Gets the subdirectories.
     *
     * @return the subdirectories
     */
    @OneToMany()
    @JoinColumn(name = "parent_id")
    public List<Directory> getSubdirectories() {
        return this.subdirectories;
    }

    /**
     * Sets the subdirectories.
     *
     * @param subdirectories
     *            the new subdirectories
     */
    public void setSubdirectories(final List<Directory> subdirectories) {
        this.subdirectories = subdirectories;
    }

}
