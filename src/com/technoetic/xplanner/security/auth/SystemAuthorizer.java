package com.technoetic.xplanner.security.auth;

import org.apache.log4j.Logger;

public class SystemAuthorizer {
	private static Authorizer authorizer;
	private static Logger log = Logger.getLogger(SystemAuthorizer.class);

	/**
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

	public static void set(final Authorizer authorizer) {
		SystemAuthorizer.authorizer = authorizer;
	}
}
