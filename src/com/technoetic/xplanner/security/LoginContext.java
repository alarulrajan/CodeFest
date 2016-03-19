package com.technoetic.xplanner.security;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.security.module.ConfigurationException;
import com.technoetic.xplanner.security.module.LoginModuleLoader;
import com.technoetic.xplanner.util.LogUtil;

/**
 * The Class LoginContext.
 */
public class LoginContext implements Serializable {
    
    /** The Constant LOG. */
    protected static final Logger LOG = LogUtil.getLogger();

    /** The login modules. */
    private transient LoginModule[] loginModules;
    
    /** The module loader. */
    private transient LoginModuleLoader moduleLoader;
    
    /** The subject. */
    private Subject subject;
    
    /** The login module index. */
    private int loginModuleIndex;

    /**
     * Instantiates a new login context.
     *
     * @param moduleLoader
     *            the module loader
     */
    public LoginContext(final LoginModuleLoader moduleLoader) {
        this.moduleLoader = moduleLoader;
    }

    /**
     * Authenticate.
     *
     * @param userId
     *            the user id
     * @param password
     *            the password
     * @throws AuthenticationException
     *             the authentication exception
     */
    public void authenticate(final String userId, final String password)
            throws AuthenticationException {
        final HashMap errorMap = new HashMap();
        final LoginModule[] modules = this.getLoginModules();
        LoginModule loginModule = null;
        for (int i = 0; i < modules.length; i++) {
            try {
                loginModule = modules[i];
                this.subject = loginModule.authenticate(userId, password);
                this.loginModuleIndex = i;
                LoginContext.LOG.debug("Authenticating successfully " + this);
                return;
            } catch (final AuthenticationException aex) {
                errorMap.put(loginModule.getName(), aex.getMessage());
            }
        }
        this.loginModuleIndex = -1;
        LoginContext.LOG.debug("Failure to authenticate " + this);
        throw new AuthenticationException(errorMap);
    }

    /**
     * Gets the login module.
     *
     * @return the login module
     * @throws ConfigurationException
     *             the configuration exception
     */
    public LoginModule getLoginModule() throws ConfigurationException {
        final LoginModule[] modules = this.getLoginModules();
        if (this.loginModuleIndex >= modules.length) {
            throw new RuntimeException("index of used login module="
                    + this.loginModuleIndex + ", number of modules="
                    + modules.length);
        }
        return modules[this.loginModuleIndex];
    }

    /**
     * Gets the subject.
     *
     * @return the subject
     */
    public Subject getSubject() {
        return this.subject;
    }

    /**
     * Logout.
     *
     * @param request
     *            the request
     * @throws AuthenticationException
     *             the authentication exception
     */
    public void logout(final HttpServletRequest request)
            throws AuthenticationException {
        final LoginModule[] modules = this.getLoginModules();
        for (int i = 0; i < modules.length; i++) {
            modules[i].logout(request);
        }
    }

    /**
     * Gets the login modules.
     *
     * @return the login modules
     * @throws ConfigurationException
     *             the configuration exception
     */
    protected LoginModule[] getLoginModules() throws ConfigurationException {
        if (this.loginModules == null) {
            this.loginModules = this.moduleLoader.loadLoginModules();
        }
        return this.loginModules;
    }

    // DEBT: The deserialization creating new LoginModule should be cleaned up
    /**
     * Read object.
     *
     * @param in
     *            the in
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException
     *             the class not found exception
     */
    // when we have a bean context per session/request...
    private final void readObject(final ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.loginModules = this.getLoginModules();
        LoginContext.LOG.debug("Deserializing... " + this);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "LoginContext{"
                + "subject="
                + this.subject
                + ", loginModuleIndex="
                + this.loginModuleIndex
                + ", loginModules ="
                + (this.loginModules == null ? null : Arrays
                        .asList(this.loginModules)) + ", moduleLoader="
                + this.moduleLoader + '}';
    }
}
