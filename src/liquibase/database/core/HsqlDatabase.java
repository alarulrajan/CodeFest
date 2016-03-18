package liquibase.database.core;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import liquibase.database.AbstractDatabase;
import liquibase.database.DatabaseConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.DateParseException;
import liquibase.servicelocator.PrioritizedService;
import liquibase.util.ISODateFormat;

public class HsqlDatabase extends AbstractDatabase {
	private static String START_CONCAT = "CONCAT(";
	private static String END_CONCAT = ")";
	private static String SEP_CONCAT = ", ";

	public HsqlDatabase() {
		super();
		super.defaultAutoIncrementStartWith = BigInteger.ZERO;
	}

	@Override
	public boolean isCorrectDatabaseImplementation(final DatabaseConnection conn)
			throws DatabaseException {
		return "HSQL Database Engine".equalsIgnoreCase(conn
				.getDatabaseProductName());
	}

	@Override
	public String getDefaultDriver(final String url) {
		if (url.startsWith("jdbc:hsqldb:")) {
			return "org.hsqldb.jdbcDriver";
		}
		return null;
	}

	@Override
	public int getPriority() {
		return PrioritizedService.PRIORITY_DEFAULT;
	}

	@Override
	public String getTypeName() {
		return "hsqldb";
	}

	@Override
	public boolean supportsSequences() {
		return true;
	}

	@Override
	public boolean supportsInitiallyDeferrableColumns() {
		return false;
	}

	@Override
	protected String getDefaultDatabaseSchemaName() throws DatabaseException {
		return "PUBLIC";
	}

	@Override
	public String getCurrentDateTimeFunction() {
		if (this.currentDateTimeFunction != null) {
			return this.currentDateTimeFunction;
		}

		return "NOW";
	}

	@Override
	public String getConcatSql(final String... values) {
		if (values == null) {
			return null;
		}

		return this.getConcatSql(Arrays.asList(values));
	}

	/**
	 * Recursive way of building CONCAT instruction
	 * 
	 * @param values
	 *            a non null List of String
	 * @return a String containing the CONCAT instruction with all elements, or
	 *         only a value if there is only one element in the list
	 */
	private String getConcatSql(final List<String> values) {
		if (values.size() == 1) {
			return values.get(0);
		} else {
			return HsqlDatabase.START_CONCAT + values.get(0)
					+ HsqlDatabase.SEP_CONCAT
					+ this.getConcatSql(values.subList(1, values.size()))
					+ HsqlDatabase.END_CONCAT;
		}
	}

	@Override
	public String getDateLiteral(final String isoDate) {
		String returnString = isoDate;
		try {
			if (this.isDateTime(isoDate)) {
				final ISODateFormat isoTimestampFormat = new ISODateFormat();
				final DateFormat dbTimestampFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.S");
				returnString = dbTimestampFormat.format(isoTimestampFormat
						.parse(isoDate));
			}
		} catch (final ParseException e) {
			throw new RuntimeException("Unexpected date format: " + isoDate, e);
		}
		return "'" + returnString + "'";
	}

	@Override
	public Date parseDate(final String dateAsString) throws DateParseException {
		try {
			if (dateAsString == "CURRENT_TIMESTAMP") {
				return new Date();
			}
			if (dateAsString.indexOf(" ") > 0) {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
						.parse(dateAsString);
			} else {
				if (dateAsString.indexOf(":") > 0) {
					return new SimpleDateFormat("HH:mm:ss").parse(dateAsString);
				} else {
					return new SimpleDateFormat("yyyy-MM-dd")
							.parse(dateAsString);
				}
			}
		} catch (final ParseException e) {
			throw new DateParseException(dateAsString);
		}
	}

	@Override
	public boolean supportsTablespaces() {
		return false;
	}

	@Override
	public String convertRequestedSchemaToSchema(final String requestedSchema)
			throws DatabaseException {
		return super.convertRequestedSchemaToSchema(requestedSchema)
				.toUpperCase();
	}

	@Override
	public String escapeDatabaseObject(final String objectName) {
		if (objectName != null) {
			if (HsqlDatabase.keywords.contains(objectName.toUpperCase())) {
				return "\"" + objectName + "\"";
			}
		}
		return objectName;
	}

