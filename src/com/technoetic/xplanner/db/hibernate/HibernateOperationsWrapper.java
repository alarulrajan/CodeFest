package com.technoetic.xplanner.db.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.HibernateOperations;

/**
 * User: Mateusz Prokopowicz Date: Apr 21, 2005 Time: 11:18:09 AM
 */
public class HibernateOperationsWrapper {
	private Session session;
	private HibernateOperations hibernateOperations;

	public HibernateOperationsWrapper(final Session session) {
		this.session = session;
	}

	public HibernateOperationsWrapper(
			final HibernateOperations hibernateOperations) {
		this.hibernateOperations = hibernateOperations;
	}

	public Object load(final Class theClass, final Serializable id)
			throws HibernateException {
		if (this.session != null) {
			return this.session.load(theClass, id);
		} else {
			return this.hibernateOperations.load(theClass, id);
		}
	}

	public List find(final String query, final Object[] values,
			final Type[] types) throws HibernateException {
		if (this.session != null) {
			return this.session.find(query, values, types);
		} else {
			return this.hibernateOperations.find(query, values);
		}
	}

	public Serializable save(final Object object) throws HibernateException {
		if (this.session != null) {
			return this.session.save(object);
		} else {
			return this.hibernateOperations.save(object);
		}
	}
}
