package com.technoetic.xplanner.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Role;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

/**
 * The Class PersonEditorForm.
 */
public class PersonEditorForm extends AbstractEditorForm {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5950275086457397788L;

	/** The name. */
	private String name;
	
	/** The email. */
	private String email;
	
	/** The phone. */
	private String phone;
	
	/** The person id. */
	private int personId;
	
	/** The initials. */
	private String initials;
	
	/** The user id. */
	private String userId;
	
	/** The password. */
	private String password;
	
	/** The new password. */
	private String newPassword;
	
	/** The new password confirm. */
	private String newPasswordConfirm;
	
	/** The is hidden. */
	private boolean isHidden;
	
	/** The project ids. */
	private List<String> projectIds = new ArrayList<String>();
	
	/** The project roles. */
	private List<String> projectRoles = new ArrayList<String>();
	
	/** The is system admin. */
	private boolean isSystemAdmin;
	
	/** The Constant PASSWORD_MISMATCH_ERROR. */
	public static final String PASSWORD_MISMATCH_ERROR = "person.editor.password_mismatch";

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public ActionErrors validate(final ActionMapping mapping,
			final HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();
		if (this.isSubmitted()) {
			AbstractEditorForm.require(errors, this.name,
					"person.editor.missing_name");
			AbstractEditorForm.require(errors, this.userId,
					"person.editor.missing_user_id");
			AbstractEditorForm.require(errors, this.email,
					"person.editor.missing_email");
			AbstractEditorForm.require(errors, this.initials,
					"person.editor.missing_initials");
			if (StringUtils.isNotEmpty(this.newPassword)
					&& !StringUtils.equals(this.newPassword,
							this.newPasswordConfirm)) {
				AbstractEditorForm.error(errors,
						PersonEditorForm.PASSWORD_MISMATCH_ERROR);
			}
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
		this.email = null;
		this.phone = null;
		this.initials = null;
		this.userId = null;
		this.personId = 0;
		this.newPassword = null;
		this.newPasswordConfirm = null;
		this.isHidden = false;
		this.isSystemAdmin = false;
	}

	/**
     * Gets the container id.
     *
     * @return the container id
     */
	public String getContainerId() {
		return Integer.toString(this.personId);
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
     * Gets the name.
     *
     * @return the name
     */
	public String getName() {
		return this.name;
	}

	/**
     * Sets the email.
     *
     * @param email
     *            the new email
     */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
     * Gets the email.
     *
     * @return the email
     */
	public String getEmail() {
		return this.email;
	}

	/**
     * Sets the phone.
     *
     * @param phone
     *            the new phone
     */
	public void setPhone(final String phone) {
		this.phone = phone;
	}

	/**
     * Gets the phone.
     *
     * @return the phone
     */
	public String getPhone() {
		return this.phone;
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
     * Gets the person id.
     *
     * @return the person id
     */
	public int getPersonId() {
		return this.personId;
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
     * Gets the initials.
     *
     * @return the initials
     */
	public String getInitials() {
		return this.initials;
	}

	/**
     * Alias for userId so field won't be filled in automatically by Mozilla,
     * etc.
     *
     * @return the user identifier
     */
	public String getUserIdentifier() {
		return this.getUserId();
	}

	/**
     * Alias for userId so field won't be filled in automatically by Mozilla,
     * etc.
     *
     * @param userId
     *            the new user identifier
     */
	public void setUserIdentifier(final String userId) {
		this.setUserId(userId);
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
     * Gets the new password.
     *
     * @return the new password
     */
	public String getNewPassword() {
		return this.newPassword;
	}

	/**
     * Sets the new password.
     *
     * @param newPassword
     *            the new new password
     */
	public void setNewPassword(final String newPassword) {
		this.newPassword = newPassword;
	}

	/**
     * Gets the new password confirm.
     *
     * @return the new password confirm
     */
	public String getNewPasswordConfirm() {
		return this.newPasswordConfirm;
	}

	/**
     * Sets the new password confirm.
     *
     * @param newPasswordConfirm
     *            the new new password confirm
     */
	public void setNewPasswordConfirm(final String newPasswordConfirm) {
		this.newPasswordConfirm = newPasswordConfirm;
	}

	/**
     * Gets the password.
     *
     * @return the password
     */
	public String getPassword() {
		return this.password;
	}

	/**
     * Sets the password.
     *
     * @param password
     *            the new password
     */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
     * Checks if is hidden.
     *
     * @return true, if is hidden
     */
	public boolean isHidden() {
		return this.isHidden;
	}

	/**
     * Sets the hidden.
     *
     * @param hidden
     *            the new hidden
     */
	public void setHidden(final boolean hidden) {
		this.isHidden = hidden;
	}

	/**
     * Sets the project id.
     *
     * @param index
     *            the index
     * @param projectId
     *            the project id
     */
	public void setProjectId(final int index, final String projectId) {
		AbstractEditorForm.ensureSize(this.projectIds, index + 1);
		this.projectIds.set(index, projectId);

	}

	/**
     * Gets the project id.
     *
     * @param index
     *            the index
     * @return the project id
     */
	public String getProjectId(final int index) {
		return this.projectIds.get(index);
	}

	/**
     * Gets the project id as int.
     *
     * @param index
     *            the index
     * @return the project id as int
     */
	public int getProjectIdAsInt(final int index) {
		return Integer.parseInt(this.getProjectId(index));
	}

	/**
     * Gets the project count.
     *
     * @return the project count
     */
	public int getProjectCount() {
		return this.projectIds.size();
	}

	/**
     * Sets the project role.
     *
     * @param index
     *            the index
     * @param role
     *            the role
     */
	public void setProjectRole(final int index, final String role) {
		AbstractEditorForm.ensureSize(this.projectRoles, index + 1);
		this.projectRoles.set(index, role);
	}

	/**
     * Gets the project role.
     *
     * @param index
     *            the index
     * @return the project role
     */
	public String getProjectRole(final int index) {
		return this.projectRoles.get(index);
	}

	/**
     * Sets the system admin.
     *
     * @param isSystemAdmin
     *            the new system admin
     */
	public void setSystemAdmin(final boolean isSystemAdmin) {
		this.isSystemAdmin = isSystemAdmin;
	}

	/**
     * Checks if is system admin.
     *
     * @return true, if is system admin
     */
	public boolean isSystemAdmin() {
		return this.isSystemAdmin;
	}

	// These helper functions should be refactored into the domain objects

	/**
     * Checks for role.
     *
     * @param roles
     *            the roles
     * @param name
     *            the name
     * @return true, if successful
     */
	private boolean hasRole(final Collection<Role> roles, final String name) {
		for (final Iterator<Role> iterator = roles.iterator(); iterator
				.hasNext();) {
			final Role role = iterator.next();
			if (role.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	// This is for 0.6. In the future there will be a more general role editing
	// and
	// management framework.
	// DEBT remove all references to roles title. This hierarchy should be
	/**
     * Gets the effective role.
     *
     * @param roles
     *            the roles
     * @return the effective role
     */
	// captured declaratively somewhere.
	private String getEffectiveRole(final Collection<Role> roles) {
		if (this.hasRole(roles, "admin")) {
			return "admin";
		} else if (this.hasRole(roles, "editor")) {
			return "editor";
		} else if (this.hasRole(roles, "viewer")) {
			return "viewer";
		} else {
			return "none";
		}
	}

	/**
     * Checks if is role selected.
     *
     * @param role
     *            the role
     * @param projectId
     *            the project id
     * @return the string
     * @throws AuthenticationException
     *             the authentication exception
     */
	public String isRoleSelected(final String role, final int projectId)
			throws AuthenticationException {
		return this.getEffectiveRole(this.getRoles(projectId)).equals(role) ? "selected='selected'"
				: "";
	}

	/**
     * Gets the roles.
     *
     * @param projectId
     *            the project id
     * @return the roles
     * @throws AuthenticationException
     *             the authentication exception
     */
	public Collection<Role> getRoles(final int projectId)
			throws AuthenticationException {
		return SystemAuthorizer.get().getRolesForPrincipalOnProject(
				this.getId(), projectId, false);
	}

	/**
     * Gets the project ids.
     *
     * @return the project ids
     */
	public List<String> getProjectIds() {
		return this.projectIds;
	}

	/**
     * Sets the project ids.
     *
     * @param projectIds
     *            the new project ids
     */
	public void setProjectIds(final List<String> projectIds) {
		this.projectIds = projectIds;
	}

	/**
     * Gets the project roles.
     *
     * @return the project roles
     */
	public List<String> getProjectRoles() {
		return this.projectRoles;
	}

	/**
     * Sets the project roles.
     *
     * @param projectRoles
     *            the new project roles
     */
	public void setProjectRoles(final List<String> projectRoles) {
		this.projectRoles = projectRoles;
	}

	/**
     * Checks if is sys admin.
     *
     * @return true, if is sys admin
     * @throws AuthenticationException
     *             the authentication exception
     */
	public boolean isSysAdmin() throws AuthenticationException {
		return CollectionUtils.find(this.getRoles(0), new Predicate() {
			@Override
			public boolean evaluate(final Object o) {
				return ((Role) o).getName().equals("sysadmin");
			}
		}) != null;
	}
}