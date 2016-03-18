package com.technoetic.xplanner.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.PersonRole;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Role;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;

import com.technoetic.xplanner.forms.RoleEditorForm;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

public class EditRoleAction extends EditObjectAction {
	public static final String SYSADMIN_ROLE_NAME = "sysadmin";

	protected void beforeObjectCommit(final Object object,
			final Session session, final ActionMapping actionMapping,
			final ActionForm actionForm, final HttpServletRequest request,
			final HttpServletResponse reply) throws Exception {

		final RoleEditorForm roleForm = (RoleEditorForm) actionForm;

		final Project project = (Project) object;

		final int projectId = project.getId();

		for (int index = 0; index < roleForm.getPersonCount(); index++) {
			final int personId = roleForm.getPersonIdAsInt(index);

			if (this.isAuthorizedRoleAdministratorForProject(request, projectId)) {

				this.deleteRoleAssociationsForProject(session, projectId,
						personId);
				this.addRoleAssociationForProject(session, projectId, personId,
						roleForm.getPersonRole(index));
			}
		}
	}

	private boolean isAuthorizedRoleAdministratorForProject(
			final HttpServletRequest request, final int projectId)
			throws AuthenticationException {
		return SystemAuthorizer.get().hasPermission(projectId,
				SecurityHelper.getRemoteUserId(request), "system.project",
				projectId, "admin.edit.role");
	}

	private void addRoleAssociationForProject(final Session session,
			final int projectId, final int personId, final String roleName)
			throws HibernateException {
		final Role role = this.getRoleByName(session, roleName);
		if (role != null) {
			session.save(new PersonRole(projectId, personId, role.getId()));
		}
	}

	private void deleteRoleAssociationsForProject(final Session session,
			final int projectId, final int personId) throws HibernateException {
		session.delete("from assoc in " + PersonRole.class
				+ " where assoc.id.personId = ? and assoc.id.projectId = ?",
				new Object[] { new Integer(personId), new Integer(projectId) },
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
	}

	private Role getRoleByName(final Session session, final String roleName)
			throws HibernateException {
		final List role = session.find("from role in " + Role.class
				+ " where role.name = ?", roleName, Hibernate.STRING);
		if (role.size() > 0) {
			return (Role) role.get(0);
		}
		return null;
	}

}
