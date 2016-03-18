package com.technoetic.xplanner.db;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

public class OLDIdSearchHelper {
	private final Class[] searchedDomainClasses = { Project.class,
			Iteration.class, UserStory.class, Task.class, Person.class,
			Note.class };

	public DomainObject search(final int oid) throws HibernateException {
		final Integer id = new Integer(oid);
		for (int i = 0; i < this.searchedDomainClasses.length; i++) {
			DomainObject o = null;
			try {
				o = (DomainObject) ThreadSession.get().get(
						this.searchedDomainClasses[i], id);
			} catch (final ObjectNotFoundException e) {
				// ignored
			}
			if (o != null) {
				if (o instanceof Note) {
					o = ((Note) o).getParent();
				}
				return o;
			}
		}
		return null;
	}

}
