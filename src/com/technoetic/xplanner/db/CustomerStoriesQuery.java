package com.technoetic.xplanner.db;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.UserStory;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

public class CustomerStoriesQuery {
	private final Logger log = Logger.getLogger(this.getClass());
	private int personId;
	private Collection stories;
	private Collection myStories;
	// DEBT Why static? Should the query go to either Story.xml or Person.xml?
	private static String query;

	public void setPersonId(final int personId) {
		this.personId = personId;
	}

	public int getPersonId() {
		return this.personId;
	}

	public Collection getMyStories() {
		if (this.myStories == null) {
			this.myStories = this.getStories();
		}
		return this.myStories;
	}

	private Collection getStories() {
		final Session session = ThreadSession.get();
		if (this.stories == null) {
			try {
				if (session == null) {
					this.log.error("no Hibernate session provided, ignoring "
							+ this);
					return Collections.EMPTY_LIST;
				}
				try {
					if (CustomerStoriesQuery.query == null) {
						CustomerStoriesQuery.query = "select distinct story from "
								+ "story in "
								+ UserStory.class
								+ ","
								+ "iteration in "
								+ Iteration.class
								+ ","
								+ "person in "
								+ Person.class
								+ " where iteration.id = story.iteration.id and iteration.endDate > ? and "
								+ "person.id = story.customer.id and person.id = ?";
					}
					this.stories = session.find(CustomerStoriesQuery.query,
							new Object[] { new Date(),
									new Integer(this.personId) }, new Type[] {
									Hibernate.DATE, Hibernate.INTEGER });
				} catch (final Exception ex) {
					this.log.error("query error", ex);
				} finally {
					session.connection().rollback();
				}
			} catch (final Exception e) {
				this.log.error("error in CustomerStoriesQuery", e);
			}

		}
		return this.stories;
	}
}
