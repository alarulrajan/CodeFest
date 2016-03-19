/**
 * 
 */
package net.sf.xplanner.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import net.sf.xplanner.dao.Dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.technoetic.xplanner.domain.Identifiable;

/**
 * XplannerPlus, agile planning software.
 *
 * @author Maksym_Chyrkov. Copyright (C) 2009 Maksym Chyrkov This program is
 *         free software: you can redistribute it and/or modify it under the
 *         terms of the GNU General Public License as published by the Free
 *         Software Foundation, either version 3 of the License, or (at your
 *         option) any later version.
 * 
 *         This program is distributed in the hope that it will be useful, but
 *         WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with this program. If not, see <http://www.gnu.org/licenses/>
 * @param <E>
 *            the element type
 */
@SuppressWarnings("unchecked")
public class BaseDao<E extends Identifiable> implements Dao<E> {
    
    /** The domain class. */
    private final Class<E> domainClass = (Class<E>) ((ParameterizedType) this
            .getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    
    /** The session factory. */
    private SessionFactory sessionFactory;

    /* (non-Javadoc)
     * @see net.sf.xplanner.dao.Dao#save(com.technoetic.xplanner.domain.Identifiable)
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int save(final E object) {
        this.getSession().saveOrUpdate(object);
        return object.getId();
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.dao.Dao#createCriteria()
     */
    @Override
    public Criteria createCriteria() {
        final Criteria criteria = this.getSession().createCriteria(
                this.domainClass);
        criteria.setCacheable(true);
        return criteria;
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.dao.Dao#delete(java.io.Serializable)
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(final Serializable objectId) {
        this.delete(this.getById(objectId));
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.dao.Dao#delete(com.technoetic.xplanner.domain.Identifiable)
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(final E object) {
        this.getSession().delete(object);
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.dao.Dao#deleteAll(java.util.List)
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAll(final List<E> objects) {
        for (final E object : objects) {
            this.delete(object);
        }
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.dao.Dao#getById(java.io.Serializable)
     */
    @Override
    @Transactional(readOnly = true)
    public E getById(final Serializable id) {
        return (E) this.getSession().get(this.domainClass, id);
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

    /* (non-Javadoc)
     * @see net.sf.xplanner.dao.Dao#getUniqueObject(java.lang.String, java.lang.Object)
     */
    @Override
    @Transactional(readOnly = true)
    public E getUniqueObject(final String field, final Object value) {
        final Criteria criteria = this.createCriteria().add(
                Restrictions.eq(field, value));
        return (E) criteria.uniqueResult();
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.dao.Dao#isNewObject(com.technoetic.xplanner.domain.Identifiable)
     */
    @Override
    public boolean isNewObject(final E object) {
        if (object.getId() > 0) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.dao.Dao#getDomainClass()
     */
    @Override
    public Class<E> getDomainClass() {
        return this.domainClass;
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.dao.Dao#evict(com.technoetic.xplanner.domain.Identifiable)
     */
    @Override
    public void evict(final E object) {
        this.getSession().evict(object);
    }
}
