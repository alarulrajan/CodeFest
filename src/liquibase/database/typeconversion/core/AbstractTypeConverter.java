package liquibase.database.typeconversion.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import liquibase.change.ColumnConfig;
import liquibase.database.Database;
import liquibase.database.core.DB2Database;
import liquibase.database.core.DerbyDatabase;
import liquibase.database.core.H2Database;
import liquibase.database.core.HsqlDatabase;
import liquibase.database.core.InformixDatabase;
import liquibase.database.core.MSSQLDatabase;
import liquibase.database.core.MySQLDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.structure.Column;
import liquibase.database.structure.type.BigIntType;
import liquibase.database.structure.type.BlobType;
import liquibase.database.structure.type.BooleanType;
import liquibase.database.structure.type.CharType;
import liquibase.database.structure.type.ClobType;
import liquibase.database.structure.type.CurrencyType;
import liquibase.database.structure.type.CustomType;
import liquibase.database.structure.type.DataType;
import liquibase.database.structure.type.DatabaseFunctionType;
import liquibase.database.structure.type.DateTimeType;
import liquibase.database.structure.type.DateType;
import liquibase.database.structure.type.DoubleType;
import liquibase.database.structure.type.FloatType;
import liquibase.database.structure.type.IntType;
import liquibase.database.structure.type.NVarcharType;
import liquibase.database.structure.type.NumberType;
import liquibase.database.structure.type.SmallIntType;
import liquibase.database.structure.type.TextType;
import liquibase.database.structure.type.TimeType;
import liquibase.database.structure.type.TinyIntType;
import liquibase.database.structure.type.UUIDType;
import liquibase.database.structure.type.VarcharType;
import liquibase.database.typeconversion.TypeConverter;
import liquibase.exception.DateParseException;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.logging.LogFactory;
import liquibase.statement.DatabaseFunction;
import liquibase.util.StringUtils;

/**
 * The Class AbstractTypeConverter.
 */
public abstract class AbstractTypeConverter implements TypeConverter {

	/* (non-Javadoc)
	 * @see liquibase.database.typeconversion.TypeConverter#convertDatabaseValueToObject(java.lang.Object, int, int, int, liquibase.database.Database)
	 */
	@Override
	public Object convertDatabaseValueToObject(final Object value,
			final int databaseDataType, final int firstParameter,
			final int secondParameter, final Database database)
			throws ParseException {
		if (value == null) {
			return null;
		} else if (value instanceof String) {
			return this
					.convertToCorrectObjectType(
							((String) value).replaceFirst("^'", "")
									.replaceFirst("'$", ""), databaseDataType,
							firstParameter, secondParameter, database);
		} else {
			return value;
		}
	}

	/* (non-Javadoc)
	 * @see liquibase.database.typeconversion.TypeConverter#getDataType(java.lang.Object)
	 */
	@Override
	public DataType getDataType(final Object object) {
		if (object instanceof BigInteger) {
			return this.getBigIntType();
		} else if (object instanceof Boolean) {
			return this.getBooleanType();
		} else if (object instanceof String) {
			return this.getVarcharType();
		} else if (object instanceof java.sql.Date) {
			return this.getDateType();
		} else if (object instanceof java.sql.Timestamp) {
			return this.getDateTimeType();
		} else if (object instanceof java.sql.Time) {
			return this.getTimeType();
		} else if (object instanceof java.util.Date) {
			return this.getDateTimeType();
		} else if (object instanceof Double) {
			return this.getDoubleType();
		} else if (object instanceof Float) {
			return this.getFloatType();
		} else if (object instanceof Integer) {
			return this.getIntType();
		} else if (object instanceof Long) {
			return this.getBigIntType();
		} else if (object instanceof DatabaseFunction) {
			return new DatabaseFunctionType();
		} else {
			throw new UnexpectedLiquibaseException("Unknown object type "
					+ object.getClass().getName());
		}
	}

