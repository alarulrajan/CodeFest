package com.technoetic.xplanner.db.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

/**
 * The Class XPlannerInterceptor.
 */
public class XPlannerInterceptor implements Interceptor {
    
    /** The last update time. */
    private static String LAST_UPDATE_TIME = "lastUpdateTime";

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#onLoad(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    @Override
    public boolean onLoad(final Object entity, final Serializable id,
            final Object[] state, final String[] propertyNames,
            final Type[] types) {
        return false;
    }

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#onFlushDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    @Override
    public boolean onFlushDirty(final Object entity, final Serializable id,
            final Object[] currentState, final Object[] previousState,
            final String[] propertyNames, final Type[] types) {
        return this.setLastUpdateTime(propertyNames, currentState);
    }

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#onSave(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    @Override
    public boolean onSave(final Object entity, final Serializable id,
            final Object[] state, final String[] propertyNames,
            final Type[] types) {
        return this.setLastUpdateTime(propertyNames, state);
    }

    /**
     * Sets the last update time.
     *
     * @param propertyNames
     *            the property names
     * @param state
     *            the state
     * @return true, if successful
     */
    private boolean setLastUpdateTime(final String[] propertyNames,
            final Object[] state) {
        for (int i = 0; i < propertyNames.length; i++) {
            if (XPlannerInterceptor.LAST_UPDATE_TIME.equals(propertyNames[i])) {
                state[i] = new Date();
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#onDelete(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    @Override
    public void onDelete(final Object entity, final Serializable id,
            final Object[] state, final String[] propertyNames,
            final Type[] types) {
        // empty
    }

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#preFlush(java.util.Iterator)
     */
    @Override
    public void preFlush(final Iterator entities) {
        // empty
    }

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#postFlush(java.util.Iterator)
     */
    @Override
    public void postFlush(final Iterator entities) {
        // empty
    }

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#findDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    @Override
    public int[] findDirty(final Object entity, final Serializable id,
            final Object[] currentState, final Object[] previousState,
            final String[] propertyNames, final Type[] types) {
        return null;
    }

    /**
     * Instantiate.
     *
     * @param clazz
     *            the clazz
     * @param id
     *            the id
     * @return the object
     * @throws CallbackException
     *             the callback exception
     */
    public Object instantiate(final Class clazz, final Serializable id)
            throws CallbackException {
        return null;
    }

    /**
     * Checks if is unsaved.
     *
     * @param entity
     *            the entity
     * @return the boolean
     */
    public Boolean isUnsaved(final Object entity) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#afterTransactionBegin(org.hibernate.Transaction)
     */
    @Override
    public void afterTransactionBegin(final Transaction tx) {
        // ChangeSoon 

    }

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#afterTransactionCompletion(org.hibernate.Transaction)
     */
    @Override
    public void afterTransactionCompletion(final Transaction tx) {
        // ChangeSoon 

    }

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#beforeTransactionCompletion(org.hibernate.Transaction)
     */
    @Override
    public void beforeTransactionCompletion(final Transaction tx) {
        // ChangeSoon 

    }

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#getEntity(java.lang.String, java.io.Serializable)
     */
    @Override
    public Object getEntity(final String entityName, final Serializable id)
            throws CallbackException {
        // ChangeSoon 
        return null;
    }

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#getEntityName(java.lang.Object)
     */
    @Override
    public String getEntityName(final Object object) throws CallbackException {
        // ChangeSoon 
        return null;
    }

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#instantiate(java.lang.String, org.hibernate.EntityMode, java.io.Serializable)
     */
    @Override
    public Object instantiate(final String entityName,
            final EntityMode entityMode, final Serializable id)
            throws CallbackException {
        // ChangeSoon 
        return null;
    }

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#isTransient(java.lang.Object)
     */
    @Override
    public Boolean isTransient(final Object entity) {
        // ChangeSoon 
        return null;
    }

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#onCollectionRecreate(java.lang.Object, java.io.Serializable)
     */
    @Override
    public void onCollectionRecreate(final Object collection,
            final Serializable key) throws CallbackException {
        // ChangeSoon 

    }

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#onCollectionRemove(java.lang.Object, java.io.Serializable)
     */
    @Override
    public void onCollectionRemove(final Object collection,
            final Serializable key) throws CallbackException {
        // ChangeSoon 

    }

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#onCollectionUpdate(java.lang.Object, java.io.Serializable)
     */
    @Override
    public void onCollectionUpdate(final Object collection,
            final Serializable key) throws CallbackException {
        // ChangeSoon 

    }

    /* (non-Javadoc)
     * @see org.hibernate.Interceptor#onPrepareStatement(java.lang.String)
     */
    @Override
    public String onPrepareStatement(final String sql) {
        // ChangeSoon 
        return sql;
    }

}
