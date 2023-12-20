package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.ArrayFriendList;
import model.Pet;
import model.PetType;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class MySqlDataAccess implements DataAccess {

    public MySqlDataAccess() throws ResponseException {
        configureDatabase();
    }

    public Pet addPet(Pet pet) throws ResponseException {
        var friendJson = new Gson().toJson(pet.friends());
        var statement = "INSERT INTO pet (name, type, friends) VALUES (?, ?, ?)";
        var id = executeUpdate(statement, pet.name(), pet.type(), friendJson);
        return new Pet(id, pet.name(), pet.type(), pet.friends());
    }

    public Pet getPet(int id) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, name, type, friends FROM pet WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readPet(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public Collection<Pet> listPets() throws ResponseException {
        var result = new ArrayList<Pet>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, name, type, friends FROM pet";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readPet(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public void deletePet(Integer id) throws ResponseException {
        var statement = "DELETE FROM pet WHERE id=?";
        executeUpdate(statement, id);
    }

    public void deleteAllPets() throws ResponseException {
        var statement = "TRUNCATE pet";
        executeUpdate(statement);
    }

    private Pet readPet(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var name = rs.getString("name");
        var type = PetType.valueOf(rs.getString("type"));
        var friendsJson = rs.getString("friends");
        var friends = new Gson().fromJson(friendsJson, ArrayFriendList.class);

        return new Pet(id, name, type, friends);
    }

    private int executeUpdate(String statement, Object... params) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof PetType p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  pet (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
              `friends` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(type),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabase() throws ResponseException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
