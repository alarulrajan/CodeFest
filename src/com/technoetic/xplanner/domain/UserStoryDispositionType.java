package com.technoetic.xplanner.domain;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * The Class UserStoryDispositionType.
 */
//FIXME: It's stub. Need rewriting.
public class UserStoryDispositionType implements UserType {
    
    /** The code. */
    private final int code;

    /** The Constant PLANNED_TEXT. */
    public static final String PLANNED_TEXT = "Planned";
    
    /** The Constant CARRIED_OVER_TEXT. */
    public static final String CARRIED_OVER_TEXT = "Carried Over";
    
    /** The Constant ADDED_TEXT. */
    public static final String ADDED_TEXT = "Added";

    /** The Constant PLANNED_KEY. */
    public static final String PLANNED_KEY = "planned";
    
    /** The Constant CARRIED_OVER_KEY. */
    public static final String CARRIED_OVER_KEY = "carriedOver";
    
    /** The Constant ADDED_KEY. */
    public static final String ADDED_KEY = "added";

    /** The Constant PLANNED. */
    public static final UserStoryDispositionType PLANNED = new UserStoryDispositionType(
            0);
    
    /** The Constant CARRIED_OVER. */
    public static final UserStoryDispositionType CARRIED_OVER = new UserStoryDispositionType(
            1);
    
    /** The Constant ADDED. */
    public static final UserStoryDispositionType ADDED = new UserStoryDispositionType(
            2);

    /** The Constant KEYS. */
    private static final String[] KEYS = { UserStoryDispositionType.PLANNED_KEY,
            UserStoryDispositionType.CARRIED_OVER_KEY,
            UserStoryDispositionType.ADDED_KEY };

    /**
     * Instantiates a new user story disposition type.
     *
     * @param code
     *            the code
     */
    private UserStoryDispositionType(final int code) {
        this.code = code;
    }

    /**
     * To int.
     *
     * @return the int
     */
    public int toInt() {
        return this.code;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return UserStoryDispositionType.KEYS[this.code];
    }

    /**
     * From key.
     *
     * @param key
     *            the key
     * @return the user story disposition type
     */
    public static UserStoryDispositionType fromKey(final String key) {
        // ChangeSoon should be handled by iterating through the keys
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

    /**
     * From int.
     *
     * @param i
     *            the i
     * @return the user story disposition type
     */
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getKey();
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return this.getKey();
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
     */
    @Override
    public Object assemble(final Serializable cached, final Object owner)
            throws HibernateException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
     */
    @Override
    public Object deepCopy(final Object value) throws HibernateException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
     */
    @Override
    public Serializable disassemble(final Object value)
            throws HibernateException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean equals(final Object x, final Object y)
            throws HibernateException {
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    
    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
     */
    @Override
    public int hashCode(final Object x) throws HibernateException {
        return 0;
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#isMutable()
     */
    @Override
    public boolean isMutable() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
     */
    @Override
    public Object nullSafeGet(final ResultSet rs, final String[] names,
            final Object owner) throws HibernateException, SQLException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
     */
    @Override
    public void nullSafeSet(final PreparedStatement st, final Object value,
            final int index) throws HibernateException, SQLException {

    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public Object replace(final Object original, final Object target,
            final Object owner) throws HibernateException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#returnedClass()
     */
    @Override
    public Class returnedClass() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#sqlTypes()
     */
    @Override
    public int[] sqlTypes() {
        return null;
    }
}
