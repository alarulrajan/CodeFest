package net.sf.xplanner.dao.impl;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.technoetic.xplanner.domain.Identifiable;

/**
 * The Class CommonDao.
 *
 * @param <E>
 *            the element type
 */
public class CommonDao<E extends Identifiable> {
	
	/** The session factory. */
	private SessionFactory sessionFactory;

	/**
     * Gets the by id.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @param id
     *            the id
     * @return the by id
     */
	public <T> T getById(final Class<T> clazz, final Serializable id) {
		return (T) this.getSession().get(clazz, id);
	}

	/**
     * Gets the session.
     *
     * @return the session
     */
	protected final Session getSession() {
		return SessionFactoryUtils.getSession(this.sessionFactory,
				Boolean.FALSE);
	}

	/**
     * Sets the session factory.
     *
     * @param sessionFactory
     *            the new session factory
     */
	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
     * Creates the criteria.
     *
     * @return the criteria
     */
	public Criteria createCriteria() {
		return null;
	}

	/**
     * Save.
     *
     * @param object
     *            the object
     * @return the int
     */
	public int save(final E object) {
		this.getSession().save(object);
		return object.getId();
	}

	/**
     * Delete.
     *
     * @param object
     *            the object
     */
	public void delete(final Object object) {
		this.getSession().delete(object);
	}

	/**
     * Flush.
     */
	public void flush() {
		this.getSession().flush();
	}

	/**
     * Rollback.
     */
	public void rollback() {
		if (this.getSession().isOpen() && this.getSession().isDirty()) {
			this.getSession().getTransaction().rollback();
		}
	}
}
