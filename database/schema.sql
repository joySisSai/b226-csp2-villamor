CREATE DATABASE IF NOT EXISTS recording_app
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE recording_app;

CREATE TABLE IF NOT EXISTS users (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS artists (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL UNIQUE,
    bio TEXT NULL,
    is_archived BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS albums (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    artist_id INT UNSIGNED NOT NULL,
    title VARCHAR(255) NOT NULL,
    release_date DATE NULL,
    genre VARCHAR(100) NULL,
    is_archived BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_album_artist_title UNIQUE (artist_id, title),
    CONSTRAINT fk_albums_artist
        FOREIGN KEY (artist_id) REFERENCES artists(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS songs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    length VARCHAR(8) NOT NULL,
    genre VARCHAR(100) NOT NULL,
    album_id INT UNSIGNED NOT NULL,
    isArchived BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_songs_album
        FOREIGN KEY (album_id) REFERENCES albums(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS playlists (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNSIGNED NOT NULL,
    name VARCHAR(150) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_playlist_user_name UNIQUE (user_id, name),
    CONSTRAINT fk_playlists_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS playlist_songs (
    playlist_id INT UNSIGNED NOT NULL,
    song_id INT NOT NULL,
    added_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (playlist_id, song_id),
    CONSTRAINT fk_playlist_songs_playlist
        FOREIGN KEY (playlist_id) REFERENCES playlists(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_playlist_songs_song
        FOREIGN KEY (song_id) REFERENCES songs(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE INDEX idx_songs_title ON songs(title);
CREATE INDEX idx_songs_genre ON songs(genre);
CREATE INDEX idx_songs_archived ON songs(isArchived);
CREATE INDEX idx_albums_title ON albums(title);
CREATE INDEX idx_artists_name ON artists(name);

-- ============================================================
-- HOW TO REGISTER AN ADMIN
-- ============================================================
-- 1. Register normally through the Java application first.
--    The application hashes the password securely and assigns
--    the new account the default USER role.
--
-- 2. Replace 'your_username' below with the registered username,
--    remove the leading "--" from the UPDATE, and execute it:
--
-- UPDATE users
-- SET role = 'ADMIN'
-- WHERE username = 'your_username';
--
-- 3. Verify the account role with:
--
-- SELECT id, username, role FROM users;
--
-- 4. Log out of the Java application and log in again. The Admin
--    Dashboard will be displayed for an account with the ADMIN role.
--
-- Do not insert passwords directly with SQL. Registration through
-- the Java application protects passwords using BCrypt hashing.
