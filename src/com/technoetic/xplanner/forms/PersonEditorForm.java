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

public class PersonEditorForm extends AbstractEditorForm {
	private static final long serialVersionUID = -5950275086457397788L;

	private String name;
	private String email;
	private String phone;
	private int personId;
	private String initials;
	private String userId;
	private String password;
	private String newPassword;
	private String newPasswordConfirm;
	private boolean isHidden;
	private List<String> projectIds = new ArrayList<String>();
	private List<String> projectRoles = new ArrayList<String>();
	private boolean isSystemAdmin;
	public static final String PASSWORD_MISMATCH_ERROR = "person.editor.password_mismatch";

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

	public String getContainerId() {
		return Integer.toString(this.personId);
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPersonId(final int personId) {
		this.personId = personId;
	}

	public int getPersonId() {
		return this.personId;
	}

	public void setInitials(final String initials) {
		this.initials = initials;
	}

	public String getInitials() {
		return this.initials;
	}

	/**
	 * Alias for userId so field won't be filled in automatically by Mozilla,
	 * etc.
	 */
	public String getUserIdentifier() {
		return this.getUserId();
	}

	/**
	 * Alias for userId so field won't be filled in automatically by Mozilla,
	 * etc.
	 */
	public void setUserIdentifier(final String userId) {
		this.setUserId(userId);
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public String getNewPassword() {
		return this.newPassword;
	}

	public void setNewPassword(final String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordConfirm() {
		return this.newPasswordConfirm;
	}

	public void setNewPasswordConfirm(final String newPasswordConfirm) {
		this.newPasswordConfirm = newPasswordConfirm;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public boolean isHidden() {
		return this.isHidden;
	}

	public void setHidden(final boolean hidden) {
		this.isHidden = hidden;
	}

	public void setProjectId(final int index, final String projectId) {
		AbstractEditorForm.ensureSize(this.projectIds, index + 1);
		this.projectIds.set(index, projectId);

	}

	public String getProjectId(final int index) {
		return this.projectIds.get(index);
	}

	public int getProjectIdAsInt(final int index) {
		return Integer.parseInt(this.getProjectId(index));
	}

	public int getProjectCount() {
		return this.projectIds.size();
	}

	public void setProjectRole(final int index, final String role) {
		AbstractEditorForm.ensureSize(this.projectRoles, index + 1);
		this.projectRoles.set(index, role);
	}

	public String getProjectRole(final int index) {
		return this.projectRoles.get(index);
	}

	public void setSystemAdmin(final boolean isSystemAdmin) {
		this.isSystemAdmin = isSystemAdmin;
	}

	public boolean isSystemAdmin() {
		return this.isSystemAdmin;
	}

	// These helper functions should be refactored into the domain objects

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

	public String isRoleSelected(final String role, final int projectId)
			throws AuthenticationException {
		return this.getEffectiveRole(this.getRoles(projectId)).equals(role) ? "selected='selected'"
				: "";
	}

	public Collection<Role> getRoles(final int projectId)
			throws AuthenticationException {
		return SystemAuthorizer.get().getRolesForPrincipalOnProject(
				this.getId(), projectId, false);
	}

	public List<String> getProjectIds() {
		return this.projectIds;
	}

	public void setProjectIds(final List<String> projectIds) {
		this.projectIds = projectIds;
	}

	public List<String> getProjectRoles() {
		return this.projectRoles;
	}

	public void setProjectRoles(final List<String> projectRoles) {
		this.projectRoles = projectRoles;
	}

	public boolean isSysAdmin() throws AuthenticationException {
		return CollectionUtils.find(this.getRoles(0), new Predicate() {
			@Override
			public boolean evaluate(final Object o) {
				return ((Role) o).getName().equals("sysadmin");
			}
		}) != null;
	}
}