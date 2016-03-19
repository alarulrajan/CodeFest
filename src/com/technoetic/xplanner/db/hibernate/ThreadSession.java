package com.technoetic.xplanner.db.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.util.LogUtil;

/**
 * The Class ThreadSession.
 */
public class ThreadSession {
	
	/** The Constant LOG. */
	protected static final Logger LOG = LogUtil.getLogger();
	
	/** The thread session. */
	private static ThreadLocal threadSession = new ThreadLocal();

	/**
     * Gets the.
     *
     * @return the session
     * @deprecated DEBT(SPRING) Should be injected instead
     */
	@Deprecated
	public static Session get() {
		final Object session = ThreadSession.threadSession.get();
		if (ThreadSession.LOG.isDebugEnabled()) {
			ThreadSession.LOG.debug("get() --> " + session);
		}
		return (Session) session;
	}

	/**
     * Sets the.
     *
     * @param session
     *            the session
     */
	public static void set(final Session session) {
		if (ThreadSession.LOG.isDebugEnabled()) {
			ThreadSession.LOG.debug("set(" + session + ")");
		}
		ThreadSession.threadSession.set(session);
	}
}
