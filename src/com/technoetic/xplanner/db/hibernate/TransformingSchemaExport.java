package com.technoetic.xplanner.db.hibernate;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.connection.ConnectionProviderFactory;
import org.hibernate.dialect.Dialect;
import org.hibernate.util.JDBCExceptionReporter;

/**
 * Modified version of Hibernate's SchemaExport to allow us to use XSL
 * transformed mapping files. The original class couldn't be used because the
 * execute() method is private.
 */
public class TransformingSchemaExport {

	/** The Constant log. */
	private static final Log log = LogFactory
			.getLog(TransformingSchemaExport.class);

	/** The drop sql. */
	private final String[] dropSQL;
	
	/** The create sql. */
	private final String[] createSQL;
	
	/** The connection properties. */
	private final Properties connectionProperties;
	
	/** The output file. */
	private String outputFile = null;
	
	/** The dialect. */
	private final Dialect dialect;
	
	/** The delimiter. */
	private String delimiter;

	/**
     * Create a schema exporter for the given Configuration.
     *
     * @param cfg
     *            the cfg
     * @throws HibernateException
     *             the hibernate exception
     */
	public TransformingSchemaExport(final Configuration cfg)
			throws HibernateException {
		this(cfg, cfg.getProperties());
	}

	/**
     * Create a schema exporter for the given Configuration, with the given
     * database connection properties.
     *
     * @param cfg
     *            the cfg
     * @param connectionProperties
     *            the connection properties
     * @throws HibernateException
     *             the hibernate exception
     */
	public TransformingSchemaExport(final Configuration cfg,
			final Properties connectionProperties) throws HibernateException {
		this.connectionProperties = connectionProperties;
		this.dialect = Dialect.getDialect(connectionProperties);
		this.dropSQL = cfg.generateDropSchemaScript(this.dialect);
		this.createSQL = cfg.generateSchemaCreationScript(this.dialect);
	}

	/**
     * Set an output filename. The generated script will be written to this
     * file.
     *
     * @param filename
     *            the filename
     * @return the transforming schema export
     */
	public TransformingSchemaExport setOutputFile(final String filename) {
		this.outputFile = filename;
		return this;
	}

	/**
     * Set the end of statement delimiter.
     *
     * @param delimiter
     *            the delimiter
     * @return the transforming schema export
     */
	public TransformingSchemaExport setDelimiter(final String delimiter) {
		this.delimiter = delimiter;
		return this;
	}

	/**
     * Run the schema creation script.
     *
     * @param script
     *            print the DDL to the console
     * @param export
     *            export the script to the database
     * @throws Exception
     *             the exception
     */
	public void create(final boolean script, final boolean export)
			throws Exception {
		this.execute(script, export, false, true);
	}

	/**
     * Run the drop schema script.
     *
     * @param script
     *            print the DDL to the console
     * @param export
     *            export the script to the database
     * @throws Exception
     *             the exception
     */
	public void drop(final boolean script, final boolean export)
			throws Exception {
		this.execute(script, export, true, true);
	}

	/**
     * Execute.
     *
     * @param script
     *            the script
     * @param export
     *            the export
     * @param justDrop
     *            the just drop
     * @param format
     *            the format
     * @throws Exception
     *             the exception
     */
	private void execute(final boolean script, final boolean export,
			final boolean justDrop, final boolean format) throws Exception {

		TransformingSchemaExport.log.info("Running hbm2ddl schema export");

		Connection connection = null;
		FileWriter fileOutput = null;
		ConnectionProvider connectionProvider = null;
		Statement statement = null;

		final Properties props = new Properties();
		props.putAll(this.dialect.getDefaultProperties());
		props.putAll(this.connectionProperties);

		try {

			if (this.outputFile != null) {
				TransformingSchemaExport.log
						.info("writing generated schema to file: "
								+ this.outputFile);
				fileOutput = new FileWriter(this.outputFile);
			}

			if (export) {
				TransformingSchemaExport.log
						.info("exporting generated schema to database");
				connectionProvider = ConnectionProviderFactory
						.newConnectionProvider(props);
				connection = connectionProvider.getConnection();
				if (!connection.getAutoCommit()) {
					connection.commit();
					connection.setAutoCommit(true);
				}
				statement = connection.createStatement();
			}

			for (int i = 0; i < this.dropSQL.length; i++) {
				try {
					String formatted = this.dropSQL[i];
					if (this.delimiter != null) {
						formatted += this.delimiter;
					}
					if (script) {
						System.out.println(formatted);
					}
					TransformingSchemaExport.log.debug(formatted);
					if (this.outputFile != null) {
						fileOutput.write(formatted + "\n");
					}
					if (export) {
						statement.executeUpdate(this.dropSQL[i]);
					}
				} catch (final SQLException e) {
					TransformingSchemaExport.log.debug("Unsuccessful: "
							+ this.dropSQL[i]);
					TransformingSchemaExport.log.debug(e.getMessage());
				}

			}

			if (!justDrop) {
				for (int j = 0; j < this.createSQL.length; j++) {
					String formatted = format ? TransformingSchemaExport
							.format(this.createSQL[j]) : this.createSQL[j];
					if (this.delimiter != null) {
						formatted += this.delimiter;
					}
					if (script) {
						System.out.println(formatted);
					}
					TransformingSchemaExport.log.debug(formatted);
					if (this.outputFile != null) {
						fileOutput.write(formatted + "\n");
					}
					if (export) {
						statement.executeUpdate(this.createSQL[j]);
					}
				}
			}

			TransformingSchemaExport.log.info("schema export complete");

		} catch (final Exception e) {
			TransformingSchemaExport.log.error("schema export unsuccessful", e);
			throw e;
		} finally {

			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					JDBCExceptionReporter.logWarnings(connection.getWarnings());
					connection.clearWarnings();
					connectionProvider.closeConnection(connection);
					connectionProvider.close();
				}
			} catch (final Exception e) {
				TransformingSchemaExport.log.error(
						"Could not close connection", e);
				throw e;
			}

			try {
				if (fileOutput != null) {
					fileOutput.close();
				}
			} catch (final IOException ioe) {
				TransformingSchemaExport.log.error(
						"Error closing output file: " + this.outputFile, ioe);
				throw ioe;
			}

		}
	}

	/**
     * Format an SQL statement using simple rules: a) Insert newline after each
     * comma; b) Indent three spaces after each inserted newline; If the
     * statement contains single/double quotes return unchanged, it is too
     * complex and could be broken by simple formatting.
     *
     * @param sql
     *            the sql
     * @return the string
     */
	private static String format(final String sql) {

		if (sql.indexOf("\"") > 0 || sql.indexOf("'") > 0) {
			return sql;
		}

		String formatted;

		if (sql.toLowerCase().startsWith("create table")) {

			final StringBuffer result = new StringBuffer();
			final StringTokenizer tokens = new StringTokenizer(sql, "(,)", true);

			int depth = 0;

			while (tokens.hasMoreTokens()) {
				final String tok = tokens.nextToken();
				if (")".equals(tok)) {
					depth--;
					if (depth == 0) {
						result.append("\n");
					}
				}
				result.append(tok);
				if (",".equals(tok) && depth == 1) {
					result.append("\n  ");
				}
				if ("(".equals(tok)) {
					depth++;
					if (depth == 1) {
						result.append("\n   ");
					}
				}
			}

			formatted = result.toString();

		} else {
			formatted = sql;
		}

		return formatted;
	}
}
