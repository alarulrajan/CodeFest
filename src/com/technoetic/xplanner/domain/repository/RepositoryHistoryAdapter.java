package com.technoetic.xplanner.domain.repository;

import java.sql.SQLException;

import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.NamedObject;

import org.hibernate.HibernateException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.technoetic.xplanner.domain.Identifiable;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.filters.ThreadServletRequest;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;

public class RepositoryHistoryAdapter extends HibernateDaoSupport implements
		ObjectRepository {
	private final Class objectClass;
	private final ObjectRepository delegate;

	public RepositoryHistoryAdapter(final Class objectClass,
			final ObjectRepository delegate) {
		this.objectClass = objectClass;
		this.delegate = delegate;
	}

	@Override
	public void delete(final int objectIdentifier) throws RepositoryException {
		// todo How should the project ID be obtained with refs to Hibernate?

		final int remoteUserId = this.getRemoteUserId();
		final NamedObject object = (NamedObject) this.getHibernateTemplate()
				.load(this.objectClass, new Integer(objectIdentifier));
		this.saveHistoryEvent(object, History.DELETED, object.getName(),
				remoteUserId);
		this.delegate.delete(objectIdentifier);
	}

	@Override
	public int insert(final Nameable object) throws RepositoryException {
		final int id = this.delegate.insert(object);
		final int remoteUserId;
		remoteUserId = this.getRemoteUserId();
		this.saveHistoryEvent(object, History.CREATED, object.getName(),
				remoteUserId);
		return id;
	}

	@Override
	public Object load(final int objectIdentifier) throws RepositoryException {
		// no load history
		return this.delegate.load(objectIdentifier);
	}

	@Override
	public void update(final Nameable object) throws RepositoryException {
		final int remoteUserId;
		remoteUserId = this.getRemoteUserId();
		this.saveHistoryEvent(object, History.UPDATED, object.getName(),
				remoteUserId);
		this.delegate.update(object);
	}

	private void saveHistoryEvent(final Nameable object,
			final String eventType, final String description,
			final int remoteUserId) {
		this.getHibernateTemplate().execute(
				new SaveEventHibernateCallback(object, eventType, description,
						remoteUserId));

	}

	private int getRemoteUserId() throws RepositoryException {
		int remoteUserId;
		try {
			remoteUserId = SecurityHelper.getRemoteUserId(ThreadServletRequest
					.get());
		} catch (final AuthenticationException e) {
			throw new RepositoryException(e);
		}
		return remoteUserId;
	}

	class SaveEventHibernateCallback implements HibernateCallback {
		private final Identifiable object;
		private final String eventType;
		private final String description;
		private final int remoteUserId;

		public SaveEventHibernateCallback(final Identifiable object,
				final String eventType, final String description,
				final int remoteUserId) {
			this.object = object;
			this.eventType = eventType;
			this.description = description;
			this.remoteUserId = remoteUserId;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || this.getClass() != o.getClass()) {
				return false;
			}

			final SaveEventHibernateCallback that = (SaveEventHibernateCallback) o;

			if (this.remoteUserId != that.remoteUserId) {
				return false;
			}
			if (this.description != null ? !this.description
					.equals(that.description) : that.description != null) {
				return false;
			}
			if (this.eventType != null ? !this.eventType.equals(that.eventType)
					: that.eventType != null) {
				return false;
			}
			if (this.object != null ? !this.object.equals(that.object)
					: that.object != null) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public Object doInHibernate(final org.hibernate.Session session)
				throws HibernateException, SQLException {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
