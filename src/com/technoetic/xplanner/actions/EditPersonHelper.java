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
 * User: Mateusz Prokopowicz Date: Dec 9, 2004 Time: 11:03:47 AM
 */
public class EditPersonHelper {
	private static Logger log = Logger.getLogger(EditPersonHelper.class);
	public static final String SYSADMIN_ROLE_NAME = "sysadmin";
	private static final int ANY_PROJECT_ID = 0;
	private RoleAssociationRepository roleAssociationRepository;
	private Authorizer authorizer;

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

	public void setRoleAssociationRepository(
			final RoleAssociationRepository roleAssociationRepository) {
		this.roleAssociationRepository = roleAssociationRepository;
	}

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

	void setSysadmin(final Person person) throws RepositoryException {
		this.addRoleAssociationForProject(EditPersonHelper.ANY_PROJECT_ID,
				person.getId(), EditPersonHelper.SYSADMIN_ROLE_NAME);
	}

	public void setRoleOnProject(final int projectId, final Person person,
			final String role) throws RepositoryException {
		this.deleteRoleAssociationsForProject(projectId, person.getId());
		this.addRoleAssociationForProject(projectId, person.getId(), role);
	}

	private boolean isCurrentUserAdminOfProject(final int projectId,
			final int remoteUserId) throws AuthenticationException {
		return this.authorizer.hasPermission(projectId, remoteUserId,
				"system.project", projectId, "admin.edit.role");
	}

	private void addRoleAssociationForProject(final int projectId,
			final int personId, final String roleName)
			throws RepositoryException {
		this.roleAssociationRepository.insertForPersonOnProject(roleName,
				personId, projectId);
	}

	private void deleteRoleAssociationsForProject(final int projectId,
			final int personId) throws RepositoryException {
		this.roleAssociationRepository.deleteAllForPersonOnProject(personId,
				projectId);
	}

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