	/**
     * Convert to correct object type.
     *
     * @param value
     *            the value
     * @param dataType
     *            the data type
     * @param columnSize
     *            the column size
     * @param decimalDigits
     *            the decimal digits
     * @param database
     *            the database
     * @return the object
     * @throws ParseException
     *             the parse exception
     */
	protected Object convertToCorrectObjectType(String value,
			final int dataType, final int columnSize, final int decimalDigits,
			final Database database) throws ParseException {
		if (value == null) {
			return null;
		}
		if (dataType == Types.CLOB || dataType == Types.VARCHAR
				|| dataType == Types.CHAR || dataType == Types.LONGVARCHAR) {
			if (value.equalsIgnoreCase("NULL")) {
				return null;
			} else {
				return value;
			}
		}

		value = StringUtils.trimToNull(value);
		if (value == null) {
			return null;
		}

		try {
			if (dataType == Types.DATE) {
				return new java.sql.Date(database.parseDate(value).getTime());
			} else if (dataType == Types.TIMESTAMP) {
				return new java.sql.Timestamp(database.parseDate(value)
						.getTime());
			} else if (dataType == Types.TIME) {
				return new java.sql.Time(database.parseDate(value).getTime());
			} else if (dataType == Types.BIGINT) {
				return new BigInteger(value);
			} else if (dataType == Types.BIT) {
				final Pattern pattern = Pattern.compile("b'",
						Pattern.CASE_INSENSITIVE);
				value = pattern.matcher(value).replaceFirst("");
				// value = value.replaceFirst("/b'/i", ""); //mysql puts wierd
				// chars in bit field
				if (value.equalsIgnoreCase("true")) {
					return Boolean.TRUE;
				} else if (value.equalsIgnoreCase("false")) {
					return Boolean.FALSE;
				} else if (value.equals("1")) {
					return Boolean.TRUE;
				} else if (value.equals("0")) {
					return Boolean.FALSE;
				} else if (value.equals("(1)")) {
					return Boolean.TRUE;
				} else if (value.equals("(0)")) {
					return Boolean.FALSE;
				}
				throw new ParseException("Unknown bit value: " + value, 0);
			} else if (dataType == Types.BOOLEAN) {
				return Boolean.valueOf(value);
			} else if (dataType == Types.DECIMAL) {
				if (decimalDigits == 0) {
					return new Integer(value);
				}
				return new BigDecimal(value);
			} else if (dataType == Types.DOUBLE || dataType == Types.NUMERIC) {
				return new BigDecimal(value);
			} else if (dataType == Types.FLOAT) {
				return new Float(value);
			} else if (dataType == Types.INTEGER) {
				return new Integer(value);
			} else if (dataType == Types.NULL) {
				return null;
			} else if (dataType == Types.REAL) {
				return new Float(value);
			} else if (dataType == Types.SMALLINT) {
				return new Integer(value);
			} else if (dataType == Types.TINYINT) {
				return new Integer(value);
			} else if (dataType == Types.BLOB) {
				return "!!!!!! LIQUIBASE CANNOT OUTPUT BLOB VALUES !!!!!!";
			} else {
				LogFactory.getLogger().warning(
						"Do not know how to convert type " + dataType);
				return value;
			}
		} catch (final DateParseException e) {
			return new DatabaseFunction(value);
		} catch (final NumberFormatException e) {
			return new DatabaseFunction(value);
		}
	}

	/**
     * Returns the database-specific datatype for the given column
     * configuration. This method will convert some generic column types (e.g.
     * boolean, currency) to the correct type for the current database.
     *
     * @param columnTypeString
     *            the column type string
     * @param autoIncrement
     *            the auto increment
     * @return the data type
     */
	@Override
	public DataType getDataType(final String columnTypeString,
			final Boolean autoIncrement) {
		// Parse out data type and precision
		// Example cases: "CLOB", "java.sql.Types.CLOB", "CLOB(10000)",
		// "java.sql.Types.CLOB(10000)
		String dataTypeName = null;
		String precision = null;
		String additionalInformation = null;
		if (columnTypeString.startsWith("java.sql.Types")
				&& columnTypeString.contains("(")) {
			precision = columnTypeString.substring(
					columnTypeString.indexOf("(") + 1,
					columnTypeString.indexOf(")"));
			dataTypeName = columnTypeString.substring(
					columnTypeString.lastIndexOf(".") + 1,
					columnTypeString.indexOf("("));
		} else if (columnTypeString.startsWith("java.sql.Types")) {
			dataTypeName = columnTypeString.substring(columnTypeString
					.lastIndexOf(".") + 1);
		} else if (columnTypeString.contains("(")) {
			precision = columnTypeString.substring(
					columnTypeString.indexOf("(") + 1,
					columnTypeString.indexOf(")"));
			dataTypeName = columnTypeString.substring(0,
					columnTypeString.indexOf("("));
		} else {
			dataTypeName = columnTypeString;
		}
		if (columnTypeString.contains(")")) {
			additionalInformation = StringUtils.trimToNull(columnTypeString
					.replaceFirst(".*\\)", ""));
		}

		return this.getDataType(columnTypeString, autoIncrement, dataTypeName,
				precision, additionalInformation);
	}

