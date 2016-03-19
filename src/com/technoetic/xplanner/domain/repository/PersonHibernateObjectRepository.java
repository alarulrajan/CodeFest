/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.domain.repository;

import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Permission;
import net.sf.xplanner.domain.Person;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.domain.Nameable;

/**
 * User: mprokopowicz Date: Feb 15, 2006 Time: 4:09:12 PM.
 */
public class PersonHibernateObjectRepository extends HibernateObjectRepository {
    
    /** The Constant CHECK_PERSON_UNIQUENESS_QUERY. */
    static final String CHECK_PERSON_UNIQUENESS_QUERY = "com.technoetic.xplanner.domain.CheckPersonUniquenessQuery";

    /**
     * Instantiates a new person hibernate object repository.
     *
     * @param objectClass
     *            the object class
     * @throws HibernateException
     *             the hibernate exception
     */
    public PersonHibernateObjectRepository(final Class objectClass)
            throws HibernateException {
        super(objectClass);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.HibernateObjectRepository#insert(com.technoetic.xplanner.domain.Nameable)
     */
    @Override
    public int insert(final Nameable domainObject) throws RepositoryException {
        final Person person = (Person) domainObject;
        this.checkPersonUniquness(person);
        final int id = super.insert(person);
        this.setUpEditPermission(person);
        return id;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.HibernateObjectRepository#delete(int)
     */
    @Override
    public void delete(final int objectIdentifier) throws RepositoryException {
        super.delete(objectIdentifier);
        this.getHibernateTemplate().bulkUpdate(
                "delete from " + Permission.class.getName()
                        + " where resource_id = ?",
                new Integer(objectIdentifier));
    }

    /**
     * Sets the up edit permission.
     *
     * @param person
     *            the new up edit permission
     */
    void setUpEditPermission(final DomainObject person) {
        this.getHibernateTemplate().save(
                new Permission("system.person", person.getId(), person.getId(),
                        "edit%"));
        // session.save(new
        // Permission("system.person",person.getId(),person.getId(),"read%"));
        this.getHibernateTemplate().save(
                new Permission("system.person", 0, person.getId(), "read%"));
        // roleAssociationRepository.insertForPersonOnProject("viewer",person.getId(),0);
    }

    /**
     * Check person uniquness.
     *
     * @param person
     *            the person
     * @throws DuplicateUserIdException
     *             the duplicate user id exception
     */
    void checkPersonUniquness(final Person person)
            throws DuplicateUserIdException {
        final List potentialDuplicatePeople = this
                .getHibernateTemplate()
                .findByNamedQuery(
                        PersonHibernateObjectRepository.CHECK_PERSON_UNIQUENESS_QUERY,
                        new Object[] { new Integer(person.getId()),
                                person.getUserId() });

        final Iterator iterator = potentialDuplicatePeople.iterator();
        if (iterator.hasNext()) {
            while (iterator.hasNext()) {
                final Person potentialDuplicatePerson = (Person) iterator
                        .next();
                if (this.hasSameUserId(person, potentialDuplicatePerson)) {
                    throw new DuplicateUserIdException();
                }
            }
        }
    }

    /**
     * Checks for same user id.
     *
     * @param person1
     *            the person1
     * @param person2
     *            the person2
     * @return true, if successful
     */
    boolean hasSameUserId(final Person person1, final Person person2) {
        // if(SecurityHelper.isAuthenticationCaseSensitive())
        // return person1.getUserId().equals(person2.getUserId());
        // else
        return person1.getUserId().equalsIgnoreCase(person2.getUserId());
    }
}
