package com.joysistvi.recordingapp.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Reverse Domain Names
// Using a reverse domain, it helps avoid name conflictse between packages from different organization
// Java doesn't enforce this, but it's a widety followed convention
//commercial(com) organization(org) network(net) edu and gov
public class DbConnection {
    private final static String URL = "jdbc:mysql://localhost:3306/recording_app";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

// ducking exception -> throw
// connection object
}
