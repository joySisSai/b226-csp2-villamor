# Recording Studio App

A Java console application for managing recording-studio songs and users. The
project uses a simple MVC structure, JDBC with MySQL, and BCrypt password
hashing.

## Project structure

```text
src/main/java/com/joysistvi/recordingapp/
|-- Main.java
|-- config/
|   `-- DbConnection.java
|-- controller/
|   |-- AuthController.java
|   `-- SongController.java
|-- model/
|   |-- Song.java
|   `-- User.java
|-- repository/
|   |-- SongRepository.java
|   `-- UserRepository.java
`-- view/
    `-- ConsoleView.java
```

- **Model:** application data.
- **View:** console output and table formatting.
- **Controller:** validation and application flow.
- **Repository:** JDBC queries and BCrypt password persistence.
- **Config:** database connection setup.

The project does not use a separate DAO package because repositories already
provide the data-access boundary.

## Requirements

- Java 26
- Maven
- MySQL
- Database named `recording_app`

Required tables:

```sql
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);
```

The existing `songs` table must contain:

```text
id, title, length, genre, album_id, isArchived
```

## Run

1. Start MySQL and create the `recording_app` database.
2. Check the credentials in `DbConnection.java`.
3. Open the project in IntelliJ IDEA.
4. Run `com.joysistvi.recordingapp.Main`.

## Main features

- Create, list, search, update, and permanently delete songs
- Archive and restore songs
- Register users with BCrypt password hashing
- Authenticate users with BCrypt verification

## Use-case roadmap

The project structure follows the supplied User/Admin use-case diagram.

Implemented foundation:

- Shared login
- User registration
- Browse active songs
- Search songs
- Admin-ready song management operations

Planned modules:

- Artist and album browsing
- Artist management
- Album management
- Create, view, and delete playlists
- Add and remove songs from playlists

The current `users` table has only `id`, `username`, and `password`. That is
enough for registration and login, but it cannot distinguish an Admin from a
regular User. Before role-based menus are implemented, add a constrained role
column:

```sql
ALTER TABLE users
ADD role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER';
```

Admin authorization must be enforced by the controllers, not merely by hiding
admin menu options in the view.
