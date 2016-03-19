package com.technoetic.xplanner.security.module.ntlm;

import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.SecureRandom;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbSession;

import org.apache.log4j.Priority;

/**
 * The Class NtlmLoginHelperImpl.
 */
public class NtlmLoginHelperImpl implements NtlmLoginHelper {
    
    /** The secure random. */
    private final SecureRandom secureRandom = new SecureRandom();

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.module.ntlm.NtlmLoginHelper#setLoggingPriority(org.apache.log4j.Priority)
     */
    @Override
    public void setLoggingPriority(final Priority loggingPriority) {
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.module.ntlm.NtlmLoginHelper#authenticate(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void authenticate(final String userId, final String password,
            final String domainController, final String domain)
            throws UnknownHostException, SmbException {
        final UniAddress dc = UniAddress.getByName(domainController, true);

        final NtlmPasswordAuthentication ntlm = new NtlmPasswordAuthentication(
                domain, userId, password);

        SmbSession.logon(dc, ntlm);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.security.module.ntlm.NtlmLoginHelper#encodePassword(java.lang.String, byte[])
     */
    @Override
    public String encodePassword(final String password, byte[] salt)
            throws Exception {
        if (salt == null) {
            salt = new byte[12];
            this.secureRandom.nextBytes(salt);
        }

        final MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(salt);
        md.update(password.getBytes("UTF8"));
        final byte[] digest = md.digest();
        final byte[] storedPassword = new byte[digest.length + 12];

        System.arraycopy(salt, 0, storedPassword, 0, 12);
        System.arraycopy(digest, 0, storedPassword, 12, digest.length);

        return new String(
                com.sabre.security.jndi.util.Base64.encode(storedPassword));
    }
}
