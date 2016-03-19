package com.technoetic.xplanner.domain.repository;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.technoetic.xplanner.db.NoteHelper;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.domain.NoteAttachable;

/**
 * The Class HibernateObjectRepository.
 */
public class HibernateObjectRepository extends HibernateDaoSupport implements
        ObjectRepository {
    
    /** The object class. */
    private final Class objectClass;
    
    /** The deletion query. */
    private final String deletionQuery;

    /**
     * Instantiates a new hibernate object repository.
     *
     * @param objectClass
     *            the object class
     * @throws HibernateException
     *             the hibernate exception
     */
    public HibernateObjectRepository(final Class objectClass)
            throws HibernateException {
        this.objectClass = objectClass;
        this.deletionQuery = "delete " + objectClass.getName()
                + " where id = ?";
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.ObjectRepository#delete(int)
     */
    @Override
    public void delete(final int objectIdentifier) throws RepositoryException {
        try {
            if (NoteAttachable.class.isAssignableFrom(this.objectClass)) {
                // FIXME This unfortunately is not enough. we have cascade
                // delete on from project down to time entry. Any of these
                // contained entity could have notes that have files. These
                // files won't be deleted
                NoteHelper.deleteNotesFor(this.objectClass, objectIdentifier,
                        this.getHibernateTemplate());
            }

            this.getHibernateTemplate().delete(
                    this.getSession().get(this.objectClass, objectIdentifier));
        } catch (final HibernateObjectRetrievalFailureException e) {
            throw new ObjectNotFoundException(e.getMessage());
        } catch (final DataAccessException ex) {
            throw new RepositoryException(ex);
        }
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.ObjectRepository#load(int)
     */
    @Override
    public Object load(final int objectIdentifier) throws RepositoryException {
        try {
            return this.getHibernateTemplate().load(this.objectClass,
                    new Integer(objectIdentifier));

        } catch (final HibernateObjectRetrievalFailureException e) {
            throw new ObjectNotFoundException(e.getMessage());
        } catch (final DataAccessException ex) {
            throw new RepositoryException(ex);
        }
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.ObjectRepository#insert(com.technoetic.xplanner.domain.Nameable)
     */
    @Override
    public int insert(final Nameable object) throws RepositoryException {
        try {
            final Integer id = (Integer) this.getSession2().save(object);
            return id.intValue();

        } catch (final HibernateObjectRetrievalFailureException e) {
            throw new ObjectNotFoundException(e.getMessage());
        } catch (final DataAccessException ex) {
            throw new RepositoryException(ex);
        }
    }

    /**
     * Gets the session2.
     *
     * @return the session2
     */
    protected Session getSession2() {
        return this.getSession();
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.ObjectRepository#update(com.technoetic.xplanner.domain.Nameable)
     */
    @Override
    public void update(final Nameable object) throws RepositoryException {
        // ChangeSoon 
    }
}
