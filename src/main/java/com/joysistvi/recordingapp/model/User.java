package com.joysistvi.recordingapp.model;

public record User(int id, String username, Role role) {
    public enum Role {
        USER,
        ADMIN;

        public static Role fromDatabase(String value) {
            return "ADMIN".equalsIgnoreCase(value) ? ADMIN : USER;
        }
    }
}
