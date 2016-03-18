package com.technoetic.xplanner.db.hibernate;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.Reference;

import org.apache.log4j.Logger;
import org.hibernate.Cache;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.TypeHelper;
import org.hibernate.classic.Session;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

import com.technoetic.xplanner.util.LogUtil;
import com.thoughtworks.proxy.Invoker;
import com.thoughtworks.proxy.factory.StandardProxyFactory;
import com.thoughtworks.proxy.toys.echo.Echoing;

public class XPlannerSessionFactory implements SessionFactory {
	protected static final Logger LOG = LogUtil.getLogger();

	private final SessionFactory delegate;

	public XPlannerSessionFactory(final SessionFactory delegate) {
		this.delegate = delegate;
	}

	@Override
	public void close() throws HibernateException {
		this.delegate.close();
	}

	@Override
	public void evict(final Class persistentClass) throws HibernateException {
		this.delegate.evict(persistentClass);
	}

	@Override
	public void evict(final Class persistentClass, final Serializable id)
			throws HibernateException {
		this.delegate.evict(persistentClass, id);
	}

	@Override
	public void evictCollection(final String roleName)
			throws HibernateException {
		this.delegate.evictCollection(roleName);
	}

	@Override
	public void evictCollection(final String roleName, final Serializable id)
			throws HibernateException {
		this.delegate.evictCollection(roleName, id);
	}

	@Override
	public void evictQueries() throws HibernateException {
		this.delegate.evictQueries();
	}

	@Override
	public void evictQueries(final String cacheRegion)
			throws HibernateException {
		this.delegate.evictQueries(cacheRegion);
	}

	@Override
	public Map getAllClassMetadata() throws HibernateException {
		return this.delegate.getAllClassMetadata();
	}

	@Override
	public Map getAllCollectionMetadata() throws HibernateException {
		return this.delegate.getAllCollectionMetadata();
	}

	@Override
	public ClassMetadata getClassMetadata(final Class persistentClass)
			throws HibernateException {
		return this.delegate.getClassMetadata(persistentClass);
	}

	@Override
	public CollectionMetadata getCollectionMetadata(final String roleName)
			throws HibernateException {
		return this.delegate.getCollectionMetadata(roleName);
	}

	@Override
	public Session openSession() throws HibernateException {
		final Session session = this.delegate
				.openSession(new XPlannerInterceptor());
		return this.logAndWrapNewSessionIfDebug(session);
	}

	@Override
	public Session openSession(final Connection connection) {
		final Session session = this.delegate.openSession(connection,
				new XPlannerInterceptor());
		return this.logAndWrapNewSessionIfDebug(session);
	}

	@Override
	public Session openSession(final Connection connection,
			final Interceptor interceptor) {
		final Session session = this.delegate.openSession(connection,
				interceptor);
		return this.logAndWrapNewSessionIfDebug(session);
	}

	@Override
	public Session openSession(final Interceptor interceptor)
			throws HibernateException {
		final Session session = this.delegate.openSession(interceptor);
		return this.logAndWrapNewSessionIfDebug(session);
	}

	private Session logAndWrapNewSessionIfDebug(final Session session) {
		if (XPlannerSessionFactory.LOG.isDebugEnabled()) {
			final PrintWriter out = new PrintWriter(
					new Log4JDebugLoggerWriter());
			return (Session) Echoing.object(Session.class, session, out,
					new StandardProxyFactory() {
						@Override
						public boolean canProxy(final Class type) {
							return Session.class.isAssignableFrom(type);
						}

						@Override
						public Object createProxy(final Class[] types,
								final Invoker invoker) {
							return super
									.createProxy(types,
											new SessionCountingInvoker(session,
													invoker));
						}
					});
		}
		return session;
	}

	@Override
	public Reference getReference() throws NamingException {
		return this.delegate.getReference();
	}

	private static int lastId = 0;
	private static int sessionCount = 0;

	class SessionCountingInvoker implements Invoker {
		private final Session session;
		private final int id;
		private final Invoker invoker;
		private final StackTraceElement[] creationStack;

		public SessionCountingInvoker(final Session session,
				final Invoker invoker) {
			this.session = session;
			this.invoker = invoker;
			this.creationStack = this.getStackTraceForMethod("openSession");
			this.id = ++XPlannerSessionFactory.lastId;
			XPlannerSessionFactory.LOG.debug("Session.new() -> "
					+ ++XPlannerSessionFactory.sessionCount + " opened. "
					+ this + " was opened:\n"
					+ this.getStackTraceString(this.creationStack));
		}

		@Override
		public String toString() {
			return "session #" + this.id + " (" + this.session + ")";
		}

		private StackTraceElement[] getStackTraceForMethod(
				final String methodName) {
			StackTraceElement[] stackTrace = new Throwable().getStackTrace();
			stackTrace = this.pruneTopDownToOpenSessionFrame(stackTrace,
					methodName);
			stackTrace = this.pruneBottomUpToFirstXPlannerFrame(stackTrace);
			return stackTrace;
		}

