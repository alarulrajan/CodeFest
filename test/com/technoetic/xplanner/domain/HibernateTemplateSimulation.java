/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.domain;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.technoetic.xplanner.acceptance.DatabaseSupport;

/**
 * User: mprokopowicz Date: Mar 30, 2006 Time: 1:17:40 PM.
 */
public class HibernateTemplateSimulation extends HibernateTemplate {
   
   /** The database support. */
   private DatabaseSupport databaseSupport;
   
   /** The session. */
   private Session session;

   /** Instantiates a new hibernate template simulation.
     *
     * @param databaseSupport
     *            the database support
     */
   public HibernateTemplateSimulation(DatabaseSupport databaseSupport) {this.databaseSupport = databaseSupport;}
   
   /** Instantiates a new hibernate template simulation.
     *
     * @param session
     *            the session
     */
   public HibernateTemplateSimulation(Session session) {this.session = session;}

    /* (non-Javadoc)
     * @see org.springframework.orm.hibernate3.HibernateTemplate#execute(org.springframework.orm.hibernate3.HibernateCallback)
     */
    @Override
   public <T> T execute(HibernateCallback<T> action) throws DataAccessException {
      try {
         Session session = databaseSupport != null ? databaseSupport.getSession() : this.session;
         return action.doInHibernate(session);
      }
      catch (HibernateException ex) {
         throw convertHibernateAccessException(ex);
      }
      catch (SQLException ex) {
         throw convertJdbcAccessException(ex);
      }
      catch (RuntimeException ex) {
         throw ex;
      }
   }
}
