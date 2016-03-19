package com.sabre.security.jndi;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.security.auth.Subject;

/**
 * The Interface JNDIAuthenticator.
 */
public interface JNDIAuthenticator {

	/** The message communication error key. */
	String MESSAGE_COMMUNICATION_ERROR_KEY = "authentication.module.message.communicationError";
	
	/** The message authentication failed key. */
	String MESSAGE_AUTHENTICATION_FAILED_KEY = "authentication.module.message.authenticationFailed";

	/**
	 * Authenticate.
	 *
	 * @param username the username
	 * @param credentials the credentials
	 * @return the subject
	 * @throws AuthenticationException the authentication exception
	 */
	Subject authenticate(String username, String credentials)
			throws AuthenticationException;

	/**
	 * Sets the options.
	 *
	 * @param options the new options
	 */
	void setOptions(Map options);

	/**
	 * Sets the digest.
	 *
	 * @param algorithm the new digest
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	void setDigest(String algorithm) throws NoSuchAlgorithmException;

	/**
	 * Sets the user subtree.
	 *
	 * @param userSubtree the new user subtree
	 */
	void setUserSubtree(String userSubtree);

	/**
	 * Sets the role subtree.
	 *
	 * @param roleSubtree the new role subtree
	 */
	void setRoleSubtree(String roleSubtree);

	/**
	 * Sets the context factory.
	 *
	 * @param contextFactory the new context factory
	 */
	void setContextFactory(String contextFactory);

	/**
	 * Sets the authentication.
	 *
	 * @param authentication the new authentication
	 */
	void setAuthentication(String authentication);

	/**
	 * Sets the connection user.
	 *
	 * @param connectionUser the new connection user
	 */
	void setConnectionUser(String connectionUser);

	/**
	 * Sets the connection password.
	 *
	 * @param connectionPassword the new connection password
	 */
	void setConnectionPassword(String connectionPassword);

	/**
	 * Sets the connection url.
	 *
	 * @param connectionURL the new connection url
	 */
	void setConnectionURL(String connectionURL);

	/**
	 * Sets the protocol.
	 *
	 * @param protocol the new protocol
	 */
	void setProtocol(String protocol);

	/**
	 * Sets the referrals.
	 *
	 * @param referrals the new referrals
	 */
	void setReferrals(String referrals);

	/**
	 * Sets the user base.
	 *
	 * @param userBase the new user base
	 */
	void setUserBase(String userBase);

	/**
	 * Sets the user search.
	 *
	 * @param userSearch the new user search
	 */
	void setUserSearch(String userSearch);

	/**
	 * Sets the user password.
	 *
	 * @param userPassword the new user password
	 */
	void setUserPassword(String userPassword);

	/**
	 * Sets the user pattern.
	 *
	 * @param userPattern the new user pattern
	 */
	void setUserPattern(String userPattern);

	/**
	 * Sets the role base.
	 *
	 * @param roleBase the new role base
	 */
	void setRoleBase(String roleBase);

	/**
	 * Sets the user role name.
	 *
	 * @param userRoleName the new user role name
	 */
	void setUserRoleName(String userRoleName);

	/**
	 * Sets the role name.
	 *
	 * @param roleName the new role name
	 */
	void setRoleName(String roleName);

	/**
	 * Sets the role search.
	 *
	 * @param roleSearch the new role search
	 */
	void setRoleSearch(String roleSearch);
}
