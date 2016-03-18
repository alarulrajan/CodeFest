package com.technoetic.xplanner.domain.repository;

import java.util.HashMap;
import java.util.Map;

public class MetaRepositoryImpl implements MetaRepository {
	private Map repositories = new HashMap();

	public void setRepositories(final Map repositories) {
		this.repositories = repositories;
	}

	@Override
	public ObjectRepository getRepository(final Class objectClass) {
		return (ObjectRepository) this.repositories.get(objectClass);
	}

	@Override
	public void setRepository(final Class objectClass,
			final ObjectRepository repository) {
		this.repositories.put(objectClass, repository);
	}
}
