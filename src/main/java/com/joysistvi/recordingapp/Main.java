package com.joysistvi.recordingapp;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.controller.AuthController;
import com.joysistvi.recordingapp.controller.SongController;
import com.joysistvi.recordingapp.repository.SongRepository;
import com.joysistvi.recordingapp.repository.UserRepository;
import com.joysistvi.recordingapp.view.ConsoleView;

public class Main {
    public static void main(String[] args) {
        DbConnection dbConnection = new DbConnection();
        ConsoleView view = new ConsoleView();

        SongController songController = new SongController(
                new SongRepository(dbConnection),
                view
        );
        AuthController authController = new AuthController(
                new UserRepository(dbConnection),
                view
        );

        // Examples:
        // authController.registerUser("josiah", "password123");
        // authController.loginUser("josiah", "password123");
        // songController.createSong("New Song", "03:30", "OPM", 1);
        // songController.searchSong("OPM");
        // songController.archiveSong(1);
        // songController.restoreSong(1);

        songController.showSongs();
    }
}
