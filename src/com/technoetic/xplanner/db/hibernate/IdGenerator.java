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

public class IdGenerator {
	int nextId;

	protected static final Logger LOG = LogUtil.getLogger();

	static IdGenerator instance;
	public static final int NEXT_ID_COL_INDEX = 1;

	public static String getUniqueId(final String prefix) {
		return prefix + IdGenerator.getInstance().getNext();
	}

	public static int getNextPersistentId() throws Exception {
		IdGenerator.getInstance();
		return IdGenerator.getFromDB(HibernateHelper.getConnection());
	}

	public static void setNextPersistentId(final int id) throws Exception {
		IdGenerator.getInstance();
		IdGenerator.setInDB(HibernateHelper.getConnection(), id);
	}

	private int getNext() {
		return this.nextId++;
	}

	private static IdGenerator getInstance() {
		if (IdGenerator.instance == null) {
			IdGenerator.instance = new IdGenerator();
		}
		return IdGenerator.instance;
	}

	private IdGenerator() {
		try {
			this.nextId = IdGenerator
					.getFromDB(HibernateHelper.getConnection());
		} catch (final Exception e) {
			IdGenerator.LOG.error(e);
		}
	}

	public static int getFromDB(final Connection connection) throws Exception {
		return IdGenerator.newTemplate(connection).queryForInt(
				HibernateIdentityGenerator.GET_NEXT_ID_QUERY);
	}

	public static void setInDB(final Connection connection, final int nextValue)
			throws Exception {
		IdGenerator.newTemplate(connection).update(
				HibernateIdentityGenerator.SET_NEXT_ID_QUERY,
				new Object[] { new Integer(nextValue) });
	}

	private static JdbcTemplate newTemplate(final Connection connection) {
		return new JdbcTemplate(
				new SingleConnectionDataSource(connection, true));
	}

	public static void main(final String[] args) {
		System.out.println("id=" + IdGenerator.getUniqueId("test"));
		System.out.println("id=" + IdGenerator.getUniqueId("test"));
		System.out.println("id=" + IdGenerator.getUniqueId("test"));
	}

}