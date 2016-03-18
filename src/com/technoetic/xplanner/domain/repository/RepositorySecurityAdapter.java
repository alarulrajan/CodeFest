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

public class RepositorySecurityAdapter extends HibernateDaoSupport implements
		ObjectRepository {
	private final Class objectClass;
	private final ObjectRepository delegate;
	private Authorizer authorizer;

	public void setAuthorizer(final Authorizer authorizer) {
		this.authorizer = authorizer;
	}

	public RepositorySecurityAdapter(final Class objectClass,
			final ObjectRepository delegate) throws HibernateException {
		this.objectClass = objectClass;
		this.delegate = delegate;
	}

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

	@Override
	public int insert(final Nameable object) throws RepositoryException {
		// do-before-release Fix insert authorization - problem with missing
		// project context
		// checkAuthorization(object, "create");
		return this.delegate.insert(object);
	}

	@Override
	public Object load(final int objectIdentifier) throws RepositoryException {
		final Object loadedObject = this.delegate.load(objectIdentifier);
		this.checkAuthorization(loadedObject, "read");
		return loadedObject;
	}

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

	@Override
	public void update(final Nameable object) throws RepositoryException {
		this.checkAuthorization(object, "edit");
		this.delegate.update(object);
	}
}
