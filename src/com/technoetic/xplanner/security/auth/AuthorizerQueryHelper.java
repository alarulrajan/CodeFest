/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.security.auth;

import java.util.Collection;
import java.util.List;

import net.sf.xplanner.domain.Project;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * User: mprokopowicz Date: Mar 29, 2006 Time: 5:36:07 PM.
 */
public class AuthorizerQueryHelper extends HibernateDaoSupport {

    /**
     * Gets the all permissions.
     *
     * @return the all permissions
     */
    public Collection getAllPermissions() {
        final Collection permission = this.getHibernateTemplate()
                .findByNamedQuery("security.personal.permissions");
        permission.addAll(this.getHibernateTemplate().findByNamedQuery(
                "security.role.permissions"));
        return permission;
    }

    /**
     * Gets the all permissions to person.
     *
     * @return the all permissions to person
     */
    public Collection getAllPermissionsToPerson() {
        return this.getHibernateTemplate().findByNamedQuery(
                "security.person.permissions");
    }

    /**
     * Gets the roles for principal on project.
     *
     * @param principalId
     *            the principal id
     * @param projectId
     *            the project id
     * @param includeWildcardProject
     *            the include wildcard project
     * @return the roles for principal on project
     */
    public Collection getRolesForPrincipalOnProject(final int principalId,
            final int projectId, final boolean includeWildcardProject) {
        return this.getHibernateTemplate().findByNamedQueryAndNamedParam(
                "security.roles",
                new String[] { "personId", "projectId",
                        "includeWildcardProject" },
                new Object[] { new Integer(principalId),
                        new Integer(projectId),
                        new Integer(includeWildcardProject ? 1 : 0) });
    }

    /**
     * Gets the all unhiden projects.
     *
     * @return the all unhiden projects
     */
    public List getAllUnhidenProjects() {
        return this.getHibernateTemplate().find(
                "from project in " + Project.class
                        + " where project.hidden = false");
    }
}
