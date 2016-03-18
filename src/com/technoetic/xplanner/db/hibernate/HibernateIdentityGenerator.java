package com.technoetic.xplanner.db.hibernate;

import java.util.Properties;

import org.hibernate.dialect.Dialect;
import org.hibernate.id.TableGenerator;
import org.hibernate.id.TableHiLoGenerator;
import org.hibernate.type.Type;

public class HibernateIdentityGenerator extends TableHiLoGenerator {
	public static final String TABLE_NAME = "identifier";
	public static final String NEXT_ID_COL = "nextId";
	public static final String SET_NEXT_ID_QUERY = "update "
			+ HibernateIdentityGenerator.TABLE_NAME + " set "
			+ HibernateIdentityGenerator.NEXT_ID_COL + " = ?";
	public static final String SET_NEXT_ID_ATOMIC_QUERY = HibernateIdentityGenerator.SET_NEXT_ID_QUERY
			+ " where " + HibernateIdentityGenerator.NEXT_ID_COL + " = ?";
	public static final String GET_NEXT_ID_QUERY = "select "
			+ HibernateIdentityGenerator.NEXT_ID_COL + " from "
			+ HibernateIdentityGenerator.TABLE_NAME;

	@Override
	public void configure(final Type type, final Properties params,
			final Dialect d) {
		if (!params.containsKey(TableGenerator.TABLE)) {
			params.setProperty(TableGenerator.TABLE,
					HibernateIdentityGenerator.TABLE_NAME);
		}
		if (!params.containsKey(TableGenerator.COLUMN)) {
			params.setProperty(TableGenerator.COLUMN,
					HibernateIdentityGenerator.NEXT_ID_COL);
		}
		if (!params.containsKey(TableHiLoGenerator.MAX_LO)) {
			params.setProperty(TableHiLoGenerator.MAX_LO, "10");
		}
		super.configure(type, params, d);
	}

	@Override
	public Object generatorKey() {

		return super.generatorKey();
	}
}