		private StackTraceElement[] pruneBottomUpToFirstXPlannerFrame(
				final StackTraceElement[] stackTrace) {
			final int firstXPlannerFrameIndex = this
					.findFirstXPlannerFrameIndex(stackTrace);
			final StackTraceElement[] prunedStackTrace = new StackTraceElement[firstXPlannerFrameIndex + 1];
			System.arraycopy(stackTrace, 0, prunedStackTrace, 0,
					firstXPlannerFrameIndex + 1);
			return prunedStackTrace;
		}

		private int findFirstXPlannerFrameIndex(
				final StackTraceElement[] stackTrace) {
			for (int i = stackTrace.length - 1; i >= 0; i--) {
				final StackTraceElement frame = stackTrace[i];
				if (frame.getClassName().indexOf("xplanner") != -1
						|| frame.getClassName().indexOf("springframework") != -1) {
					return i;
				}
			}
			return stackTrace.length - 1;
		}

		private StackTraceElement[] pruneTopDownToOpenSessionFrame(
				final StackTraceElement[] stackTrace, final String methodName) {
			final int openSessionFrameIndex = this
					.findFirstTopFrameIndexForMethod(stackTrace, methodName);
			final StackTraceElement[] prunedStackTrace = new StackTraceElement[stackTrace.length
					- openSessionFrameIndex];
			System.arraycopy(stackTrace, openSessionFrameIndex,
					prunedStackTrace, 0, prunedStackTrace.length);
			return prunedStackTrace;
		}

		private int findFirstTopFrameIndexForMethod(
				final StackTraceElement[] stackTrace, final String methodName) {
			for (int i = 0; i < stackTrace.length; i++) {
				final StackTraceElement frame = stackTrace[i];
				if (methodName.equals(frame.getMethodName())) {
					return i;
				}
			}
			return 0;
		}

		private String getStackTraceString(final StackTraceElement[] stackTrace) {
			final StringBuffer buf = new StringBuffer();
			buf.append("\n");
			for (int i = 0; i < stackTrace.length; i++) {
				final StackTraceElement frame = stackTrace[i];
				buf.append("\tat " + frame + "\n");
			}
			buf.append("\n");
			return buf.toString();
		}

		@Override
		public Object invoke(final Object proxy, final Method method,
				final Object[] args) throws Throwable {
			final boolean connected = this.session.isConnected();
			try {
				return this.invoker.invoke(proxy, method, args);
			} finally {
				if (method.getName().equals("close")) {
					if (connected) {
						--XPlannerSessionFactory.sessionCount;
					}
					XPlannerSessionFactory.LOG.debug("Session.close() -> "
							+ (!connected ? "did not close session. " : " ")
							+ XPlannerSessionFactory.sessionCount
							+ " still opened. "
							+ this
							+ " was closed:\n"
							+ this.getStackTraceString(this
									.getStackTraceForMethod("close")));
				}
			}
		}

		@Override
		protected void finalize() throws Throwable {
			if (this.session.isOpen()) {
				XPlannerSessionFactory.LOG
						.debug("     ############# Session.finalize() -> "
								+ this + " was not closed ###############\n"
								+ "Session was allocated from:\n"
								+ this.getStackTraceString(this.creationStack));
			}
			super.finalize();
		}
	}

	private static class Log4JDebugLoggerWriter extends Writer {
		StringBuffer buf = new StringBuffer();

		@Override
		public void close() {
		}

		@Override
		public void flush() {
			XPlannerSessionFactory.LOG.debug(this.buf.toString());
			this.buf = new StringBuffer();
		}

		@Override
		public void write(final char[] cbuf, final int off, final int len) {
			this.buf.append(cbuf, off, len);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.SessionFactory#evictEntity(java.lang.String)
	 */
	@Override
	public void evictEntity(final String s) throws HibernateException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.SessionFactory#evictEntity(java.lang.String,
	 * java.io.Serializable)
	 */
	@Override
	public void evictEntity(final String s, final Serializable serializable)
			throws HibernateException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.SessionFactory#getClassMetadata(java.lang.String)
	 */
	@Override
	public ClassMetadata getClassMetadata(final String s)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.SessionFactory#getCurrentSession()
	 */
	@Override
	public Session getCurrentSession() throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.SessionFactory#getDefinedFilterNames()
	 */
	@Override
	public Set getDefinedFilterNames() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.SessionFactory#getFilterDefinition(java.lang.String)
	 */
	@Override
	public FilterDefinition getFilterDefinition(final String s)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.SessionFactory#getStatistics()
	 */
	@Override
	public Statistics getStatistics() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.SessionFactory#isClosed()
	 */
	@Override
	public boolean isClosed() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.SessionFactory#openStatelessSession()
	 */
	@Override
	public StatelessSession openStatelessSession() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.SessionFactory#openStatelessSession(java.sql.Connection)
	 */
	@Override
	public StatelessSession openStatelessSession(final Connection connection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cache getCache() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsFetchProfileDefinition(final String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TypeHelper getTypeHelper() {
		// TODO Auto-generated method stub
		return null;
	}
}