package com.technoetic.xplanner.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.actions.UpdateTimeNotificationReceivers;

public class ProjectEditorForm extends AbstractEditorForm {
	private static final long serialVersionUID = -1575902032643150244L;
	private String name;
	private String description;
	private boolean hidden;
	private boolean sendemail;
	private boolean optEscapeBrackets;
	private String wikiUrl;
	private String personToDelete;
	private String personToAddId;
	private final List<PersonInfo> people = new ArrayList<PersonInfo>();

	public String getWikiUrl() {
		return this.wikiUrl;
	}

	public void setWikiUrl(final String wikiUrl) {
		this.wikiUrl = wikiUrl;
	}

	public boolean isSendingMissingTimeEntryReminderToAcceptor() {
		return this.sendemail;
	}

	public void setSendemail(final boolean sendemail) {
		this.sendemail = sendemail;
	}

	public boolean isOptEscapeBrackets() {
		return this.optEscapeBrackets;
	}

	public void setOptEscapeBrackets(final boolean optEscapeBrackets) {
		this.optEscapeBrackets = optEscapeBrackets;
	}

	public String getContainerId() {
		return null;
	}

	@Override
	public ActionErrors validate(final ActionMapping mapping,
			final HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();
		if (this.isSubmitted()
				&& !this.getAction()
						.equals(UpdateTimeNotificationReceivers.ADD)
				&& !this.getAction().equals(
						UpdateTimeNotificationReceivers.DELETE)) {
			AbstractEditorForm.require(errors, this.name,
					"project.editor.missing_name");
		}
		return errors;
	}

	@Override
	public void reset(final ActionMapping mapping,
			final HttpServletRequest request) {
		super.reset(mapping, request);
		this.name = null;
		this.description = null;
		this.wikiUrl = null;
		this.people.clear();
		this.personToDelete = null;

		this.hidden = false;
		this.sendemail = false;
		this.optEscapeBrackets = false;
	}

	public void reset() {

	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public boolean isHidden() {
		return this.hidden;
	}

	public void setHidden(final boolean hidden) {
		this.hidden = hidden;
	}

	public String getPersonToDelete() {
		return this.personToDelete;
	}

	public void setPersonToDelete(final String personToDelete) {
		this.personToDelete = personToDelete;
	}

	public String getPersonToAddId() {
		return this.personToAddId;
	}

	public void setPersonToAddId(final String personToAddId) {
		this.personToAddId = personToAddId;
	}

	public List<PersonInfo> getPeople() {
		return this.people;
	}

	public void addPersonInfo(final String id, final String userId,
			final String initials, final String personName) {
		final PersonInfo personInfo = this.new PersonInfo();
		personInfo.setId(id);
		personInfo.setUserId(userId);
		personInfo.setName(personName);
		personInfo.setInitials(initials);
		this.people.add(personInfo);
	}

	public void removePersonInfo(final int rowNbr) {
		this.people.remove(rowNbr);
	}

	public class PersonInfo implements Serializable {
		private static final long serialVersionUID = -5782893977075679811L;
		private String id;
		private String userId;
		private String initials;
		private String personName;

		public String getId() {
			return this.id;
		}

		public void setId(final String id) {
			this.id = id;
		}

		public String getUserId() {
			return this.userId;
		}

		public void setUserId(final String userId) {
			this.userId = userId;
		}

		public String getInitials() {
			return this.initials;
		}

		public void setInitials(final String initials) {
			this.initials = initials;
		}

		public String getName() {
			return this.personName;
		}

		public void setName(final String name) {
			this.personName = name;
		}

		@Override
		public int hashCode() {
			if (this.id == null) {
				throw new RuntimeException("Person Info id is missing");
			}
			return this.id.hashCode();
		}

		@Override
		public boolean equals(final Object obj) {
			if (!(obj instanceof PersonInfo)) {
				return false;
			}
			return this.id.equals(((PersonInfo) obj).getId());
		}
	}

}