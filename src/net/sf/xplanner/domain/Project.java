package net.sf.xplanner.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.technoetic.xplanner.domain.NoteAttachable;
import com.technoetic.xplanner.domain.repository.IterationRepository;

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
@Table(name = "project")
@XmlRootElement
public class Project extends NamedObject implements java.io.Serializable,
        NoteAttachable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6137321799143662647L;
    
    /** The Constant HIDDEN. */
    public static final String HIDDEN = "hidden";
    
    /** The hidden. */
    private Boolean hidden;
    
    /** The backlog. */
    private Iteration backlog;
    
    /** The iterations. */
    private List<Iteration> iterations = new ArrayList<Iteration>();
    
    /** The notification receivers. */
    private List<Person> notificationReceivers;

    /**
     * Instantiates a new project.
     */
    public Project() {
    }

    /**
     * Gets the hidden.
     *
     * @return the hidden
     */
    @Column(name = "is_hidden")
    public Boolean getHidden() {
        return this.hidden;
    }

    /**
     * Sets the hidden.
     *
     * @param isHidden
     *            the new hidden
     */
    public void setHidden(final Boolean isHidden) {
        this.hidden = isHidden;
    }

    /**
     * Gets the iterations.
     *
     * @return the iterations
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public List<Iteration> getIterations() {
        return this.iterations;
    }

    /**
     * Sets the iterations.
     *
     * @param iterations
     *            the new iterations
     */
    public void setIterations(final List<Iteration> iterations) {
        this.iterations = iterations;
    }

    /**
     * Sets the notification receivers.
     *
     * @param notificationReceivers
     *            the new notification receivers
     */
    public void setNotificationReceivers(
            final List<Person> notificationReceivers) {
        this.notificationReceivers = notificationReceivers;
    }

    /**
     * Gets the notification receivers.
     *
     * @return the notification receivers
     */
    @ManyToMany()
    @JoinTable(name = "notification_receivers", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    public List<Person> getNotificationReceivers() {
        return this.notificationReceivers;
    }

    /**
     * Gets the backlog.
     *
     * @return the backlog
     */
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "backlog_id", insertable = true, updatable = true, unique = true, nullable = true)
    public Iteration getBacklog() {
        return this.backlog;
    }

    /**
     * Sets the backlog.
     *
     * @param backlog
     *            the new backlog
     */
    public void setBacklog(final Iteration backlog) {
        this.backlog = backlog;
    }

    /**
     * Gets the current iteration.
     *
     * @return the current iteration
     */
    @Transient
    public Iteration getCurrentIteration() {
        return IterationRepository.getCurrentIteration(this.getId());
    }

    /**
     * Checks if is hidden.
     *
     * @return the boolean
     */
    @Transient
    public Boolean isHidden() {
        return this.hidden;
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.domain.DomainObject#getAttributes()
     */
    @Override
    @ElementCollection
    @JoinTable(name = "attribute", joinColumns = @JoinColumn(name = "targetId"))
    @MapKeyColumn(name = "name")
    @Column(name = "\"value\"")
    public Map<String, String> getAttributes() {
        return super.getAttributes();
    }

}
