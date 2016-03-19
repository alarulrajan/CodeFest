package com.technoetic.xplanner.tags.displaytag;

import net.sf.xplanner.domain.Person;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

/**
 * The Class PersonIdDecorator.
 */
public class PersonIdDecorator {
    
    /**
     * Gets the person name.
     *
     * @param id
     *            the id
     * @return the person name
     */
    public static String getPersonName(final int id) {
        if (id <= 0) {
            return "";
        }
        final Person person = PersonIdDecorator.getPerson(id);
        return person == null ? "" : person.getName();
    }

    /**
     * Gets the person.
     *
     * @param id
     *            the id
     * @return the person
     */
    private static Person getPerson(final int id) {
        try {
            return (Person) ThreadSession.get().get(Person.class,
                    new Integer(id));
        } catch (final HibernateException e) {
            return null;
        }
    }
}