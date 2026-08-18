package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.controller.AuthController;
import com.joysistvi.recordingapp.controller.SongController;
import com.joysistvi.recordingapp.model.User;

import java.util.Scanner;

public class ConsoleApp {
    private final AuthController authController;
    private final SongController songController;
    private final Scanner scanner;

    public ConsoleApp(AuthController authController, SongController songController, Scanner scanner) {
        this.authController = authController;
        this.songController = songController;
        this.scanner = scanner;
    }

    public void run() {
        System.out.println("\n=== RECORDING STUDIO APP ===");
        boolean running = true;
        while (running) {
            printGuestMenu();
            switch (readChoice()) {
                case 1 -> login();
                case 2 -> register();
                case 0 -> running = false;
                default -> System.out.println("Please choose one of the listed options.");
            }
        }
        System.out.println("Thank you for using Recording Studio App.");
    }

    private void login() {
        String username = readText("Username: ");
        String password = readText("Password: ");
        authController.authenticate(username, password).ifPresent(this::openDashboard);
    }

    private void register() {
        String username = readText("Choose a username: ");
        String password = readText("Choose a password (at least 8 characters): ");
        if (password.length() < 8) {
            System.out.println("Password must contain at least 8 characters.");
            return;
        }
        authController.registerUser(username, password);
    }

    private void openDashboard(User user) {
        System.out.printf("\nWelcome, %s!%n", user.username());
        if (user.role() == User.Role.ADMIN) {
            adminDashboard();
        } else {
            userDashboard();
        }
    }

    private void userDashboard() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- USER DASHBOARD ---");
            System.out.println("1. Browse songs");
            System.out.println("2. Search songs");
            System.out.println("0. Logout");
            switch (readChoice()) {
                case 1 -> songController.showSongs();
                case 2 -> songController.searchSong(readText("Title or genre: "));
                case 0 -> loggedIn = false;
                default -> System.out.println("Please choose one of the listed options.");
            }
        }
    }

    private void adminDashboard() {
        boolean loggedIn = true;
        while (loggedIn) {
            printAdminMenu();
            switch (readChoice()) {
                case 1 -> songController.showSongs();
                case 2 -> songController.searchSong(readText("Title or genre: "));
                case 3 -> createSong();
                case 4 -> updateSong();
                case 5 -> songController.archiveSong(readPositiveInt("Song ID to archive: "));
                case 6 -> songController.showArchivedSongs();
                case 7 -> songController.restoreSong(readPositiveInt("Song ID to restore: "));
                case 8 -> deleteSong();
                case 0 -> loggedIn = false;
                default -> System.out.println("Please choose one of the listed options.");
            }
        }
    }

    private void createSong() {
        String title = readText("Title: ");
        String length = readText("Length (MM:SS): ");
        String genre = readText("Genre: ");
        int albumId = readPositiveInt("Album ID: ");
        songController.createSong(title, length, genre, albumId);
    }

    private void updateSong() {
        int songId = readPositiveInt("Song ID to update: ");
        String title = readText("New title: ");
        String length = readText("New length (MM:SS): ");
        String genre = readText("New genre: ");
        int albumId = readPositiveInt("New album ID: ");
        songController.updateSong(songId, title, length, genre, albumId);
    }

    private void deleteSong() {
        int songId = readPositiveInt("Song ID to permanently delete: ");
        String confirmation = readText("Type DELETE to confirm: ");
        if ("DELETE".equals(confirmation)) {
            songController.deleteSong(songId);
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    private int readChoice() {
        return readInt("Choose an option: ");
    }

    private int readPositiveInt(String prompt) {
        int value;
        do {
            value = readInt(prompt);
            if (value <= 0) {
                System.out.println("Please enter a positive number.");
            }
        } while (value <= 0);
        return value;
    }

    private int readInt(String prompt) {
        while (true) {
            String value = readText(prompt);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private void printGuestMenu() {
        System.out.println("\n1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");
    }

    private void printAdminMenu() {
        System.out.println("\n--- ADMIN DASHBOARD ---");
        System.out.println("1. Browse active songs");
        System.out.println("2. Search songs");
        System.out.println("3. Add song");
        System.out.println("4. Update song");
        System.out.println("5. Archive song");
        System.out.println("6. View archived songs");
        System.out.println("7. Restore song");
        System.out.println("8. Permanently delete song");
        System.out.println("0. Logout");
    }
}