	/**
     * Gets the data type.
     *
     * @param columnTypeString
     *            the column type string
     * @param autoIncrement
     *            the auto increment
     * @param dataTypeName
     *            the data type name
     * @param precision
     *            the precision
     * @param additionalInformation
     *            the additional information
     * @return the data type
     */
	protected DataType getDataType(final String columnTypeString,
			final Boolean autoIncrement, final String dataTypeName,
			final String precision, final String additionalInformation) {
		// Translate type to database-specific type, if possible
		DataType returnTypeName = null;
		if (dataTypeName.equalsIgnoreCase("BIGINT")) {
			returnTypeName = this.getBigIntType();
		} else if (dataTypeName.equalsIgnoreCase("NUMBER")
				|| dataTypeName.equalsIgnoreCase("NUMERIC")) {
			returnTypeName = this.getNumberType();
		} else if (dataTypeName.equalsIgnoreCase("BLOB")) {
			returnTypeName = this.getBlobType();
		} else if (dataTypeName.equalsIgnoreCase("BOOLEAN")) {
			returnTypeName = this.getBooleanType();
		} else if (dataTypeName.equalsIgnoreCase("CHAR")) {
			returnTypeName = this.getCharType();
		} else if (dataTypeName.equalsIgnoreCase("CLOB")) {
			returnTypeName = this.getClobType();
		} else if (dataTypeName.equalsIgnoreCase("CURRENCY")) {
			returnTypeName = this.getCurrencyType();
		} else if (dataTypeName.equalsIgnoreCase("DATE")
				|| dataTypeName.equalsIgnoreCase(this.getDateType()
						.getDataTypeName())) {
			returnTypeName = this.getDateType();
		} else if (dataTypeName.equalsIgnoreCase("DATETIME")
				|| dataTypeName.equalsIgnoreCase(this.getDateTimeType()
						.getDataTypeName())) {
			returnTypeName = this.getDateTimeType();
		} else if (dataTypeName.equalsIgnoreCase("DOUBLE")) {
			returnTypeName = this.getDoubleType();
		} else if (dataTypeName.equalsIgnoreCase("FLOAT")) {
			returnTypeName = this.getFloatType();
		} else if (dataTypeName.equalsIgnoreCase("INT")) {
			returnTypeName = this.getIntType();
		} else if (dataTypeName.equalsIgnoreCase("INTEGER")) {
			returnTypeName = this.getIntType();
		} else if (dataTypeName.equalsIgnoreCase("LONGBLOB")) {
			returnTypeName = this.getLongBlobType();
		} else if (dataTypeName.equalsIgnoreCase("LONGVARBINARY")) {
			returnTypeName = this.getBlobType();
		} else if (dataTypeName.equalsIgnoreCase("LONGVARCHAR")) {
			returnTypeName = this.getClobType();
		} else if (dataTypeName.equalsIgnoreCase("SMALLINT")) {
			returnTypeName = this.getSmallIntType();
		} else if (dataTypeName.equalsIgnoreCase("TEXT")) {
			returnTypeName = this.getClobType();
		} else if (dataTypeName.equalsIgnoreCase("TIME")
				|| dataTypeName.equalsIgnoreCase(this.getTimeType()
						.getDataTypeName())) {
			returnTypeName = this.getTimeType();
		} else if (dataTypeName.toUpperCase().contains("TIMESTAMP")) {
			returnTypeName = this.getDateTimeType();
		} else if (dataTypeName.equalsIgnoreCase("TINYINT")) {
			returnTypeName = this.getTinyIntType();
		} else if (dataTypeName.equalsIgnoreCase("UUID")) {
			returnTypeName = this.getUUIDType();
		} else if (dataTypeName.equalsIgnoreCase("VARCHAR")) {
			returnTypeName = this.getVarcharType();
		} else if (dataTypeName.equalsIgnoreCase("NVARCHAR")) {
			returnTypeName = this.getNVarcharType();
		} else {
			return new CustomType(columnTypeString, 0, 2);
		}

		if (returnTypeName == null) {
			throw new UnexpectedLiquibaseException("Could not determine "
					+ dataTypeName + " for " + this.getClass().getName());
		}
		this.addPrecisionToType(precision, returnTypeName);
		returnTypeName.setAdditionalInformation(additionalInformation);

		return returnTypeName;
	}

	/**
     * Adds the precision to type.
     *
     * @param precision
     *            the precision
     * @param returnTypeName
     *            the return type name
     * @throws NumberFormatException
     *             the number format exception
     */
	protected void addPrecisionToType(final String precision,
			final DataType returnTypeName) throws NumberFormatException {
		if (precision != null) {
			final String[] params = precision.split(",");
			returnTypeName.setFirstParameter(params[0].trim());
			if (params.length > 1) {
				returnTypeName.setSecondParameter(params[1].trim());
			}
		}
	}

