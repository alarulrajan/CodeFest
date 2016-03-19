package com.technoetic.xplanner.domain.repository;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.filters.ThreadServletRequest;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.AuthorizationException;
import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.tags.DomainContext;

// dao -- how should other methods be protected? repository-specific ones?

/**
 * The Class RepositorySecurityAdapter.
 */
public class RepositorySecurityAdapter extends HibernateDaoSupport implements
		ObjectRepository {
	
	/** The object class. */
	private final Class objectClass;
	
	/** The delegate. */
	private final ObjectRepository delegate;
	
	/** The authorizer. */
	private Authorizer authorizer;

	/**
     * Sets the authorizer.
     *
     * @param authorizer
     *            the new authorizer
     */
	public void setAuthorizer(final Authorizer authorizer) {
		this.authorizer = authorizer;
	}

	/**
     * Instantiates a new repository security adapter.
     *
     * @param objectClass
     *            the object class
     * @param delegate
     *            the delegate
     * @throws HibernateException
     *             the hibernate exception
     */
	public RepositorySecurityAdapter(final Class objectClass,
			final ObjectRepository delegate) throws HibernateException {
		this.objectClass = objectClass;
		this.delegate = delegate;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.repository.ObjectRepository#delete(int)
	 */
	@Override
	public void delete(final int objectIdentifier) throws RepositoryException {
		try {
			// todo How should the project ID be obtained without refs to
			// Hibernate?
			final Object object = this.getHibernateTemplate().load(
					this.objectClass, new Integer(objectIdentifier));
			this.checkAuthorization(object, "delete");
		} catch (final HibernateObjectRetrievalFailureException e) {
			throw new ObjectNotFoundException(e.getMessage());
		} catch (final DataAccessException e) {
			throw new RepositoryException(e);
		}
		this.delegate.delete(objectIdentifier);
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.repository.ObjectRepository#insert(com.technoetic.xplanner.domain.Nameable)
	 */
	@Override
	public int insert(final Nameable object) throws RepositoryException {
		// do-before-release Fix insert authorization - problem with missing
		// project context
		// checkAuthorization(object, "create");
		return this.delegate.insert(object);
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.repository.ObjectRepository#load(int)
	 */
	@Override
	public Object load(final int objectIdentifier) throws RepositoryException {
		final Object loadedObject = this.delegate.load(objectIdentifier);
		this.checkAuthorization(loadedObject, "read");
		return loadedObject;
	}

	/**
     * Check authorization.
     *
     * @param object
     *            the object
     * @param permission
     *            the permission
     * @throws RepositoryException
     *             the repository exception
     */
	private void checkAuthorization(final Object object, final String permission)
			throws RepositoryException {
		final DomainContext context = new DomainContext();
		try {
			context.populate(object);
			final int objectIdentifier = ((Integer) PropertyUtils.getProperty(
					object, "id")).intValue();
			if (!this.authorizer.hasPermission(context.getProjectId(),
					SecurityHelper.getRemoteUserId(ThreadServletRequest.get()),
					object, permission)) {
				throw new AuthorizationException("not authorized for object "
						+ permission + ": " + this.objectClass + " "
						+ objectIdentifier);
			}
		} catch (final RuntimeException e) {
			throw e;
		} catch (final Exception e) {
			throw new RepositoryException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.repository.ObjectRepository#update(com.technoetic.xplanner.domain.Nameable)
	 */
	@Override
	public void update(final Nameable object) throws RepositoryException {
		this.checkAuthorization(object, "edit");
		this.delegate.update(object);
	}
}
