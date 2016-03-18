package com.technoetic.xplanner.soap;

public class ObjectNotFoundException extends Exception {
	public ObjectNotFoundException() {
	}

	public ObjectNotFoundException(final String message) {
		super(message);
	}

	public ObjectNotFoundException(final Throwable cause) {
		super(cause);
	}

	public ObjectNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ObjectNotFoundException(final Class aClass, final int id,
			final ObjectNotFoundException ex) {
		this(aClass.getName() + " with id=" + id + " not found", ex);
	}
}