	/* (non-Javadoc)
	 * @see liquibase.database.typeconversion.TypeConverter#getDataType(liquibase.change.ColumnConfig)
	 */
	@Override
	public DataType getDataType(final ColumnConfig columnConfig) {
		return this.getDataType(columnConfig.getType(),
				columnConfig.isAutoIncrement());
	}

	/**
     * Returns the actual database-specific data type to use a "date" (no time
     * information) column.
     *
     * @return the date type
     */
	@Override
	public DateType getDateType() {
		return new DateType();
	}

	/**
     * Returns the actual database-specific data type to use a "time" column.
     *
     * @return the time type
     */
	@Override
	public TimeType getTimeType() {
		return new TimeType();
	}

	/* (non-Javadoc)
	 * @see liquibase.database.typeconversion.TypeConverter#getDateTimeType()
	 */
	@Override
	public DateTimeType getDateTimeType() {
		return new DateTimeType();
	}

	/* (non-Javadoc)
	 * @see liquibase.database.typeconversion.TypeConverter#getBigIntType()
	 */
	@Override
	public BigIntType getBigIntType() {
		return new BigIntType();
	}

	/**
     * Returns the actual database-specific data type to use for a "char"
     * column.
     *
     * @return the char type
     */
	@Override
	public CharType getCharType() {
		return new CharType();
	}

	/**
     * Returns the actual database-specific data type to use for a "varchar"
     * column.
     *
     * @return the varchar type
     */
	@Override
	public VarcharType getVarcharType() {
		return new VarcharType();
	}

	/**
     * Returns the actual database-specific data type to use for a "varchar"
     * column.
     *
     * @return the n varchar type
     */
	public NVarcharType getNVarcharType() {
		return new NVarcharType();
	}

	/**
	 * Returns the actual database-specific data type to use for a "float"
	 * column.
	 * 
	 * @return database-specific type for float
	 */
	@Override
	public FloatType getFloatType() {
		return new FloatType();
	}

	/**
	 * Returns the actual database-specific data type to use for a "double"
	 * column.
	 * 
	 * @return database-specific type for double
	 */
	@Override
	public DoubleType getDoubleType() {
		return new DoubleType();
	}

	/**
	 * Returns the actual database-specific data type to use for a "int" column.
	 * 
	 * @return database-specific type for int
	 */
	@Override
	public IntType getIntType() {
		return new IntType();
	}

	/**
	 * Returns the actual database-specific data type to use for a "tinyint"
	 * column.
	 * 
	 * @return database-specific type for tinyint
	 */
	@Override
	public TinyIntType getTinyIntType() {
		return new TinyIntType();
	}

	/**
     * Gets the small int type.
     *
     * @return the small int type
     */
	public SmallIntType getSmallIntType() {
		return new SmallIntType();
	}

	/* (non-Javadoc)
	 * @see liquibase.database.typeconversion.TypeConverter#getBooleanType()
	 */
	@Override
	public BooleanType getBooleanType() {
		return new BooleanType();
	}

	/**
     * Gets the number type.
     *
     * @return the number type
     */
	public NumberType getNumberType() {
		return new NumberType();
	}

	/* (non-Javadoc)
	 * @see liquibase.database.typeconversion.TypeConverter#getCurrencyType()
	 */
	@Override
	public CurrencyType getCurrencyType() {
		return new CurrencyType();
	}

	/* (non-Javadoc)
	 * @see liquibase.database.typeconversion.TypeConverter#getUUIDType()
	 */
	@Override
	public UUIDType getUUIDType() {
		return new UUIDType();
	}

	/* (non-Javadoc)
	 * @see liquibase.database.typeconversion.TypeConverter#getTextType()
	 */
	public TextType getTextType() {
		return this.getClobType();
	}

	/* (non-Javadoc)
	 * @see liquibase.database.typeconversion.TypeConverter#getClobType()
	 */
	@Override
	public ClobType getClobType() {
		return new ClobType();
	}

	/* (non-Javadoc)
	 * @see liquibase.database.typeconversion.TypeConverter#getBlobType()
	 */
	@Override
	public BlobType getBlobType() {
		return new BlobType();
	}

	/* (non-Javadoc)
	 * @see liquibase.database.typeconversion.TypeConverter#getLongBlobType()
	 */
	@Override
	public BlobType getLongBlobType() {
		return this.getBlobType();
	}

