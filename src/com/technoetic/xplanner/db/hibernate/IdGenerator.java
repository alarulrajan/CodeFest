/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Mar 26, 2005
 * Time: 9:29:01 AM
 */
package com.technoetic.xplanner.db.hibernate;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.technoetic.xplanner.util.LogUtil;

/**
 * The Class IdGenerator.
 */
public class IdGenerator {
	
	/** The next id. */
	int nextId;

	/** The Constant LOG. */
	protected static final Logger LOG = LogUtil.getLogger();

	/** The instance. */
	static IdGenerator instance;
	
	/** The Constant NEXT_ID_COL_INDEX. */
	public static final int NEXT_ID_COL_INDEX = 1;

	/**
     * Gets the unique id.
     *
     * @param prefix
     *            the prefix
     * @return the unique id
     */
	public static String getUniqueId(final String prefix) {
		return prefix + IdGenerator.getInstance().getNext();
	}

	/**
     * Gets the next persistent id.
     *
     * @return the next persistent id
     * @throws Exception
     *             the exception
     */
	public static int getNextPersistentId() throws Exception {
		IdGenerator.getInstance();
		return IdGenerator.getFromDB(HibernateHelper.getConnection());
	}

	/**
     * Sets the next persistent id.
     *
     * @param id
     *            the new next persistent id
     * @throws Exception
     *             the exception
     */
	public static void setNextPersistentId(final int id) throws Exception {
		IdGenerator.getInstance();
		IdGenerator.setInDB(HibernateHelper.getConnection(), id);
	}

	/**
     * Gets the next.
     *
     * @return the next
     */
	private int getNext() {
		return this.nextId++;
	}

	/**
     * Gets the single instance of IdGenerator.
     *
     * @return single instance of IdGenerator
     */
	private static IdGenerator getInstance() {
		if (IdGenerator.instance == null) {
			IdGenerator.instance = new IdGenerator();
		}
		return IdGenerator.instance;
	}

	/**
     * Instantiates a new id generator.
     */
	private IdGenerator() {
		try {
			this.nextId = IdGenerator
					.getFromDB(HibernateHelper.getConnection());
		} catch (final Exception e) {
			IdGenerator.LOG.error(e);
		}
	}

	/**
     * Gets the from db.
     *
     * @param connection
     *            the connection
     * @return the from db
     * @throws Exception
     *             the exception
     */
	public static int getFromDB(final Connection connection) throws Exception {
		return IdGenerator.newTemplate(connection).queryForInt(
				HibernateIdentityGenerator.GET_NEXT_ID_QUERY);
	}

	/**
     * Sets the in db.
     *
     * @param connection
     *            the connection
     * @param nextValue
     *            the next value
     * @throws Exception
     *             the exception
     */
	public static void setInDB(final Connection connection, final int nextValue)
			throws Exception {
		IdGenerator.newTemplate(connection).update(
				HibernateIdentityGenerator.SET_NEXT_ID_QUERY,
				new Object[] { new Integer(nextValue) });
	}

	/**
     * New template.
     *
     * @param connection
     *            the connection
     * @return the jdbc template
     */
	private static JdbcTemplate newTemplate(final Connection connection) {
		return new JdbcTemplate(
				new SingleConnectionDataSource(connection, true));
	}

	/**
     * The main method.
     *
     * @param args
     *            the arguments
     */
	public static void main(final String[] args) {
		System.out.println("id=" + IdGenerator.getUniqueId("test"));
		System.out.println("id=" + IdGenerator.getUniqueId("test"));
		System.out.println("id=" + IdGenerator.getUniqueId("test"));
	}

}