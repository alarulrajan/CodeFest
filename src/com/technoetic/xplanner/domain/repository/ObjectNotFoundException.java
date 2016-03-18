package com.technoetic.xplanner.domain.repository;

public class ObjectNotFoundException extends RepositoryException {
	public ObjectNotFoundException(final String message) {
		super(message);
	}
}
