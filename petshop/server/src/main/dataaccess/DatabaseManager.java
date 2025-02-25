package dataaccess;

import exception.ResponseException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * A simple utility for creating a database and managing connections.
 * <br>
 * This class requires a <pre>db.properties</pre> file to contains several important pieces of information.
 * The file is lazily loaded only when required to avoid throwing errors when the file doesn't need to be expected.
 */
public class DatabaseManager {
    private static String databaseName;
    private static String user;
    private static String password;
    private static String connectionUrl;
    private static Boolean configLoaded = false;

    /**
     * Load the database information for the db.properties file.
     */
    private static void initDbProperties() {
        try {
            try (InputStream in = DatabaseManager.class.getClassLoader().getResourceAsStream("db.properties")) {
                Properties props = new Properties();
                props.load(in);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);

            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Ensures that config properties are available for use within the class.
     * Within our lazy-loaded config properties system, this should be called before attempting to access lazy-loaded variables.
     * <br>
     * This is <b>not</b> a threadsafe function. Calling it multiple times in succession
     * may result in reading the config data multiple times.
     */
    private static void requireLoadedConfig() {
        if (configLoaded) return;
        initDbProperties();
        configLoaded = true;
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws ResponseException {
        requireLoadedConfig();
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            try (var preparedStatement = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws ResponseException {
        requireLoadedConfig();
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}
