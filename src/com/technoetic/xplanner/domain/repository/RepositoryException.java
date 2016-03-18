package com.technoetic.xplanner.domain.repository;

public class RepositoryException extends Exception {
	public RepositoryException() {
	}

	public RepositoryException(final Throwable cause) {
		super(cause);
	}

	public RepositoryException(final String message) {
		super(message);
	}

	public RepositoryException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
