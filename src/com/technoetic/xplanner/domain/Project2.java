package com.technoetic.xplanner.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Iteration;

import com.technoetic.xplanner.domain.repository.IterationRepository;

/**
 * The Class Project2.
 */
public class Project2 extends DomainObject implements Nameable, NoteAttachable,
        Describable {
    
    /** The name. */
    private String name;
    
    /** The iterations. */
    private Collection iterations = new HashSet();
    
    /** The notification receivers. */
    private Collection notificationReceivers = new TreeSet();
    
    /** The description. */
    private String description;
    
    /** The hidden. */
    private boolean hidden;

    // private boolean sendemail;
    // private boolean optEscapeBrackets;

    // public boolean isSendingMissingTimeEntryReminderToAcceptor() {
    // return sendemail;
    // }
    //
    // public void setSendemail(boolean newSendemail) {
    // sendemail = newSendemail;
    // }

    // public boolean isOptEscapeBrackets() {
    // return optEscapeBrackets;
    // }
    //
    // public void setOptEscapeBrackets(boolean optEscapeBrackets) {
    // this.optEscapeBrackets = optEscapeBrackets;
    // }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.Nameable#getName()
     */
    @Override
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
     * Gets the iterations.
     *
     * @return the iterations
     */
    public Collection getIterations() {
        return this.iterations;
    }

    /**
     * Sets the iterations.
     *
     * @param iterations
     *            the new iterations
     */
    public void setIterations(final Collection iterations) {
        this.iterations = iterations;
    }

    /**
     * Gets the current iteration.
     *
     * @return the current iteration
     */
    public Iteration getCurrentIteration() {
        return IterationRepository.getCurrentIteration(this.getId());
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.Describable#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.Nameable#getDescription()
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Checks if is hidden.
     *
     * @return true, if is hidden
     */
    public boolean isHidden() {
        return this.hidden;
    }

    /**
     * Sets the hidden.
     *
     * @param flag
     *            the new hidden
     */
    public void setHidden(final boolean flag) {
        this.hidden = flag;
    }

    /**
     * Gets the notification receivers.
     *
     * @return the notification receivers
     */
    public Collection getNotificationReceivers() {
        return this.notificationReceivers;
    }

    /**
     * Sets the notification receivers.
     *
     * @param notificationReceivers
     *            the new notification receivers
     */
    public void setNotificationReceivers(final Collection notificationReceivers) {
        this.notificationReceivers = notificationReceivers;
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.domain.DomainObject#toString()
     */
    @Override
    public String toString() {
        return "Project{" + "id='" + this.getId() + ", " + "name='" + this.name
                + "'" + "}";
    }
}