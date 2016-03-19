package com.technoetic.xplanner.security.module.ntlm;

import java.net.UnknownHostException;

import jcifs.smb.SmbException;

import org.apache.log4j.Priority;

/**
 * User: Mateusz Prokopowicz Date: Mar 10, 2005 Time: 4:26:59 PM.
 */
public interface NtlmLoginHelper {
	
	/**
     * Sets the logging priority.
     *
     * @param loggingPriority
     *            the new logging priority
     */
	void setLoggingPriority(Priority loggingPriority);

	/**
     * Authenticate.
     *
     * @param userId
     *            the user id
     * @param password
     *            the password
     * @param domainController
     *            the domain controller
     * @param domain
     *            the domain
     * @throws UnknownHostException
     *             the unknown host exception
     * @throws SmbException
     *             the smb exception
     */
	void authenticate(String userId, String password, String domainController,
			String domain) throws UnknownHostException, SmbException;

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
	String encodePassword(String password, byte[] salt) throws Exception;
}
