package com.technoetic.xplanner.domain.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.domain.Attribute;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

public class AttributeRepositoryImpl implements AttributeRepository {
	@Override
	public void setAttribute(final int targetId, final String name,
			final String value) throws RepositoryException {
		try {
			final Session session = ThreadSession.get();
			final Attribute attribute = new Attribute(targetId, name, value);
			final List existingAttributes = session.find("from a in "
					+ Attribute.class + " where targetId = ? and name= ?",
					new Object[] { new Integer(targetId), name }, new Type[] {
							Hibernate.INTEGER, Hibernate.STRING });
			if (existingAttributes.size() == 0) {
				session.save(attribute);
			} else {
				session.update(attribute);
			}
		} catch (final RuntimeException e) {
			throw e;
		}
	}

	@Override
	public String getAttribute(final int targetId, final String name)
			throws RepositoryException {
		try {
			final Session session = ThreadSession.get();
			final Attribute id = new Attribute(targetId, name, null);
			final Attribute attribute = (Attribute) session.load(
					Attribute.class, id);
			return attribute.getValue();
		} catch (final RuntimeException e) {
			throw e;
		}
	}

	@Override
	public Map getAttributes(final int targetId, final String prefix)
			throws RepositoryException {
		final HashMap attributes = new HashMap();
		try {
			final Session session = ThreadSession.get();
			final String pattern = (prefix != null ? prefix : "") + "%";
			final List attributeObjects = session.find("from a in "
					+ Attribute.class + " where targetId = ? and name like ?",
					new Object[] { new Integer(targetId), pattern },
					new Type[] { Hibernate.INTEGER, Hibernate.STRING });
			for (int i = 0; i < attributeObjects.size(); i++) {
				final Attribute attribute = (Attribute) attributeObjects.get(i);
				String name = attribute.getName();
				if (StringUtils.isNotEmpty(prefix)) {
					name = name.replaceAll("^" + prefix, "");
				}
				attributes.put(name, attribute.getValue());
			}
			return attributes;
		} catch (final RuntimeException e) {
			throw e;
		}
	}

	@Override
	public void delete(final int targetId, final String name)
			throws RepositoryException {
		try {
			ThreadSession.get().delete(
					"from a in " + Attribute.class
							+ " where a.targetId = ? and a.name = ?",
					new Object[] { new Integer(targetId), name },
					new Type[] { Hibernate.INTEGER, Hibernate.STRING });
		} catch (final RuntimeException e) {
			throw e;
		}
	}
}
