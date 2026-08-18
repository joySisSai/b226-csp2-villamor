package com.joysistvi.recordingapp.controller;

import com.joysistvi.recordingapp.repository.SongRepository;
import com.joysistvi.recordingapp.view.ConsoleView;

import java.sql.SQLException;

public class SongController {
    private final SongRepository songRepository;
    private final ConsoleView view;

    public SongController(SongRepository songRepository, ConsoleView view) {
        this.songRepository = songRepository;
        this.view = view;
    }

    public void showSongs() {
        try {
            view.displaySongs("ACTIVE SONGS", songRepository.findActiveSongs());
        } catch (SQLException exception) {
            view.displayError("Unable to load songs: " + exception.getMessage());
        }
    }

    public void showArchivedSongs() {
        try {
            view.displaySongs("ARCHIVED SONGS", songRepository.findArchivedSongs());
        } catch (SQLException exception) {
            view.displayError("Unable to load archived songs: " + exception.getMessage());
        }
    }

    public void searchSong(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            view.displayMessage("Please enter a search keyword");
            return;
        }

        try {
            view.displaySongs(
                    "SEARCH RESULTS",
                    songRepository.searchActiveSongs(keyword)
            );
        } catch (SQLException exception) {
            view.displayError("Unable to search songs: " + exception.getMessage());
        }
    }

    public boolean createSong(String title, String length, String genre, int albumId) {
        if (!isValidSong(title, length, genre, albumId)) {
            return false;
        }

        try {
            boolean created = songRepository.createSong(
                    title.trim(),
                    length.trim(),
                    genre.trim(),
                    albumId
            );
            view.displayMessage(created ? "Song added successfully" : "Song was not added");
            return created;
        } catch (SQLException exception) {
            view.displayError("Unable to add song: " + exception.getMessage());
            return false;
        }
    }

    public boolean updateSong(
            int songId,
            String title,
            String length,
            String genre,
            int albumId
    ) {
        if (songId <= 0) {
            view.displayMessage("Song ID must be a positive number");
            return false;
        }
        if (!isValidSong(title, length, genre, albumId)) {
            return false;
        }

        try {
            boolean updated = songRepository.updateSong(
                    songId,
                    title.trim(),
                    length.trim(),
                    genre.trim(),
                    albumId
            );
            view.displayMessage(updated ? "Song updated successfully" : "Song was not found");
            return updated;
        } catch (SQLException exception) {
            view.displayError("Unable to update song: " + exception.getMessage());
            return false;
        }
    }

    public boolean deleteSong(int songId) {
        if (songId <= 0) {
            view.displayMessage("Song ID must be a positive number");
            return false;
        }
        try {
            boolean deleted = songRepository.deleteSong(songId);
            view.displayMessage(deleted ? "Song deleted successfully" : "Song was not found");
            return deleted;
        } catch (SQLException exception) {
            view.displayError("Unable to delete song: " + exception.getMessage());
            return false;
        }
    }

    public boolean archiveSong(int songId) {
        if (songId <= 0) {
            view.displayMessage("Song ID must be a positive number");
            return false;
        }
        try {
            boolean archived = songRepository.archiveSong(songId);
            view.displayMessage(
                    archived
                            ? "Song archived successfully"
                            : "Song was not found or is already archived"
            );
            return archived;
        } catch (SQLException exception) {
            view.displayError("Unable to archive song: " + exception.getMessage());
            return false;
        }
    }

    public boolean restoreSong(int songId) {
        if (songId <= 0) {
            view.displayMessage("Song ID must be a positive number");
            return false;
        }
        try {
            boolean restored = songRepository.restoreSong(songId);
            view.displayMessage(
                    restored
                            ? "Song restored successfully"
                            : "Song was not found or is already active"
            );
            return restored;
        } catch (SQLException exception) {
            view.displayError("Unable to restore song: " + exception.getMessage());
            return false;
        }
    }

    private boolean isValidSong(String title, String length, String genre, int albumId) {
        if (title == null || title.isBlank() || length == null || length.isBlank()
                || genre == null || genre.isBlank()) {
            view.displayMessage("Title, length, and genre are required");
            return false;
        }
        if (!length.trim().matches("\\d{1,2}:[0-5]\\d")) {
            view.displayMessage("Length must use MM:SS format (for example, 03:30)");
            return false;
        }
        if (albumId <= 0) {
            view.displayMessage("Album ID must be a positive number");
            return false;
        }
        return true;
    }
}
