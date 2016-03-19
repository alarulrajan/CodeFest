package net.sf.xplanner.domain.view;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.xplanner.domain.NamedObject;

import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.StoryStatus;

/**
 * The Class UserStoryView.
 */
@XmlRootElement
public class UserStoryView extends NamedObject {
    
    /** The estimated hours. */
    private double estimatedHours;
    
    /** The priority. */
    private int priority;
    
    /** The status. */
    private char status = StoryStatus.DRAFT.getCode();
    
    /** The original estimated hours. */
    private Double originalEstimatedHours;
    
    /** The disposition code. */
    private char dispositionCode = StoryDisposition.PLANNED.getCode();
    
    /** The postponed hours. */
    private double postponedHours;
    
    /** The order no. */
    private int orderNo;
    
    /** The actual hours. */
    private double actualHours;
    
    /** The tracker id. */
    private int trackerId;
    
    /** The tasks. */
    private List<TaskView> tasks = new ArrayList<TaskView>();

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
     * Gets the priority.
     *
     * @return the priority
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * Sets the priority.
     *
     * @param priority
     *            the new priority
     */
    public void setPriority(final int priority) {
        this.priority = priority;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public char getStatus() {
        return this.status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
    public void setStatus(final char status) {
        this.status = status;
    }

    /**
     * Gets the original estimated hours.
     *
     * @return the original estimated hours
     */
    public Double getOriginalEstimatedHours() {
        return this.originalEstimatedHours;
    }

    /**
     * Sets the original estimated hours.
     *
     * @param originalEstimatedHours
     *            the new original estimated hours
     */
    public void setOriginalEstimatedHours(final Double originalEstimatedHours) {
        this.originalEstimatedHours = originalEstimatedHours;
    }

    /**
     * Gets the disposition code.
     *
     * @return the disposition code
     */
    public char getDispositionCode() {
        return this.dispositionCode;
    }

    /**
     * Sets the disposition code.
     *
     * @param dispositionCode
     *            the new disposition code
     */
    public void setDispositionCode(final char dispositionCode) {
        this.dispositionCode = dispositionCode;
    }

    /**
     * Gets the postponed hours.
     *
     * @return the postponed hours
     */
    public double getPostponedHours() {
        return this.postponedHours;
    }

    /**
     * Sets the postponed hours.
     *
     * @param postponedHours
     *            the new postponed hours
     */
    public void setPostponedHours(final double postponedHours) {
        this.postponedHours = postponedHours;
    }

    /**
     * Gets the order no.
     *
     * @return the order no
     */
    public int getOrderNo() {
        return this.orderNo;
    }

    /**
     * Sets the order no.
     *
     * @param orderNo
     *            the new order no
     */
    public void setOrderNo(final int orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * Gets the actual hours.
     *
     * @return the actual hours
     */
    public double getActualHours() {
        return this.actualHours;
    }

    /**
     * Sets the actual hours.
     *
     * @param actualHours
     *            the new actual hours
     */
    public void setActualHours(final double actualHours) {
        this.actualHours = actualHours;
    }

    /**
     * Gets the tasks.
     *
     * @return the tasks
     */
    @XmlElement(name = "task")
    public List<TaskView> getTasks() {
        return this.tasks;
    }

    /**
     * Sets the tasks.
     *
     * @param tasks
     *            the new tasks
     */
    public void setTasks(final List<TaskView> tasks) {
        this.tasks = tasks;
    }

    /**
     * Gets the tracker id.
     *
     * @return the tracker id
     */
    public int getTrackerId() {
        return this.trackerId;
    }

    /**
     * Sets the tracker id.
     *
     * @param trackerId
     *            the new tracker id
     */
    public void setTrackerId(final int trackerId) {
        this.trackerId = trackerId;
    }
}
