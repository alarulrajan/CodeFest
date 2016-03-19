package com.technoetic.xplanner.domain.repository;

import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.Role;

import org.hibernate.HibernateException;

/**
 * The Class RoleRepositoryImpl.
 */
public class RoleRepositoryImpl extends HibernateObjectRepository implements
		RoleRepository {
	
	/**
     * Instantiates a new role repository impl.
     *
     * @throws HibernateException
     *             the hibernate exception
     */
	public RoleRepositoryImpl() throws HibernateException {
		super(Role.class);
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.repository.RoleRepository#findRoleByName(java.lang.String)
	 */
	@Override
	public Role findRoleByName(final String rolename)
			throws RepositoryException {
		List roles = null;
		roles = this.getHibernateTemplate().find(
				"from role in class " + Role.class.getName()
						+ " where role.name = ?", rolename);
		Role role = null;
		final Iterator roleIterator = roles.iterator();
		if (roleIterator.hasNext()) {
			role = (Role) roleIterator.next();
		}
		return role;
	}
}
