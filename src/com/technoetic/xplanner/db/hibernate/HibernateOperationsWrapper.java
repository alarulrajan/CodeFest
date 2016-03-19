package com.technoetic.xplanner.db.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.HibernateOperations;

/**
 * User: Mateusz Prokopowicz Date: Apr 21, 2005 Time: 11:18:09 AM.
 */
public class HibernateOperationsWrapper {
	
	/** The session. */
	private Session session;
	
	/** The hibernate operations. */
	private HibernateOperations hibernateOperations;

	/**
     * Instantiates a new hibernate operations wrapper.
     *
     * @param session
     *            the session
     */
	public HibernateOperationsWrapper(final Session session) {
		this.session = session;
	}

	/**
     * Instantiates a new hibernate operations wrapper.
     *
     * @param hibernateOperations
     *            the hibernate operations
     */
	public HibernateOperationsWrapper(
			final HibernateOperations hibernateOperations) {
		this.hibernateOperations = hibernateOperations;
	}

	/**
     * Load.
     *
     * @param theClass
     *            the the class
     * @param id
     *            the id
     * @return the object
     * @throws HibernateException
     *             the hibernate exception
     */
	public Object load(final Class theClass, final Serializable id)
			throws HibernateException {
		if (this.session != null) {
			return this.session.load(theClass, id);
		} else {
			return this.hibernateOperations.load(theClass, id);
		}
	}

	/**
     * Find.
     *
     * @param query
     *            the query
     * @param values
     *            the values
     * @param types
     *            the types
     * @return the list
     * @throws HibernateException
     *             the hibernate exception
     */
	public List find(final String query, final Object[] values,
			final Type[] types) throws HibernateException {
		if (this.session != null) {
			return this.session.find(query, values, types);
		} else {
			return this.hibernateOperations.find(query, values);
		}
	}

	/**
     * Save.
     *
     * @param object
     *            the object
     * @return the serializable
     * @throws HibernateException
     *             the hibernate exception
     */
	public Serializable save(final Object object) throws HibernateException {
		if (this.session != null) {
			return this.session.save(object);
		} else {
			return this.hibernateOperations.save(object);
		}
	}
}
