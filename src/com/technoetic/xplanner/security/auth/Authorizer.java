package com.technoetic.xplanner.security.auth;

import java.util.Collection;
import java.util.List;

import com.technoetic.xplanner.security.AuthenticationException;

//DEBT Should throw AuthorizationException not AuthenticationException

/**
 * The Interface Authorizer.
 */
public interface Authorizer {
    
    /**
     * Checks for permission.
     *
     * @param projectId
     *            the project id
     * @param personId
     *            the person id
     * @param resourceType
     *            the resource type
     * @param resourceId
     *            the resource id
     * @param permission
     *            the permission
     * @return true, if successful
     * @throws AuthenticationException
     *             the authentication exception
     */
    boolean hasPermission(int projectId, int personId, String resourceType,
            int resourceId, String permission) throws AuthenticationException;

    /**
     * Checks for permission.
     *
     * @param projectId
     *            the project id
     * @param personId
     *            the person id
     * @param resource
     *            the resource
     * @param permission
     *            the permission
     * @return true, if successful
     * @throws AuthenticationException
     *             the authentication exception
     */
    boolean hasPermission(int projectId, int personId, Object resource,
            String permission) throws AuthenticationException;

    /**
     * Checks for permission for some project.
     *
     * @param personlId
     *            the personl id
     * @param resourceType
     *            the resource type
     * @param resourceId
     *            the resource id
     * @param permissions
     *            the permissions
     * @return true, if successful
     * @throws AuthenticationException
     *             the authentication exception
     */
    boolean hasPermissionForSomeProject(int personlId, String resourceType,
            int resourceId, String permissions) throws AuthenticationException;

    /**
     * Checks for permission for some project.
     *
     * @param projects
     *            the projects
     * @param personId
     *            the person id
     * @param resourceType
     *            the resource type
     * @param resourceId
     *            the resource id
     * @param permission
     *            the permission
     * @return true, if successful
     * @throws AuthenticationException
     *             the authentication exception
     */
    boolean hasPermissionForSomeProject(Collection projects, int personId,
            String resourceType, int resourceId, String permission)
            throws AuthenticationException;

    /**
     * Gets the people with permission on project.
     *
     * @param allPeople
     *            the all people
     * @param projectId
     *            the project id
     * @return the people with permission on project
     * @throws AuthenticationException
     *             the authentication exception
     */
    Collection getPeopleWithPermissionOnProject(List allPeople, int projectId)
            throws AuthenticationException;

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
     * @throws AuthenticationException
     *             the authentication exception
     */
    Collection getRolesForPrincipalOnProject(int principalId, int projectId,
            boolean includeWildcardProject) throws AuthenticationException;
}
