package com.joysistvi.recordingapp;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.controller.AuthController;
import com.joysistvi.recordingapp.controller.SongController;
import com.joysistvi.recordingapp.repository.SongRepository;
import com.joysistvi.recordingapp.repository.UserRepository;
import com.joysistvi.recordingapp.view.ConsoleApp;
import com.joysistvi.recordingapp.view.ConsoleView;

import java.util.Scanner;

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

        new ConsoleApp(authController, songController, new Scanner(System.in)).run();
    }
}
