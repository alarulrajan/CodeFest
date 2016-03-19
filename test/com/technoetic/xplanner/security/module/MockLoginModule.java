package com.technoetic.xplanner.security.module;

import java.util.Map;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.LoginModule;

/**
 * The Class MockLoginModule.
 */
public class MockLoginModule implements LoginModule {
    
    /** The options. */
    public Map options;
    
    /** The name. */
    private String name = null;

    /** Instantiates a new mock login module.
     */
    public MockLoginModule() {
    }

    /** The authenticate called. */
    public boolean authenticateCalled;
    
    /** The authenticate user id. */
    public String authenticateUserId;
    
    /** The authenticate password. */
    public String authenticatePassword;
    
    /** The authenticate return. */
    public Subject authenticateReturn;
    
    /** The authenticate exception. */
    public AuthenticationException authenticateException;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.LoginModule#authenticate(java.lang.String, java.lang.String)
     */
    public Subject authenticate(String userId, String password) throws AuthenticationException {
        authenticateCalled = true;
        authenticateUserId = userId;
        authenticatePassword = password;
        if (authenticateException != null) {
            throw authenticateException;
        }
        return authenticateReturn;
    }

    /** The is capable of changing passwords return. */
    public Boolean isCapableOfChangingPasswordsReturn;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.LoginModule#isCapableOfChangingPasswords()
     */
    public boolean isCapableOfChangingPasswords() {
        return isCapableOfChangingPasswordsReturn.booleanValue();
    }

    /** The change password called. */
    public boolean changePasswordCalled;
    
    /** The change password user id. */
    public String changePasswordUserId;
    
    /** The change password password. */
    public String changePasswordPassword;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.LoginModule#changePassword(java.lang.String, java.lang.String)
     */
    public void changePassword(String userId, String password)
            throws AuthenticationException {
        changePasswordCalled = true;
        changePasswordUserId = userId;
        changePasswordPassword = password;
    }

    /** The logout called. */
    public boolean logoutCalled;
    
    /** The logout request. */
    public HttpServletRequest logoutRequest;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.LoginModule#logout(javax.servlet.http.HttpServletRequest)
     */
    public void logout(HttpServletRequest request) throws AuthenticationException {
        logoutCalled = true;
        logoutRequest = request;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.LoginModule#getName()
     */
    public String getName()
    {
        return name;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.LoginModule#setName(java.lang.String)
     */
    public void setName(String name)
    {
        this.name = name;
    }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.security.LoginModule#setOptions(java.util.Map)
    */
   public void setOptions(Map options) {
      this.options = options;
   }
}
