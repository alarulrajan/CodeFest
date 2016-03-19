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

/**
 * The Class RepositoryHistoryAdapter.
 */
public class RepositoryHistoryAdapter extends HibernateDaoSupport implements
        ObjectRepository {
    
    /** The object class. */
    private final Class objectClass;
    
    /** The delegate. */
    private final ObjectRepository delegate;

    /**
     * Instantiates a new repository history adapter.
     *
     * @param objectClass
     *            the object class
     * @param delegate
     *            the delegate
     */
    public RepositoryHistoryAdapter(final Class objectClass,
            final ObjectRepository delegate) {
        this.objectClass = objectClass;
        this.delegate = delegate;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.ObjectRepository#delete(int)
     */
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

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.ObjectRepository#insert(com.technoetic.xplanner.domain.Nameable)
     */
    @Override
    public int insert(final Nameable object) throws RepositoryException {
        final int id = this.delegate.insert(object);
        final int remoteUserId;
        remoteUserId = this.getRemoteUserId();
        this.saveHistoryEvent(object, History.CREATED, object.getName(),
                remoteUserId);
        return id;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.ObjectRepository#load(int)
     */
    @Override
    public Object load(final int objectIdentifier) throws RepositoryException {
        // no load history
        return this.delegate.load(objectIdentifier);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.ObjectRepository#update(com.technoetic.xplanner.domain.Nameable)
     */
    @Override
    public void update(final Nameable object) throws RepositoryException {
        final int remoteUserId;
        remoteUserId = this.getRemoteUserId();
        this.saveHistoryEvent(object, History.UPDATED, object.getName(),
                remoteUserId);
        this.delegate.update(object);
    }

    /**
     * Save history event.
     *
     * @param object
     *            the object
     * @param eventType
     *            the event type
     * @param description
     *            the description
     * @param remoteUserId
     *            the remote user id
     */
    private void saveHistoryEvent(final Nameable object,
            final String eventType, final String description,
            final int remoteUserId) {
        this.getHibernateTemplate().execute(
                new SaveEventHibernateCallback(object, eventType, description,
                        remoteUserId));

    }

    /**
     * Gets the remote user id.
     *
     * @return the remote user id
     * @throws RepositoryException
     *             the repository exception
     */
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

    /**
     * The Class SaveEventHibernateCallback.
     */
    class SaveEventHibernateCallback implements HibernateCallback {
        
        /** The object. */
        private final Identifiable object;
        
        /** The event type. */
        private final String eventType;
        
        /** The description. */
        private final String description;
        
        /** The remote user id. */
        private final int remoteUserId;

        /**
         * Instantiates a new save event hibernate callback.
         *
         * @param object
         *            the object
         * @param eventType
         *            the event type
         * @param description
         *            the description
         * @param remoteUserId
         *            the remote user id
         */
        public SaveEventHibernateCallback(final Identifiable object,
                final String eventType, final String description,
                final int remoteUserId) {
            this.object = object;
            this.eventType = eventType;
            this.description = description;
            this.remoteUserId = remoteUserId;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
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

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return 0;
        }

        /* (non-Javadoc)
         * @see org.springframework.orm.hibernate3.HibernateCallback#doInHibernate(org.hibernate.Session)
         */
        @Override
        public Object doInHibernate(final org.hibernate.Session session)
                throws HibernateException, SQLException {
            // ChangeSoon 
            return null;
        }
    }
}
