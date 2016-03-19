package com.technoetic.xplanner.domain.repository;

/**
 * The Interface RoleAssociationRepository.
 */
public interface RoleAssociationRepository extends ObjectRepository {
	
	/**
     * Delete all for person on project.
     *
     * @param personId
     *            the person id
     * @param projectId
     *            the project id
     * @throws RepositoryException
     *             the repository exception
     */
	void deleteAllForPersonOnProject(int personId, int projectId)
			throws RepositoryException;

	/**
     * Delete for person on project.
     *
     * @param roleName
     *            the role name
     * @param personId
     *            the person id
     * @param projectId
     *            the project id
     * @throws RepositoryException
     *             the repository exception
     */
	void deleteForPersonOnProject(String roleName, int personId, int projectId)
			throws RepositoryException;

	/**
     * Insert for person on project.
     *
     * @param roleName
     *            the role name
     * @param personId
     *            the person id
     * @param projectId
     *            the project id
     * @throws RepositoryException
     *             the repository exception
     */
	void insertForPersonOnProject(String roleName, int personId, int projectId)
			throws RepositoryException;
}
