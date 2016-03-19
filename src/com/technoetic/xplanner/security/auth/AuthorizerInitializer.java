package com.technoetic.xplanner.security.auth;

/**
 * The Class AuthorizerInitializer.
 */
public class AuthorizerInitializer {
    
    /** The authorizer. */
    private final Authorizer authorizer;

    /**
     * Instantiates a new authorizer initializer.
     *
     * @param authorizer
     *            the authorizer
     */
    public AuthorizerInitializer(final Authorizer authorizer) {
        this.authorizer = authorizer;
    }

    /**
     * Inits the.
     */
    public void init() {
        SystemAuthorizer.set(this.authorizer);
    }
}
