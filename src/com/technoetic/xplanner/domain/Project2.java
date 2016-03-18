package com.technoetic.xplanner.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Iteration;

import com.technoetic.xplanner.domain.repository.IterationRepository;

public class Project2 extends DomainObject implements Nameable, NoteAttachable,
		Describable {
	private String name;
	private Collection iterations = new HashSet();
	private Collection notificationReceivers = new TreeSet();
	private String description;
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

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Collection getIterations() {
		return this.iterations;
	}

	public void setIterations(final Collection iterations) {
		this.iterations = iterations;
	}

	public Iteration getCurrentIteration() {
		return IterationRepository.getCurrentIteration(this.getId());
	}

	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	public boolean isHidden() {
		return this.hidden;
	}

	public void setHidden(final boolean flag) {
		this.hidden = flag;
	}

	public Collection getNotificationReceivers() {
		return this.notificationReceivers;
	}

	public void setNotificationReceivers(final Collection notificationReceivers) {
		this.notificationReceivers = notificationReceivers;
	}

	@Override
	public String toString() {
		return "Project{" + "id='" + this.getId() + ", " + "name='" + this.name
				+ "'" + "}";
	}
}