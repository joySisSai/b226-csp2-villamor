package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Song;

import java.util.List;

public class ConsoleView {
    private static final String TABLE_LINE =
            "+------+------------------------------+----------+--------------------+----------+";
    private static final String ROW_FORMAT =
            "| %-4s | %-28.28s | %-8.8s | %-18.18s | %-8s |%n";

    public void displaySongs(String heading, List<Song> songs) {
        System.out.println();
        System.out.println(heading);
        System.out.println(TABLE_LINE);
        System.out.printf(ROW_FORMAT, "ID", "TITLE", "LENGTH", "GENRE", "ALBUM ID");
        System.out.println(TABLE_LINE);

        for (Song song : songs) {
            System.out.printf(
                    ROW_FORMAT,
                    song.id(),
                    song.title(),
                    song.length(),
                    song.genre(),
                    song.albumId()
            );
        }

        if (songs.isEmpty()) {
            System.out.printf("| %-78s |%n", "No songs found");
        }
        System.out.println(TABLE_LINE);
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayError(String message) {
        System.err.println(message);
    }
}
