package com.technoetic.xplanner.db;

public class QueryException extends Exception {
	public QueryException() {
	}

	public QueryException(final Throwable cause) {
		super(cause);
	}

	public QueryException(final String message) {
		super(message);
	}

	public QueryException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
