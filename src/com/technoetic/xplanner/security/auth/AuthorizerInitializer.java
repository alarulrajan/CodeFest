package com.technoetic.xplanner.security.auth;

public class AuthorizerInitializer {
	private final Authorizer authorizer;

	public AuthorizerInitializer(final Authorizer authorizer) {
		this.authorizer = authorizer;
	}

	public void init() {
		SystemAuthorizer.set(this.authorizer);
	}
}
