package com.technoetic.xplanner.db.hsqldb;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hsqldb.DatabaseURL;
import org.hsqldb.Server;
import org.hsqldb.server.ServerConstants;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.util.LogUtil;

/**
 * The Class HsqlServer.
 *
 * @noinspection UseOfSystemOutOrSystemErr,EmptyCatchBlock
 */

public class HsqlServer {
    
    /** The Constant LOG. */
    public static final Logger LOG = LogUtil.getLogger();
    
    /** The Constant DATABASE_NAME. */
    public static final String DATABASE_NAME = "xplanner";

    /** The server. */
    public static HsqlServer theServer;

    /** The Constant HSQLDB_DB_PATH. */
    public static final String HSQLDB_DB_PATH = "xplanner.hsqldb.server.database";
    
    /** The Constant HSQLDB_URL. */
    public static final String HSQLDB_URL = XPlannerProperties.CONNECTION_URL_KEY;
    
    /** The Constant WEBAPP_ROOT_TOKEN. */
    public static final String WEBAPP_ROOT_TOKEN = "${WEBAPP_ROOT}";
    
    /** The Constant WEBAPP_ROOT_TOKEN_PATTERN_STRING. */
    public static final String WEBAPP_ROOT_TOKEN_PATTERN_STRING = "\\$\\{WEBAPP_ROOT\\}";

    /** The Constant FILE_URL_PREFIX. */
    public static final String FILE_URL_PREFIX = DatabaseURL.S_URL_PREFIX
            + DatabaseURL.S_FILE;
    
    /** The Constant HSQLDB_FILE_URL_PATTERN_STRING. */
    public static final String HSQLDB_FILE_URL_PATTERN_STRING = HsqlServer.FILE_URL_PREFIX
            + ":([a-zA-Z0-9/\\\\]+)";
    
    /** The Constant HSQLDB_FILE_URL_PATTERN. */
    public static final Pattern HSQLDB_FILE_URL_PATTERN = Pattern
            .compile(HsqlServer.HSQLDB_FILE_URL_PATTERN_STRING);
    
    /** The Constant HSQL_URL_PREFIX. */
    public static final String HSQL_URL_PREFIX = DatabaseURL.S_URL_PREFIX
            + DatabaseURL.S_HSQL;
    
    /** The Constant HSQLDB_HSQL_URL_PATTERN_STRING. */
    public static final String HSQLDB_HSQL_URL_PATTERN_STRING = HsqlServer.HSQL_URL_PREFIX
            + "([a-zA-Z0-9.]*)(:([0-9]+))?/([a-zA-Z0-9]+)";
    
    /** The Constant HSQLDB_HSQL_URL_PATTERN. */
    public static final Pattern HSQLDB_HSQL_URL_PATTERN = Pattern
            .compile(HsqlServer.HSQLDB_HSQL_URL_PATTERN_STRING);
    
    /** The Constant ABSOLUTE_PATH_PATTERN_STRING. */
    public static final String ABSOLUTE_PATH_PATTERN_STRING = "^([a-zA-Z]:|/|\\\\).*";
    
    /** The Constant ABSOLUTE_PATH_PATTERN. */
    public static final Pattern ABSOLUTE_PATH_PATTERN = Pattern
            .compile(HsqlServer.ABSOLUTE_PATH_PATTERN_STRING);

    /** The web root path. */
    private String webRootPath = "";
    
    /** The server. */
    private Server server;
    
    /** The database name. */
    private String databaseName;
    
    /** The db path. */
    private String dbPath;
    
    /** The port. */
    private int port = 9001;

    /**
     * Start.
     */
    public static void start() {
        HsqlServer.start("");
    }

