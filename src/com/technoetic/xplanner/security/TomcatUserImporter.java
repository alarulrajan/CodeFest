package com.technoetic.xplanner.security;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Role;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.security.module.LoginSupportImpl;
import com.technoetic.xplanner.security.module.XPlannerLoginModule;

/**
 * The Class TomcatUserImporter.
 */
public class TomcatUserImporter {
    
    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(final String[] args) {
        final Logger log = Logger.getLogger(TomcatUserImporter.class);

        String filename = null;
        if (args.length > 0) {
            filename = args[0];
        } else {
            log.error("usage: TomcatUserImporter filename");
            return;
        }

        final Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("tomcat-users", ArrayList.class);

        digester.addObjectCreate("tomcat-users/user", User.class);
        digester.addSetProperties("tomcat-users/user");
        digester.addSetNext("tomcat-users/user", "add");

        try {
            final FileInputStream in = new FileInputStream(filename);
            final List users = (List) digester.parse(in);

            HibernateHelper.initializeHibernate();
            final Session session = GlobalSessionFactory.get().openSession();
            session.connection().setAutoCommit(false);
            final XPlannerLoginModule encryptor = new XPlannerLoginModule(
                    new LoginSupportImpl());
            final Iterator userItr = users.iterator();
            while (userItr.hasNext()) {
                final User user = (User) userItr.next();
                try {
                    final List projects = session.find("from project in class "
                            + Project.class.getName());
                    final List people = session.find("from person in class "
                            + Person.class.getName() + " where userid = ?",
                            user.getUsername(), Hibernate.STRING);
                    Person person = null;
                    final Iterator peopleIterator = people.iterator();
                    if (peopleIterator.hasNext()) {
                        person = (Person) peopleIterator.next();
                        if (person.getPassword() == null) {
                            log.info("setting password: user="
                                    + user.getUsername());
                            person.setPassword(encryptor.encodePassword(
                                    user.getPassword(), null));
                        }
                        final String[] roles = user.getRoles().split(",");
                        for (int i = 0; i < roles.length; i++) {
                            final Iterator projectItr = projects.iterator();
                            while (projectItr.hasNext()) {
                                final Project project = (Project) projectItr
                                        .next();
                                if (!TomcatUserImporter.isUserInRole(person,
                                        project.getId(), roles[i])) {
                                    final Role role = new Role(roles[i]);
                                    log.info("adding role: user="
                                            + user.getUsername() + ", role="
                                            + roles[i]);
                                    session.save(role);
                                }
                            }
                        }
                    } else {
                        log.warn("no xplanner user: " + user.getUsername());
                    }
                    session.flush();
                    session.connection().commit();
                } catch (final Exception e) {
                    session.connection().rollback();
                }
            }
            session.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if is user in role.
     *
     * @param person
     *            the person
     * @param projectId
     *            the project id
     * @param roleName
     *            the role name
     * @return true, if is user in role
     */
    private static boolean isUserInRole(final Person person,
            final int projectId, final String roleName) {
        // Iterator roleItr = person.getRoles().iterator();
        // while (roleItr.hasNext()) {
        // Role role = (Role)roleItr.next();
        // // do-before-release Check if Importer is still correct
        // if (role.fromNameKey().equals(roleName)) {
        // return true;
        // }
        // }
        return false;
    }

    /**
     * The Class User.
     */
    public static class User {
        
        /** The username. */
        private String username;
        
        /** The password. */
        private String password;
        
        /** The roles. */
        private String roles;

        /**
         * Gets the username.
         *
         * @return the username
         */
        public String getUsername() {
            return this.username;
        }

        /**
         * Sets the username.
         *
         * @param username
         *            the new username
         */
        public void setUsername(final String username) {
            this.username = username;
        }

        /**
         * Gets the password.
         *
         * @return the password
         */
        public String getPassword() {
            return this.password;
        }

        /**
         * Sets the password.
         *
         * @param password
         *            the new password
         */
        public void setPassword(final String password) {
            this.password = password;
        }

        /**
         * Gets the roles.
         *
         * @return the roles
         */
        public String getRoles() {
            return this.roles;
        }

        /**
         * Sets the roles.
         *
         * @param roles
         *            the new roles
         */
        public void setRoles(final String roles) {
            this.roles = roles;
        }
    }
}
