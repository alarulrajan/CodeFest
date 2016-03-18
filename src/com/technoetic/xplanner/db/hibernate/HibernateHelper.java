package com.technoetic.xplanner.db.hibernate;

import java.sql.Connection;
import java.util.Properties;

import javax.servlet.ServletRequest;

import net.sf.xplanner.domain.Attribute;
import net.sf.xplanner.domain.DataSample;
import net.sf.xplanner.domain.Directory;
import net.sf.xplanner.domain.File;
import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Identifier;
import net.sf.xplanner.domain.Integration;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.ObjectType;
import net.sf.xplanner.domain.Permission;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.PersonRole;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Role;
import net.sf.xplanner.domain.Setting;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.hibernate.connection.ConnectionProviderFactory;
import org.hibernate.dialect.Dialect;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.util.LogUtil;

/** @noinspection ClassNamePrefixedWithPackageName */
public class HibernateHelper {
	private static final Logger LOG = LogUtil.getLogger();
	public static final String SESSION_ATTRIBUTE_KEY = "HibernateSession";

	private HibernateHelper() {
	}

	public static void initializeHibernate() throws HibernateException {
		if (GlobalSessionFactory.get() == null) {
			final Configuration cfg = HibernateHelper.initializeConfiguration();
			final SessionFactory sessionFactory = cfg.buildSessionFactory();
			// XPlanner uses a custom session factory so that all sessions will
			// be
			// automatically configured with an XPlanner-related Hibernate
			// interceptor.
			GlobalSessionFactory
					.set(new XPlannerSessionFactory(sessionFactory));
		}
	}

	public static SessionFactory getSessionFactory() throws HibernateException {
		HibernateHelper.initializeHibernate();
		return GlobalSessionFactory.get();
	}

	public static Configuration initializeConfiguration()
			throws HibernateException {
		final Properties properties = HibernateHelper.getProperties();
		try {
			if (properties.containsKey("xplanner.hibernate.mappingtransform")) {
				final String transformerFileName = properties
						.getProperty("xplanner.hibernate.mappingtransform");
				HibernateHelper.LOG
						.info("Using Hibernate mapping transformer: "
								+ transformerFileName);
			}
			final AnnotationConfiguration cfg = new AnnotationConfiguration();
			cfg.addAnnotatedClass(Identifier.class);
			cfg.addAnnotatedClass(Attribute.class);
			cfg.addAnnotatedClass(DataSample.class);
			cfg.addAnnotatedClass(Project.class);
			cfg.addAnnotatedClass(Iteration.class);
			cfg.addAnnotatedClass(UserStory.class);
			cfg.addAnnotatedClass(Task.class);
			cfg.addAnnotatedClass(TimeEntry.class);
			cfg.addAnnotatedClass(Integration.class);
			cfg.addAnnotatedClass(Note.class);
			cfg.addAnnotatedClass(Role.class);
			cfg.addAnnotatedClass(Person.class);
			cfg.addAnnotatedClass(History.class);
			cfg.addAnnotatedClass(File.class);
			cfg.addAnnotatedClass(Directory.class);
			cfg.addAnnotatedClass(PersonRole.class);
			cfg.addAnnotatedClass(Permission.class);
			cfg.addAnnotatedClass(ObjectType.class);
			cfg.addAnnotatedClass(Setting.class);

			// cfg.addInputStream(getMappingStream("mappings/Permission.xml",
			// transformer));
			// cfg.addInputStream(getMappingStream("mappings/RoleAssociation.xml",
			// transformer));
			// cfg.addInputStream(getMappingStream("mappings/DataSample.xml",
			// transformer));
			// cfg.addInputStream(getMappingStream("mappings/Project.xml",
			// transformer));
			// cfg.addInputStream(getMappingStream("mappings/Iteration.xml",
			// transformer));
			// cfg.addInputStream(getMappingStream("mappings/UserStory.xml",
			// transformer));
			// cfg.addInputStream(getMappingStream("mappings/Task.xml",
			// transformer));
			// cfg.addInputStream(getMappingStream("mappings/TimeEntry.xml",
			// transformer));
			// cfg.addInputStream(getMappingStream("mappings/Integration.xml",
			// transformer));
			// cfg.addInputStream(getMappingStream("mappings/Note.xml",
			// transformer));
			// cfg.addInputStream(getMappingStream("mappings/Role.xml",
			// transformer));
			// cfg.addInputStream(getMappingStream("mappings/Person.xml",
			// transformer));
			// cfg.addInputStream(getMappingStream("mappings/History.xml",
			// transformer));
			// cfg.addInputStream(getMappingStream("mappings/File.xml",
			// transformer));
			// cfg.addInputStream(getMappingStream("mappings/Directory.xml",
			// transformer));
			// FEATURE:
			// cfg.addInputStream(getMappingStream("mappings/Feature.xml",
			// transformer));
			cfg.addProperties(properties);
			EHCacheHelper.configure(cfg);
			return cfg;
		} catch (final Exception e) {
			throw new HibernateException(e);
		}
	}

	public static Properties getProperties() {
		return new XPlannerProperties().get();
	}

	public static Session getSession(final ServletRequest request) {
		final Session session = (Session) request
				.getAttribute(HibernateHelper.SESSION_ATTRIBUTE_KEY);
		if (HibernateHelper.LOG.isDebugEnabled()) {
			if (session != ThreadSession.get()) {
				HibernateHelper.LOG.info("Session storage mismatch thread="
						+ ThreadSession.get() + " HBHelper=" + session);
			}
		}
		return session;
	}

	public static void setSession(final ServletRequest request,
			final Session session) {
		request.setAttribute(HibernateHelper.SESSION_ATTRIBUTE_KEY, session);
	}

	public static Dialect getDialect() throws HibernateException {
		return Dialect.getDialect(HibernateHelper.getProperties());
	}

	static public Connection getConnection() {
		try {
			final Properties properties = new XPlannerProperties().get();
			return ConnectionProviderFactory.newConnectionProvider(properties)
					.getConnection();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}
