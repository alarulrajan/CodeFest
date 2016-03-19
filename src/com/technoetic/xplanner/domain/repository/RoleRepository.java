package com.technoetic.xplanner.domain.repository;

import net.sf.xplanner.domain.Role;

/**
 * The Interface RoleRepository.
 */
public interface RoleRepository extends ObjectRepository {
    
    /**
     * Find role by name.
     *
     * @param name
     *            the name
     * @return the role
     * @throws RepositoryException
     *             the repository exception
     */
    Role findRoleByName(String name) throws RepositoryException;
}
