package Dao;

import Entities.User;

import java.sql.*;
import java.util.Optional;
import ConnectionToDB.ConnectionToDB;

/**
 * Created by Radion on 16.04.2017.
 */
public class UserDao {
    private static final Object LOCK = new Object();
    private static UserDao INSTANCE = null;

    public static UserDao getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new UserDao();
                }
            }
        }
        return INSTANCE;
    }

    public Optional<User> save(User user) {
        try (Connection connection = ConnectionToDB.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO users (user_name, password, email) VALUES (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.getFullName());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.executeUpdate();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getLong(1));
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public User getUserById(Long id) {
        try (Connection connection = ConnectionToDB.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE id = ?")) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return new User(resultSet.getString("user_name"),
                            resultSet.getString("password"),
                            resultSet.getString("email"), id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
