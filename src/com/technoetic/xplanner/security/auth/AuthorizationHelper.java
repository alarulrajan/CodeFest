package com.technoetic.xplanner.security.auth;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.tags.PageHelper;

/**
 * User: Mateusz Prokopowicz Date: Feb 15, 2005 Time: 11:08:59 AM.
 */
public class AuthorizationHelper {

    /**
     * Checks for permission to any.
     *
     * @param permissionArray
     *            the permission array
     * @param objectCollection
     *            the object collection
     * @param request
     *            the request
     * @return true, if successful
     * @throws AuthenticationException
     *             the authentication exception
     */
    public static boolean hasPermissionToAny(final String[] permissionArray,
            final Collection objectCollection, final ServletRequest request)
            throws AuthenticationException {
        return AuthorizationHelper.hasPermissionToAny(permissionArray,
                objectCollection, request, 0);
    }

    /**
     * Checks for permission to any.
     *
     * @param permissionArray
     *            the permission array
     * @param objectCollection
     *            the object collection
     * @param request
     *            the request
     * @param projectId
     *            the project id
     * @return true, if successful
     * @throws AuthenticationException
     *             the authentication exception
     */
    public static boolean hasPermissionToAny(final String[] permissionArray,
            final Collection objectCollection, final ServletRequest request,
            int projectId) throws AuthenticationException {
        boolean isAuthorized = false;
        final int remoteUserId = SecurityHelper
                .getRemoteUserId((HttpServletRequest) request);
        for (final Iterator iterator = objectCollection.iterator(); !isAuthorized
                && iterator.hasNext();) {
            final Object resource = iterator.next();
            projectId = PageHelper.getProjectId(resource, request);
            for (int i = 0; i < permissionArray.length; i++) {
                final String permission = permissionArray[i];
                if (SystemAuthorizer.get().hasPermission(projectId,
                        remoteUserId, resource, permission)) {
                    isAuthorized = true;
                    break;
                }
            }
        }
        return isAuthorized;
    }

    /**
     * Checks for permission.
     *
     * @param projectId
     *            the project id
     * @param principalId
     *            the principal id
     * @param resourceId
     *            the resource id
     * @param resourceType
     *            the resource type
     * @param permission
     *            the permission
     * @param resource
     *            the resource
     * @param request
     *            the request
     * @return true, if successful
     * @throws AuthenticationException
     *             the authentication exception
     */
    public static boolean hasPermission(final int projectId, int principalId,
            final int resourceId, final String resourceType,
            final String permission, final Object resource,
            final ServletRequest request) throws AuthenticationException {
        boolean hasPermission;
        if (principalId == 0) {
            principalId = SecurityHelper
                    .getRemoteUserId((HttpServletRequest) request);
        }
        if (resourceType != null) {
            hasPermission = !SystemAuthorizer.get().hasPermission(projectId,
                    principalId, resourceType, resourceId, permission);
        } else {
            hasPermission = !SystemAuthorizer.get().hasPermission(projectId,
                    principalId, resource, permission);
        }
        return hasPermission;
    }
}
