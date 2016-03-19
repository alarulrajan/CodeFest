package com.technoetic.xplanner.security.install;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.Permission;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.PersonRole;
import net.sf.xplanner.domain.Role;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hsqldb.HsqlServer;

/**
 * The Class BootstrapSystemUser.
 */
public class BootstrapSystemUser {
	
	/** The log. */
	private final Logger log = Logger.getLogger(this.getClass());
	
	/** The Constant SYSADMIN_USER_ID. */
	protected static final String SYSADMIN_USER_ID = "sysadmin";
	
	/** The Constant PATCH_LEVEL. */
	protected static final int PATCH_LEVEL = 2;
	
	/** The Constant PATCH_NAME. */
	protected static final String PATCH_NAME = "XPlanner bootstrap";

	/**
     * Instantiates a new bootstrap system user.
     */
	public BootstrapSystemUser() {
		// setLevel(new Integer(PATCH_LEVEL));
		// setName(PATCH_NAME);
	}

	/**
     * Run.
     *
     * @param sysadminId
     *            the sysadmin id
     * @throws Exception
     *             the exception
     */
	public void run(final String sysadminId) throws Exception {
		try {
			// HibernateHelper.initializeHibernate();
			final Session session = GlobalSessionFactory.get().openSession();
			session.connection().setAutoCommit(false);
			try {
				final List people = session.find("from person in class "
						+ Person.class.getName() + " where person.userId = ?",
						sysadminId, Hibernate.STRING);
				final Iterator personItr = people.iterator();
				Person sysadmin;
				if (personItr.hasNext()) {
					sysadmin = (Person) personItr.next();
					this.log.info("using " + sysadminId + " user");
				} else {
					sysadmin = this.createSysAdmin(sysadminId, session);
				}
				final Role viewerRole = this.initializeRole(session, "viewer",
						1, 8);
				final Role editorRole = this.initializeRole(session, "editor",
						2, 7);
				final Role adminRole = this.initializeRole(session, "admin", 3,
						6);
				final Role sysadminRole = this.initializeRole(session,
						BootstrapSystemUser.SYSADMIN_USER_ID, 4, 5);
				this.addRoleAssociation(session, sysadminRole.getId(),
						sysadmin.getId(), 0);
				this.createPermission(session, sysadminRole, "%", "%");
				this.createNegativePermission(session, editorRole,
						"system.project", "create.project");
				this.createNegativePermission(session, editorRole,
						"system.person", "create.person");
				this.createNegativePermission(session, adminRole,
						"system.project", "create.project");
				this.createPermission(session, adminRole, "%", "admin%");
				this.createPermission(session, editorRole, "%", "create%");
				this.createPermission(session, editorRole, "%", "edit%");
				this.createPermission(session, editorRole, "%", "integrate%");
				this.createPermission(session, editorRole, "%", "delete%");
				this.createPermission(session, viewerRole, "%", "read%");
				session.flush();
				session.connection().commit();
			} finally {
				session.close();
			}
		} catch (final Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
     * Creates the sys admin.
     *
     * @param sysadminId
     *            the sysadmin id
     * @param session
     *            the session
     * @return the person
     * @throws HibernateException
     *             the hibernate exception
     */
	private Person createSysAdmin(final String sysadminId, final Session session)
			throws HibernateException {
		Person sysadmin;
		this.log.info("creating " + sysadminId + " user");
		sysadmin = new Person();
		sysadmin.setUserId(sysadminId);
		sysadmin.setName(sysadminId);
		sysadmin.setInitials("SYS");
		sysadmin.setEmail("no@reply.com");
		sysadmin.setPassword("1tGWp1Bdm02Sw4bD7/o0N2ao405Tf8kjxGBW/A=="); // password=admin
		sysadmin.setLastUpdateTime(new Date());
		session.save(sysadmin);
		return sysadmin;
	}

	/**
     * Adds the role association.
     *
     * @param session
     *            the session
     * @param roleId
     *            the role id
     * @param personId
     *            the person id
     * @param projectId
     *            the project id
     * @throws HibernateException
     *             the hibernate exception
     */
	private void addRoleAssociation(final Session session, final int roleId,
			final int personId, final int projectId) throws HibernateException {
		session.save(new PersonRole(projectId, personId, roleId));
	}

	/**
     * Creates the permission.
     *
     * @param session
     *            the session
     * @param sysadminRole
     *            the sysadmin role
     * @param resourceType
     *            the resource type
     * @param permissionName
     *            the permission name
     * @throws HibernateException
     *             the hibernate exception
     */
	private void createPermission(final Session session,
			final Role sysadminRole, final String resourceType,
			final String permissionName) throws HibernateException {
		final Permission permission = new Permission(resourceType, 0,
				sysadminRole.getId(), permissionName);
		session.save(permission);
	}

	/**
     * Creates the negative permission.
     *
     * @param session
     *            the session
     * @param sysadminRole
     *            the sysadmin role
     * @param resourceType
     *            the resource type
     * @param permissionName
     *            the permission name
     * @throws HibernateException
     *             the hibernate exception
     */
	private void createNegativePermission(final Session session,
			final Role sysadminRole, final String resourceType,
			final String permissionName) throws HibernateException {
		final Permission permission = new Permission(resourceType, 0,
				sysadminRole.getId(), permissionName);
		permission.setPositive(false);
		session.save(permission);
	}

	/**
     * Initialize role.
     *
     * @param session
     *            the session
     * @param roleName
     *            the role name
     * @param left
     *            the left
     * @param right
     *            the right
     * @return the role
     * @throws HibernateException
     *             the hibernate exception
     */
	private Role initializeRole(final Session session, final String roleName,
			final int left, final int right) throws HibernateException {
		final List roles = session.find(
				"from role in class " + Role.class.getName()
						+ " where role.name = ?", roleName, Hibernate.STRING);
		Role role;
		if (roles.size() == 0) {
			this.log.info("creating role: " + roleName);
			role = new Role(roleName);
			role.setLeft(left);
			role.setRight(right);
			session.save(role);
		} else {
			role = (Role) roles.get(0);
		}
		return role;
	}

	/**
     * Migrate.
     *
     * @param context
     *            the context
     * @throws MigrationException
     *             the migration exception
     */
	public void migrate(final MigrationContext context)
			throws MigrationException {
		try {
			this.run(BootstrapSystemUser.SYSADMIN_USER_ID);
		} catch (final Exception e) {
			throw new MigrationException("error during migration", e);
		} finally {
			HsqlServer.shutdown();
		}
	}

	/**
     * The main method.
     *
     * @param args
     *            the arguments
     */
	public static void main(final String[] args) {
		String sysadminId = BootstrapSystemUser.SYSADMIN_USER_ID;
		if (args.length == 1) {
			sysadminId = args[0];
		}
		final BootstrapSystemUser action = new BootstrapSystemUser();
		try {
			action.run(sysadminId);
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			HsqlServer.shutdown();
		}
	}
}
