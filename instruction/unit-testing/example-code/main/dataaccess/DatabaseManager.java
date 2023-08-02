package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class DatabaseManager {

    private Connection conn;

    public void openConnection() throws DatabaseException {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:spellcheck.sqlite";

            // Open a database connection
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DatabaseException("openConnection failed", e);
        }
    }

    public void closeConnection(boolean commit) throws DatabaseException {
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            throw new DatabaseException("closeConnection failed", e);
        }
    }

    public void createTables() throws DatabaseException {
        try (Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("drop table if exists dictionary");
            stmt.executeUpdate("create table dictionary ( word text not null unique )");
        } catch (SQLException e) {
            throw new DatabaseException("createTables failed", e);
        }
    }

    public void fillDictionary() throws DatabaseException {
        String[] words = {"fred", "wilma", "betty", "barney"};

        String sql = "insert into dictionary (word) values (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (String word : words) {
                stmt.setString(1, word);

                if (stmt.executeUpdate() != 1) {
                    throw new DatabaseException("fillDictionary failed: Could not insert word");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("fillDictionary failed", e);
        }
    }

    public Set<String> loadDictionary() throws DatabaseException {

        String sql = "select word from dictionary";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            Set<String> words = new HashSet<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String word = rs.getString(1);
                    words.add(word);
                }
            }

            return words;
        } catch (SQLException e) {
            throw new DatabaseException("fillDictionary failed", e);
        }
    }


    public static void main(String[] args) {
        try {
            DatabaseManager db = new DatabaseManager();

            db.openConnection();
            db.createTables();
            db.fillDictionary();
            db.closeConnection(true);

            System.out.println("Database created and loaded.");
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}