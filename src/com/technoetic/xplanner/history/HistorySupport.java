package com.technoetic.xplanner.history;

import java.util.Date;
import java.util.List;

import net.sf.xplanner.domain.History;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.technoetic.xplanner.domain.DomainMetaDataRepository;
import com.technoetic.xplanner.domain.Identifiable;

public class HistorySupport {
	private static Logger log = Logger.getLogger(HistorySupport.class);
	private static long FIFTEEN_MINUTES = 3600000 * 15;
	private SessionFactory sessionFactory;

	// DEBT should be spring loaded (metadatarepository should be passed in and
	// we should not have private methods
	private void saveEvent(final Class objectClass, final int containerOid,
			final int oid, final String action, final String description,
			final int personId, final Date when) {
		try {
			final History History = new History(when, containerOid, oid,
					DomainMetaDataRepository.getInstance().classToTypeName(
							objectClass), action, description, personId);
			if (!this.isEventThrottled(this.getSession(), History)) {
				this.getSession().save(History);
			}
			if (action.equals(net.sf.xplanner.domain.History.DELETED)) {
				// Set name in event descriptions for deleted objects
				final List events = this.getEvents(oid);
				for (int i = 0; i < events.size(); i++) {
					final History event = (History) events.get(i);
					if (StringUtils.isEmpty(event.getDescription())) {
						event.setDescription(description);
					}
				}
			}
		} catch (final HibernateException e) {
			HistorySupport.log.error("history error", e);
		}
	}

	private boolean isEventThrottled(final Session session, final History event)
			throws HibernateException {
		if (event.getAction().equals(History.UPDATED)) {
			final History previousEvent = (History) session
					.createQuery(
							"from event in "
									+ History.class
									+ " where event.targetId = :oid and event.action = :action order by event.whenHappened desc")
					.setInteger("oid", event.getTargetId())
					.setString("action", History.UPDATED).setMaxResults(1)
					.uniqueResult();
			return previousEvent != null
					&& event.getWhenHappened().getTime()
							- previousEvent.getWhenHappened().getTime() < HistorySupport.FIFTEEN_MINUTES;
		} else {
			return false;
		}
	}

	public void saveEvent(final Identifiable object, final String action,
			final String description, final int personId, final Date when) {
		try {
			final Integer id = (Integer) PropertyUtils
					.getProperty(object, "id");
			this.saveEvent(object.getClass(), DomainMetaDataRepository
					.getInstance().getParentId(object), id.intValue(), action,
					description, personId, when);
		} catch (final Exception e) {
			HistorySupport.log.error("history error", e);
		}
	}

	public List getEvents(final int oid) throws HibernateException {
		// TODO externalize these queries into mapping file
		final Query query = this
				.getSession()
				.createQuery(
						"from event in "
								+ History.class
								+ " where event.targetId = ? order by event.whenHappened desc");
		query.setInteger(0, oid);
		return query.list();
	}

	public List getContainerEvents(final int oid) throws Exception {
		final Query query = this.getSession().createQuery(
				"from event in " + History.class
						+ " where event.containerId = ? "
						+ " and event.action in ('" + History.CREATED + "','"
						+ History.DELETED
						+ "') order by event.whenHappened desc");
		query.setInteger(0, oid);
		return query.list();
	}

	public Object getHistoricalObject(final History event)
			throws HibernateException {
		if (event.getAction().equals(History.DELETED)) {
			return null;
		}
		return DomainMetaDataRepository.getInstance().getObject(
				event.getObjectType(), event.getTargetId());
	}

	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected final Session getSession() {
		return SessionFactoryUtils.getSession(this.sessionFactory,
				Boolean.FALSE);
	}

}
