package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class responsible for creating connections to the database
 */
public class Database {

    //FIXME replace with your database URL
    //private static final String MYSQL_URL = "localhost:3306/MyAwesomeChessDatabase";
    private static final String MYSQL_URL = "localhost:3306/chess";

    //FIXME replace with your database username
    //private static final String DB_USERNAME = "AwesomeChessServer";
    private static final String DB_USERNAME = "root";

    //FIXME replace with your database password (null if no password)
    //private static final String DB_PASSWORD = null;
    private static final String DB_PASSWORD = "admin";

    /**
     * The connection to the database
     */
    private static Connection conn;
    private static final String DATABASE_DRIVER = "jdbc:mysql://";

    /**
     * Initiates a connection to the database
     * @return new Connection object
     * @throws DataAccessException
     */
    public Connection openConnection() throws DataAccessException {
        try {
            //shouldn't try to open an active connection
            if (conn != null){
                throw new DataAccessException("Database connection already open");
            }

            //The Structure for this Connection is driver:language:path
            //The path assumes you start in the root of your project unless given a non-relative path
            final String CONNECTION_URL = DATABASE_DRIVER + MYSQL_URL;

            // Open a database connection to the file given in the path
            conn = DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD);

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    /**
     * If a connection exists, opens a connection. Otherwise, just uses current connection
     * @return Connection to database
     * @throws DataAccessException
     */
    public Connection getConnection() throws DataAccessException {
        if (conn == null) {
            return openConnection();
        } else {
            return conn;
        }
    }

    /**
     * Closes connection to database.
     * @param commit If we want to commit changes to the database. If false, no changes will be made
     * @throws DataAccessException
     */
    public void closeConnection(boolean commit) {
        if (conn == null) {
            return;
        }

        try {
            if (commit) {
                //This will commit the changes to the database
                conn.commit();
            } else {
                //If we find out something went wrong, pass a false into closeConnection and this
                //will rollback any changes we made during this connection
                conn.rollback();
            }

            conn.close();
            conn = null;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
