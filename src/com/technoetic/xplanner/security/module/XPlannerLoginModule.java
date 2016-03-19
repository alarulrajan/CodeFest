package com.technoetic.xplanner.security.module;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Map;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Person;

import org.apache.log4j.Logger;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.LoginModule;

/**
 * The Class XPlannerLoginModule.
 */
public class XPlannerLoginModule implements LoginModule {
    
    /** The log. */
    private transient final Logger log = Logger.getLogger(this.getClass());
    
    /** The secure random. */
    private final transient SecureRandom secureRandom = new SecureRandom();
    
    /** The name. */
    private String name;
    
    /** The login support. */
    private final LoginSupport loginSupport;

    /**
     * Instantiates a new x planner login module.
     *
     * @param support
     *            the support
     */
    public XPlannerLoginModule(final LoginSupport support) {
        this.loginSupport = support;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.LoginModule#setOptions(java.util.Map)
     */
    @Override
    public void setOptions(final Map options) {
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.LoginModule#authenticate(java.lang.String, java.lang.String)
     */
    @Override
    public Subject authenticate(final String userId, final String password)
            throws AuthenticationException {
        this.log.debug(LoginModule.ATTEMPTING_TO_AUTHENTICATE + this.getName()
                + " (" + userId + ")");
        final Subject subject = this.loginSupport.createSubject();
        final Person person = this.loginSupport
                .populateSubjectPrincipalFromDatabase(subject, userId);
        if (!this.isPasswordMatched(person, password)) {
            throw new AuthenticationException(
                    LoginModule.MESSAGE_AUTHENTICATION_FAILED_KEY);
        }
        this.log.debug(LoginModule.AUTHENTICATION_SUCCESFULL + this.getName());
        return subject;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.LoginModule#isCapableOfChangingPasswords()
     */
    @Override
    public boolean isCapableOfChangingPasswords() {
        return true;
    }

    /**
     * Checks if is password matched.
     *
     * @param person
     *            the person
     * @param password
     *            the password
     * @return true, if is password matched
     * @throws AuthenticationException
     *             the authentication exception
     */
    boolean isPasswordMatched(final Person person, final String password)
            throws AuthenticationException {
        this.log.debug("evaluating password match for " + person.getUserId());
        final String storedPassword = person.getPassword();
        if (storedPassword == null) {
            throw new AuthenticationException(
                    LoginModule.MESSAGE_NULL_PASSWORD_KEY);
        }
        final byte[] storedPasswordBytesWithSalt = com.sabre.security.jndi.util.Base64
                .decode(storedPassword.getBytes());

        if (storedPasswordBytesWithSalt.length < 12) {
            throw new AuthenticationException(
                    LoginModule.MESSAGE_AUTHENTICATION_FAILED_KEY);
        }
        final byte[] salt = new byte[12];
        System.arraycopy(storedPasswordBytesWithSalt, 0, salt, 0, 12);

        final byte[] digestForGivenPassword = this.digestPassword(salt,
                password);
        final byte[] digestForExistingPassword = new byte[storedPasswordBytesWithSalt.length - 12];
        System.arraycopy(storedPasswordBytesWithSalt, 12,
                digestForExistingPassword, 0,
                storedPasswordBytesWithSalt.length - 12);

        final boolean isMatching = Arrays.equals(digestForGivenPassword,
                digestForExistingPassword);
        this.log.debug("passwords "
                + (isMatching ? "matched" : "did not match") + " for "
                + person.getUserId());
        return isMatching;
    }

    /**
     * Digest password.
     *
     * @param salt
     *            the salt
     * @param password
     *            the password
     * @return the byte[]
     * @throws AuthenticationException
     *             the authentication exception
     */
    private byte[] digestPassword(final byte[] salt, final String password)
            throws AuthenticationException {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt);
            md.update(password.getBytes("UTF8"));
            return md.digest();
        } catch (final Exception e) {
            throw new AuthenticationException(
                    LoginModule.MESSAGE_CONFIGURATION_ERROR_KEY, e);
        }
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.LoginModule#changePassword(java.lang.String, java.lang.String)
     */
    @Override
    public void changePassword(final String userId, final String password)
            throws AuthenticationException {
        this.log.debug("changing password for " + userId);
        try {
            final Session session = ThreadSession.get();
            session.connection().setAutoCommit(false);
            try {
                final Person person = this.loginSupport.getPerson(userId);
                if (person != null) {
                    person.setPassword(this.encodePassword(password, null));
                    session.flush();
                    session.connection().commit();
                } else {
                    throw new AuthenticationException("couldn't find person.");
                }
            } catch (final Exception ex) {
                session.connection().rollback();
                this.log.error("error during password change.", ex);
                throw new AuthenticationException("server error.");
            }

        } catch (final Exception e) {
            this.log.error("error", e);
            throw new AuthenticationException("server error.");
        }
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.LoginModule#logout(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void logout(final HttpServletRequest request)
            throws AuthenticationException {
        request.getSession().invalidate();
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.LoginModule#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.LoginModule#setName(java.lang.String)
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Encode password.
     *
     * @param password
     *            the password
     * @param salt
     *            the salt
     * @return the string
     * @throws Exception
     *             the exception
     */
    public String encodePassword(final String password, byte[] salt)
            throws Exception {
        if (salt == null) {
            salt = new byte[12];
            this.secureRandom.nextBytes(salt);
        }

        final byte[] digest = this.digestPassword(salt, password);
        final byte[] storedPassword = new byte[digest.length + 12];

        System.arraycopy(salt, 0, storedPassword, 0, 12);
        System.arraycopy(digest, 0, storedPassword, 12, digest.length);

        return new String(
                com.sabre.security.jndi.util.Base64.encode(storedPassword));
    }

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    // This main can be used to generate a hashed password by hand, if needed.
    public static void main(final String[] args) {
        try {
            String password;
            if (args.length == 0) {
                password = "admin";
            } else {
                password = args[0];
            }
            System.out.println(new XPlannerLoginModule(new LoginSupportImpl())
                    .encodePassword(password, null));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
