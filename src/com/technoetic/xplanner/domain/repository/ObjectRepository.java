package com.technoetic.xplanner.domain.repository;

import com.technoetic.xplanner.domain.Nameable;

/**
 * The Interface ObjectRepository.
 */
@Deprecated
public interface ObjectRepository {
	
	/**
     * Delete an object using it's object ID (OID).
     *
     * @param objectIdentifier
     *            the object identifier
     * @throws RepositoryException
     *             the repository exception
     */
	void delete(int objectIdentifier) throws RepositoryException;

	/**
     * Load an instance of an object.
     *
     * @param objectIdentifier
     *            the object identifier
     * @return the object
     * @throws RepositoryException
     *             the repository exception
     */
	Object load(int objectIdentifier) throws RepositoryException;

	/**
     * Create a new instance in the repository.
     *
     * @param object
     *            the object to insert
     * @return the object identifier
     * @throws RepositoryException
     *             the repository exception
     */
	int insert(Nameable object) throws RepositoryException;

	/**
     * Updates an object in the repository. Note: This is a no-op for Hibernate.
     *
     * @param object
     *            - the object to update
     * @throws RepositoryException
     *             the repository exception
     */
	void update(Nameable object) throws RepositoryException;
}
