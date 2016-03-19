package com.technoetic.xplanner.security;

import java.io.Serializable;
import java.util.Map;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

/**
 * The Interface LoginModule.
 */
public interface LoginModule extends Serializable {
	
	/** The message null password key. */
	String MESSAGE_NULL_PASSWORD_KEY = "authentication.module.message.passwordNotSet";
	
	/** The message server error key. */
	String MESSAGE_SERVER_ERROR_KEY = "authentication.module.message.serverError";
	
	/** The message authentication failed key. */
	String MESSAGE_AUTHENTICATION_FAILED_KEY = "authentication.module.message.authenticationFailed";
	
	/** The message user not found key. */
	String MESSAGE_USER_NOT_FOUND_KEY = "authentication.module.message.userNotFound";
	
	/** The message communication error key. */
	String MESSAGE_COMMUNICATION_ERROR_KEY = "authentication.module.message.communicationError";
	
	/** The message server not found key. */
	String MESSAGE_SERVER_NOT_FOUND_KEY = "authentication.module.message.serverNotFound";
	
	/** The message configuration error key. */
	String MESSAGE_CONFIGURATION_ERROR_KEY = "authentication.module.message.serverConfigurationError";
	
	/** The message no module name specified error key. */
	String MESSAGE_NO_MODULE_NAME_SPECIFIED_ERROR_KEY = "authentication.module.message.serverConfigurationError.noModuleName";
	
	/** The attempting to authenticate. */
	String ATTEMPTING_TO_AUTHENTICATE = "Attempting to authenticate with login module: ";
	
	/** The authentication succesfull. */
	String AUTHENTICATION_SUCCESFULL = "Authentication successful with login module: ";

	/**
     * Authenticates a user through some specific mechansism.
     *
     * @param userId
     *            the user id
     * @param password
     *            the password
     * @return A Subject containing at least a Person principal and one or more
     *         Role principals.
     * @throws AuthenticationException
     *             the authentication exception
     */
	Subject authenticate(String userId, String password)
			throws AuthenticationException;

	/**
	 * Predicate that indicates whether this module can modify passwords.
	 * 
	 * @return True if password can be changed, false otherwise.
	 */
	boolean isCapableOfChangingPasswords();

	/**
     * Changes a user's password.
     *
     * @param userId
     *            the user id
     * @param password
     *            the password
     * @throws AuthenticationException
     *             if password cannot be changed.
     */
	void changePassword(String userId, String password)
			throws AuthenticationException;

	/**
     * Log out a user. At a minimum this method should invalidate the user's
     * session.
     *
     * @param request
     *            the request
     * @throws AuthenticationException
     *             the authentication exception
     */
	void logout(HttpServletRequest request) throws AuthenticationException;

	/**
     * Gets the name.
     *
     * @return the name
     */
	String getName();

	/**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
	void setName(String name);

	/**
     * Sets the options.
     *
     * @param options
     *            the new options
     */
	void setOptions(Map options);
}
