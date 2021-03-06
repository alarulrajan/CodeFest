/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.security.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.domain.Permission;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * User: mprokopowicz Date: Mar 29, 2006 Time: 6:47:12 PM.
 */
public class PrincipalSpecificPermissionHelper {
    
    /** The authorizer query helper. */
    private AuthorizerQueryHelper authorizerQueryHelper;

    /**
     * Sets the authorizer query helper.
     *
     * @param authorizerQueryHelper
     *            the new authorizer query helper
     */
    public void setAuthorizerQueryHelper(
            final AuthorizerQueryHelper authorizerQueryHelper) {
        this.authorizerQueryHelper = authorizerQueryHelper;
    }

    /**
     * Gets the permissions for principal.
     *
     * @param principalId
     *            the principal id
     * @return the permissions for principal
     */
    public Map<Integer, List<Permission>> getPermissionsForPrincipal(
            final int principalId) {
        final Map<Integer, List<Permission>> map = new HashMap<Integer, List<Permission>>();
        this.addProjectsPermissions(principalId, map);
        this.addPersonPermissions(principalId, map);
        return map;
    }

    /**
     * Gets the permissions specific to person.
     *
     * @param principalId
     *            the principal id
     * @return the permissions specific to person
     */
    public Collection getPermissionsSpecificToPerson(final int principalId) {
        final Collection personPermissionsCol = this.authorizerQueryHelper
                .getAllPermissionsToPerson();
        return CollectionUtils.select(personPermissionsCol, new PersonFilter(
                principalId) {
            @Override
            protected boolean isEqual(final Integer actualId) {
                return actualId.intValue() == 0
                        || actualId.intValue() == this.id;
            }
        });
    }

    /**
     * Gets the permissions based on person roles.
     *
     * @param principalId
     *            the principal id
     * @return the permissions based on person roles
     */
    public Collection getPermissionsBasedOnPersonRoles(final int principalId) {
        return CollectionUtils.select(
                this.authorizerQueryHelper.getAllPermissions(),
                new PersonFilter(principalId));
    }

    /**
     * Adds the person permissions.
     *
     * @param principalId
     *            the principal id
     * @param map
     *            the map
     */
    void addPersonPermissions(final int principalId,
            final Map<Integer, List<Permission>> map) {
        final List<List<Permission>> permissionsLists = new ArrayList<List<Permission>>(
                map.values());
        for (final Iterator<List<Permission>> it = permissionsLists.iterator(); it
                .hasNext();) {
            final List<Permission> list = new ArrayList<Permission>(it.next());
            for (final Iterator<Permission> itPerm = list.iterator(); itPerm
                    .hasNext();) {
                final Permission p = itPerm.next();
                if (p.getResourceType().equals("%")
                        || p.getResourceType().equals("system.person")) {
                    final Permission personPermission = new Permission(
                            "system.person", p.getResourceId(),
                            p.getPrincipal(), p.getName());
                    this.addPermissionForProject(map, 0, personPermission);
                }
            }
        }
        final List permissions = (List) this
                .getPermissionsSpecificToPerson(principalId);
        for (int i = 0; i < permissions.size(); i++) {
            final Object permission = ((Object[]) permissions.get(i))[1];
            this.addPermissionForProject(map, 0, (Permission) permission);
        }
    }

    /**
     * Adds the permission for project.
     *
     * @param permissionMap
     *            the permission map
     * @param projectId
     *            the project id
     * @param permission
     *            the permission
     */
    void addPermissionForProject(
            final Map<Integer, List<Permission>> permissionMap,
            final int projectId, final Permission permission) {
        final Integer projectKey = new Integer(projectId);
        List<Permission> permissions = permissionMap.get(projectKey);
        if (permissions == null) {
            permissions = new ArrayList<Permission>();
            permissionMap.put(projectKey, permissions);
        }
        permissions.add(permission);
    }

    /**
     * Adds the projects permissions.
     *
     * @param principalId
     *            the principal id
     * @param map
     *            the map
     */
    private void addProjectsPermissions(final int principalId,
            final Map<Integer, List<Permission>> map) {

        final List permissions = (List) this
                .getPermissionsBasedOnPersonRoles(principalId);
        for (int i = 0; i < permissions.size(); i++) {
            final Object[] result = (Object[]) permissions.get(i);
            this.addPermissionForProject(map, ((Integer) result[1]).intValue(),
                    (Permission) result[2]);
        }
    }

    /**
     * The Class PersonFilter.
     */
    protected class PersonFilter implements Predicate {
        
        /** The id. */
        int id;

        /**
         * Instantiates a new person filter.
         *
         * @param id
         *            the id
         */
        public PersonFilter(final int id) {
            this.id = id;
        }

        /* (non-Javadoc)
         * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
         */
        @Override
        public boolean evaluate(final Object obj) {
            final Object[] row = (Object[]) obj;
            final Integer actualId = (Integer) row[0];
            return this.isEqual(actualId);
        }

        /**
         * Checks if is equal.
         *
         * @param actualId
         *            the actual id
         * @return true, if is equal
         */
        protected boolean isEqual(final Integer actualId) {
            return actualId.intValue() == this.id;
        }
    }
}
