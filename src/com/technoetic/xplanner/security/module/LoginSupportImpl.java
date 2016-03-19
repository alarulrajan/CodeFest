package com.technoetic.xplanner.security.module;

import java.util.Iterator;
import java.util.List;

import javax.security.auth.Subject;

import net.sf.xplanner.domain.Person;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.LoginModule;
import com.technoetic.xplanner.security.PersonPrincipal;

/**
 * The Class LoginSupportImpl.
 */
public class LoginSupportImpl implements LoginSupport {
	
	/** The Constant MESSAGE_STORAGE_ERROR_KEY. */
	public static final String MESSAGE_STORAGE_ERROR_KEY = "authentication.module.message.storageError";

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.security.module.LoginSupport#populateSubjectPrincipalFromDatabase(javax.security.auth.Subject, java.lang.String)
	 */
	@Override
	public Person populateSubjectPrincipalFromDatabase(final Subject subject,
			final String userId) throws AuthenticationException {
		Person person = null;
		try {
			person = this.getPerson(userId);
		} catch (final HibernateException e) {
			throw new AuthenticationException(
					LoginSupportImpl.MESSAGE_STORAGE_ERROR_KEY);
		}
		if (person == null) {
			throw new AuthenticationException(
					LoginModule.MESSAGE_USER_NOT_FOUND_KEY);
		}
		subject.getPrincipals().clear();
		subject.getPrincipals().add(new PersonPrincipal(person));
		return person;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.security.module.LoginSupport#getPerson(java.lang.String)
	 */
	@Override
	public Person getPerson(final String userId) throws HibernateException {
		final Session session = ThreadSession.get();
		final List people = session.find(
				"from person in class " + Person.class.getName()
						+ " where userid = ?", userId, Hibernate.STRING);
		final Iterator peopleIterator = people.iterator();
		if (peopleIterator.hasNext()) {
			return (Person) peopleIterator.next();
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.security.module.LoginSupport#createSubject()
	 */
	@Override
	public Subject createSubject() {
		return new Subject();
	}
}