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

/**
 * A factory for creating XPlannerSession objects.
 */
public class XPlannerSessionFactory implements SessionFactory {
    
    /** The Constant LOG. */
    protected static final Logger LOG = LogUtil.getLogger();

    /** The delegate. */
    private final SessionFactory delegate;

    /** Instantiates a new x planner session factory.
     *
     * @param delegate
     *            the delegate
     */
    public XPlannerSessionFactory(final SessionFactory delegate) {
        this.delegate = delegate;
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#close()
     */
    @Override
    public void close() throws HibernateException {
        this.delegate.close();
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#evict(java.lang.Class)
     */
    @Override
    public void evict(final Class persistentClass) throws HibernateException {
        this.delegate.evict(persistentClass);
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#evict(java.lang.Class, java.io.Serializable)
     */
    @Override
    public void evict(final Class persistentClass, final Serializable id)
            throws HibernateException {
        this.delegate.evict(persistentClass, id);
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#evictCollection(java.lang.String)
     */
    @Override
    public void evictCollection(final String roleName)
            throws HibernateException {
        this.delegate.evictCollection(roleName);
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#evictCollection(java.lang.String, java.io.Serializable)
     */
    @Override
    public void evictCollection(final String roleName, final Serializable id)
            throws HibernateException {
        this.delegate.evictCollection(roleName, id);
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#evictQueries()
     */
    @Override
    public void evictQueries() throws HibernateException {
        this.delegate.evictQueries();
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#evictQueries(java.lang.String)
     */
    @Override
    public void evictQueries(final String cacheRegion)
            throws HibernateException {
        this.delegate.evictQueries(cacheRegion);
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#getAllClassMetadata()
     */
    @Override
    public Map getAllClassMetadata() throws HibernateException {
        return this.delegate.getAllClassMetadata();
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#getAllCollectionMetadata()
     */
    @Override
    public Map getAllCollectionMetadata() throws HibernateException {
        return this.delegate.getAllCollectionMetadata();
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#getClassMetadata(java.lang.Class)
     */
    @Override
    public ClassMetadata getClassMetadata(final Class persistentClass)
            throws HibernateException {
        return this.delegate.getClassMetadata(persistentClass);
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#getCollectionMetadata(java.lang.String)
     */
    @Override
    public CollectionMetadata getCollectionMetadata(final String roleName)
            throws HibernateException {
        return this.delegate.getCollectionMetadata(roleName);
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#openSession()
     */
    @Override
    public Session openSession() throws HibernateException {
        final Session session = this.delegate
                .openSession(new XPlannerInterceptor());
        return this.logAndWrapNewSessionIfDebug(session);
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#openSession(java.sql.Connection)
     */
    @Override
    public Session openSession(final Connection connection) {
        final Session session = this.delegate.openSession(connection,
                new XPlannerInterceptor());
        return this.logAndWrapNewSessionIfDebug(session);
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#openSession(java.sql.Connection, org.hibernate.Interceptor)
     */
    @Override
    public Session openSession(final Connection connection,
            final Interceptor interceptor) {
        final Session session = this.delegate.openSession(connection,
                interceptor);
        return this.logAndWrapNewSessionIfDebug(session);
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#openSession(org.hibernate.Interceptor)
     */
    @Override
    public Session openSession(final Interceptor interceptor)
            throws HibernateException {
        final Session session = this.delegate.openSession(interceptor);
        return this.logAndWrapNewSessionIfDebug(session);
    }

    /** Log and wrap new session if debug.
     *
     * @param session
     *            the session
     * @return the session
     */
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

    /* (non-Javadoc)
     * @see javax.naming.Referenceable#getReference()
     */
    @Override
    public Reference getReference() throws NamingException {
        return this.delegate.getReference();
    }

    /** The last id. */
    private static int lastId = 0;
    
    /** The session count. */
    private static int sessionCount = 0;

    /** The Class SessionCountingInvoker.
     */
    class SessionCountingInvoker implements Invoker {
        
        /** The session. */
        private final Session session;
        
        /** The id. */
        private final int id;
        
        /** The invoker. */
        private final Invoker invoker;
        
        /** The creation stack. */
        private final StackTraceElement[] creationStack;

        /** Instantiates a new session counting invoker.
         *
         * @param session
         *            the session
         * @param invoker
         *            the invoker
         */
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

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "session #" + this.id + " (" + this.session + ")";
        }

        /** Gets the stack trace for method.
         *
         * @param methodName
         *            the method name
         * @return the stack trace for method
         */
        private StackTraceElement[] getStackTraceForMethod(
                final String methodName) {
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            stackTrace = this.pruneTopDownToOpenSessionFrame(stackTrace,
                    methodName);
            stackTrace = this.pruneBottomUpToFirstXPlannerFrame(stackTrace);
            return stackTrace;
        }

        /** Prune bottom up to first x planner frame.
         *
         * @param stackTrace
         *            the stack trace
         * @return the stack trace element[]
         */
        private StackTraceElement[] pruneBottomUpToFirstXPlannerFrame(
                final StackTraceElement[] stackTrace) {
            final int firstXPlannerFrameIndex = this
                    .findFirstXPlannerFrameIndex(stackTrace);
            final StackTraceElement[] prunedStackTrace = new StackTraceElement[firstXPlannerFrameIndex + 1];
            System.arraycopy(stackTrace, 0, prunedStackTrace, 0,
                    firstXPlannerFrameIndex + 1);
            return prunedStackTrace;
        }

        /** Find first x planner frame index.
         *
         * @param stackTrace
         *            the stack trace
         * @return the int
         */
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

        /** Prune top down to open session frame.
         *
         * @param stackTrace
         *            the stack trace
         * @param methodName
         *            the method name
         * @return the stack trace element[]
         */
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

        /** Find first top frame index for method.
         *
         * @param stackTrace
         *            the stack trace
         * @param methodName
         *            the method name
         * @return the int
         */
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

        /** Gets the stack trace string.
         *
         * @param stackTrace
         *            the stack trace
         * @return the stack trace string
         */
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

        /* (non-Javadoc)
         * @see com.thoughtworks.proxy.Invoker#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
         */
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
    }

    /** The Class Log4JDebugLoggerWriter.
     */
    private static class Log4JDebugLoggerWriter extends Writer {
        
        /** The buf. */
        StringBuffer buf = new StringBuffer();

        /* (non-Javadoc)
         * @see java.io.Writer#close()
         */
        @Override
        public void close() {
        }

        /* (non-Javadoc)
         * @see java.io.Writer#flush()
         */
        @Override
        public void flush() {
            XPlannerSessionFactory.LOG.debug(this.buf.toString());
            this.buf = new StringBuffer();
        }

        /* (non-Javadoc)
         * @see java.io.Writer#write(char[], int, int)
         */
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
        // ChangeSoon 

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
        // ChangeSoon 

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.SessionFactory#getClassMetadata(java.lang.String)
     */
    @Override
    public ClassMetadata getClassMetadata(final String s)
            throws HibernateException {
        // ChangeSoon 
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.SessionFactory#getCurrentSession()
     */
    @Override
    public Session getCurrentSession() throws HibernateException {
        // ChangeSoon 
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.SessionFactory#getDefinedFilterNames()
     */
    @Override
    public Set getDefinedFilterNames() {
        // ChangeSoon 
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
        // ChangeSoon 
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.SessionFactory#getStatistics()
     */
    @Override
    public Statistics getStatistics() {
        // ChangeSoon 
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.SessionFactory#isClosed()
     */
    @Override
    public boolean isClosed() {
        // ChangeSoon 
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.SessionFactory#openStatelessSession()
     */
    @Override
    public StatelessSession openStatelessSession() {
        // ChangeSoon 
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
        // ChangeSoon 
        return null;
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#getCache()
     */
    @Override
    public Cache getCache() {
        // ChangeSoon 
        return null;
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#containsFetchProfileDefinition(java.lang.String)
     */
    @Override
    public boolean containsFetchProfileDefinition(final String name) {
        // ChangeSoon 
        return false;
    }

    /* (non-Javadoc)
     * @see org.hibernate.SessionFactory#getTypeHelper()
     */
    @Override
    public TypeHelper getTypeHelper() {
        // ChangeSoon 
        return null;
    }
}