	/* (non-Javadoc)
	 * @see liquibase.database.typeconversion.TypeConverter#convertToDatabaseTypeString(liquibase.database.structure.Column, liquibase.database.Database)
	 */
	@Override
	public String convertToDatabaseTypeString(final Column referenceColumn,
			final Database database) {

		final List<Integer> noParens = Arrays.asList(Types.ARRAY, Types.BIGINT,
				Types.BINARY, Types.BIT, Types.BLOB, Types.BOOLEAN, Types.CLOB,
				Types.DATALINK, Types.DATE, Types.DISTINCT, Types.INTEGER,
				Types.JAVA_OBJECT, Types.LONGVARBINARY, Types.NULL,
				Types.OTHER, Types.REF, Types.SMALLINT, Types.STRUCT,
				Types.TIME, Types.TIMESTAMP, Types.TINYINT, Types.LONGVARCHAR);

		final List<Integer> oneParam = Arrays.asList(Types.CHAR, -15, // Types.NCHAR
																		// in
																		// java
																		// 1.6,
				Types.VARCHAR, -9, // Types.NVARCHAR in java 1.6,
				Types.VARBINARY, Types.DOUBLE, Types.FLOAT);

		final List<Integer> twoParams = Arrays.asList(Types.DECIMAL,
				Types.NUMERIC, Types.REAL);

		String translatedTypeName = referenceColumn.getTypeName();
		if (database instanceof PostgresDatabase) {
			if ("bpchar".equals(translatedTypeName)) {
				translatedTypeName = "char";
			}
		}

		if (database instanceof HsqlDatabase || database instanceof H2Database
				|| database instanceof DerbyDatabase
				|| database instanceof DB2Database) {
			if (referenceColumn.getDataType() == Types.FLOAT
					|| referenceColumn.getDataType() == Types.DOUBLE) {
				return "float";
			}
		}

		if (database instanceof InformixDatabase) {
			/*
			 * rs.getInt("DATA_TYPE") returns 1 (Types.CHAR) for interval types
			 * (bug in JDBC driver?) So if you comment this out, the the
			 * columnsize will be appended and the type becomes:
			 * "INTERVAL HOUR TO FRACTION(3)(2413)"
			 */
			if (translatedTypeName.toUpperCase().startsWith("INTERVAL")) {
				return translatedTypeName;
			}
			if (referenceColumn.getDataType() == Types.REAL) {
				return "SMALLFLOAT";
			}
		}

		String dataType;
		if (noParens.contains(referenceColumn.getDataType())) {
			dataType = translatedTypeName;
		} else if (oneParam.contains(referenceColumn.getDataType())) {
			if (database instanceof PostgresDatabase
					&& translatedTypeName.equalsIgnoreCase("TEXT")) {
				return translatedTypeName;
			} else if (database instanceof MSSQLDatabase
					&& translatedTypeName.equals("uniqueidentifier")) {
				return translatedTypeName;
			} else if (database instanceof MySQLDatabase
					&& (translatedTypeName.startsWith("enum(") || translatedTypeName
							.startsWith("set("))) {
				return translatedTypeName;
			} else if (database instanceof OracleDatabase
					&& translatedTypeName.equals("VARCHAR2")) {
				return translatedTypeName + "("
						+ referenceColumn.getColumnSize() + " "
						+ referenceColumn.getLengthSemantics() + ")";
			} else if (database instanceof MySQLDatabase
					&& translatedTypeName.equalsIgnoreCase("DOUBLE")) {
				return translatedTypeName;
			} else if (database instanceof MySQLDatabase
					&& translatedTypeName.equalsIgnoreCase("DOUBLE PRECISION")) {
				return translatedTypeName;
			}
			dataType = translatedTypeName + "("
					+ referenceColumn.getColumnSize() + ")";
		} else if (twoParams.contains(referenceColumn.getDataType())) {
			if (database instanceof PostgresDatabase
					&& referenceColumn.getColumnSize() == 131089) {
				dataType = "DECIMAL";
			} else if (database instanceof MSSQLDatabase
					&& translatedTypeName.toLowerCase().contains("money")) {
				dataType = translatedTypeName.toUpperCase();
			} else {
				dataType = translatedTypeName;
				if (referenceColumn.isInitPrecision()) {
					dataType += "(" + referenceColumn.getColumnSize() + ","
							+ referenceColumn.getDecimalDigits() + ")";
				}
			}
		} else {
			LogFactory.getLogger().warning(
					"Unknown Data Type: " + referenceColumn.getDataType()
							+ " (" + referenceColumn.getTypeName()
							+ ").  Assuming it does not take parameters");
			dataType = referenceColumn.getTypeName();
		}
		return dataType;

	}
}