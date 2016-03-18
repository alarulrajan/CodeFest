package com.technoetic.xplanner.export;

public class ExportException extends Exception {
	public ExportException() {
	}

	public ExportException(final String message) {
		super(message);
	}

	public ExportException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ExportException(final Throwable cause) {
		super(cause);
	}
}
