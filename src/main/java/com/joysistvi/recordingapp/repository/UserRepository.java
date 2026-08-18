package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepository {
    private static final int BCRYPT_COST = 12;

    private final DbConnection dbConnection;

    public UserRepository(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public boolean registerUser(String username, String password) throws SQLException {
        String normalizedUsername = username.trim();
        if (usernameExists(normalizedUsername)) {
            return false;
        }

        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_COST));

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, normalizedUsername);
            statement.setString(2, passwordHash);
            return statement.executeUpdate() > 0;
        }
    }

    public Optional<User> authenticate(String username, String password) throws SQLException {
        String query = "SELECT id, username, password, role FROM users WHERE username = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username.trim());

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return Optional.empty();
                }

                String passwordHash = result.getString("password");
                try {
                    if (!BCrypt.checkpw(password, passwordHash)) {
                        return Optional.empty();
                    }
                    return Optional.of(new User(
                            result.getInt("id"),
                            result.getString("username"),
                            User.Role.fromDatabase(result.getString("role"))
                    ));
                } catch (IllegalArgumentException exception) {
                    return Optional.empty();
                }
            }
        }
    }

    public boolean loginUser(String username, String password) throws SQLException {
        return authenticate(username, password).isPresent();
    }

    private boolean usernameExists(String username) throws SQLException {
        String query = "SELECT 1 FROM users WHERE username = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }
}
