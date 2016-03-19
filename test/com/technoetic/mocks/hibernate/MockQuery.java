package com.technoetic.mocks.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.MappingException;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.Type;

/**
 * The Class MockQuery.
 */
public class MockQuery implements Query {
    
    /* (non-Javadoc)
     * @see org.hibernate.Query#getNamedParameters()
     */
    public String[] getNamedParameters() throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#getQueryString()
     */
    public String getQueryString() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#getReturnTypes()
     */
    public Type[] getReturnTypes() throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#iterate()
     */
    public Iterator iterate() throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /** The list return. */
    public List listReturn;
    
    /* (non-Javadoc)
     * @see org.hibernate.Query#list()
     */
    public List list() throws HibernateException {
        return listReturn;
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#scroll()
     */
    public ScrollableResults scroll() throws HibernateException {
        throw new UnsupportedOperationException();
    }

  /* (non-Javadoc)
   * @see org.hibernate.Query#scroll(org.hibernate.ScrollMode)
   */
  public ScrollableResults scroll(ScrollMode scrollMode) throws HibernateException {
    return null;
  }

  /* (non-Javadoc)
   * @see org.hibernate.Query#setBigDecimal(java.lang.String, java.math.BigDecimal)
   */
  public Query setBigDecimal(String name, BigDecimal number) {
      throw new UnsupportedOperationException();
  }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setBigDecimal(int, java.math.BigDecimal)
     */
    public Query setBigDecimal(int position, BigDecimal number) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setBinary(java.lang.String, byte[])
     */
    public Query setBinary(String name, byte[] val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setBinary(int, byte[])
     */
    public Query setBinary(int position, byte[] val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setBoolean(java.lang.String, boolean)
     */
    public Query setBoolean(String name, boolean val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setBoolean(int, boolean)
     */
    public Query setBoolean(int position, boolean val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setByte(java.lang.String, byte)
     */
    public Query setByte(String name, byte val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setByte(int, byte)
     */
    public Query setByte(int position, byte val) {
        throw new UnsupportedOperationException();
    }

    /** The set cacheable. */
    public boolean setCacheable;
    
    /* (non-Javadoc)
     * @see org.hibernate.Query#setCacheable(boolean)
     */
    public Query setCacheable(boolean cacheable) {
        setCacheable = true;
        return this;
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setCacheRegion(java.lang.String)
     */
    public Query setCacheRegion(String cacheRegion) {
        throw new UnsupportedOperationException();
    }

  /**
     * Sets the force cache refresh.
     *
     * @param forceCacheRefresh
     *            the force cache refresh
     * @return the query
     */
  public Query setForceCacheRefresh(boolean forceCacheRefresh) {
    return null;
  }

  /* (non-Javadoc)
   * @see org.hibernate.Query#setCalendar(java.lang.String, java.util.Calendar)
   */
  public Query setCalendar(String name, Calendar calendar) {
      throw new UnsupportedOperationException();
  }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setCalendar(int, java.util.Calendar)
     */
    public Query setCalendar(int position, Calendar calendar) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setCalendarDate(java.lang.String, java.util.Calendar)
     */
    public Query setCalendarDate(String name, Calendar calendar) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setCalendarDate(int, java.util.Calendar)
     */
    public Query setCalendarDate(int position, Calendar calendar) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setCharacter(java.lang.String, char)
     */
    public Query setCharacter(String name, char val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setCharacter(int, char)
     */
    public Query setCharacter(int position, char val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setDate(java.lang.String, java.util.Date)
     */
    public Query setDate(String name, Date date) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setDate(int, java.util.Date)
     */
    public Query setDate(int position, Date date) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setDouble(java.lang.String, double)
     */
    public Query setDouble(String name, double val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setDouble(int, double)
     */
    public Query setDouble(int position, double val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setEntity(java.lang.String, java.lang.Object)
     */
    public Query setEntity(String name, Object val) // use setParameter for null values
    {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setEntity(int, java.lang.Object)
     */
    public Query setEntity(int position, Object val) // use setParameter for null values
    {
        throw new UnsupportedOperationException();
    }

    /** Sets the enum.
     *
     * @param name
     *            the name
     * @param val
     *            the val
     * @return the query
     * @throws MappingException
     *             the mapping exception
     */
    public Query setEnum(String name, Object val) throws MappingException // use setParameter for null values
    {
        throw new UnsupportedOperationException();
    }

    /** Sets the enum.
     *
     * @param position
     *            the position
     * @param val
     *            the val
     * @return the query
     * @throws MappingException
     *             the mapping exception
     */
    public Query setEnum(int position, Object val) throws MappingException // use setParameter for null values
    {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setFirstResult(int)
     */
    public Query setFirstResult(int firstResult) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setFloat(java.lang.String, float)
     */
    public Query setFloat(String name, float val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setFloat(int, float)
     */
    public Query setFloat(int position, float val) {
        throw new UnsupportedOperationException();
    }

    /** The set integer name. */
    public String setIntegerName;
    
    /** The set integer value. */
    public int setIntegerValue;
    
    /* (non-Javadoc)
     * @see org.hibernate.Query#setInteger(java.lang.String, int)
     */
    public Query setInteger(String name, int value) {
        setIntegerName = name;
        setIntegerValue = value;
        return this;
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setInteger(int, int)
     */
    public Query setInteger(int position, int val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setLocale(java.lang.String, java.util.Locale)
     */
    public Query setLocale(String name, Locale locale) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setLocale(int, java.util.Locale)
     */
    public Query setLocale(int position, Locale locale) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setLockMode(java.lang.String, org.hibernate.LockMode)
     */
    public Query setLockMode(String alias, LockMode lockMode) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setLong(java.lang.String, long)
     */
    public Query setLong(String name, long val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setLong(int, long)
     */
    public Query setLong(int position, long val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setMaxResults(int)
     */
    public Query setMaxResults(int maxResults) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setParameter(java.lang.String, java.lang.Object)
     */
    public Query setParameter(String name, Object val) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setParameter(java.lang.String, java.lang.Object, org.hibernate.type.Type)
     */
    public Query setParameter(String name, Object val, Type type) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setParameter(int, java.lang.Object)
     */
    public Query setParameter(int position, Object val) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setParameter(int, java.lang.Object, org.hibernate.type.Type)
     */
    public Query setParameter(int position, Object val, Type type) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setParameterList(java.lang.String, java.util.Collection)
     */
    public Query setParameterList(String name, Collection vals) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setParameterList(java.lang.String, java.util.Collection, org.hibernate.type.Type)
     */
    public Query setParameterList(String name, Collection vals, Type type) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setParameterList(java.lang.String, java.lang.Object[])
     */
    public Query setParameterList(String name, Object[] vals) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setParameterList(java.lang.String, java.lang.Object[], org.hibernate.type.Type)
     */
    public Query setParameterList(String name, Object[] vals, Type type) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setProperties(java.lang.Object)
     */
    public Query setProperties(Object bean) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setSerializable(java.lang.String, java.io.Serializable)
     */
    public Query setSerializable(String name, Serializable val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setSerializable(int, java.io.Serializable)
     */
    public Query setSerializable(int position, Serializable val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setShort(java.lang.String, short)
     */
    public Query setShort(String name, short val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setShort(int, short)
     */
    public Query setShort(int position, short val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setString(java.lang.String, java.lang.String)
     */
    public Query setString(String name, String val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setString(int, java.lang.String)
     */
    public Query setString(int position, String val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setText(java.lang.String, java.lang.String)
     */
    public Query setText(String name, String val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setText(int, java.lang.String)
     */
    public Query setText(int position, String val) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setTime(java.lang.String, java.util.Date)
     */
    public Query setTime(String name, Date date) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setTime(int, java.util.Date)
     */
    public Query setTime(int position, Date date) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setTimeout(int)
     */
    public Query setTimeout(int timeout) {
        throw new UnsupportedOperationException();
    }

  /* (non-Javadoc)
   * @see org.hibernate.Query#setFetchSize(int)
   */
  public Query setFetchSize(int fetchSize) {
    return null;
  }

  /* (non-Javadoc)
   * @see org.hibernate.Query#setTimestamp(java.lang.String, java.util.Date)
   */
  public Query setTimestamp(String name, Date date) {
      throw new UnsupportedOperationException();
  }

    /* (non-Javadoc)
     * @see org.hibernate.Query#setTimestamp(int, java.util.Date)
     */
    public Query setTimestamp(int position, Date date) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.hibernate.Query#uniqueResult()
     */
    public Object uniqueResult() throws HibernateException {
        throw new UnsupportedOperationException();
    }

	/* (non-Javadoc)
	 * @see org.hibernate.Query#executeUpdate()
	 */
	@Override
	public int executeUpdate() throws HibernateException {
		// ChangeSoon 
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.Query#getReturnAliases()
	 */
	@Override
	public String[] getReturnAliases() throws HibernateException {
		// ChangeSoon 
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.Query#setBigInteger(int, java.math.BigInteger)
	 */
	@Override
	public Query setBigInteger(int position, BigInteger number) {
		// ChangeSoon 
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.Query#setBigInteger(java.lang.String, java.math.BigInteger)
	 */
	@Override
	public Query setBigInteger(String name, BigInteger number) {
		// ChangeSoon 
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.Query#setCacheMode(org.hibernate.CacheMode)
	 */
	@Override
	public Query setCacheMode(CacheMode cacheMode) {
		// ChangeSoon 
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.Query#setComment(java.lang.String)
	 */
	@Override
	public Query setComment(String comment) {
		// ChangeSoon 
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.Query#setFlushMode(org.hibernate.FlushMode)
	 */
	@Override
	public Query setFlushMode(FlushMode flushMode) {
		// ChangeSoon 
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.Query#setParameters(java.lang.Object[], org.hibernate.type.Type[])
	 */
	@Override
	public Query setParameters(Object[] values, Type[] types)
			throws HibernateException {
		// ChangeSoon 
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.Query#setProperties(java.util.Map)
	 */
	@Override
	public Query setProperties(Map bean) throws HibernateException {
		// ChangeSoon 
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.Query#setReadOnly(boolean)
	 */
	@Override
	public Query setReadOnly(boolean readOnly) {
		// ChangeSoon 
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.Query#setResultTransformer(org.hibernate.transform.ResultTransformer)
	 */
	@Override
	public Query setResultTransformer(ResultTransformer transformer) {
		// ChangeSoon 
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.Query#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		// ChangeSoon 
		return false;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.Query#setLockOptions(org.hibernate.LockOptions)
	 */
	@Override
	public Query setLockOptions(LockOptions lockOptions) {
		// ChangeSoon 
		return null;
	}
}
