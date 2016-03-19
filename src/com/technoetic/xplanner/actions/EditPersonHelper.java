/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.actions;

import java.util.Iterator;
import java.util.Map;

import net.sf.xplanner.domain.Person;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.domain.repository.RoleAssociationRepository;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.LoginModule;
import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

/**
 * User: Mateusz Prokopowicz Date: Dec 9, 2004 Time: 11:03:47 AM.
 */
public class EditPersonHelper {
	
	/** The log. */
	private static Logger log = Logger.getLogger(EditPersonHelper.class);
	
	/** The Constant SYSADMIN_ROLE_NAME. */
	public static final String SYSADMIN_ROLE_NAME = "sysadmin";
	
	/** The Constant ANY_PROJECT_ID. */
	private static final int ANY_PROJECT_ID = 0;
	
	/** The role association repository. */
	private RoleAssociationRepository roleAssociationRepository;
	
	/** The authorizer. */
	private Authorizer authorizer;

	/**
     * Sets the authorizer.
     *
     * @param authorizer
     *            the new authorizer
     */
	public void setAuthorizer(final Authorizer authorizer) {
		final Authorizer systemAuthorizer = SystemAuthorizer.get();
		if (authorizer != systemAuthorizer) {
			EditPersonHelper.log
					.warn("Which authorizer do you want me to use? "
							+ authorizer + " or what SystemAuthorizer has, "
							+ systemAuthorizer + "?????");
		}

		this.authorizer = authorizer;
	}

	/**
     * Sets the role association repository.
     *
     * @param roleAssociationRepository
     *            the new role association repository
     */
	public void setRoleAssociationRepository(
			final RoleAssociationRepository roleAssociationRepository) {
		this.roleAssociationRepository = roleAssociationRepository;
	}

	/**
     * Modify roles.
     *
     * @param projectRoleMap
     *            the project role map
     * @param person
     *            the person
     * @param isSystemAdmin
     *            the is system admin
     * @param remoteUserId
     *            the remote user id
     * @throws AuthenticationException
     *             the authentication exception
     * @throws RepositoryException
     *             the repository exception
     */
	public void modifyRoles(final Map<String, String> projectRoleMap,
			final Person person, final boolean isSystemAdmin,
			final int remoteUserId) throws AuthenticationException,
			RepositoryException {
		for (final Iterator<String> iterator = projectRoleMap.keySet()
				.iterator(); iterator.hasNext();) {
			final String projectId = iterator.next();
			final String role = projectRoleMap.get(projectId);
			if (this.isCurrentUserAdminOfProject(Integer.parseInt(projectId),
					remoteUserId)) {
				this.setRoleOnProject(Integer.parseInt(projectId), person, role);
			}
		}
		if (this.isCurrentUserAdminOfProject(EditPersonHelper.ANY_PROJECT_ID,
				remoteUserId)) {
			this.roleAssociationRepository.deleteForPersonOnProject(
					EditPersonHelper.SYSADMIN_ROLE_NAME, person.getId(),
					EditPersonHelper.ANY_PROJECT_ID);
			if (isSystemAdmin) {
				this.setSysadmin(person);
			}
		}
	}

	/**
     * Sets the sysadmin.
     *
     * @param person
     *            the new sysadmin
     * @throws RepositoryException
     *             the repository exception
     */
	void setSysadmin(final Person person) throws RepositoryException {
		this.addRoleAssociationForProject(EditPersonHelper.ANY_PROJECT_ID,
				person.getId(), EditPersonHelper.SYSADMIN_ROLE_NAME);
	}

	/**
     * Sets the role on project.
     *
     * @param projectId
     *            the project id
     * @param person
     *            the person
     * @param role
     *            the role
     * @throws RepositoryException
     *             the repository exception
     */
	public void setRoleOnProject(final int projectId, final Person person,
			final String role) throws RepositoryException {
		this.deleteRoleAssociationsForProject(projectId, person.getId());
		this.addRoleAssociationForProject(projectId, person.getId(), role);
	}

	/**
     * Checks if is current user admin of project.
     *
     * @param projectId
     *            the project id
     * @param remoteUserId
     *            the remote user id
     * @return true, if is current user admin of project
     * @throws AuthenticationException
     *             the authentication exception
     */
	private boolean isCurrentUserAdminOfProject(final int projectId,
			final int remoteUserId) throws AuthenticationException {
		return this.authorizer.hasPermission(projectId, remoteUserId,
				"system.project", projectId, "admin.edit.role");
	}

	/**
     * Adds the role association for project.
     *
     * @param projectId
     *            the project id
     * @param personId
     *            the person id
     * @param roleName
     *            the role name
     * @throws RepositoryException
     *             the repository exception
     */
	private void addRoleAssociationForProject(final int projectId,
			final int personId, final String roleName)
			throws RepositoryException {
		this.roleAssociationRepository.insertForPersonOnProject(roleName,
				personId, projectId);
	}

	/**
     * Delete role associations for project.
     *
     * @param projectId
     *            the project id
     * @param personId
     *            the person id
     * @throws RepositoryException
     *             the repository exception
     */
	private void deleteRoleAssociationsForProject(final int projectId,
			final int personId) throws RepositoryException {
		this.roleAssociationRepository.deleteAllForPersonOnProject(personId,
				projectId);
	}

	/**
     * Change user password.
     *
     * @param newPassword
     *            the new password
     * @param userId
     *            the user id
     * @param loginModule
     *            the login module
     * @throws AuthenticationException
     *             the authentication exception
     */
	public void changeUserPassword(final String newPassword,
			final String userId, final LoginModule loginModule)
			throws AuthenticationException {
		if (StringUtils.isNotEmpty(newPassword)) {
			if (loginModule != null) {
				loginModule.changePassword(userId, newPassword);
			}
		}
	}

}
