package com.technoetic.xplanner.security.auth;

import junit.framework.TestCase;

/**
 * The Class TestAuthorizerInitializer.
 */
public class TestAuthorizerInitializer extends TestCase {
    
    /** The authorizer initializer. */
    AuthorizerInitializer authorizerInitializer;
    
    /** The authorizer. */
    private MockAuthorizer authorizer;

    /** Test init.
     *
     * @throws Exception
     *             the exception
     */
    public void testInit() throws Exception {
        SystemAuthorizer.set(null);
        assertNull(SystemAuthorizer.get());
        authorizer = new MockAuthorizer();
        authorizerInitializer = new AuthorizerInitializer(authorizer);
        authorizerInitializer.init();
        assertSame(authorizer,  SystemAuthorizer.get());
    }
}