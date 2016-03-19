package com.technoetic.xplanner.domain.repository;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class MetaRepositoryImpl.
 */
public class MetaRepositoryImpl implements MetaRepository {
    
    /** The repositories. */
    private Map repositories = new HashMap();

    /**
     * Sets the repositories.
     *
     * @param repositories
     *            the new repositories
     */
    public void setRepositories(final Map repositories) {
        this.repositories = repositories;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.MetaRepository#getRepository(java.lang.Class)
     */
    @Override
    public ObjectRepository getRepository(final Class objectClass) {
        return (ObjectRepository) this.repositories.get(objectClass);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.repository.MetaRepository#setRepository(java.lang.Class, com.technoetic.xplanner.domain.repository.ObjectRepository)
     */
    @Override
    public void setRepository(final Class objectClass,
            final ObjectRepository repository) {
        this.repositories.put(objectClass, repository);
    }
}
