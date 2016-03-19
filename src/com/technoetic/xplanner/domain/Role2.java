package com.technoetic.xplanner.domain;

import java.io.Serializable;
import java.security.Principal;

import net.sf.xplanner.domain.NamedObject;

/**
 * The Class Role2.
 */
public class Role2 extends NamedObject implements Principal, Serializable {
    
    /** The Constant SYSADMIN. */
    public static final String SYSADMIN = "sysadmin";
    
    /** The Constant ADMIN. */
    public static final String ADMIN = "admin";
    
    /** The Constant EDITOR. */
    public static final String EDITOR = "editor";
    
    /** The Constant VIEWER. */
    public static final String VIEWER = "viewer";
    
    /** The left. */
    private int left;
    
    /** The right. */
    private int right;

    /**
     * Instantiates a new role2.
     */
    Role2() {
        // for Hibernate
    }

    /**
     * Instantiates a new role2.
     *
     * @param name
     *            the name
     */
    public Role2(final String name) {
        this.setName(name);
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.domain.NamedObject#toString()
     */
    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * Gets the left.
     *
     * @return the left
     */
    public int getLeft() {
        return this.left;
    }

    /**
     * Sets the left.
     *
     * @param left
     *            the new left
     */
    public void setLeft(final int left) {
        this.left = left;
    }

    /**
     * Gets the right.
     *
     * @return the right
     */
    public int getRight() {
        return this.right;
    }

    /**
     * Sets the right.
     *
     * @param right
     *            the new right
     */
    public void setRight(final int right) {
        this.right = right;
    }

    /**
     * Checks if is sysadmin.
     *
     * @return true, if is sysadmin
     */
    public boolean isSysadmin() {
        return this.getName().equals(Role2.SYSADMIN);
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.domain.NamedObject#getDescription()
     */
    @Override
    public String getDescription() {
        return "";
    }
}
