package com.technoetic.mocks.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Set;

import org.hibernate.Cache;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.TypeHelper;
import org.hibernate.classic.Session;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.exception.SQLExceptionConverter;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;
import org.springframework.validation.DataBinder;

/**
 * A factory for creating MockSession objects.
 */
public class MockSessionFactory implements SessionFactory {

    /** The open session called. */
    public boolean openSessionCalled;
    
    /** The open session return. */
    public Session openSessionReturn;

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#openSession()
     */
    public Session openSession() {
        openSessionCalled = true;
        return openSessionReturn;
    }

    /** The open session2 called. */
    public boolean openSession2Called;
    
    /** The open session2 connection. */
    public java.sql.Connection openSession2Connection;
    
    /** The open session2 return. */
    public Session openSession2Return;

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#openSession(java.sql.Connection)
     */
    public Session openSession(java.sql.Connection connection) {
        openSession2Called = true;
        openSession2Connection = connection;
        return openSession2Return;
    }

    /** The open session3 called. */
    public boolean openSession3Called;
    
    /** The open session3 return. */
    public Session openSession3Return;
    
    /** The open session3 interceptor. */
    public Interceptor openSession3Interceptor;

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#openSession(org.hibernate.Interceptor)
     */
    public Session openSession(Interceptor interceptor) {
        openSession3Called = true;
        openSession3Interceptor = interceptor;
        return openSession3Return;
    }

    /** The open session4 called. */
    public boolean openSession4Called;
    
    /** The open session4 return. */
    public Session openSession4Return;
    
    /** The open session4 interceptor. */
    public Interceptor openSession4Interceptor;
    
    /** The open session4 connection. */
    public java.sql.Connection openSession4Connection;

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#openSession(java.sql.Connection, org.hibernate.Interceptor)
     */
    public Session openSession(java.sql.Connection connection, Interceptor interceptor) {
        openSession4Called = true;
        openSession4Connection = connection;
        openSession4Interceptor = interceptor;
        return openSession4Return;
    }


    /** The open databinder called. */
    public boolean openDatabinderCalled;
    
    /** The open databinder return. */
    public DataBinder openDatabinderReturn;
    
    /** The open databinder hibernate exception. */
    public HibernateException openDatabinderHibernateException;

    /** Open databinder.
     *
     * @return the data binder
     * @throws HibernateException
     *             the hibernate exception
     */
    public DataBinder openDatabinder() throws HibernateException {
        openDatabinderCalled = true;
        if (openDatabinderHibernateException != null) {
            throw openDatabinderHibernateException;
        }
        return openDatabinderReturn;
    }

    /** The get class metadata called. */
    public boolean getClassMetadataCalled;
    
    /** The get class metadata return. */
    public ClassMetadata getClassMetadataReturn;
    
    /** The get class metadata hibernate exception. */
    public HibernateException getClassMetadataHibernateException;
    
    /** The get class metadata persistent class. */
    public java.lang.Class getClassMetadataPersistentClass;

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#getClassMetadata(java.lang.Class)
     */
    public ClassMetadata getClassMetadata(java.lang.Class persistentClass) throws HibernateException {
        getClassMetadataCalled = true;
        getClassMetadataPersistentClass = persistentClass;
        if (getClassMetadataHibernateException != null) {
            throw getClassMetadataHibernateException;
        }
        return getClassMetadataReturn;
    }

    /** The get collection metadata called. */
    public boolean getCollectionMetadataCalled;
    
    /** The get collection metadata return. */
    public CollectionMetadata getCollectionMetadataReturn;
    
    /** The get collection metadata hibernate exception. */
    public HibernateException getCollectionMetadataHibernateException;
    
    /** The get collection metadata role name. */
    public java.lang.String getCollectionMetadataRoleName;

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#getCollectionMetadata(java.lang.String)
     */
    public CollectionMetadata getCollectionMetadata(java.lang.String roleName) throws HibernateException {
        getCollectionMetadataCalled = true;
        getCollectionMetadataRoleName = roleName;
        if (getCollectionMetadataHibernateException != null) {
            throw getCollectionMetadataHibernateException;
        }
        return getCollectionMetadataReturn;
    }

