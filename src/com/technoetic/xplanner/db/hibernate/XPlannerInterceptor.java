package com.technoetic.xplanner.db.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

public class XPlannerInterceptor implements Interceptor {
	private static String LAST_UPDATE_TIME = "lastUpdateTime";

	@Override
	public boolean onLoad(final Object entity, final Serializable id,
			final Object[] state, final String[] propertyNames,
			final Type[] types) {
		return false;
	}

	@Override
	public boolean onFlushDirty(final Object entity, final Serializable id,
			final Object[] currentState, final Object[] previousState,
			final String[] propertyNames, final Type[] types) {
		return this.setLastUpdateTime(propertyNames, currentState);
	}

	@Override
	public boolean onSave(final Object entity, final Serializable id,
			final Object[] state, final String[] propertyNames,
			final Type[] types) {
		return this.setLastUpdateTime(propertyNames, state);
	}

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

	@Override
	public void onDelete(final Object entity, final Serializable id,
			final Object[] state, final String[] propertyNames,
			final Type[] types) {
		// empty
	}

	@Override
	public void preFlush(final Iterator entities) {
		// empty
	}

	@Override
	public void postFlush(final Iterator entities) {
		// empty
	}

	@Override
	public int[] findDirty(final Object entity, final Serializable id,
			final Object[] currentState, final Object[] previousState,
			final String[] propertyNames, final Type[] types) {
		return null;
	}

	public Object instantiate(final Class clazz, final Serializable id)
			throws CallbackException {
		return null;
	}

	public Boolean isUnsaved(final Object entity) {
		return null;
	}

	@Override
	public void afterTransactionBegin(final Transaction tx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTransactionCompletion(final Transaction tx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTransactionCompletion(final Transaction tx) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getEntity(final String entityName, final Serializable id)
			throws CallbackException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEntityName(final Object object) throws CallbackException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object instantiate(final String entityName,
			final EntityMode entityMode, final Serializable id)
			throws CallbackException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isTransient(final Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCollectionRecreate(final Object collection,
			final Serializable key) throws CallbackException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCollectionRemove(final Object collection,
			final Serializable key) throws CallbackException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCollectionUpdate(final Object collection,
			final Serializable key) throws CallbackException {
		// TODO Auto-generated method stub

	}

	@Override
	public String onPrepareStatement(final String sql) {
		// TODO Auto-generated method stub
		return sql;
	}

}
