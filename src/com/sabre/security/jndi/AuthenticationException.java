package com.sabre.security.jndi;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: sg897500 Date: Oct 14, 2005 Time: 3:14:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticationException extends Exception {

	private static final long serialVersionUID = 201603310102030405L;

	private final Map<String, String> errorByModule = new HashMap<String, String>();

	public AuthenticationException(final String message) {
		super(message);
		this.errorByModule.put("Default", message);
	}

	public AuthenticationException(final String message, final Throwable cause) {
		super(message, cause);
		this.errorByModule.put("", message);
	}

	public AuthenticationException(final Throwable cause) {
		super(cause);
	}

	public AuthenticationException(final Map<String, String> errorByModule) {
		if (errorByModule == null) {
			return;
		}
		this.errorByModule.putAll(errorByModule);
	}

	public Map<String, String> getErrorsByModule() {
		return this.errorByModule;
	}

}
