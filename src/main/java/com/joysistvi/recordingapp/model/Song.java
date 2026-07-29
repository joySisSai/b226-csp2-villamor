package com.joysistvi.recordingapp.model;

public record Song(
        int id,
        String title,
        String length,
        String genre,
        int albumId,
        boolean archived
) {
}
