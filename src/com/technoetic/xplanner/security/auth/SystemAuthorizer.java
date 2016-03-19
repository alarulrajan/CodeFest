package com.technoetic.xplanner.security.auth;

import org.apache.log4j.Logger;

/**
 * The Class SystemAuthorizer.
 */
public class SystemAuthorizer {
	
	/** The authorizer. */
	private static Authorizer authorizer;
	
	/** The log. */
	private static Logger log = Logger.getLogger(SystemAuthorizer.class);

	/**
     * Gets the.
     *
     * @return the authorizer
     * @deprecated DEBT(SPRING) Should be injected
     */
	@Deprecated
	public static Authorizer get() {
		if (SystemAuthorizer.authorizer == null) {
			SystemAuthorizer.log
					.warn("The authorizer has not been set yet $$$$$$$$");
		}

		return SystemAuthorizer.authorizer;
	}

	/**
     * Sets the.
     *
     * @param authorizer
     *            the authorizer
     */
	public static void set(final Authorizer authorizer) {
		SystemAuthorizer.authorizer = authorizer;
	}
}
