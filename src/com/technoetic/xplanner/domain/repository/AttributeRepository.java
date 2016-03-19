package com.technoetic.xplanner.domain.repository;

import java.util.Map;

/**
 * The Interface AttributeRepository.
 */
public interface AttributeRepository {
	
	/**
     * Sets the attribute.
     *
     * @param targetId
     *            the target id
     * @param name
     *            the name
     * @param value
     *            the value
     * @throws RepositoryException
     *             the repository exception
     */
	void setAttribute(int targetId, String name, String value)
			throws RepositoryException;

	/**
     * Delete.
     *
     * @param targetId
     *            the target id
     * @param name
     *            the name
     * @throws RepositoryException
     *             the repository exception
     */
	void delete(int targetId, String name) throws RepositoryException;

	/**
     * Gets the attributes.
     *
     * @param targetId
     *            the target id
     * @param prefix
     *            the prefix
     * @return the attributes
     * @throws RepositoryException
     *             the repository exception
     */
	Map getAttributes(int targetId, String prefix) throws RepositoryException;

	/**
     * Gets the attribute.
     *
     * @param targetId
     *            the target id
     * @param name
     *            the name
     * @return the attribute
     * @throws RepositoryException
     *             the repository exception
     */
	String getAttribute(int targetId, String name) throws RepositoryException;
}
