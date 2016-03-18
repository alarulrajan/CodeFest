package net.sf.xplanner.dao.impl;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.technoetic.xplanner.domain.Identifiable;

public class CommonDao<E extends Identifiable> {
	private SessionFactory sessionFactory;

	public <T> T getById(final Class<T> clazz, final Serializable id) {
		return (T) this.getSession().get(clazz, id);
	}

	protected final Session getSession() {
		return SessionFactoryUtils.getSession(this.sessionFactory,
				Boolean.FALSE);
	}

	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Criteria createCriteria() {
		return null;
	}

	public int save(final E object) {
		this.getSession().save(object);
		return object.getId();
	}

	public void delete(final Object object) {
		this.getSession().delete(object);
	}

	public void flush() {
		this.getSession().flush();
	}

	public void rollback() {
		if (this.getSession().isOpen() && this.getSession().isDirty()) {
			this.getSession().getTransaction().rollback();
		}
	}
}
