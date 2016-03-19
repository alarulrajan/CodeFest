package net.sf.xplanner.domain.view;

import java.util.Date;

import net.sf.xplanner.domain.NamedObject;

/**
 * The Class TaskView.
 */
public class TaskView extends NamedObject {
    
    /** The type. */
    private String type;
    
    /** The completed. */
    private boolean completed;
    
    /** The original estimate. */
    private double originalEstimate;
    
    /** The estimated hours. */
    private double estimatedHours;
    
    /** The created date. */
    private Date createdDate;
    
    /** The acceptor id. */
    private int acceptorId;

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * Checks if is completed.
     *
     * @return true, if is completed
     */
    public boolean isCompleted() {
        return this.completed;
    }

    /**
     * Sets the completed.
     *
     * @param completed
     *            the new completed
     */
    public void setCompleted(final boolean completed) {
        this.completed = completed;
    }

    /**
     * Gets the original estimate.
     *
     * @return the original estimate
     */
    public double getOriginalEstimate() {
        return this.originalEstimate;
    }

    /**
     * Sets the original estimate.
     *
     * @param originalEstimate
     *            the new original estimate
     */
    public void setOriginalEstimate(final double originalEstimate) {
        this.originalEstimate = originalEstimate;
    }

    /**
     * Gets the created date.
     *
     * @return the created date
     */
    public Date getCreatedDate() {
        return this.createdDate;
    }

    /**
     * Sets the created date.
     *
     * @param createdDate
     *            the new created date
     */
    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Gets the estimated hours.
     *
     * @return the estimated hours
     */
    public double getEstimatedHours() {
        return this.estimatedHours;
    }

    /**
     * Sets the estimated hours.
     *
     * @param estimatedHours
     *            the new estimated hours
     */
    public void setEstimatedHours(final double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    /**
     * Gets the acceptor id.
     *
     * @return the acceptor id
     */
    public int getAcceptorId() {
        return this.acceptorId;
    }

    /**
     * Sets the acceptor id.
     *
     * @param acceptorId
     *            the new acceptor id
     */
    public void setAcceptorId(final int acceptorId) {
        this.acceptorId = acceptorId;
    }
}
