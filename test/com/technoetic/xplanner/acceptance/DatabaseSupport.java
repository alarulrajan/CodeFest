package com.technoetic.xplanner.acceptance;

import java.sql.SQLException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.mocks.hibernate.MockSessionFactory;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.PersistentObjectMother;

/**
 * The Class DatabaseSupport.
 */
public class DatabaseSupport {
   
   /** The session. */
   protected Session session;
   
   /** The previous logging level. */
   protected Level previousLoggingLevel;
   
   /** The mom. */
   PersistentObjectMother mom;

   /** Instantiates a new database support.
     */
   public DatabaseSupport() { }

   /** Sets the up.
     *
     * @throws Exception
     *             the exception
     */
   protected void setUp() throws Exception {
      previousLoggingLevel = Logger.getRootLogger().getLevel();
      Logger.getRootLogger().setLevel(Level.WARN);
      initializeHibernate();
      mom = new PersistentObjectMother();
      openSession();
   }

   /** Initialize hibernate.
     *
     * @throws HibernateException
     *             the hibernate exception
     */
   private void initializeHibernate() throws HibernateException {
      if (isMockFactoryInstalled()) GlobalSessionFactory.set(null);
      HibernateHelper.initializeHibernate();
   }

   /** Checks if is mock factory installed.
     *
     * @return true, if is mock factory installed
     */
   private boolean isMockFactoryInstalled() {
      return GlobalSessionFactory.get() != null && GlobalSessionFactory.get() instanceof MockSessionFactory;
   }

   /** Tear down.
     *
     * @throws Exception
     *             the exception
     */
   protected void tearDown() throws Exception {
      try {
         openSession();
         mom.deleteTestObjects();
         Logger.getRootLogger().setLevel(previousLoggingLevel);
         commitSession();
      } catch (Exception e) {
         e.printStackTrace();
         rollbackSession();
         throw e;
      } finally {
         closeSession();
      }
   }


   /** Commit close and open session.
     *
     * @return the session
     * @throws HibernateException
     *             the hibernate exception
     * @throws SQLException
     *             the SQL exception
     */
   public Session commitCloseAndOpenSession() throws HibernateException, SQLException {
      commitSession();
      closeSession();
      return openSession();
   }

   /** Open session.
     *
     * @return the session
     * @throws HibernateException
     *             the hibernate exception
     */
   public Session openSession() throws HibernateException {
      if (isSessionOpened()) return session;
      session = GlobalSessionFactory.get().openSession();
      ThreadSession.set(session);
      mom.setSession(session);
      return session;
   }

   /** Rollback session.
     */
   public void rollbackSession() {
      if (!isSessionOpened()) return;
      try {
         session.connection().rollback();
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   /** Commit session.
     *
     * @throws HibernateException
     *             the hibernate exception
     * @throws SQLException
     *             the SQL exception
     */
   public void commitSession() throws HibernateException, SQLException {
      if (!isSessionOpened()) return;
      session.flush();
      session.connection().commit();
   }

   /** Checks if is session opened.
     *
     * @return true, if is session opened
     */
   private boolean isSessionOpened() {
      return session != null && session.isOpen();
   }

   /** Close session.
     */
   public void closeSession() {
      try {
         if (!isSessionOpened()) return;
         session.close();
         session = null;
         ThreadSession.set(null);
      } catch (HibernateException e) {
         e.printStackTrace();
      }
   }


   /** Gets the mom.
     *
     * @return the mom
     */
   public PersistentObjectMother getMom() {
      return mom;
   }

   /** Gets the session.
     *
     * @return the session
     * @throws HibernateException
     *             the hibernate exception
     */
   public Session getSession() throws HibernateException {
      openSession();
      return session;
   }
}