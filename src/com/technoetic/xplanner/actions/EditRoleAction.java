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

/**
 * The Class EditRoleAction.
 */
public class EditRoleAction extends EditObjectAction {
	
	/** The Constant SYSADMIN_ROLE_NAME. */
	public static final String SYSADMIN_ROLE_NAME = "sysadmin";

	/**
     * Before object commit.
     *
     * @param object
     *            the object
     * @param session
     *            the session
     * @param actionMapping
     *            the action mapping
     * @param actionForm
     *            the action form
     * @param request
     *            the request
     * @param reply
     *            the reply
     * @throws Exception
     *             the exception
     */
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

	/**
     * Checks if is authorized role administrator for project.
     *
     * @param request
     *            the request
     * @param projectId
     *            the project id
     * @return true, if is authorized role administrator for project
     * @throws AuthenticationException
     *             the authentication exception
     */
	private boolean isAuthorizedRoleAdministratorForProject(
			final HttpServletRequest request, final int projectId)
			throws AuthenticationException {
		return SystemAuthorizer.get().hasPermission(projectId,
				SecurityHelper.getRemoteUserId(request), "system.project",
				projectId, "admin.edit.role");
	}

	/**
     * Adds the role association for project.
     *
     * @param session
     *            the session
     * @param projectId
     *            the project id
     * @param personId
     *            the person id
     * @param roleName
     *            the role name
     * @throws HibernateException
     *             the hibernate exception
     */
	private void addRoleAssociationForProject(final Session session,
			final int projectId, final int personId, final String roleName)
			throws HibernateException {
		final Role role = this.getRoleByName(session, roleName);
		if (role != null) {
			session.save(new PersonRole(projectId, personId, role.getId()));
		}
	}

	/**
     * Delete role associations for project.
     *
     * @param session
     *            the session
     * @param projectId
     *            the project id
     * @param personId
     *            the person id
     * @throws HibernateException
     *             the hibernate exception
     */
	private void deleteRoleAssociationsForProject(final Session session,
			final int projectId, final int personId) throws HibernateException {
		session.delete("from assoc in " + PersonRole.class
				+ " where assoc.id.personId = ? and assoc.id.projectId = ?",
				new Object[] { new Integer(personId), new Integer(projectId) },
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
	}

	/**
     * Gets the role by name.
     *
     * @param session
     *            the session
     * @param roleName
     *            the role name
     * @return the role by name
     * @throws HibernateException
     *             the hibernate exception
     */
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
