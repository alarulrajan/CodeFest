package com.technoetic.xplanner.security.module.jaas;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * The Class UserIdAndPasswordCallbackHandler.
 */
public class UserIdAndPasswordCallbackHandler implements CallbackHandler {
	
	/** The user id. */
	private final String userId;
	
	/** The password. */
	private final String password;

	/**
     * Instantiates a new user id and password callback handler.
     *
     * @param userId
     *            the user id
     * @param password
     *            the password
     */
	public UserIdAndPasswordCallbackHandler(final String userId,
			final String password) {
		this.userId = userId;
		this.password = password;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
	 */
	@Override
	public void handle(final Callback[] callbacks) throws java.io.IOException,
			UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof NameCallback) {
				((NameCallback) callbacks[i]).setName(this.userId);
			}
			if (callbacks[i] instanceof PasswordCallback) {
				((PasswordCallback) callbacks[i]).setPassword(this.password
						.toCharArray());
			}
		}
	}
}
