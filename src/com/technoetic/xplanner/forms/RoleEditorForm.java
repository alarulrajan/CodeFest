package com.technoetic.xplanner.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Role;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

public class RoleEditorForm extends AbstractEditorForm {
	private final ArrayList personIds = new ArrayList();
	private final ArrayList personRoles = new ArrayList();

	@Override
	public ActionErrors validate(final ActionMapping mapping,
			final HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();
		return errors;
	}

	@Override
	public void reset(final ActionMapping mapping,
			final HttpServletRequest request) {
		super.reset(mapping, request);
	}

	public void reset() {

	}

	public int getPersonCount() {
		return this.personIds.size();
	}

	public String getPersonId(final int index) {
		return (String) this.personIds.get(index);
	}

	public int getPersonIdAsInt(final int index) {
		if (this.getPersonId(index) == null) {
			return -1;
		} else {
			return Integer.parseInt(this.getPersonId(index));
		}
	}

	public void setPersonId(final int index, final String personId) {
		AbstractEditorForm.ensureSize(this.personIds, index + 1);
		this.personIds.set(index, personId);

	}

	/* D�finir le r�le d'une personne */
	public void setPersonRole(final int index, final String role) {
		AbstractEditorForm.ensureSize(this.personRoles, index + 1);
		this.personRoles.set(index, role);
	}

	/* Renvoi du r�le d'une personne */
	public String getPersonRole(final int index) {
		return (String) this.personRoles.get(index);
	}

	private boolean hasRole(final Collection roles, final String name) {
		for (final Iterator iterator = roles.iterator(); iterator.hasNext();) {
			final Role role = (Role) iterator.next();
			if (role.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	private String getEffectiveRole(final Collection roles) {
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

	public String isRoleSelected(final String role, final int personId)
			throws AuthenticationException {
		return this.getEffectiveRole(this.getRoles(personId)).equals(role) ? "selected='selected'"
				: "";
	}

	public Collection getRoles(final int personId)
			throws AuthenticationException {
		return SystemAuthorizer.get().getRolesForPrincipalOnProject(personId,
				this.getId(), false);
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
