package com.technoetic.xplanner.security.module;

import javax.security.auth.Subject;

import net.sf.xplanner.domain.Person;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.security.AuthenticationException;

/**
 * The Interface LoginSupport.
 */
public interface LoginSupport {
    
    /**
     * Populate subject principal from database.
     *
     * @param subject
     *            the subject
     * @param userId
     *            the user id
     * @return the person
     * @throws AuthenticationException
     *             the authentication exception
     */
    Person populateSubjectPrincipalFromDatabase(Subject subject, String userId)
            throws AuthenticationException;

    /**
     * Gets the person.
     *
     * @param userId
     *            the user id
     * @return the person
     * @throws HibernateException
     *             the hibernate exception
     */
    Person getPerson(String userId) throws HibernateException;

    /**
     * Creates the subject.
     *
     * @return the subject
     */
    Subject createSubject();
}
