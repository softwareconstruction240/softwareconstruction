package dataaccess;

public record DataAccessConfig(String serverUrl, String username, String password, String dbName) {
    public static DataAccessConfig testDefault = new DataAccessConfig("jdbc:mysql://localhost:3306", "root", "monkeypie", "testpetstore");
}
