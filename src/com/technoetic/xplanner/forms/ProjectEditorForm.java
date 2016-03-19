package com.technoetic.xplanner.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.actions.UpdateTimeNotificationReceivers;

/**
 * The Class ProjectEditorForm.
 */
public class ProjectEditorForm extends AbstractEditorForm {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1575902032643150244L;
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/** The hidden. */
	private boolean hidden;
	
	/** The sendemail. */
	private boolean sendemail;
	
	/** The opt escape brackets. */
	private boolean optEscapeBrackets;
	
	/** The wiki url. */
	private String wikiUrl;
	
	/** The person to delete. */
	private String personToDelete;
	
	/** The person to add id. */
	private String personToAddId;
	
	/** The people. */
	private final List<PersonInfo> people = new ArrayList<PersonInfo>();

	/**
     * Gets the wiki url.
     *
     * @return the wiki url
     */
	public String getWikiUrl() {
		return this.wikiUrl;
	}

	/**
     * Sets the wiki url.
     *
     * @param wikiUrl
     *            the new wiki url
     */
	public void setWikiUrl(final String wikiUrl) {
		this.wikiUrl = wikiUrl;
	}

	/**
     * Checks if is sending missing time entry reminder to acceptor.
     *
     * @return true, if is sending missing time entry reminder to acceptor
     */
	public boolean isSendingMissingTimeEntryReminderToAcceptor() {
		return this.sendemail;
	}

	/**
     * Sets the sendemail.
     *
     * @param sendemail
     *            the new sendemail
     */
	public void setSendemail(final boolean sendemail) {
		this.sendemail = sendemail;
	}

	/**
     * Checks if is opt escape brackets.
     *
     * @return true, if is opt escape brackets
     */
	public boolean isOptEscapeBrackets() {
		return this.optEscapeBrackets;
	}

	/**
     * Sets the opt escape brackets.
     *
     * @param optEscapeBrackets
     *            the new opt escape brackets
     */
	public void setOptEscapeBrackets(final boolean optEscapeBrackets) {
		this.optEscapeBrackets = optEscapeBrackets;
	}

	/**
     * Gets the container id.
     *
     * @return the container id
     */
	public String getContainerId() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
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

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.forms.AbstractEditorForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
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

	/**
     * Reset.
     */
	public void reset() {

	}

	/**
     * Gets the name.
     *
     * @return the name
     */
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
     * Sets the description.
     *
     * @param description
     *            the new description
     */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
     * Gets the description.
     *
     * @return the description
     */
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
     * @param hidden
     *            the new hidden
     */
	public void setHidden(final boolean hidden) {
		this.hidden = hidden;
	}

	/**
     * Gets the person to delete.
     *
     * @return the person to delete
     */
	public String getPersonToDelete() {
		return this.personToDelete;
	}

	/**
     * Sets the person to delete.
     *
     * @param personToDelete
     *            the new person to delete
     */
	public void setPersonToDelete(final String personToDelete) {
		this.personToDelete = personToDelete;
	}

	/**
     * Gets the person to add id.
     *
     * @return the person to add id
     */
	public String getPersonToAddId() {
		return this.personToAddId;
	}

	/**
     * Sets the person to add id.
     *
     * @param personToAddId
     *            the new person to add id
     */
	public void setPersonToAddId(final String personToAddId) {
		this.personToAddId = personToAddId;
	}

	/**
     * Gets the people.
     *
     * @return the people
     */
	public List<PersonInfo> getPeople() {
		return this.people;
	}

	/**
     * Adds the person info.
     *
     * @param id
     *            the id
     * @param userId
     *            the user id
     * @param initials
     *            the initials
     * @param personName
     *            the person name
     */
	public void addPersonInfo(final String id, final String userId,
			final String initials, final String personName) {
		final PersonInfo personInfo = this.new PersonInfo();
		personInfo.setId(id);
		personInfo.setUserId(userId);
		personInfo.setName(personName);
		personInfo.setInitials(initials);
		this.people.add(personInfo);
	}

	/**
     * Removes the person info.
     *
     * @param rowNbr
     *            the row nbr
     */
	public void removePersonInfo(final int rowNbr) {
		this.people.remove(rowNbr);
	}

	/**
     * The Class PersonInfo.
     */
	public class PersonInfo implements Serializable {
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -5782893977075679811L;
		
		/** The id. */
		private String id;
		
		/** The user id. */
		private String userId;
		
		/** The initials. */
		private String initials;
		
		/** The person name. */
		private String personName;

		/**
         * Gets the id.
         *
         * @return the id
         */
		public String getId() {
			return this.id;
		}

		/**
         * Sets the id.
         *
         * @param id
         *            the new id
         */
		public void setId(final String id) {
			this.id = id;
		}

		/**
         * Gets the user id.
         *
         * @return the user id
         */
		public String getUserId() {
			return this.userId;
		}

		/**
         * Sets the user id.
         *
         * @param userId
         *            the new user id
         */
		public void setUserId(final String userId) {
			this.userId = userId;
		}

		/**
         * Gets the initials.
         *
         * @return the initials
         */
		public String getInitials() {
			return this.initials;
		}

		/**
         * Sets the initials.
         *
         * @param initials
         *            the new initials
         */
		public void setInitials(final String initials) {
			this.initials = initials;
		}

		/**
         * Gets the name.
         *
         * @return the name
         */
		public String getName() {
			return this.personName;
		}

		/**
         * Sets the name.
         *
         * @param name
         *            the new name
         */
		public void setName(final String name) {
			this.personName = name;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			if (this.id == null) {
				throw new RuntimeException("Person Info id is missing");
			}
			return this.id.hashCode();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(final Object obj) {
			if (!(obj instanceof PersonInfo)) {
				return false;
			}
			return this.id.equals(((PersonInfo) obj).getId());
		}
	}

}