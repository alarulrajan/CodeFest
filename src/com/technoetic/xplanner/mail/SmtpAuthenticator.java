package com.technoetic.xplanner.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * The Class SmtpAuthenticator.
 */
public class SmtpAuthenticator extends Authenticator {
    
    /** The username. */
    private final String username;
    
    /** The password. */
    private final String password;

    /**
     * Instantiates a new smtp authenticator.
     *
     * @param username
     *            the username
     * @param password
     *            the password
     */
    public SmtpAuthenticator(final String username, final String password) {
        super();
        this.username = username;
        this.password = password;
    }

    /* (non-Javadoc)
     * @see javax.mail.Authenticator#getPasswordAuthentication()
     */
    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.username, this.password);
    }
}