	private static List keywords = Arrays.asList("ADD", "ALL", "ALLOCATE",
			"ALTER", "AND", "ANY", "ARE", "ARRAY", "AS", "ASENSITIVE",
			"ASYMMETRIC", "AT", "ATOMIC", "AUTHORIZATION", "AVG", "BEGIN",
			"BETWEEN", "BIGINT", "BINARY", "BLOB", "BOOLEAN", "BOTH", "BY",
			"CALL", "CALLED", "CASCADED", "CASE", "CAST", "CHAR", "CHARACTER",
			"CHECK", "CLOB", "CLOSE", "COLLATE", "COLUMN", "COMMIT",
			"CONDITION", "CONNECT", "CONSTRAINT", "CONTINUE", "CORRESPONDING",
			"COUNT", "CREATE", "CROSS", "CUBE", "CURRENT", "CURRENT_DATE",
			"CURRENT_DEFAULT_TRANSFORM_GRO", "CURRENT_PATH", "CURRENT_ROLE",
			"CURRENT_TIME", "CURRENT_TIMESTAMP",
			"CURRENT_TRANSFORM_GROUP_FOR_T", "CURRENT_USER", "CURSOR", "DATE",
			"DAY", "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT",
			"DELETE", "DEREF", "DESCRIBE", "DETERMINISTIC", "DISCONNECT",
			"DISTINCT", "DO", "DOUBLE", "DROP", "DYNAMIC", "EACH", "ELEMENT",
			"ELSE", "ELSEIF", "END", "ESCAPE", "EXCEPT", "EXEC", "EXECUTE",
			"EXISTS", "EXIT", "EXTERNAL", "FALSE", "FETCH", "FILTER", "FLOAT",
			"FOR", "FOREIGN", "FREE", "FROM", "FULL", "FUNCTION", "GET",
			"GLOBAL", "GRANT", "GROUP", "HAVING", "HOLD", "HOUR", "IDENTITY",
			"IF", "IMMEDIATE", "IN", "INDICATOR", "INNER", "INOUT", "INPUT",
			"INSENSITIVE", "INSERT", "INT", "INTEGER", "INTERSECT", "INTERVAL",
			"INTO", "IS", "ITERATE", "JOIN", "LANGUAGE", "LARGE", "LEADING",
			"LEAVE", "LEFT", "LIKE", "LOCAL", "LOCALTIME", "LOCALTIMESTAMP",
			"LOOP", "MATCH", "MAX", "MEMBER", "MERGE", "METHOD", "MIN",
			"MINUTE", "MODIFIES", "MODULE", "MONTH", "MULTISET", "NATIONAL",
			"NATURAL", "NCHAR", "NCLOB", "NEW", "NO", "NONE", "NOT", "NULL",
			"NUMERIC", "OF", "ON", "ONLY", "OPEN", "OR", "ORDER", "OUT",
			"OUTER", "OUTPUT", "OVER", "OVERLAPS", "PARAMETER", "PARTITION",
			"PRECISION", "PREPARE", "PRIMARY", "PROCEDURE", "RANGE", "READS",
			"REAL", "RECURSIVE", "REF", "REFERENCES", "REFERENCING", "RELEASE",
			"REPEAT", "RESIGNAL", "RESULT", "RETURN", "RETURNS", "REVOKE",
			"RIGHT", "ROLLBACK", "ROLLUP", "ROW", "ROWS", "SAVEPOINT", "SCOPE",
			"SCROLL", "SEARCH", "SELECT", "SENSITIVE", "SESSION_USER", "SET",
			"SIGNAL", "SIMILAR", "SMALLINT", "SOME", "SPECIFIC",
			"SPECIFICTYPE", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING",
			"START", "STATIC", "SUBMULTISET", "SYMMETRIC", "SYSTEM",
			"SYSTEM_USER", "TABLE", "TABLESAMPLE", "THEN", "TIME", "TIMESTAMP",
			"TIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", "TRAILING", "TREAT",
			"TRIGGER", "TRUE", "UNDO", "UNION", "UNIQUE", "UNNEST", "UNTIL",
			"UPDATE", "USER", "USING", "VALUE", "VALUES", "VARCHAR", "VARYING",
			"WHEN", "WHENEVER", "WHERE", "WHILE", "WINDOW", "WITHIN",
			"WITHOUT", "YEAR", "ALIAS", "AUTOCOMMIT", "CACHED", "CHECKPOINT",
			"EXPLAIN", "IGNORECASE", "INDEX", "LOGSIZE", "MATCHED", "MAXROWS",
			"MEMORY", "MINUS", "NEXT", "OPENBRACKET", "PLAN", "PROPERTY",
			"READONLY", "REFERENTIAL_INTEGRITY", "RENAME", "RESTART", "SCRIPT",
			"SCRIPTFORMAT", "SEMICOLON", "SEQUENCE", "SHUTDOWN", "SOURCE",
			"TEMP", "TEXT", "VIEW", "WRITE_DELAY", "VAR_POP", "VAR_SAMP",
			"STDDEV_POP", "STDDEV_SAMP", "DEFRAG", "INCREMENT", "TOCHAR",
			"DATABASE", "SCHEMA", "ROLE", "DOW", "INITIAL");
}
