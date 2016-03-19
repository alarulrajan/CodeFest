package com.technoetic.xplanner.security;

import javax.servlet.http.HttpServletRequest;

/**
 * The Class MockAuthenticator.
 */
public class MockAuthenticator implements Authenticator {
   
   /** The authenticate called. */
   public boolean authenticateCalled;
   
   /** The authenticate request. */
   public HttpServletRequest authenticateRequest;
   
   /** The authenticate user id. */
   public String authenticateUserId;
   
   /** The authenticate password. */
   public String authenticatePassword;
   
   /** The authenticate exception. */
   public AuthenticationException authenticateException;

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.security.Authenticator#authenticate(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
    */
   public void authenticate(HttpServletRequest request, String userId, String password) throws AuthenticationException {
      authenticateCalled = true;
      authenticateRequest = request;
      authenticateUserId = userId;
      authenticatePassword = password;
      if (authenticateException != null) {
         throw authenticateException;
      }
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.security.Authenticator#logout(javax.servlet.http.HttpServletRequest, int)
    */
   public void logout(HttpServletRequest request, int principalId) throws AuthenticationException {
      //To change body of implemented methods use File | Settings | File Templates.
   }
}
