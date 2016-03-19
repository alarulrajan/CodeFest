package com.technoetic.xplanner.domain;

import java.util.Date;

import net.sf.xplanner.domain.DomainObject;

/**
 * The Class Integration.
 */
public class Integration extends DomainObject {
    
    /** The Constant PENDING. */
    public static final char PENDING = 'P';
    
    /** The Constant ACTIVE. */
    public static final char ACTIVE = 'A';
    
    /** The Constant FINISHED. */
    public static final char FINISHED = 'F';
    
    /** The Constant CANCELED. */
    public static final char CANCELED = 'X';

    /** The person id. */
    private int personId;
    
    /** The state. */
    private char state;
    
    /** The comment. */
    private String comment;
    
    /** The when started. */
    private Date whenStarted;
    
    /** The when requested. */
    private Date whenRequested;
    
    /** The when complete. */
    private Date whenComplete;
    
    /** The project id. */
    private int projectId;

    /**
     * Gets the person id.
     *
     * @return the person id
     */
    public int getPersonId() {
        return this.personId;
    }

    /**
     * Sets the person id.
     *
     * @param personId
     *            the new person id
     */
    public void setPersonId(final int personId) {
        this.personId = personId;
    }

    /**
     * Sets the state.
     *
     * @param state
     *            the new state
     */
    public void setState(final char state) {
        this.state = state;
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public char getState() {
        return this.state;
    }

    /**
     * Sets the comment.
     *
     * @param comment
     *            the new comment
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * Gets the comment.
     *
     * @return the comment
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return this.getComment();
    }

    /**
     * Sets the when started.
     *
     * @param whenStarted
     *            the new when started
     */
    public void setWhenStarted(final Date whenStarted) {
        this.whenStarted = whenStarted;
    }

    /**
     * Gets the when started.
     *
     * @return the when started
     */
    public Date getWhenStarted() {
        return this.whenStarted;
    }

    /**
     * Sets the when requested.
     *
     * @param whenRequested
     *            the new when requested
     */
    public void setWhenRequested(final Date whenRequested) {
        this.whenRequested = whenRequested;
    }

    /**
     * Gets the when requested.
     *
     * @return the when requested
     */
    public Date getWhenRequested() {
        return this.whenRequested;
    }

    /**
     * Gets the when complete.
     *
     * @return the when complete
     */
    public Date getWhenComplete() {
        return this.whenComplete;
    }

    /**
     * Sets the when complete.
     *
     * @param whenComplete
     *            the new when complete
     */
    public void setWhenComplete(final Date whenComplete) {
        this.whenComplete = whenComplete;
    }

    /**
     * Gets the duration.
     *
     * @return the duration
     */
    public double getDuration() {
        if (this.whenStarted != null && this.whenComplete != null) {
            final long delta = this.whenComplete.getTime()
                    - this.whenStarted.getTime();
            return delta / 3600000.0;
        } else {
            return 0;
        }
    }

    /**
     * Gets the project id.
     *
     * @return the project id
     */
    public int getProjectId() {
        return this.projectId;
    }

    /**
     * Sets the project id.
     *
     * @param projectId
     *            the new project id
     */
    public void setProjectId(final int projectId) {
        this.projectId = projectId;
    }

    // DEBT: LSP violation. Should have a class of domain object that are not
    /**
     * Gets the name.
     *
     * @return the name
     */
    // Nameable
    public String getName() {
        return "";
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.domain.DomainObject#toString()
     */
    @Override
    public String toString() {
        return "Integration(id=" + this.getId() + ", personId="
                + this.getPersonId() + ", projectId=" + this.getProjectId()
                + ", comment=" + this.getComment() + ", lastUpdateTime="
                + this.getLastUpdateTime() + ", whenComplete="
                + this.getWhenComplete() + ", whenStarted="
                + this.getWhenStarted() + ")";
    }
}