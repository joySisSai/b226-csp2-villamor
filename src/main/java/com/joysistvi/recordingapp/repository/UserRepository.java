package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public boolean loginUser(String username, String password) throws SQLException {
        String query = "SELECT password FROM users WHERE username = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username.trim());

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return false;
                }

                String passwordHash = result.getString("password");
                try {
                    return BCrypt.checkpw(password, passwordHash);
                } catch (IllegalArgumentException exception) {
                    return false;
                }
            }
        }
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
