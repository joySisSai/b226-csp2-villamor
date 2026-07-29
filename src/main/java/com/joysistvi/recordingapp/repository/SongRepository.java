package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongRepository {
    private final DbConnection dbConnection;

    public SongRepository(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<Song> findActiveSongs() throws SQLException {
        String query = "SELECT id, title, length, genre, album_id, isArchived " +
                "FROM songs WHERE isArchived = 0";
        return findSongs(query);
    }

    public List<Song> findArchivedSongs() throws SQLException {
        String query = "SELECT id, title, length, genre, album_id, isArchived " +
                "FROM songs WHERE isArchived = 1";
        return findSongs(query);
    }

    public List<Song> searchActiveSongs(String keyword) throws SQLException {
        String query = "SELECT id, title, length, genre, album_id, isArchived " +
                "FROM songs " +
                "WHERE isArchived = 0 AND (title LIKE ? OR genre LIKE ?)";
        String searchValue = "%" + keyword.trim() + "%";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, searchValue);
            statement.setString(2, searchValue);

            try (ResultSet result = statement.executeQuery()) {
                return mapSongs(result);
            }
        }
    }

    public boolean createSong(String title, String length, String genre, int albumId)
            throws SQLException {
        String query = "INSERT INTO songs (title, length, genre, album_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            statement.setString(2, length);
            statement.setString(3, genre);
            statement.setInt(4, albumId);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean updateSong(
            int songId,
            String title,
            String length,
            String genre,
            int albumId
    ) throws SQLException {
        String query = "UPDATE songs " +
                "SET title = ?, length = ?, genre = ?, album_id = ? " +
                "WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            statement.setString(2, length);
            statement.setString(3, genre);
            statement.setInt(4, albumId);
            statement.setInt(5, songId);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteSong(int songId) throws SQLException {
        String query = "DELETE FROM songs WHERE id = ?";
        return executeBySongId(query, songId);
    }

    public boolean archiveSong(int songId) throws SQLException {
        String query = "UPDATE songs SET isArchived = 1 " +
                "WHERE id = ? AND isArchived = 0";
        return executeBySongId(query, songId);
    }

    public boolean restoreSong(int songId) throws SQLException {
        String query = "UPDATE songs SET isArchived = 0 " +
                "WHERE id = ? AND isArchived = 1";
        return executeBySongId(query, songId);
    }

    private List<Song> findSongs(String query) throws SQLException {
        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {
            return mapSongs(result);
        }
    }

    private List<Song> mapSongs(ResultSet result) throws SQLException {
        List<Song> songs = new ArrayList<>();
        while (result.next()) {
            songs.add(new Song(
                    result.getInt("id"),
                    result.getString("title"),
                    result.getString("length"),
                    result.getString("genre"),
                    result.getInt("album_id"),
                    result.getBoolean("isArchived")
            ));
        }
        return songs;
    }

    private boolean executeBySongId(String query, int songId) throws SQLException {
        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, songId);
            return statement.executeUpdate() > 0;
        }
    }
}
