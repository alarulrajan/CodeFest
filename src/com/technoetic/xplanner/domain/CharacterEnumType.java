/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.domain;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.hsqldb.types.Types;

public abstract class CharacterEnumType implements UserType {
	private static final int[] SQL_TYPES = { Types.CHAR };

	@Override
	public int[] sqlTypes() {
		return CharacterEnumType.SQL_TYPES;
	}

	@Override
	public abstract Class returnedClass();

	@Override
	public boolean equals(final Object x, final Object y)
			throws HibernateException {
		return x == y;
	}

	@Override
	public Object deepCopy(final Object value) throws HibernateException {
		return value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Object nullSafeGet(final ResultSet resultSet, final String[] names,
			final Object owner) throws HibernateException, SQLException {

		final String name = resultSet.getString(names[0]);
		return resultSet.wasNull() ? null : this.getType(name);
	}

	protected abstract CharacterEnum getType(String code);

	@Override
	public void nullSafeSet(final PreparedStatement statement,
			final Object value, final int index) throws HibernateException,
			SQLException {
		if (value == null) {
			statement.setNull(index, Types.CHAR);
		} else {
			statement.setString(index, this.convert(value));
		}
	}

	private String convert(final Object value) {
		final CharacterEnum characterEnum = (CharacterEnum) value;

		return String.copyValueOf(new char[] { characterEnum.getCode() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable,
	 * java.lang.Object)
	 */
	@Override
	public Object assemble(final Serializable serializable, final Object obj)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
	 */
	@Override
	public Serializable disassemble(final Object obj) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
	 */
	@Override
	public int hashCode(final Object obj) throws HibernateException {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.usertype.UserType#replace(java.lang.Object,
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object replace(final Object obj, final Object obj1, final Object obj2)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

}