    /** The get all class metadata called. */
    public boolean getAllClassMetadataCalled;
    
    /** The get all class metadata return. */
    public java.util.Map getAllClassMetadataReturn;
    
    /** The get all class metadata hibernate exception. */
    public HibernateException getAllClassMetadataHibernateException;

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#getAllClassMetadata()
     */
    public java.util.Map getAllClassMetadata() throws HibernateException {
        getAllClassMetadataCalled = true;
        if (getAllClassMetadataHibernateException != null) {
            throw getAllClassMetadataHibernateException;
        }
        return getAllClassMetadataReturn;
    }

    /** The get all collection metadata called. */
    public boolean getAllCollectionMetadataCalled;
    
    /** The get all collection metadata return. */
    public java.util.Map getAllCollectionMetadataReturn;
    
    /** The get all collection metadata hibernate exception. */
    public HibernateException getAllCollectionMetadataHibernateException;

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#getAllCollectionMetadata()
     */
    public java.util.Map getAllCollectionMetadata() throws HibernateException {
        getAllCollectionMetadataCalled = true;
        if (getAllCollectionMetadataHibernateException != null) {
            throw getAllCollectionMetadataHibernateException;
        }
        return getAllCollectionMetadataReturn;
    }

    /** The get reference called. */
    public boolean getReferenceCalled;
    
    /** The get reference return. */
    public javax.naming.Reference getReferenceReturn;
    
    /** The get reference naming exception. */
    public javax.naming.NamingException getReferenceNamingException;

    /* (non-Javadoc)
     * @see javax.naming.Referenceable#getReference()
     */
    public javax.naming.Reference getReference() throws javax.naming.NamingException {
        getReferenceCalled = true;
        if (getReferenceNamingException != null) {
            throw getReferenceNamingException;
        }
        return getReferenceReturn;
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#close()
     */
    public void close() throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#evict(java.lang.Class)
     */
    public void evict(Class persistentClass) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#evict(java.lang.Class, java.io.Serializable)
     */
    public void evict(Class persistentClass, Serializable id) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#evictCollection(java.lang.String)
     */
    public void evictCollection(String roleName) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#evictCollection(java.lang.String, java.io.Serializable)
     */
    public void evictCollection(String roleName, Serializable id) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#evictQueries()
     */
    public void evictQueries() throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#evictQueries(java.lang.String)
     */
    public void evictQueries(String cacheRegion) throws HibernateException {
        throw new UnsupportedOperationException();
    }

  /**
     * Gets the SQL exception converter.
     *
     * @return the SQL exception converter
     */
  public SQLExceptionConverter getSQLExceptionConverter() {
    return null;
  }

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#evictEntity(java.lang.String)
 */
@Override
public void evictEntity(String entityName) throws HibernateException {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#evictEntity(java.lang.String, java.io.Serializable)
 */
@Override
public void evictEntity(String entityName, Serializable id)
        throws HibernateException {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#getClassMetadata(java.lang.String)
 */
@Override
public ClassMetadata getClassMetadata(String entityName)
        throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#getCurrentSession()
 */
@Override
public Session getCurrentSession() throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#getDefinedFilterNames()
 */
@Override
public Set getDefinedFilterNames() {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#getFilterDefinition(java.lang.String)
 */
@Override
public FilterDefinition getFilterDefinition(String filterName)
        throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#getStatistics()
 */
@Override
public Statistics getStatistics() {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#isClosed()
 */
@Override
public boolean isClosed() {
    // ChangeSoon 
    return false;
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#openStatelessSession()
 */
@Override
public StatelessSession openStatelessSession() {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#openStatelessSession(java.sql.Connection)
 */
@Override
public StatelessSession openStatelessSession(Connection connection) {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#getCache()
 */
@Override
public Cache getCache() {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#containsFetchProfileDefinition(java.lang.String)
 */
@Override
public boolean containsFetchProfileDefinition(String name) {
    // ChangeSoon 
    return false;
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#getTypeHelper()
 */
@Override
public TypeHelper getTypeHelper() {
    // ChangeSoon 
    return null;
}
}