    /**
     * Start.
     *
     * @param webRoot
     *            the web root
     */
    public static void start(final String webRoot) {
        final XPlannerProperties properties = new XPlannerProperties();
        if (!HsqlServer.isHsqldb(properties)) {
            return;
        }

        HsqlServer.theServer = new HsqlServer();
        HsqlServer.theServer.initProperties(webRoot, properties);
        HsqlServer.registerShutdownHook();

        if (HsqlServer.isEmbeddedPrivateDatabase(properties)) {
            HsqlServer.LOG
                    .info("HSQL: detected an embedded database, in-process public server not started");
            return;
        }
        if (HsqlServer.isRemoteDatabase(properties)) {
            HsqlServer.LOG
                    .info("HSQL: detected a remote database, in-process server not started");
            return;
        }
        if (HsqlServer.isLocalOutOfProcessDatabase(properties)) {
            HsqlServer.LOG
                    .info("HSQL: detected a local out-of-process database, in-process server not started");
            return;
        }
        HsqlServer.theServer.startPublicServer();
    }

    /**
     * Register shutdown hook.
     */
    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                HsqlServer.shutdown();
            }
        });
    }

    /**
     * Start public server.
     */
    public void startPublicServer() {
        if (this.dbPath == null) {
            throw new RuntimeException(
                    HsqlServer.HSQLDB_DB_PATH
                            + " property is required in order to start an in-process HSQLDB server");
        }

        HsqlServer.LOG.info("Starting HSQLDB server for db "
                + this.databaseName + " stored at " + this.dbPath);

        this.server = this.createServer();
        this.server.setDatabasePath(0, this.dbPath);
        this.server.setDatabaseName(0, this.databaseName);
        this.server.setNoSystemExit(true);
        this.server.setPort(this.port);
        this.server.setTrace(HsqlServer.isTraceOn());
        this.server.setSilent(!HsqlServer.isTraceOn());
        this.server.start();

        this.waitForServerToTransitionOutOf(ServerConstants.SERVER_STATE_OPENING);
        HsqlServer.LOG.info("HSQLDB server started");
    }

    /**
     * Inits the properties.
     *
     * @param webRootPath
     *            the web root path
     * @param properties
     *            the properties
     */
    public void initProperties(final String webRootPath,
            final XPlannerProperties properties) {
        this.webRootPath = webRootPath;

        if (HsqlServer.isHsqlProtocol(properties)) {
            this.dbPath = this.getDbPath(properties);
            if (this.dbPath != null) {
                final Matcher matcher = HsqlServer.HSQLDB_HSQL_URL_PATTERN
                        .matcher(HsqlServer.getUrl(properties));
                if (matcher.matches()) {
                    this.databaseName = this.getDatabaseName(matcher);
                    this.port = this.getPort(matcher);
                }
            }
        }

        HsqlServer.setUrl(properties,
                this.replaceWebRootToken(HsqlServer.getUrl(properties)));

        HsqlServer.LOG.debug("    "
                + this.dumpPropertyValue(HsqlServer.HSQLDB_URL, properties));
        HsqlServer.LOG
                .debug("    "
                        + this.dumpPropertyValue(HsqlServer.HSQLDB_DB_PATH,
                                properties));
        HsqlServer.LOG.debug("    "
                + this.dumpNameValuePair("webroot", webRootPath));
    }

    /**
     * Dump property value.
     *
     * @param property
     *            the property
     * @param properties
     *            the properties
     * @return the string
     */
    private String dumpPropertyValue(final String property,
            final XPlannerProperties properties) {
        return this.dumpNameValuePair(property,
                properties.getProperty(property));
    }

    /**
     * Dump name value pair.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     * @return the string
     */
    private String dumpNameValuePair(final String name, final String value) {
        return name + "='" + value + "'";
    }

    /**
     * Gets the url.
     *
     * @param properties
     *            the properties
     * @return the url
     */
    private static String getUrl(final XPlannerProperties properties) {
        return properties.getProperty(HsqlServer.HSQLDB_URL);
    }

    /**
     * Sets the url.
     *
     * @param properties
     *            the properties
     * @param value
     *            the value
     */
    private static void setUrl(final XPlannerProperties properties,
            final String value) {
        properties.setProperty(HsqlServer.HSQLDB_URL, value);
    }

    /**
     * Gets the database name.
     *
     * @param matcher
     *            the matcher
     * @return the database name
     */
    private String getDatabaseName(final Matcher matcher) {
        final String name = matcher.group(4);
        if (name == null) {
            return HsqlServer.DATABASE_NAME;
        }
        return name;
    }

    /**
     * Gets the port.
     *
     * @param matcher
     *            the matcher
     * @return the port
     */
    private int getPort(final Matcher matcher) {
        final String port = matcher.group(3);
        if (port == null) {
            return 9001;
        }
        return Integer.parseInt(port);
    }

    /**
     * Checks if is hsqldb.
     *
     * @param properties
     *            the properties
     * @return true, if is hsqldb
     */
    public static boolean isHsqldb(final XPlannerProperties properties) {
        return properties.getProperty(HsqlServer.HSQLDB_URL, "").startsWith(
                DatabaseURL.S_URL_PREFIX);
    }

    /**
     * Checks if is local public database started.
     *
     * @return true, if is local public database started
     */
    public static boolean isLocalPublicDatabaseStarted() {
        return HsqlServer.theServer != null
                && HsqlServer.theServer.server != null
                && HsqlServer.theServer.server.getState() == ServerConstants.SERVER_STATE_ONLINE;
    }

    /**
     * Checks if is local out of process database.
     *
     * @param properties
     *            the properties
     * @return true, if is local out of process database
     */
    private static boolean isLocalOutOfProcessDatabase(
            final XPlannerProperties properties) {
        return HsqlServer.isLocalHsqlProtocolDatabase(properties)
                && !HsqlServer.isEmbeddedDatabaseSpecified(properties);
    }

    /**
     * Checks if is remote database.
     *
     * @param properties
     *            the properties
     * @return true, if is remote database
     */
    private static boolean isRemoteDatabase(final XPlannerProperties properties) {
        return !HsqlServer.isLocalHsqlProtocolDatabase(properties)
                && HsqlServer.isHsqlProtocol(properties);
    }

    /**
     * Checks if is hsql protocol.
     *
     * @param properties
     *            the properties
     * @return true, if is hsql protocol
     */
    private static boolean isHsqlProtocol(final XPlannerProperties properties) {
        return HsqlServer.getUrl(properties) != null
                && HsqlServer.getUrl(properties).startsWith(
                        HsqlServer.HSQL_URL_PREFIX);
    }

    /**
     * Checks if is embedded database specified.
     *
     * @param properties
     *            the properties
     * @return true, if is embedded database specified
     */
    private static boolean isEmbeddedDatabaseSpecified(
            final XPlannerProperties properties) {
        return properties.getProperty(HsqlServer.HSQLDB_DB_PATH) != null;
    }

    /**
     * Checks if is local hsql protocol database.
     *
     * @param properties
     *            the properties
     * @return true, if is local hsql protocol database
     */
    // Could use HSQL DatabaseUrl
    private static boolean isLocalHsqlProtocolDatabase(
            final XPlannerProperties properties) {
        if (HsqlServer.getUrl(properties) == null) {
            return false;
        }
        final Matcher matcher = HsqlServer.HSQLDB_HSQL_URL_PATTERN
                .matcher(HsqlServer.getUrl(properties));
        if (!matcher.matches()) {
            return false;
        }
        final String host = matcher.group(1);
        return "localhost".equals(host);
    }

    /**
     * Checks if is embedded private database.
     *
     * @param properties
     *            the properties
     * @return true, if is embedded private database
     */
    private static boolean isEmbeddedPrivateDatabase(
            final XPlannerProperties properties) {
        return !HsqlServer.isLocalHsqlProtocolDatabase(properties)
                || !HsqlServer.isEmbeddedDatabaseSpecified(properties);
    }

    /**
     * Gets the db path.
     *
     * @param properties
     *            the properties
     * @return the db path
     */
    private String getDbPath(final XPlannerProperties properties) {
        final String dbPath = properties.getProperty(HsqlServer.HSQLDB_DB_PATH);
        if (dbPath == null) {
            return null;
        }
        if (dbPath.startsWith(HsqlServer.WEBAPP_ROOT_TOKEN)) {
            return this.replaceWebRootToken(dbPath);
        }
        if (HsqlServer.ABSOLUTE_PATH_PATTERN.matcher(dbPath).matches()) {
            return dbPath;
        }
        return new File(this.getStartDirectory() + File.separator + dbPath)
                .getAbsolutePath();
    }

    /**
     * Replace web root token.
     *
     * @param oldPath
     *            the old path
     * @return the string
     */
    private String replaceWebRootToken(final String oldPath) {
        // String prefix =
        // oldPath.replaceFirst(WEBAPP_ROOT_TOKEN_PATTERN_STRING, webRootPath);
        // // ChangeSoon: Once in JDK 1.5 try this one again, in 1.4 it get a
        // StringIndexOutOfBoundsEx if the webroot is
        // "C:\\Projects\\xplanner-trunk\\build\\deploy\\"
        final int tokenStartPos = oldPath.indexOf(HsqlServer.WEBAPP_ROOT_TOKEN);
        final int semiCommaPos = oldPath.indexOf(';');
        if (tokenStartPos == -1) {
            return oldPath;
        }
        final int tokenEndPos = tokenStartPos
                + HsqlServer.WEBAPP_ROOT_TOKEN.length() + 1; // THe slash in
                                                                // ${WEBAPP_ROOT}/
        final String prefix = oldPath.substring(0, tokenStartPos);
        String suffix = "";
        String oldFilePath = "";
        if (semiCommaPos != -1) {
            oldFilePath = oldPath.substring(tokenEndPos, semiCommaPos);
            suffix = oldPath.substring(semiCommaPos);
        } else {
            oldFilePath = oldPath.substring(tokenEndPos);
        }
        String filePath = "";
        if (this.webRootPath != null) {
            filePath += this.webRootPath;
            if (!this.webRootPath.endsWith("\\")
                    && !this.webRootPath.endsWith("/")) {
                filePath += File.separator;
            }
        }
        filePath += oldFilePath;
        return prefix + new File(filePath).getAbsolutePath() + suffix;
    }

    /**
     * Gets the start directory.
     *
     * @return the start directory
     */
    private String getStartDirectory() {
        try {
            return new File(".").getCanonicalPath();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the server.
     *
     * @return the server
     */
    protected Server createServer() {
        return new Server();
    }

    /**
     * Checks if is trace on.
     *
     * @return true, if is trace on
     */
    private static boolean isTraceOn() {
        return Boolean.valueOf(
                new XPlannerProperties().getProperty("hibernate.show_sql"))
                .booleanValue();
    }

    /**
     * Shutdown.
     */
    public static void shutdown() {
        if (HsqlServer.theServer != null) {
            HsqlServer.theServer.stop();
        }
    }

    /**
     * Stop.
     */
    public void stop() {
        HsqlServer.LOG.info("Stopping HSQLDB server");
        this.sendShutdownCommand();

        if (this.server != null) {
            this.server.stop();
            this.waitForServerToTransitionOutOf(ServerConstants.SERVER_STATE_CLOSING);
            this.server = null;
        }
        HsqlServer.theServer = null;
        HsqlServer.LOG.info("HSQLDB server stopped");
    }

    /**
     * Send shutdown command.
     */
    private void sendShutdownCommand() {
        final JdbcTemplate template = new JdbcTemplate(
                new SingleConnectionDataSource(HibernateHelper.getConnection(),
                        false));
        template.execute("SHUTDOWN");
    }

    /**
     * Wait for server to transition out of.
     *
     * @param serverState
     *            the server state
     */
    private void waitForServerToTransitionOutOf(final int serverState) {
        while (this.server.getState() == serverState) {
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) {
            }
        }
    }

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     * @throws SQLException
     *             the SQL exception
     */
    public static void main(final String[] args) throws SQLException {
        HsqlServer.start();
    }

    /**
     * Gets the server.
     *
     * @return the server
     */
    public Server getServer() {
        return this.server;
    }

    /**
     * Gets the db path.
     *
     * @return the db path
     */
    public String getDbPath() {
        return this.dbPath;
    }

    /**
     * Gets the single instance of HsqlServer.
     *
     * @return single instance of HsqlServer
     */
    public static HsqlServer getInstance() {
        return HsqlServer.theServer;
    }

}
