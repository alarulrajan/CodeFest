package com.technoetic.xplanner.domain.repository;

import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.Role;

import org.hibernate.HibernateException;

public class RoleRepositoryImpl extends HibernateObjectRepository implements
		RoleRepository {
	public RoleRepositoryImpl() throws HibernateException {
		super(Role.class);
	}

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
