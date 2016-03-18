package com.technoetic.xplanner.domain;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

//FIXME: It's stub. Need rewriting.
//TODO delete this class and replace it with Disposition
public class UserStoryDispositionType implements UserType {
	private final int code;

	public static final String PLANNED_TEXT = "Planned";
	public static final String CARRIED_OVER_TEXT = "Carried Over";
	public static final String ADDED_TEXT = "Added";

	public static final String PLANNED_KEY = "planned";
	public static final String CARRIED_OVER_KEY = "carriedOver";
	public static final String ADDED_KEY = "added";

	public static final UserStoryDispositionType PLANNED = new UserStoryDispositionType(
			0);
	public static final UserStoryDispositionType CARRIED_OVER = new UserStoryDispositionType(
			1);
	public static final UserStoryDispositionType ADDED = new UserStoryDispositionType(
			2);

	public static final String[] KEYS = { UserStoryDispositionType.PLANNED_KEY,
			UserStoryDispositionType.CARRIED_OVER_KEY,
			UserStoryDispositionType.ADDED_KEY };

	private UserStoryDispositionType(final int code) {
		this.code = code;
	}

	public int toInt() {
		return this.code;
	}

	public String getKey() {
		return UserStoryDispositionType.KEYS[this.code];
	}

	public static UserStoryDispositionType fromKey(final String key) {
		// TODO should be handled by iterating through the keys
		if (key == null) {
			return null;
		} else if (UserStoryDispositionType.PLANNED_KEY.equals(key)) {
			return UserStoryDispositionType.PLANNED;
		} else if (UserStoryDispositionType.CARRIED_OVER_KEY.equals(key)) {
			return UserStoryDispositionType.CARRIED_OVER;
		} else if (UserStoryDispositionType.ADDED_KEY.equals(key)) {
			return UserStoryDispositionType.ADDED;
		} else {
			throw new RuntimeException("Unknown disposition key");
		}
	}

	public static UserStoryDispositionType fromInt(final int i) {
		switch (i) {
		case 0:
			return UserStoryDispositionType.PLANNED;
		case 1:
			return UserStoryDispositionType.CARRIED_OVER;
		case 2:
			return UserStoryDispositionType.ADDED;
		default:
			throw new RuntimeException("Unknown disposition code");
		}
	}

	@Override
	public String toString() {
		return this.getKey();
	}

	public String getName() {
		return this.getKey();
	}

	@Override
	public Object assemble(final Serializable cached, final Object owner)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object deepCopy(final Object value) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable disassemble(final Object value)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(final Object x, final Object y)
			throws HibernateException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode(final Object x) throws HibernateException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isMutable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object nullSafeGet(final ResultSet rs, final String[] names,
			final Object owner) throws HibernateException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void nullSafeSet(final PreparedStatement st, final Object value,
			final int index) throws HibernateException, SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object replace(final Object original, final Object target,
			final Object owner) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class returnedClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] sqlTypes() {
		// TODO Auto-generated method stub
		return null;
	}
}
