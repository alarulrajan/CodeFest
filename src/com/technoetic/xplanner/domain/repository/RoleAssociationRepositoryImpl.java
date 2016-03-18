package com.technoetic.xplanner.domain.repository;

import java.util.List;

import net.sf.xplanner.domain.PersonRole;
import net.sf.xplanner.domain.Role;

import org.hibernate.HibernateException;

public class RoleAssociationRepositoryImpl extends HibernateObjectRepository
		implements RoleAssociationRepository {
	private RoleRepository roleRepository;

	public RoleAssociationRepositoryImpl() throws HibernateException {
		super(PersonRole.class);
	}

	@Override
	public void deleteAllForPersonOnProject(final int personId,
			final int projectId) throws RepositoryException {
		final List list = this
				.getHibernateTemplate()
				.find("select assoc from assoc in "
						+ PersonRole.class
						+ " where assoc.id.personId = ? and assoc.id.projectId = ?",
						new Object[] { new Integer(personId),
								new Integer(projectId) });
		this.getHibernateTemplate().deleteAll(list);
	}

	@Override
	public void deleteForPersonOnProject(final String roleName,
			final int personId, final int projectId) throws RepositoryException {
		final Role role = this.roleRepository.findRoleByName(roleName);
		final List list = this
				.getHibernateTemplate()
				.find("select assoc from assoc in "
						+ PersonRole.class
						+ " where assoc.id.personId = ? and assoc.id.projectId = ? and assoc.id.roleId = ?",
						new Object[] { new Integer(personId),
								new Integer(projectId),
								new Integer(role.getId()) });
		this.getHibernateTemplate().deleteAll(list);

	}

	@Override
	public void insertForPersonOnProject(final String roleName,
			final int personId, final int projectId) throws RepositoryException {
		final Role role = this.roleRepository.findRoleByName(roleName);
		if (role != null) {
			this.getHibernateTemplate().save(
					new PersonRole(projectId, personId, role.getId()));
		}
	}

	public void setRoleRepository(final RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
}
