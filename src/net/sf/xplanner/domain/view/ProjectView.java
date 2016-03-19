package net.sf.xplanner.domain.view;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.NamedObject;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Person;

//@Entity
//@Loader(namedQuery="project.loadView")
/**
 * The Class ProjectView.
 */
//@NamedQuery(name="project.loadView", query="select project.id as id,project.name as name from project in class net.sf.xplanner.domain.Project where project.id=?")
@XmlRootElement
public class ProjectView extends NamedObject {
	
	/** The hidden. */
	private Boolean hidden;
	
	/** The notification receivers. */
	private List<Person> notificationReceivers;
	
	/** The notes. */
	private List<Note> notes;
	
	/** The current iteration. */
	private Iteration currentIteration;

	/**
     * Gets the hidden.
     *
     * @return the hidden
     */
	public Boolean getHidden() {
		return this.hidden;
	}

	/**
     * Sets the hidden.
     *
     * @param hidden
     *            the new hidden
     */
	public void setHidden(final Boolean hidden) {
		this.hidden = hidden;
	}

	// @OneToMany()
	// @JoinTable(name="notification_receivers",
	/**
     * Gets the notification receivers.
     *
     * @return the notification receivers
     */
	// joinColumns=@JoinColumn(name="attachedTo_id"))
	@XmlTransient
	public List<Person> getNotificationReceivers() {
		return this.notificationReceivers;
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

	// @ManyToMany()
	// @JoinTable(name="note", joinColumns=@JoinColumn(name="project_id"),
	/**
     * Gets the notes.
     *
     * @return the notes
     */
	// inverseJoinColumns=@JoinColumn(name="person_id"))
	@XmlTransient
	public List<Note> getNotes() {
		return this.notes;
	}

	/**
     * Sets the notes.
     *
     * @param notes
     *            the new notes
     */
	public void setNotes(final List<Note> notes) {
		this.notes = notes;
	}

	/**
     * Gets the current iteration.
     *
     * @return the current iteration
     */
	@XmlTransient
	public Iteration getCurrentIteration() {
		return this.currentIteration;
	}

	/**
     * Sets the current iteration.
     *
     * @param currentIteration
     *            the new current iteration
     */
	public void setCurrentIteration(final Iteration currentIteration) {
		this.currentIteration = currentIteration;
	}

}
