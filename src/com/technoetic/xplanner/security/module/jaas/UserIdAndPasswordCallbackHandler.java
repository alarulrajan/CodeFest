package com.technoetic.xplanner.security.module.jaas;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class UserIdAndPasswordCallbackHandler implements CallbackHandler {
	private final String userId;
	private final String password;

	public UserIdAndPasswordCallbackHandler(final String userId,
			final String password) {
		this.userId = userId;
		this.password = password;
	}

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
