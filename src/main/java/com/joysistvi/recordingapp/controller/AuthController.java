package com.joysistvi.recordingapp.controller;

import com.joysistvi.recordingapp.repository.UserRepository;
import com.joysistvi.recordingapp.view.ConsoleView;

import java.sql.SQLException;

public class AuthController {
    private final UserRepository userRepository;
    private final ConsoleView view;

    public AuthController(UserRepository userRepository, ConsoleView view) {
        this.userRepository = userRepository;
        this.view = view;
    }

    public boolean registerUser(String username, String password) {
        if (hasBlankCredentials(username, password)) {
            view.displayMessage("Username and password are required");
            return false;
        }

        try {
            boolean registered = userRepository.registerUser(username, password);
            view.displayMessage(
                    registered
                            ? "User registered successfully"
                            : "Username is already registered"
            );
            return registered;
        } catch (SQLException exception) {
            view.displayError("Unable to register user: " + exception.getMessage());
            return false;
        }
    }

    public boolean loginUser(String username, String password) {
        if (hasBlankCredentials(username, password)) {
            view.displayMessage("Username and password are required");
            return false;
        }

        try {
            boolean authenticated = userRepository.loginUser(username, password);
            view.displayMessage(authenticated ? "Login successful" : "Invalid username or password");
            return authenticated;
        } catch (SQLException exception) {
            view.displayError("Unable to log in: " + exception.getMessage());
            return false;
        }
    }

    private boolean hasBlankCredentials(String username, String password) {
        return username == null || username.isBlank()
                || password == null || password.isBlank();
    }
}
