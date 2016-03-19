/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.security.auth;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import net.sf.xplanner.domain.Person;

import com.technoetic.xplanner.security.AuthenticationException;

//DEBT duplicate with the Authorizer.getPeopleWithPermissionOnProject
/**
 * The Class PermissionHelper.
 */
//DEBT Move the query to get all people to be cached in PermissionCache (to be renamed SecurityCache) and replace all hard coded usage of the people query
public class PermissionHelper {
    
    /**
     * Gets the people with project role.
     *
     * @param projectId
     *            the project id
     * @param people
     *            the people
     * @return the people with project role
     * @throws AuthenticationException
     *             the authentication exception
     */
    public static Collection getPeopleWithProjectRole(final String projectId,
            final Collection people) throws AuthenticationException {
        final int projectOid = Integer.parseInt(projectId);
        final Collection peopleToShow = new HashSet();

        if (PermissionHelper.showFilterOnProject(projectOid)) {
            final Iterator i = people.iterator();
            while (i.hasNext()) {
                final Person p = (Person) i.next();
                if (PermissionHelper.isProjectAccessibleByPerson(projectOid, p)) {
                    peopleToShow.add(p);
                }
            }
        } else {
            peopleToShow.addAll(people);
        }
        return peopleToShow;
    }

    /**
     * Checks if is project accessible by person.
     *
     * @param projectOid
     *            the project oid
     * @param p
     *            the p
     * @return true, if is project accessible by person
     * @throws AuthenticationException
     *             the authentication exception
     */
    private static boolean isProjectAccessibleByPerson(final int projectOid,
            final Person p) throws AuthenticationException {
        return SystemAuthorizer.get().hasPermission(projectOid, p.getId(),
                "system.project", projectOid, "read%");
    }

    /**
     * Show filter on project.
     *
     * @param projectOid
     *            the project oid
     * @return true, if successful
     */
    private static boolean showFilterOnProject(final int projectOid) {
        return projectOid > 0;
    }
}
